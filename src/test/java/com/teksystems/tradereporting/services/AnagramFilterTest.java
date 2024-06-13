package com.teksystems.tradereporting.services;


import com.teksystems.tradereporting.model.TradeEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class AnagramFilterTest {

    @Autowired
    AnagramFilter filter;

    @Test
    void filterOutAnagrams() {
        List<TradeEvent> trades = filter.filterTrades(List.of(
                new TradeEvent("ABC", "CBA", new BigDecimal("100.00"), "AUD"),
                new TradeEvent("REST", "TRES", new BigDecimal("60.00"), "AUD"),
                new TradeEvent("BUYER", "SELLER", new BigDecimal("300.00"), "AUD")
        ));
        Assertions.assertEquals(1, trades.size());
        TradeEvent trade = trades.get(0);
        Assertions.assertEquals("BUYER", trade.getBuyerParty());
        Assertions.assertEquals("SELLER", trade.getSellerParty());
    }

    @Test
    void filterOutEverything() {
        List<TradeEvent> trades = filter.filterTrades(List.of(
                new TradeEvent("ABC", "CBA", new BigDecimal("100.00"), "AUD"),
                new TradeEvent("REST", "TRES", new BigDecimal("60.00"), "AUD")
        ));
        Assertions.assertTrue(trades.isEmpty());
    }

    @Test
    void noNeedToFilter() {
        List<TradeEvent> trades = filter.filterTrades(List.of());
        Assertions.assertTrue(trades.isEmpty());
    }

}
