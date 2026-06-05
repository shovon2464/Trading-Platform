package com.shovon.tradingserver.service;


import com.shovon.tradingserver.dto.request.StockInput;
import com.shovon.tradingserver.model.Stock;
import com.shovon.tradingserver.repository.StockRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

  @Autowired
  private StockRepository stockRepository;

  public Stock register(StockInput stockInput) {
    Optional<Stock> optStock = this.stockRepository.findFirstBySymbol(stockInput.getSymbol());

    if (optStock.isPresent()) {
      throw new RuntimeException("Stock with this symbol already exists");
    }
    Stock stock = Stock.builder()
        .symbol(stockInput.getSymbol())
        .companyName(stockInput.getCompanyName())
        .iconUrl(stockInput.getIconUrl())
        .lastDayTradedPrice(stockInput.getLastDayTradedPrice())
        .currentPrice(stockInput.getCurrentPrice())
        .build();
    return this.stockRepository.save(stock);
  }

  public List<Stock> getAllStocks() {
    return stockRepository.findAllWithoutTimeSeries();
  }

  public Stock getStockBySymbol(String symbol) {
    return stockRepository.findBySymbolWithoutTimeSeries(symbol)
        .orElseThrow(() -> new RuntimeException("Stock not found"));
  }

}
