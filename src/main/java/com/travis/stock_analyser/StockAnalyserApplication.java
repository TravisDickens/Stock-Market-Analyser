package com.travis.stock_analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockAnalyserApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockAnalyserApplication.class, args);
	}

}
