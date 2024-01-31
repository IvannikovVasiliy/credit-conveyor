package com.neoflex.creditconveyer.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableConfigServer
public class ConfigurationApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApp.class, args);
    }
}
