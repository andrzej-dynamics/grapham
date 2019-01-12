package com.dynamics.andrzej.grapham.services;

import com.dynamics.andrzej.grapham.Edge;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class GraphService {
    @Setter
    @Getter
    private DirectedAcyclicGraph<Integer, Edge> graph;

    public void addVertex(int source, boolean isToSourceDirection) {
        if (graph == null || !graph.containsVertex(source)) {
            throw new IllegalArgumentException("Graph not loaded or source does not exists!");
        }
        final int v = getNextVertexNumber();
        graph.addVertex(v);
        if (isToSourceDirection) {
            graph.addEdge(v, source);
        } else {
            graph.addEdge(source, v);
        }
    }

    public void removeVertex(int vertex) {
        if (graph == null || !graph.containsVertex(vertex)) {
            throw new IllegalArgumentException("Graph not loaded or source does not exists!");
        }
        if (graph.getDescendants(vertex).size() != 0) {
            throw new IllegalArgumentException("Vertex has descendants, cannot be removed!");
        }
        graph.removeVertex(vertex);
    }

    public void addEdge(int source, int target) {
        graph.addEdge(source, target);
    }

    public void switchSubgraphs(int source, int target) {
        AllDirectedPaths<Integer, Edge> allDirectedPaths = new AllDirectedPaths<>(graph);
        final List<GraphPath<Integer, Edge>> allPaths = allDirectedPaths.getAllPaths(source, target, false, Integer.MAX_VALUE);
        if (!allPaths.isEmpty()) {
            throw new IllegalArgumentException("Cannot perform switch, there is path between edges!");
        }
        final Set<Edge> incomingSource = new HashSet<>(graph.incomingEdgesOf(source));
        final Set<Edge> outgoingSource = new HashSet<>(graph.outgoingEdgesOf(source));
        final Set<Edge> incomingTarget = new HashSet<>(graph.incomingEdgesOf(target));
        final Set<Edge> outgoingTarget = new HashSet<>(graph.outgoingEdgesOf(target));

        graph.removeVertex(source);
        graph.removeVertex(target);

        graph.addVertex(source);
        graph.addVertex(target);

        addIncomingEdges(incomingSource, target);
        addOutgoingEdges(outgoingSource, target);

        addIncomingEdges(incomingTarget, source);
        addOutgoingEdges(outgoingTarget, source);
    }

    private void addIncomingEdges(Set<Edge> incomingEdges, int vertex) {
        for (Edge e : incomingEdges) {
            graph.addEdge(e.source(), vertex);
        }
    }

    private void addOutgoingEdges(Set<Edge> outgoingEdges, int vertex) {
        for (Edge e : outgoingEdges) {
            graph.addEdge(vertex, e.target());
        }
    }

    private int getNextVertexNumber() {
        final Set<Integer> vertices = graph.vertexSet();
        int max = -1;
        for (int v : vertices) {
            if (v > max) {
                max = v;
            }
        }
        return max + 1;
    }
}
