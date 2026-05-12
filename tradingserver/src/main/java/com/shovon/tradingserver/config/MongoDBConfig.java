package com.shovon.tradingserver.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDBConfig {

  @Value("${MONGO_URL}")
  private String mongoUrl;

  @Value("${MONGO_DATABASE}")
  private String mongoDatabase;

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create(mongoUrl);
  }

  @Bean
  public MongoTemplate mongoTemplate(MongoClient mongoClient) {
    return new MongoTemplate(mongoClient, mongoDatabase);
  }

}
