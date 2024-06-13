package com.teksystems.tradereporting.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class TradeEventResultEventRepositoryTest {

	@Autowired
	TradeEventRepository tradeEventRepository;

	@AfterEach
	void cleanup() {
		tradeEventRepository.deleteAll();
	}

	@BeforeEach
	void setup() {
		// Given
		tradeEventRepository.save(new TradeEvent("BUYER_A", "SELLER_W", new BigDecimal("100.00"), "AUD"));
		tradeEventRepository.save(new TradeEvent("BUYER_B", "SELLER_X", new BigDecimal("80.00"), "AUD"));
		tradeEventRepository.save(new TradeEvent("BUYER_C", "SELLER_Y", new BigDecimal("500.50"), "EUR"));
		tradeEventRepository.save(new TradeEvent("BUYER_D", "SELLER_Z", new BigDecimal("99.99"), "USD"));
	}

	@Test
	void findsTradesBySellerAndCurrency() {
		// When
		List<TradeEvent> results = tradeEventRepository.findByTradeEventIdSellerPartyAndTradeEventIdPremiumCurrency("SELLER_X", "AUD");
		// Then
		Assertions.assertEquals(1, results.size());
		TradeEvent tradeEvent = results.get(0);
		Assertions.assertEquals("BUYER_B", tradeEvent.getBuyerParty());
		Assertions.assertEquals("SELLER_X", tradeEvent.getSellerParty());
		Assertions.assertEquals(new BigDecimal("80.00"), tradeEvent.getPremiumAmount());
		Assertions.assertEquals("AUD", tradeEvent.getPremiumCurrency());
	}

	@Test
	void findsNoTrades() {
		// When
		List<TradeEvent> results = tradeEventRepository.findByTradeEventIdSellerPartyAndTradeEventIdPremiumCurrency("SELLER_X", "JPY");
		// Then
		Assertions.assertTrue(results.isEmpty());
	}

}
