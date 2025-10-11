package com.shopme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity"})
@EnableJpaRepositories({"com.shopme.repository"})
public class ShopmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmeApplication.class, args);
    }
}