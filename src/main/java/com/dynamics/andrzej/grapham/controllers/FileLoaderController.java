package com.dynamics.andrzej.grapham.controllers;

import com.dynamics.andrzej.grapham.services.GraphLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class FileLoaderController {
    private final GraphLoader graphLoader;

    public FileLoaderController(GraphLoader graphLoader) {
        this.graphLoader = graphLoader;
    }

    @PostMapping("/file")
    public ResponseEntity<String> loadFile(@RequestParam("file") MultipartFile file){
        try {
            log.info("Got file to load graph: {}", file.getName());
            graphLoader.loadGraph(file.getBytes());
            return new ResponseEntity<>("Loaded", HttpStatus.OK);
        } catch (IllegalArgumentException | IOException e) {
            log.error("Cannot process graph", e);
            return new ResponseEntity<>("Not loaded", HttpStatus.BAD_REQUEST);
        }
    }
}
