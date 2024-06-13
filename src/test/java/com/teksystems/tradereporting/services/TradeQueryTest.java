package com.teksystems.tradereporting.services;

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
class TradeQueryTest {

    @Autowired
    TradeEventRepository tradeEventRepository;

    @Autowired
    TradeQuery tradeQuery;

    @AfterEach
    void cleanup() {
        tradeEventRepository.deleteAll();
    }

    @Test
    void canQueryTrades() {
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
        List<TradeEvent> trades = tradeQuery.getTrades();
        // Then
        Assertions.assertEquals(4, trades.size());
        TradeEvent trade1 = trades.get(0);
        TradeEvent trade2 = trades.get(1);
        TradeEvent trade3 = trades.get(2);
        TradeEvent trade4 = trades.get(3);
        Assertions.assertEquals("BISON_BANK", trade1.getSellerParty());
        Assertions.assertEquals("USD", trade1.getPremiumCurrency());
        Assertions.assertEquals("BISON_BANK", trade2.getSellerParty());
        Assertions.assertEquals("USD", trade2.getPremiumCurrency());
        Assertions.assertEquals("EMU_BANK", trade3.getSellerParty());
        Assertions.assertEquals("AUD", trade3.getPremiumCurrency());
        Assertions.assertEquals("EMU_BANK", trade4.getSellerParty());
        Assertions.assertEquals("AUD", trade4.getPremiumCurrency());
    }

    @Test
    void findsNoTrades() {
        // Given
        tradeEventRepository.save(new TradeEvent("BUYER_A", "SELLER_X", new BigDecimal("70.00"), "USD"));
        tradeEventRepository.save(new TradeEvent("BUYER_B", "SELLER_Y", new BigDecimal("900.00"), "AUD"));
        tradeEventRepository.save(new TradeEvent("BUYER_C", "SELLER_Z", new BigDecimal("100.00"), "AUD"));
        // When
        List<TradeEvent> trades = tradeQuery.getTrades();
        // Then
        Assertions.assertTrue(trades.isEmpty());
    }

}
