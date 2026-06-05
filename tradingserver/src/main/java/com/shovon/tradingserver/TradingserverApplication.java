package com.shovon.tradingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradingserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingserverApplication.class, args);
	}

}
