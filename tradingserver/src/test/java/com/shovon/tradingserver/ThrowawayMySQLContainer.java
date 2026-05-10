package com.shovon.tradingserver;

import org.testcontainers.containers.MySQLContainer;

public class ThrowawayMySQLContainer extends MySQLContainer<ThrowawayMySQLContainer> {

  private static final String IMAGE_VERSION = "mysql:8.0.36";
  private static final String DATABASE_NAME = "testdb";
  private static final String USERNAME = "sa";
  private static final String PASSWORD = "password";


  public static final MySQLContainer<ThrowawayMySQLContainer> mysqlContainer = new ThrowawayMySQLContainer();

  private ThrowawayMySQLContainer() {
    super(IMAGE_VERSION);
    super.withDatabaseName(DATABASE_NAME)
        .withUsername(USERNAME)
        .withPassword(PASSWORD);
  }

}