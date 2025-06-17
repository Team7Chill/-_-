package com.example.outsourcing_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OutsourcingProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutsourcingProjectApplication.class, args);
    }

}
