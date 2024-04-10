package org.example.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CamelApplication {
    //access the H2 console at http://localhost:8080/h2-console
    public static void main(String[] args) {
        SpringApplication.run(CamelApplication.class, args);
    }

}
