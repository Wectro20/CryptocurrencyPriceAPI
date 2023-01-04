package com.andrii.oriltesttask;

import com.andrii.oriltesttask.threads.PriceSaver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptocurrencyPriceApplication {

	public static void threadStarter() {
		PriceSaver priceSaverBTC = new PriceSaver("BTC", 100000);
		PriceSaver priceSaverETH = new PriceSaver("ETH", 100000);
		PriceSaver priceSaverXRP = new PriceSaver("XRP", 100000);
		priceSaverBTC.start();
		priceSaverETH.start();
		priceSaverXRP.start();
	}

	public static void main(String[] args) {
		SpringApplication.run(CryptocurrencyPriceApplication.class, args);
		threadStarter();
	}
}
