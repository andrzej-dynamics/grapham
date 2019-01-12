package com.dynamics.andrzej.grapham.services;

import lombok.NonNull;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class GraphLoader {
    public Graph<String, DefaultEdge> loadGraph(@NonNull byte[] fileBytes) throws IOException {
        final String file = new String(fileBytes);
        final BufferedReader fileReader = new BufferedReader(new StringReader(file));

        int numOfVertices = Integer.parseInt(fileReader.readLine());
        if (numOfVertices < 1) {
            throw new IllegalArgumentException("num of vertices has to be greater than 0!");
        }
        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        fileReader.mark(fileBytes.length);
        String graphInfoLine = fileReader.readLine();
        while (graphInfoLine != null) {
            String vertexName = graphInfoLine.split(" ")[0];
            graph.addVertex(vertexName);
            graphInfoLine = fileReader.readLine();
        }

        fileReader.reset();
        graphInfoLine = fileReader.readLine();
        while (graphInfoLine != null) {
            String[] lineParts = graphInfoLine.split(" ");
            String sourceVertex = lineParts[0];
            for (int i = 0; i < Integer.parseInt(lineParts[1]); i++) {
                graph.addEdge(sourceVertex, lineParts[i + 2]);
            }
            graph.addVertex(lineParts[0]);
            graphInfoLine = fileReader.readLine();
        }
        return graph;
    }
}
