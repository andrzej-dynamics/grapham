package com.dynamics.andrzej.grapham.services;

import com.dynamics.andrzej.grapham.Edge;
import com.dynamics.andrzej.grapham.dtos.ModificationVertexInfo;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.*;

public class GraphServiceTest {
    private GraphService graphService = new GraphService(new GraphMapper());

    @BeforeMethod
    public void setup() throws IOException, URISyntaxException {
        final GraphLoader graphLoader = new GraphLoader();
        final Path file = Paths.get(getClass().getClassLoader().getResource("graph.txt").toURI());
        final DirectedAcyclicGraph<Integer, Edge> graph = graphLoader.loadGraph(Files.readAllBytes(file));
        graphService.setGraph(graph);
    }

    @Test
    public void testAddVertex() {
        graphService.addVertex(9, true);
        assertTrue(graphService.getGraph().vertexSet().contains(10));
        assertNotNull(graphService.getGraph().getEdge(10, 9));
    }

    @Test
    public void testRemoveVertex() {
        graphService.removeVertex(9);
        assertEquals(graphService.getGraph().vertexSet().size(), 9);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveInvalidVertex() {
        graphService.removeVertex(3);
    }

    @Test
    public void testAddEdge() {
        graphService.addEdge(8, 9);
        assertNotNull(graphService.getGraph().getEdge(8, 9));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddCyclicEdge() {
        graphService.addEdge(9, 3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddNonexistentEdge() {
        graphService.addEdge(9, 15);
    }

    @Test
    public void testSwitchEdges() {
        graphService.switchSubgraphs(4, 5);
        assertNotNull(graphService.getGraph().getEdge(4, 8));
        assertNotNull(graphService.getGraph().getEdge(5, 9));
    }

    @Test
    public void testSwitchEdges2() {
        graphService.switchSubgraphs(3, 2);
        assertNotNull(graphService.getGraph().getEdge(2, 4));
        assertNotNull(graphService.getGraph().getEdge(2, 5));
        assertNotNull(graphService.getGraph().getEdge(2, 6));
        assertNotNull(graphService.getGraph().getEdge(2, 7));
        assertNotNull(graphService.getGraph().getEdge(2, 7));
        assertNotNull(graphService.getGraph().getEdge(0, 2));
        assertNotNull(graphService.getGraph().getEdge(3, 8));
        assertNotNull(graphService.getGraph().getEdge(1, 3));

        assertNull(graphService.getGraph().getEdge(1, 2));
        assertNull(graphService.getGraph().getEdge(2, 8));
        assertNull(graphService.getGraph().getEdge(0, 3));
        assertNull(graphService.getGraph().getEdge(3, 4));
        assertNull(graphService.getGraph().getEdge(3, 5));
        assertNull(graphService.getGraph().getEdge(3, 6));
        assertNull(graphService.getGraph().getEdge(3, 7));
        assertNull(graphService.getGraph().getEdge(3, 7));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSwitchInvalidEdges() {
        graphService.switchSubgraphs(3, 5);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSwitchInvalidEdges2() {
        graphService.switchSubgraphs(3, 8);
    }

    @Test
    public void testCanRemove() {
        assertTrue(graphService.canRemoveEdge(0, 3));
        assertFalse(graphService.canRemoveEdge(3, 7));
    }

    @Test
    public void testCanRemove2() {
        graphService.addVertex(7, true);
        ModificationVertexInfo modificationVertexInfo = graphService.canPerformVertexModification(7);
        assertFalse(modificationVertexInfo.isCanRemove());
    }

    @Test
    public void testIsSingleGraph() {
        graphService.addVertex(7, true);
        graphService.removeVertex(7);
        assertFalse(graphService.isSingleGraph());
    }

    @Test
    public void testIsSingleGraph2() {
        graphService.addVertex(7, true);
        assertTrue(graphService.isSingleGraph());
    }


    @Test
    public void testCanMove() {
        assertTrue(graphService.canMoveEdge(0, 1, 4));
        assertFalse(graphService.canMoveEdge(3, 5, 0));
        assertFalse(graphService.canMoveEdge(3, 6, 1));
    }

    @Test
    public void testRemove() {
        assertNotNull(graphService.removeEdge(0, 3));
    }

    @Test
    public void testMove() {
        assertNotNull(graphService.moveEdge(0, 1, 4));
    }
}