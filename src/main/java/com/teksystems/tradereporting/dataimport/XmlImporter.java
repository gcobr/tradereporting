package com.teksystems.tradereporting.dataimport;

import com.teksystems.tradereporting.dto.TradeEventFromFile;
import com.teksystems.tradereporting.model.TradeEvent;
import com.teksystems.tradereporting.model.TradeEventId;
import com.teksystems.tradereporting.model.TradeEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Profile("!test")
public class XmlImporter implements CommandLineRunner {

    private final DocumentReader documentReader;
    private final TradeEventRepository tradeEventRepository;

    private final Logger logger = Logger.getLogger(XmlImporter.class.getSimpleName());

    public XmlImporter(DocumentReader documentReader, TradeEventRepository tradeEventRepository) {
        this.documentReader = documentReader;
        this.tradeEventRepository = tradeEventRepository;
    }

    @Override
    public void run(String... directoriesList) throws Exception {
        if (directoriesList.length < 1) return;
        // If arguments were provided, we'll try and import the XML files
        List<Path> allFiles = findAllXMLs(directoriesList);
        if (allFiles.isEmpty()) {
            this.logger.log(Level.WARNING,
                    "Could not find any files in the directories provided: " + String.join(", ", directoriesList));
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        for (Path path : allFiles) {
            Document document;
            try {
                document = builder.parse(path.toFile());
            } catch (IOException | SAXException e) {
                this.logger.log(Level.SEVERE, "Failed to parse file: %s".formatted(path.toString()), e);
                // Move on to the next file as it might be OK
                continue;
            }
            TradeEventFromFile tradeEventFromFile;
            try {
                tradeEventFromFile = documentReader.readEventFromDocument(document);
            } catch (DocumentReaderException e) {
                this.logger.log(Level.SEVERE, "Failed to read trade from file: %s".formatted(path.toString()), e);
                // Move on to the next file as it might be OK
                continue;
            }
            // Persist trade event in the database only if it is not there yet
            TradeEvent potentiallyNewTradeEvent = tradeEventFromRecord(tradeEventFromFile);
            if (this.tradeEventRepository.findById(potentiallyNewTradeEvent.getTradeId()).isEmpty()) {
                this.tradeEventRepository.save(potentiallyNewTradeEvent);
                this.logger.log(Level.INFO, "Imported trade event: %s".formatted(tradeEventFromFile));
            }
        }
    }

    private static TradeEvent tradeEventFromRecord(TradeEventFromFile tradeEventRecord) {
        TradeEventId tradeEventId = new TradeEventId(
                tradeEventRecord.buyerParty(),
                tradeEventRecord.sellerParty(),
                tradeEventRecord.premiumAmount(),
                tradeEventRecord.premiumCurrency(),
                tradeEventRecord.creationTimestamp()
        );
        return new TradeEvent(tradeEventId);
    }

    private List<Path> findAllXMLs(String... directoriesList) throws IOException {
        List<Path> allFiles = new ArrayList<>();
        for (String directory : directoriesList) {
            Path path = Paths.get(directory);
            if (Files.exists(path)) {
                allFiles.addAll(findXMLs(path));
            } else {
                this.logger.log(Level.SEVERE, "Directory does not exist: %s".formatted(path.toString()));
            }
        }
        return allFiles;
    }

    public static List<Path> findXMLs(Path directory) throws IOException {
        List<Path> matches = new ArrayList<>();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.xml");
        Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (matcher.matches(file)) {
                    matches.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return matches;
    }

}
