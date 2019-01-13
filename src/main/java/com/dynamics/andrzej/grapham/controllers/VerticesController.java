package com.dynamics.andrzej.grapham.controllers;

import com.dynamics.andrzej.grapham.dtos.GraphDTO;
import com.dynamics.andrzej.grapham.dtos.ModificationBetweenVerticesInfo;
import com.dynamics.andrzej.grapham.dtos.ModificationVertexInfo;
import com.dynamics.andrzej.grapham.services.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/vertices")
public class VerticesController {
    private final GraphService graphService;

    public VerticesController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/add")
    public GraphDTO addVertex(@RequestParam("source") int source, @RequestParam("direction") boolean isToSourceDirection) {
        log.info("Adding vertex: {} direction: {}", source, isToSourceDirection);
        return graphService.addVertex(source, isToSourceDirection);
    }

    @GetMapping("/remove")
    public GraphDTO removeVertex(int vertex) {
        log.info("Removing vertex: {}", vertex);
        return graphService.removeVertex(vertex);
    }

    @GetMapping("/switch")
    public GraphDTO switchSubgraphs(int source, int target) {
        log.info("Switching vertex: {} to {}", source, target);
        return graphService.switchSubgraphs(source, target);
    }

    @GetMapping("/add-edge")
    public GraphDTO addEdge(int source, int target) {
        log.info("Adding edge: {} to: {}", source, target);
        return graphService.addEdge(source, target);
    }

    @GetMapping("/can-perform-single")
    public ModificationVertexInfo canPerformVertexModification(int vertex) {
        log.info("Checking whether can perform operations: {}", vertex);
        return graphService.canPerformVertexModification(vertex);
    }

    @GetMapping("/can-perform-double")
    public ModificationBetweenVerticesInfo canPerformModificationBetweenVertices(int source, int target) {
        log.info("Checking whether can perform operations between: {} and: {}", source, target);
        return graphService.canPerformModificationBetweenVertices(source, target);
    }
}
