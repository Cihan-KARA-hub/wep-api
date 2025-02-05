package com.yelman.blogservices;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class BlogServicesApplication {

    public static void main(String[] args) {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
            SpringApplication.run(BlogServicesApplication.class, args);
    }

}
