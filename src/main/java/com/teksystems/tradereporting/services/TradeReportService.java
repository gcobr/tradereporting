package com.teksystems.tradereporting.services;

import com.teksystems.tradereporting.dto.TradeEventResult;

import java.util.List;

public interface TradeReportService {

    List<TradeEventResult> getFilteredTrades();
}
