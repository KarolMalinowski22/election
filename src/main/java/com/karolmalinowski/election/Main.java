package com.karolmalinowski.election;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
//		SpringApplication.run(ElectionApplication.class, args);
        Application.launch(ElectionApplication.class, args);
    }
}
