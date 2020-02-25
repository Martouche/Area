package com.server.Area;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Scheduled;

@RestController
public class Cron {
    Cron() {
        System.out.println("class CRON OUVERTE");
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void updateDataBase() {
        System.out.println("je suis alallalalalla");
    }
}