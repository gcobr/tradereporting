package com.teksystems.tradereporting.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Embeddable
public class TradeEventId implements Serializable {

    private String buyerParty;
    private String sellerParty;
    private BigDecimal premiumAmount;
    private String premiumCurrency;
    private OffsetDateTime creationTimestamp;

    public TradeEventId() {
    }

    public TradeEventId(String buyerParty, String sellerParty, BigDecimal premiumAmount, String premiumCurrency, OffsetDateTime creationTimestamp) {
        this.buyerParty = buyerParty;
        this.sellerParty = sellerParty;
        this.premiumAmount = premiumAmount;
        this.premiumCurrency = premiumCurrency;
        this.creationTimestamp = creationTimestamp;
    }

    public OffsetDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(OffsetDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getBuyerParty() {
        return buyerParty;
    }

    public void setBuyerParty(String buyerParty) {
        this.buyerParty = buyerParty;
    }

    public String getSellerParty() {
        return sellerParty;
    }

    public void setSellerParty(String sellerParty) {
        this.sellerParty = sellerParty;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getPremiumCurrency() {
        return premiumCurrency;
    }

    public void setPremiumCurrency(String premiumCurrency) {
        this.premiumCurrency = premiumCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeEventId tradeEventId = (TradeEventId) o;
        return Objects.equals(buyerParty, tradeEventId.buyerParty) && Objects.equals(sellerParty, tradeEventId.sellerParty) && Objects.equals(premiumAmount, tradeEventId.premiumAmount) && Objects.equals(premiumCurrency, tradeEventId.premiumCurrency) && Objects.equals(creationTimestamp, tradeEventId.creationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerParty, sellerParty, premiumAmount, premiumCurrency, creationTimestamp);
    }
}
