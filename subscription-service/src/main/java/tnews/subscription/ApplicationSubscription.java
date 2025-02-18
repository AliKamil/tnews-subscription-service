package tnews.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableFeignClients(basePackages = {"tnews.subscription", "tnews.aggregator.client"})
@ComponentScan(basePackages = {"tnews.subscription", "tnews.aggregator.client"})
public class ApplicationSubscription {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationSubscription.class, args);
    }
}