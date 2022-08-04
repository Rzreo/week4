package com.sparta.jwtprac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing//timestamped 위해 필수
@SpringBootApplication
public class JwTpracApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwTpracApplication.class, args);
    }

}
