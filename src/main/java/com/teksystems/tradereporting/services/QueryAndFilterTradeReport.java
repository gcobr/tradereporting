package com.teksystems.tradereporting.services;

import com.teksystems.tradereporting.dto.TradeEventResult;
import com.teksystems.tradereporting.model.TradeEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryAndFilterTradeReport implements TradeReportService {

    private final TradeQuery tradeQuery;
    private final TradeFilter[] tradeFilters;

    public QueryAndFilterTradeReport(TradeQuery tradeQuery, TradeFilter... tradeFilters) {
        this.tradeQuery = tradeQuery;
        this.tradeFilters = tradeFilters;
    }

    @Override
    public List<TradeEventResult> getFilteredTrades() {
        // Retrieve trades from DB using a specialized component
        List<TradeEvent> trades = tradeQuery.getTrades();
        // Apply any other filters that might have been configured to narrow down the result set even further
        for (TradeFilter filter : tradeFilters) {
            trades = filter.filterTrades(trades);
        }
        // Lastly, convert the results to DTOs (records)
        return trades.stream().map(e -> new TradeEventResult(
                e.getBuyerParty(),
                e.getSellerParty(),
                e.getPremiumAmount(),
                e.getPremiumCurrency())).toList();
    }
}
