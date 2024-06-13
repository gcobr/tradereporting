package com.teksystems.tradereporting.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeEventRepository extends JpaRepository<TradeEvent, TradeEventId> {

    List<TradeEvent> findByTradeEventIdSellerPartyAndTradeEventIdPremiumCurrency(String tradeEventIdSellerParty, String tradeEventIdPremiumCurrency);

}
