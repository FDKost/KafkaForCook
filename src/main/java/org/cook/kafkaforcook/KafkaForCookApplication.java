package org.cook.kafkaforcook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaForCookApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaForCookApplication.class, args);
	}

}
