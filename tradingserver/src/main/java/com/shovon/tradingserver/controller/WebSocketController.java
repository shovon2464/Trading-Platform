package com.shovon.tradingserver.controller;

import com.shovon.tradingserver.service.StockSubscriptionStore;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private StockSubscriptionStore subscriptionStore;

    @MessageMapping("/subscribeToStock")
    public void subscribeToStock(@Payload String stockSymbol, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        System.out.println("Client " + sessionId + " subscribed to stock: " + stockSymbol);
        subscriptionStore.subscribeSingle(sessionId, stockSymbol);
    }

    @MessageMapping("/subscribeToMultipleStocks")
    public void subscribeToMultipleStocks(@Payload List<String> stockSymbols, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        System.out.println("Client " + sessionId + " subscribed to multiple stocks: " + stockSymbols);
        subscriptionStore.subscribeMultiple(sessionId, stockSymbols);
    }
}