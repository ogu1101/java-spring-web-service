package com.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Simple class to start up the application.
 *
 * @SpringBootApplication
 * @Configuration
 * @EnableAutoConfiguration
 * @ComponentScan
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    /**
     * mainメソッド.
     *
     * @param args 引数
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected final SpringApplicationBuilder configure(
            final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
