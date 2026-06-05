package com.shovon.tradingserver.repository;

import com.shovon.tradingserver.model.Holding;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingRepository extends MongoRepository<Holding, String> {

    List<Holding> findByUserId(Long userId);

    Optional<Holding> findByUserIdAndStockId(Long userId, String stockId);
}