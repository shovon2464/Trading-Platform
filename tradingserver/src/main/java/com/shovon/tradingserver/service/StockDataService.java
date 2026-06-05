package com.shovon.tradingserver.service;

import com.shovon.tradingserver.model.Stock;
import com.shovon.tradingserver.repository.StockRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockDataService {

    @Autowired
    private StockRepository stockRepository;

    private static final Random random = new Random();

    private double roundToTwoDecimals(double value) {
        return Math.round((value + Double.MIN_VALUE) * 100.0) / 100.0;
    }

    public void generateStockData(String symbol) {
        Stock stock = this.stockRepository.findFirstBySymbol(symbol)
            .orElseThrow(() -> new RuntimeException("Stock with symbol " + symbol + " not found"));

        Instant now = Instant.now();
        double minChange = -0.02;
        double maxChange = 0.02;
        double trendChange = 0.005;
        double currentPrice = stock.getCurrentPrice();

        // Determine trend
        double trendType = random.nextDouble();
        double trendModifier;
        if (trendType < 0.33) {
            trendModifier = 0; // Sideways
        } else if (trendType < 0.66) {
            trendModifier = trendChange; // Uptrend
        } else {
            trendModifier = -trendChange; // Downtrend
        }

        double changePercentage = random.nextDouble() * (maxChange - minChange) + minChange + trendModifier;
        double close = roundToTwoDecimals(currentPrice * (1 + changePercentage));

        // Determine candle pattern
        double patternType = random.nextDouble();
        double high, low;

        if (patternType < 0.15) {
            // Marubozu
            high = Math.max(currentPrice, close);
            low = Math.min(currentPrice, close);
        } else if (patternType < 0.30) {
            high = Math.max(currentPrice, close);
            low = Math.min(currentPrice, close) - random.nextDouble() * 2;
        } else if (patternType < 0.45) {
            // Inverted Hammer
            high = Math.max(currentPrice, close) + random.nextDouble() * 2;
            low = Math.min(currentPrice, close);
        } else if (patternType < 0.60) {
            // Shooting Star
            high = Math.max(currentPrice, close) + random.nextDouble() * 2;
            low = Math.min(currentPrice, close);
        } else {
            if (random.nextDouble() < 0.5) {
                high = close + random.nextDouble() * 4; // Long bullish
                low = close - random.nextDouble() * 2;
            } else {
                high = close + random.nextDouble() * 2;
                low = close - random.nextDouble() * 4; // Long bearish
            }
        }

        high = roundToTwoDecimals(high);
        low = roundToTwoDecimals(low);

        String timestamp = now.toString();
        long time = now.getEpochSecond();

        List<Map<String, Object>> dayTimeSeries = stock.getDayTimeSeries();
        Map<String, Object> lastItem = dayTimeSeries.isEmpty() ? null : dayTimeSeries.get(dayTimeSeries.size() - 1);

        if (lastItem == null || Duration.between(
                Instant.parse((String) lastItem.get("timestamp")), now).toMillis() > 60_000) {
            // New candle — 1 minute has passed
            Map<String, Object> newCandle = new LinkedHashMap<>();
            newCandle.put("timestamp", timestamp);
            newCandle.put("time", time);
            newCandle.put("_internal_originalTime", time);
            newCandle.put("open", roundToTwoDecimals(currentPrice));
            newCandle.put("high", high);
            newCandle.put("low", low);
            newCandle.put("close", close);
            dayTimeSeries.add(newCandle);
        } else {
            // Update existing candle
            double updatedHigh = roundToTwoDecimals(Math.max((double) lastItem.get("high"), close + random.nextDouble()));
            double updatedLow = roundToTwoDecimals(Math.min((double) lastItem.get("low"), close - random.nextDouble()));

            lastItem.put("high", updatedHigh);
            lastItem.put("low", updatedLow);
            lastItem.put("close", roundToTwoDecimals(close));
            dayTimeSeries.set(dayTimeSeries.size() - 1, lastItem);
        }

        // Keep last 390 candles
        if (dayTimeSeries.size() > 390) {
            stock.setDayTimeSeries(dayTimeSeries.subList(dayTimeSeries.size() - 390, dayTimeSeries.size()));
        }

        stock.setCurrentPrice(close);

        try {
            this.stockRepository.save(stock);
        } catch (Exception e) {
            System.out.println("Skipping Conflicts");
        }
    }

    public void store10Min(String symbol) {
        Stock stock = this.stockRepository.findFirstBySymbol(symbol)
            .orElseThrow(() -> new RuntimeException("Stock not found"));

        List<Map<String, Object>> dayTimeSeries = stock.getDayTimeSeries();
        if (dayTimeSeries.isEmpty()) return;

        Map<String, Object> latestItem = dayTimeSeries.get(dayTimeSeries.size() - 1);

        Instant now = Instant.now();
        String timestamp = now.toString();
        long time = now.getEpochSecond();

        Map<String, Object> tenMinCandle = new LinkedHashMap<>();
        tenMinCandle.put("timestamp", timestamp);
        tenMinCandle.put("time", time);
        tenMinCandle.put("_internal_originalTime", time);
        tenMinCandle.put("open", roundToTwoDecimals(stock.getCurrentPrice()));
        tenMinCandle.put("high", roundToTwoDecimals((double) latestItem.get("high")));
        tenMinCandle.put("low", roundToTwoDecimals((double) latestItem.get("low")));
        tenMinCandle.put("close", roundToTwoDecimals((double) latestItem.get("close")));

        stock.getTenMinTimeSeries().add(tenMinCandle);

        try {
            this.stockRepository.save(stock);
        } catch (Exception e) {
            System.out.println("Skipping Conflicts");
        }
    }
}
