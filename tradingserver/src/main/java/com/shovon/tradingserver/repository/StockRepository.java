package com.shovon.tradingserver.repository;

import com.shovon.tradingserver.model.Stock;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StockRepository extends MongoRepository<Stock, String>, StockRepositoryCustom {

    Optional<Stock> findBySymbol(String symbol);

    Optional<Stock> findFirstBySymbol(String symbol);

    boolean existsBySymbol(String symbol);

    void deleteBySymbol(String symbol);
}
