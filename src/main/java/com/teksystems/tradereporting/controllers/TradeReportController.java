package com.teksystems.tradereporting.controllers;

import com.teksystems.tradereporting.dto.TradeEventResult;
import com.teksystems.tradereporting.services.TradeReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeReportController {

    private final TradeReportService tradeReportService;

    public TradeReportController(TradeReportService tradeReportService) {
        this.tradeReportService = tradeReportService;
    }

    @GetMapping("/trades")
    public List<TradeEventResult> getTrades() {
        return this.tradeReportService.getFilteredTrades();
    }

}
