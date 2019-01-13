package com.dynamics.andrzej.grapham.services;

import com.dynamics.andrzej.grapham.Edge;
import com.dynamics.andrzej.grapham.dtos.GraphDTO;
import com.dynamics.andrzej.grapham.dtos.ModificationBetweenVerticesInfo;
import com.dynamics.andrzej.grapham.dtos.ModificationVertexInfo;
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
    private final GraphMapper graphMapper;

    public GraphService(GraphMapper graphMapper) {
        this.graphMapper = graphMapper;
    }

    @Setter
    @Getter
    private DirectedAcyclicGraph<Integer, Edge> graph;

    public GraphDTO addVertex(int source, boolean isToSourceDirection) {
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
        return graphMapper.map(graph);
    }

    public GraphDTO removeVertex(int vertex) {
        if (graph == null || !graph.containsVertex(vertex)) {
            throw new IllegalArgumentException("Graph not loaded or source does not exists!");
        }
        if (!graph.getDescendants(vertex).isEmpty()) {
            throw new IllegalArgumentException("Vertex has descendants, cannot be removed!");
        }
        graph.removeVertex(vertex);
        return graphMapper.map(graph);
    }

    public GraphDTO addEdge(int source, int target) {
        graph.addEdge(source, target);
        return graphMapper.map(graph);
    }

    public GraphDTO switchSubgraphs(int source, int target) {
        if (!canSwitchSubgraphs(source, target)) {
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

        return graphMapper.map(graph);
    }

    public ModificationVertexInfo canPerformVertexModification(int vertex) {
        boolean canRemove = graph.getDescendants(vertex).isEmpty();
        return new ModificationVertexInfo(canRemove);
    }

    public ModificationBetweenVerticesInfo canPerformModificationBetweenVertices(int source, int target) {
        boolean canAddEdge = false;
        try {
            graph.addEdge(source, target);
        } catch (IllegalArgumentException e) {
            canAddEdge = true;
        }
        graph.removeEdge(source, target);

        boolean canSwitchGraphs = canSwitchSubgraphs(source, target);
        return new ModificationBetweenVerticesInfo(canAddEdge, canSwitchGraphs);
    }

    public boolean canRemove(int source, int target) {
        boolean canRemove = true;
        graph.removeEdge(source, target);
        if (graph.getAncestors(source).isEmpty() && graph.getDescendants(source).isEmpty()) {
            canRemove = false;
        }
        if (graph.getAncestors(target).isEmpty() && graph.getDescendants(target).isEmpty()) {
            canRemove = false;
        }
        graph.addEdge(source, target);
        return canRemove;
    }
    public GraphDTO remove(int source, int target) {
        if (!canRemove(source, target)) {
            throw new IllegalArgumentException("cannot remove edge!");
        }
        graph.removeEdge(source, target);
        return graphMapper.map(graph);
    }

    public boolean canMove(int source, int target, int newTarget) {
        boolean canMove = canRemove(source, target);
        try {
            graph.removeEdge(source, target);
            graph.addEdge(source, newTarget);

            graph.removeEdge(source, newTarget);
            graph.addEdge(source, target);
        } catch (IllegalArgumentException e) {
            canMove = false;
        }
        return canMove;
    }

    public GraphDTO move(int source, int target, int newTarget) {
        if (!canMove(source, target, newTarget)) {
            throw new IllegalArgumentException("cannot move edge!");
        }
        graph.removeEdge(source, target);
        graph.addEdge(source, newTarget);
        return graphMapper.map(graph);
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

    private boolean canSwitchSubgraphs(int source, int target) {
        AllDirectedPaths<Integer, Edge> allDirectedPaths = new AllDirectedPaths<>(graph);
        final List<GraphPath<Integer, Edge>> allPaths = allDirectedPaths.getAllPaths(source, target, false, Integer.MAX_VALUE);
        return allPaths.isEmpty();
    }
}
