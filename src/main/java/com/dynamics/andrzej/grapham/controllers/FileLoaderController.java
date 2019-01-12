package com.dynamics.andrzej.grapham.controllers;

import com.dynamics.andrzej.grapham.Edge;
import com.dynamics.andrzej.grapham.dtos.GraphDTO;
import com.dynamics.andrzej.grapham.services.GraphLoader;
import com.dynamics.andrzej.grapham.services.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final GraphService graphService;

    public FileLoaderController(GraphLoader graphLoader, GraphService graphService) {
        this.graphLoader = graphLoader;
        this.graphService = graphService;
    }

    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GraphDTO> loadFile(@RequestParam("file") MultipartFile file){
        try {
            log.info("Got file to load graph: {}", file.getName());
            final DirectedAcyclicGraph<Integer, Edge> graph = graphLoader.loadGraph(file.getBytes());
            final GraphDTO dto = graphLoader.map(graph);
            graphService.setGraph(graph);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException | IOException e) {
            log.error("Cannot process graph", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
