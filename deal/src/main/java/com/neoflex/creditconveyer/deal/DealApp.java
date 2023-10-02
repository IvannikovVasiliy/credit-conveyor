package com.neoflex.creditconveyer.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DealApp {

    public static void main(String[] args) {
        SpringApplication.run(DealApp.class, args);
    }
}
