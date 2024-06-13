package com.teksystems.tradereporting.dataimport;

import com.teksystems.tradereporting.dto.TradeEventFromFile;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.xpath.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
public class XPathDocumentReader implements DocumentReader {

    private final XPathExpression buyerPartyRef;
    private final XPathExpression sellerPartyRef;
    private final XPathExpression paymentAmount;
    private final XPathExpression paymentCurrency;
    private final XPathExpression creationTimestamp;

    private static final String xpathSellerPartyRef = "//sellerPartyReference/@href";
    private static final String xpathBuyerPartyRef = "//buyerPartyReference/@href";
    private static final String xpathPaymentAmount = "//paymentAmount/amount/text()";
    private static final String xpathPaymentCurrency = "//paymentAmount/currency/text()";
    private static final String xpathCreationTimestamp = "//creationTimestamp/text()";

    public XPathDocumentReader() throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        this.buyerPartyRef = xPath.compile(xpathBuyerPartyRef);
        this.sellerPartyRef = xPath.compile(xpathSellerPartyRef);
        this.paymentAmount = xPath.compile(xpathPaymentAmount);
        this.paymentCurrency = xPath.compile(xpathPaymentCurrency);
        this.creationTimestamp = xPath.compile(xpathCreationTimestamp);
    }

    @Override
    public TradeEventFromFile readEventFromDocument(Document document) throws DocumentReaderException {
        return new TradeEventFromFile(
                readStringFromXPath(buyerPartyRef, xpathBuyerPartyRef, document),
                readStringFromXPath(sellerPartyRef, xpathSellerPartyRef, document),
                readBigDecimalFromXPath(paymentAmount, xpathPaymentAmount, document),
                readStringFromXPath(paymentCurrency, xpathPaymentCurrency, document),
                readTimestampFromXPath(creationTimestamp, xpathCreationTimestamp, document));
    }

    private static OffsetDateTime readTimestampFromXPath(XPathExpression expression, String xPath, Document document) throws DocumentReaderException {
        String value = readStringFromXPath(expression, xPath, document);
        try {
            return OffsetDateTime.parse(value);
        } catch (NumberFormatException e) {
            throw new DocumentReaderException("Invalid timestamp in XPath: %s: %s".formatted(xPath, value));
        }
    }

    private static BigDecimal readBigDecimalFromXPath(XPathExpression expression, String xPath, Document document) throws DocumentReaderException {
        String value = readStringFromXPath(expression, xPath, document);
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new DocumentReaderException("Invalid number in XPath: %s: %s".formatted(xPath, value));
        }
    }

    private static String readStringFromXPath(XPathExpression expression, String xPath, Document document) throws DocumentReaderException {
        Node node;
        try {
            node = (Node) expression.evaluate(document, XPathConstants.NODE);
            if (node == null) {
                throw new DocumentReaderException("Missing node in XPath: %s".formatted(xPath));
            }
            String nodeValue = node.getNodeValue();
            if (nodeValue == null || nodeValue.isEmpty() || nodeValue.isBlank()) {
                throw new DocumentReaderException("Missing value in XPath: %s".formatted(xPath));
            }
            return nodeValue.trim();
        } catch (XPathExpressionException e) {
            throw new DocumentReaderException("Failed to read XPath: %s".formatted(xPath), e);
        }
    }

}
