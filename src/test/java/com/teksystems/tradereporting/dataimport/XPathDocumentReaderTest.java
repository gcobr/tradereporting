package com.teksystems.tradereporting.dataimport;

import com.teksystems.tradereporting.dto.TradeEventFromFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Negative test scenarios in this file are not an exhaustive list
 * of everything that could be wrong with a trade XML. They are a basic check that
 * the code that looks for values using XPath is capable of dealing with missing nodes,
 * missing values, and unparsable values (for numbers and timestamps).
 * In an ideal scenario, pre-validation should be conducted using XML Schema; however,
 * an XSD file was not provided with the requirements for this demo application.
 */
public class XPathDocumentReaderTest {

    private XPathDocumentReader instance;
    private static DocumentBuilder builder;

    @BeforeAll
    public static void staticSetup() throws ParserConfigurationException {
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    @BeforeEach
    public void setup() throws XPathExpressionException {
        this.instance = new XPathDocumentReader();
    }

    @Test
    public void canReadTradeFromXml() throws Exception {
        Document document = loadTestDocument("/event.xml");
        TradeEventFromFile importedTrade = this.instance.readEventFromDocument(document);
        Assertions.assertEquals("KANMU_EB", importedTrade.buyerParty());
        Assertions.assertEquals("EMU_BANK", importedTrade.sellerParty());
        Assertions.assertEquals(new BigDecimal("300.00"), importedTrade.premiumAmount());
        Assertions.assertEquals("AUD", importedTrade.premiumCurrency());
        Assertions.assertEquals(OffsetDateTime.of(2009, 1, 27, 15,
                        38, 0, 0, ZoneOffset.UTC),
                importedTrade.creationTimestamp());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/eventWithoutTimestamp.xml",
            "/eventWithoutBuyerParty.xml",
            "/eventWithInvalidAmount.xml"})
    public void failToReadInconsistentTrade(String fileName) throws Exception {
        Document document = loadTestDocument(fileName);
        Assertions.assertThrows(DocumentReaderException.class,
                () -> this.instance.readEventFromDocument(document));
    }

    private static Document loadTestDocument(String fileName) throws IOException, SAXException {
        try (InputStream is = XPathDocumentReaderTest.class.getResourceAsStream(fileName)) {
            return builder.parse(is);
        }
    }

}
