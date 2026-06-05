package com.shovon.tradingserver.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class StockSubscriptionStore {

    // sessionId -> single stock symbol
    private final Map<String, String> singleSubscriptions = new ConcurrentHashMap<>();

    // sessionId -> list of symbols
    private final Map<String, List<String>> multipleSubscriptions = new ConcurrentHashMap<>();

    public void subscribeSingle(String sessionId, String symbol) {
        singleSubscriptions.put(sessionId, symbol);
    }

    public void subscribeMultiple(String sessionId, List<String> symbols) {
        multipleSubscriptions.put(sessionId, symbols);
    }

    public void removeSession(String sessionId) {
        singleSubscriptions.remove(sessionId);
        multipleSubscriptions.remove(sessionId);
    }

    public Map<String, String> getSingleSubscriptions() {
        return singleSubscriptions;
    }

    public Map<String, List<String>> getMultipleSubscriptions() {
        return multipleSubscriptions;
    }
}
