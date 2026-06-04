package com.shovon.tradingserver.repository;

import com.shovon.tradingserver.model.Stock;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class StockRepositoryImpl implements StockRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Stock> findAllWithoutTimeSeries() {
        Query query = new Query();
        query.fields().exclude("dayTimeSeries").exclude("tenMinTimeSeries");
        return mongoTemplate.find(query, Stock.class);
    }

    @Override
    public Optional<Stock> findBySymbolWithoutTimeSeries(String symbol) {
        Query query = new Query();
        query.addCriteria(Criteria.where("symbol").is(symbol));
        query.fields().exclude("dayTimeSeries").exclude("tenMinTimeSeries");

        return Optional.ofNullable(mongoTemplate.findOne(query, Stock.class));
    }
}


