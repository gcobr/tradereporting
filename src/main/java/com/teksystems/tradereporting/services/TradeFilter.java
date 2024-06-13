package com.teksystems.tradereporting.services;

import com.teksystems.tradereporting.model.TradeEvent;

import java.util.List;

public interface TradeFilter {

    List<TradeEvent> filterTrades(List<TradeEvent> trades);

}
