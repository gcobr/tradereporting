package com.teksystems.tradereporting.services;

import com.teksystems.tradereporting.dto.TradeEventResult;
import com.teksystems.tradereporting.model.TradeEvent;
import com.teksystems.tradereporting.model.TradeEventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class TradeReportServiceTest {

    @Autowired
    TradeReportService tradeReportService;
    @Autowired
    TradeEventRepository tradeEventRepository;

    @AfterEach
    void cleanup() {
        tradeEventRepository.deleteAll();
    }

    @Test
    void canSearchAndFilterTrades() {
        // Given
        tradeEventRepository.save(new TradeEvent("BUYER_A", "SELLER_W", new BigDecimal("100.00"), "AUD"));
        tradeEventRepository.save(new TradeEvent("BUYER_B", "SELLER_X", new BigDecimal("80.00"), "AUD"));
        tradeEventRepository.save(new TradeEvent("BUYER_C", "BISON_BANK", new BigDecimal("100.00"), "AUD"));
        tradeEventRepository.save(new TradeEvent("BUYER_D", "BISON_BANK", new BigDecimal("70.00"), "USD"));
        tradeEventRepository.save(new TradeEvent("BANK_BISON", "BISON_BANK", new BigDecimal("100.00"), "USD"));
        tradeEventRepository.save(new TradeEvent("BUYER_E", "EMU_BANK", new BigDecimal("100.00"), "USD"));
        tradeEventRepository.save(new TradeEvent("BUYER_F", "EMU_BANK", new BigDecimal("900.00"), "AUD"));
        tradeEventRepository.save(new TradeEvent("BANK_EMU", "EMU_BANK", new BigDecimal("100.00"), "AUD"));
        // When
        List<TradeEventResult> tradeEventResults = tradeReportService.getFilteredTrades();
        // Then
        Assertions.assertEquals(2, tradeEventResults.size());
        TradeEventResult firstMatch = tradeEventResults.get(0);
        Assertions.assertEquals("BUYER_D", firstMatch.buyerParty());
        Assertions.assertEquals("BISON_BANK", firstMatch.sellerParty());
        Assertions.assertEquals("USD", firstMatch.premiumCurrency());
        Assertions.assertEquals(new BigDecimal("70.00"), firstMatch.premiumAmount());
        TradeEventResult secondMatch = tradeEventResults.get(1);
        Assertions.assertEquals("BUYER_F", secondMatch.buyerParty());
        Assertions.assertEquals("EMU_BANK", secondMatch.sellerParty());
        Assertions.assertEquals("AUD", secondMatch.premiumCurrency());
        Assertions.assertEquals(new BigDecimal("900.00"), secondMatch.premiumAmount());
    }

}
