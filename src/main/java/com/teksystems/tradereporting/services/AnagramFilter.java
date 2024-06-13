package com.teksystems.tradereporting.services;

import com.teksystems.tradereporting.model.TradeEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AnagramFilter implements TradeFilter {

    private static boolean areAnagrams(String s1, String s2) {
        char[] s1Chars = s1.toCharArray();
        char[] s2Chars = s2.toCharArray();
        Arrays.sort(s1Chars);
        Arrays.sort(s2Chars);
        return Arrays.equals(s1Chars, s2Chars);
    }

    @Override
    public List<TradeEvent> filterTrades(List<TradeEvent> trades) {
        return trades.stream().filter(trade -> !areAnagrams(trade.getBuyerParty(), trade.getSellerParty())).toList();
    }

}
