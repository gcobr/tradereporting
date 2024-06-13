package com.teksystems.tradereporting.controllers;

import com.teksystems.tradereporting.dto.TradeEventResult;
import com.teksystems.tradereporting.services.TradeReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class TradeReportingControllerTest {

    @MockBean
    TradeReportService mockTradeReportService;

    @Autowired
    TradeReportController tradeReportController;

    @Test
    void returnsResultsFromService() {
        // Given
        List<TradeEventResult> serviceResults = List.of(
                new TradeEventResult("LEFT_BANK", "RIGHT_BANK", new BigDecimal("100.00"), "EUR"),
                new TradeEventResult("RIGHT_BANK", "LEFT_BANK", new BigDecimal("80.00"), "USD"));
        Mockito.when(mockTradeReportService.getFilteredTrades()).thenReturn(serviceResults);
        // When
        List<TradeEventResult> trades = tradeReportController.getTrades();
        Assertions.assertEquals(serviceResults, trades);
    }

    @Test
    void returnsEmptyListFromService() {
        // Given
        Mockito.when(mockTradeReportService.getFilteredTrades()).thenReturn(List.of());
        // When
        List<TradeEventResult> trades = tradeReportController.getTrades();
        // Then
        Assertions.assertTrue(trades.isEmpty());
    }

}
