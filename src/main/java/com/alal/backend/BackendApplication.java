package com.alal.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
//@PropertySource(value = {"classpath:database/database.yml"})
//@PropertySource(value = {"classpath:oauth2/oauth2.yml"})
//@PropertySource(value = {"classpath:swagger/springdoc.yml"})
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
