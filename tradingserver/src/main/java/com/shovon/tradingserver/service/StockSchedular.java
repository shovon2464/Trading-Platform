package com.shovon.tradingserver.service;

import com.shovon.tradingserver.model.Stock;
import com.shovon.tradingserver.repository.StockRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockSchedular {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StockDataService stockDataService;

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

    private boolean isNewTradeDay() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek day = now.getDayOfWeek();
        boolean isWeekday = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
        String today = LocalDate.now().toString();
        return isWeekday && !HOLIDAYS.contains(today);
    }

    // Runs at 9:15 AM Monday–Friday
    @Scheduled(cron = "0 15 9 * * MON-FRI")
    public void scheduleDayReset() {
        if (!isNewTradeDay()) return;

        Update update = new Update()
            .set("dayTimeSeries", new ArrayList<>())
            .set("tenMinTimeSeries", new ArrayList<>());

        // Set lastDayTradedPrice = currentPrice before reset
        List<Stock> stocks = stockRepository.findAll();
        stocks.forEach(stock -> {
            stock.setLastDayTradedPrice(stock.getCurrentPrice());
            stock.setDayTimeSeries(new ArrayList<>());
            stock.setTenMinTimeSeries(new ArrayList<>());
            stockRepository.save(stock);
        });

        System.out.println("Day reset completed at 9:15 AM");
    }

    // Runs every 10 minutes
    @Scheduled(cron = "0 */10 * * * *")
    public void update10MinCandle() {
        if (!isTradingHour()) return;

        stockRepository.findAll().forEach(stock -> {
            try {
                stockDataService.store10Min(stock.getSymbol());
            } catch (Exception e) {
                System.out.println("Error storing 10min candle for " + stock.getSymbol() + ": " + e.getMessage());
            }
        });
    }

    // Runs every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void generateRandomDataEvery5Seconds() {
        //if (!isTradingHour()) return;

        stockRepository.findAll().forEach(stock -> {
            try {
                stockDataService.generateStockData(stock.getSymbol());
            } catch (Exception e) {
                System.out.println("Error generating data for " + stock.getSymbol() + ": " + e.getMessage());
            }
        });
    }
}
