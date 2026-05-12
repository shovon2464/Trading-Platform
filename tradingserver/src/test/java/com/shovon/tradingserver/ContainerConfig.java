package com.shovon.tradingserver;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ContainerConfig implements BeforeAllCallback {

  @Override
  public void beforeAll(ExtensionContext context) {
    this.updateSystemProps();
    this.updateAWSProps();
    this.updateMongoDBProps();
    if(!ThrowawayMySQLContainer.mysqlContainer.isRunning()) {
      ThrowawayMySQLContainer.mysqlContainer.start();
      this.updateMySQLProps();
      this.flywayMigration();
    }
  }

  public void updateSystemProps() {
    System.setProperty("JWT_SECRET","petokingdomSecretKeyForDevOnly");
    System.setProperty("JWT_EXPIRATION","86400000");
    System.setProperty("JWT_RESET_PASSWORD_EXPIRATION","86400000");
    System.setProperty("JWT_REFRESH_EXPIRATION","2592000000");
    System.setProperty("JWT_SERVER_ACCESS_EXPIRATION", "2592000000");
  }

  public void updateAWSProps() {
    System.setProperty("APP_ENV", "test");
    System.setProperty("AWS_REGION", "testRegion");
  }

  public void updateMongoDBProps() {
    System.setProperty("MONGO_URL", "mongodb://localhost:27017");
    System.setProperty("MONGO_DATABASE", "tradingdb_test");
  }



  public void updateMySQLProps() {
    System.setProperty("MYSQL_URL", ThrowawayMySQLContainer.mysqlContainer.getJdbcUrl());
    System.setProperty("MYSQL_USERNAME", ThrowawayMySQLContainer.mysqlContainer.getUsername());
    System.setProperty("MYSQL_PASSWORD", ThrowawayMySQLContainer.mysqlContainer.getPassword());
    System.setProperty("SPRING_DATASOURCE_DRIVER", ThrowawayMySQLContainer.mysqlContainer.getDriverClassName());
    System.setProperty("FLYWAY_ENABLED", "false");
  }

  public void flywayMigration() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(ThrowawayMySQLContainer.mysqlContainer.getJdbcUrl());
    dataSource.setUsername(ThrowawayMySQLContainer.mysqlContainer.getUsername());
    dataSource.setPassword(ThrowawayMySQLContainer.mysqlContainer.getPassword());
    Flyway flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration", "classpath:db/mysql")
        .load();
    flyway.migrate();
  }

}
