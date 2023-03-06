package com.santander.efx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import com.santander.efx.service.FxPriceFeedFakePublisher;

@SpringBootApplication
//@ComponentScan("com.santander.efx.service")
public class EfxApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(EfxApplication.class, args);

		FxPriceFeedFakePublisher publisher = applicationContext.getBean(FxPriceFeedFakePublisher.class);

		publisher.publishToSubscriber();
	}

}
