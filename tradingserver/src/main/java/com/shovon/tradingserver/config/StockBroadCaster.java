package com.shovon.tradingserver.config;

import com.shovon.tradingserver.model.Stock;
import com.shovon.tradingserver.repository.StockRepository;
import com.shovon.tradingserver.service.StockSubscriptionStore;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockBroadCaster {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockSubscriptionStore subscriptionStore;

    private static final List<String> HOLIDAYS = List.of("2025-08-24", "2025-08-31");

    private boolean isTradingHour() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek day = now.getDayOfWeek();
        boolean isWeekday = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;

        int hour = now.getHour();
        int minute = now.getMinute();
        boolean isTradingTime =
            (hour == 9 && minute >= 30) ||
            (hour > 9 && hour < 15) ||
            (hour == 15 && minute <= 30);

        String today = LocalDate.now().toString();
        return isWeekday && isTradingTime && !HOLIDAYS.contains(today);
    }

    @Scheduled(fixedRate = 5000)
    public void broadcastMultipleStockUpdates() {
        //if (!isTradingHour()) return;

        subscriptionStore.getMultipleSubscriptions().forEach((sessionId, symbols) -> {
            List<Map<String, Object>> stockData = symbols.stream()
                .map(symbol -> stockRepository.findFirstBySymbol(symbol))
                .filter(Optional::isPresent)
                .map(opt -> {
                    Stock stock = opt.get();
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("symbol", stock.getSymbol());
                    data.put("currentPrice", stock.getCurrentPrice());
                    data.put("lastDayTradedPrice", stock.getLastDayTradedPrice());
                    return data;
                })
                .collect(Collectors.toList());

            messagingTemplate.convertAndSend("/topic/multipleStocksData/" + sessionId, stockData);
        });
    }

    @Scheduled(fixedRate = 5000)
    public void broadcastSingleStockUpdates() {
        System.out.println("--- Broadcaster running! Total subscriptions: "
            + subscriptionStore.getSingleSubscriptions().size() + " ---");

        subscriptionStore.getSingleSubscriptions().forEach((sessionId, symbol) -> {
            System.out.println("Trying to broadcast for symbol: " + symbol);

            stockRepository.findBySymbol(symbol).ifPresentOrElse(stock -> {
                System.out.println("✅ Found stock in DB! Sending to /topic/" + symbol);
                messagingTemplate.convertAndSend("/topic/" + symbol, stock);
            }, () -> {
                System.out.println("❌ Error: Stock NOT found in DB for symbol: " + symbol);
            });
        });
    }
}