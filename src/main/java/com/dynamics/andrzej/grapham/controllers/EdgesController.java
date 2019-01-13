package com.dynamics.andrzej.grapham.controllers;

import com.dynamics.andrzej.grapham.dtos.GraphDTO;
import com.dynamics.andrzej.grapham.services.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/edges")
public class EdgesController {
    private final GraphService graphService;

    public EdgesController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/can-remove")
    public boolean canRemove(@RequestParam("source") int source, @RequestParam("target") int target) {
        log.info("Checking whether can remove edge: {}, {}", source, target);
        return graphService.canRemoveEdge(source, target);
    }

    @GetMapping("/remove")
    public GraphDTO remove(@RequestParam("source") int source, @RequestParam("target") int target) {
        log.info("Removing edge: {}, {}", source, target);
        return graphService.removeEdge(source, target);
    }

    @GetMapping("/can-move")
    public boolean canMove(@RequestParam("source") int source, @RequestParam("target") int target, @RequestParam("newTarget") int newTarget) {
        log.info("Checking whether can move edge: {}, {}, {}", source, target, newTarget);
        return graphService.canMoveEdge(source, target, newTarget);
    }

    @GetMapping("/move")
    public GraphDTO move(@RequestParam("source") int source, @RequestParam("target") int target, @RequestParam("newTarget") int newTarget) {
        log.info("Removing edge: {}, {}, {}", source, target, newTarget);
        return graphService.moveEdge(source, target, newTarget);
    }
}
