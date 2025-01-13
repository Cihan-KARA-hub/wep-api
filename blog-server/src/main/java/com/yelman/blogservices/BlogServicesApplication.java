package com.yelman.blogservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BlogServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogServicesApplication.class, args);
    }

}
