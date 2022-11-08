package com.example.jwe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JweApplication {

    private final JWEExample jwe;
    private final JWTExample jwt;

    public JweApplication(JWEExample jwe, JWTExample jwt) {
        this.jwe = jwe;
        this.jwt = jwt;
    }

    public static void main(String[] args) {
        SpringApplication.run(JweApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {

            // jwe.runJWE();
            jwt.runJWT();

        };
    }
}
