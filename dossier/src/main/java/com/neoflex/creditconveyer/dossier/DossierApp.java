package com.neoflex.creditconveyer.dossier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DossierApp {

    public static void main(String[] args) {
        SpringApplication.run(DossierApp.class, args);
    }
}
