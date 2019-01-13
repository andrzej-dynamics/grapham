package com.dynamics.andrzej.grapham.services;

import com.dynamics.andrzej.grapham.Edge;
import com.dynamics.andrzej.grapham.dtos.EdgeDTO;
import com.dynamics.andrzej.grapham.dtos.GraphDTO;
import org.jgrapht.Graph;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraphMapper {
    public GraphDTO map(Graph<Integer, Edge> graph) {
        List<EdgeDTO> edges = new ArrayList<>();
        List<Integer> vertices = new ArrayList<>(graph.vertexSet());
        for (Edge e : graph.edgeSet()) {
            edges.add(new EdgeDTO(e.source(), e.target()));
        }
        return new GraphDTO(vertices, edges);
    }
}
