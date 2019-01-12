package com.dynamics.andrzej.grapham.services;

import com.dynamics.andrzej.grapham.Edge;
import com.dynamics.andrzej.grapham.dtos.EdgeDTO;
import com.dynamics.andrzej.grapham.dtos.GraphDTO;
import lombok.NonNull;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GraphLoader {
    public DirectedAcyclicGraph<Integer, Edge> loadGraph(@NonNull byte[] fileBytes) throws IOException {
        final String file = new String(fileBytes);
        final BufferedReader fileReader = new BufferedReader(new StringReader(file));

        int numOfVertices = Integer.parseInt(fileReader.readLine());
        if (numOfVertices < 1) {
            throw new IllegalArgumentException("num of vertices has to be greater than 0!");
        }
        DirectedAcyclicGraph<Integer, Edge> graph = new DirectedAcyclicGraph<>(Edge.class);
        String graphInfoLine = fileReader.readLine();
        while (graphInfoLine != null) {
            String[] lineParts = graphInfoLine.split(" ");
            int sourceVertex = Integer.parseInt(lineParts[0]);
            graph.addVertex(sourceVertex);
            for (int i = 0; i < Integer.parseInt(lineParts[1]); i++) {
                int targetVertex = Integer.parseInt(lineParts[i + 2]);
                graph.addVertex(targetVertex);
                graph.addEdge(sourceVertex, targetVertex);
            }
            graphInfoLine = fileReader.readLine();
        }
        return graph;
    }

    public GraphDTO map(Graph<Integer, Edge> graph) {
        List<EdgeDTO> edges = new ArrayList<>();
        List<Integer> vertices = new ArrayList<>(graph.vertexSet());
        for (Edge e : graph.edgeSet()) {
            edges.add(new EdgeDTO(e.source(), e.target()));
        }
        return new GraphDTO(vertices, edges);
    }
}
