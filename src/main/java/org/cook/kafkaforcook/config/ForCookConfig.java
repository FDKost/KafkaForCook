package org.cook.kafkaforcook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ForCookConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
