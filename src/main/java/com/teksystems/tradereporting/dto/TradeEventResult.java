package com.teksystems.tradereporting.dto;

import java.math.BigDecimal;

public record TradeEventResult(String buyerParty, String sellerParty, BigDecimal premiumAmount,
                               String premiumCurrency
) {

}
