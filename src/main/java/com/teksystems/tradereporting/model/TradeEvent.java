package com.teksystems.tradereporting.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class TradeEvent {

    @EmbeddedId
    private TradeEventId tradeEventId;

    public TradeEvent() { }

    public TradeEvent(String buyerParty, String sellerParty, BigDecimal premiumAmount, String premiumCurrency) {
        this(new TradeEventId(buyerParty, sellerParty, premiumAmount, premiumCurrency, OffsetDateTime.now()));
    }

    public TradeEvent(String buyerParty, String sellerParty, BigDecimal premiumAmount, String premiumCurrency, OffsetDateTime creationTimestamp) {
        this(new TradeEventId(buyerParty, sellerParty, premiumAmount, premiumCurrency, creationTimestamp));
    }

    public TradeEvent(TradeEventId tradeEventId) {
        this.tradeEventId = tradeEventId;
    }

    public TradeEventId getTradeId() {
        return tradeEventId;
    }

    public void setTradeId(TradeEventId tradeEventId) {
        this.tradeEventId = tradeEventId;
    }

    public String getBuyerParty() {
        return this.tradeEventId == null ? null : this.tradeEventId.getBuyerParty();
    }

    public String getSellerParty() {
        return this.tradeEventId == null ? null : this.tradeEventId.getSellerParty();
    }

    public String getPremiumCurrency() {
        return this.tradeEventId == null ? null : this.tradeEventId.getPremiumCurrency();
    }

    public BigDecimal getPremiumAmount() {
        return this.tradeEventId == null ? null : this.tradeEventId.getPremiumAmount();
    }

    public OffsetDateTime getCreationTimestamp() {
        return this.tradeEventId == null ? null : this.tradeEventId.getCreationTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeEvent tradeEvent = (TradeEvent) o;
        return Objects.equals(tradeEventId, tradeEvent.tradeEventId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tradeEventId);
    }
}
