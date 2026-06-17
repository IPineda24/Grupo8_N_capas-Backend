package com.example.telemedicina;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class TelemedicinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelemedicinaApplication.class, args);
    }

}
