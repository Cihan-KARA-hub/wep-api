package com.yelman.identityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class IdentityServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdentityServerApplication.class, args);
    }

}
