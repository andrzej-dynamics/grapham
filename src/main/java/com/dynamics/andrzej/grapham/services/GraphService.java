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

import java.util.*;
import java.util.stream.Collectors;

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
        if (!canRemoveVertex(vertex)) {
            throw new IllegalArgumentException("Vertex cannot be removed!");
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
        boolean canRemove = canRemoveVertex(vertex);
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

    public boolean canRemoveEdge(int source, int target) {
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

    public GraphDTO removeEdge(int source, int target) {
        if (!canRemoveEdge(source, target)) {
            throw new IllegalArgumentException("cannot remove edge!");
        }
        graph.removeEdge(source, target);
        return graphMapper.map(graph);
    }

    public boolean canMoveEdge(int source, int target, int newTarget) {
        boolean canMove = canRemoveEdge(source, target);
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

    public GraphDTO moveEdge(int source, int target, int newTarget) {
        if (!canMoveEdge(source, target, newTarget)) {
            throw new IllegalArgumentException("cannot move edge!");
        }
        graph.removeEdge(source, target);
        graph.addEdge(source, newTarget);
        return graphMapper.map(graph);
    }

    public boolean isSingleGraph() {
        Iterator<Integer> iterator = graph.vertexSet().iterator();
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("Fill graph first!");
        }
        Set<Integer> vertices = new HashSet<>();
        findAllVerticesInBranch(iterator.next(), vertices);
        return graph.vertexSet().size() == vertices.size();
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

    private boolean canRemoveVertex(int vertex) {
        final Set<Edge> incomingSource = new HashSet<>(graph.incomingEdgesOf(vertex));
        final Set<Edge> outgoingSource = new HashSet<>(graph.outgoingEdgesOf(vertex));

        graph.removeVertex(vertex);

        boolean canRemove = isSingleGraph();

        graph.addVertex(vertex);

        addIncomingEdges(incomingSource, vertex);
        addOutgoingEdges(outgoingSource, vertex);

        return canRemove;
    }

    private void findAllVerticesInBranch(int v, Set<Integer> vertices) {
        Set<Integer> neighbours = graph.getDescendants(v);
        neighbours.addAll(graph.getAncestors(v));

        for (int candidate : neighbours) {
            if (vertices.contains(candidate)) {
                continue;
            }
            vertices.add(candidate);
            findAllVerticesInBranch(candidate, vertices);
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
