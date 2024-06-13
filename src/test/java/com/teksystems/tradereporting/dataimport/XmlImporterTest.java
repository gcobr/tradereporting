package com.teksystems.tradereporting.dataimport;


import com.teksystems.tradereporting.model.TradeEvent;
import com.teksystems.tradereporting.model.TradeEventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

@SpringBootTest
class XmlImporterTest {

    @Autowired
    XmlImporter importer;

    @Autowired
    TradeEventRepository tradeEventRepository;

    @AfterEach
    void cleanup() {
        tradeEventRepository.deleteAll();
    }

    @Test
    void canImportValidXMLs() throws Exception {
        // Given
        String xmlLocation = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
        // When
        importer.run(xmlLocation);
        // Then
        List<TradeEvent> all = tradeEventRepository.findAll();
        Assertions.assertEquals(1, all.size());
    }

    @Test
    void doesNotImportFromUnknownPath() throws Exception {
        // When
        importer.run("/this/path/does/not/exist");
        // Then
        List<TradeEvent> all = tradeEventRepository.findAll();
        Assertions.assertTrue(all.isEmpty());
    }

}
