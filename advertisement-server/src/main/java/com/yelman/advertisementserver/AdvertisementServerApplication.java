package com.yelman.advertisementserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication

public class AdvertisementServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvertisementServerApplication.class, args);
	}

}
