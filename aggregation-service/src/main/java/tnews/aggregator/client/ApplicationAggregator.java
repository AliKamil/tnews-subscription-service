package tnews.aggregator.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApplicationAggregator {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationAggregator.class, args);
    }
}