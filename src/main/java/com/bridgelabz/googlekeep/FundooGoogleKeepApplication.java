package com.bridgelabz.googlekeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FundooGoogleKeepApplication {
	public static void main(String[] args) {
		SpringApplication.run(FundooGoogleKeepApplication.class, args);
	}

}
