package com.teksystems.tradereporting.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TradeEventFromFile(String buyerParty, String sellerParty, BigDecimal premiumAmount,
                                 String premiumCurrency, OffsetDateTime creationTimestamp) {

}
