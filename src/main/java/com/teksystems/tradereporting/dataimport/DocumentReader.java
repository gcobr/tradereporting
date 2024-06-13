package com.teksystems.tradereporting.dataimport;

import com.teksystems.tradereporting.dto.TradeEventFromFile;
import org.w3c.dom.Document;

public interface DocumentReader {

    TradeEventFromFile readEventFromDocument(Document document) throws DocumentReaderException;

}
