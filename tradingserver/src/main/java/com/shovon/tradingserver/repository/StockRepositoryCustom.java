package com.shovon.tradingserver.repository;

import com.shovon.tradingserver.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockRepositoryCustom {


  List<Stock> findAllWithoutTimeSeries();

  Optional<Stock> findBySymbolWithoutTimeSeries(String symbol);

}
