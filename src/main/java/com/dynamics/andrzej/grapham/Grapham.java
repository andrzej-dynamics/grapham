package com.dynamics.andrzej.grapham;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Grapham {
    public static void main(String[] args) {
        log.info("Starting Grapham...");
        SpringApplication.run(Grapham.class);
    }
}
