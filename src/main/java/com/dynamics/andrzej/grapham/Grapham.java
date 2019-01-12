package com.dynamics.andrzej.grapham;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.util.SupplierUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Iterator;

@Slf4j
@SpringBootApplication
public class Grapham {
    // number of vertices
    private static final int SIZE = 10;

    /**
     * Main demo entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        log.info("Starting Grapham...");
        SpringApplication.run(Grapham.class);
        // Create the graph object
//        Graph<String, DefaultEdge> completeGraph = new DirectedAcyclicGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(), false);

        // Create the CompleteGraphGenerator object
//        CompleteGraphGenerator<String, DefaultEdge> completeGenerator = new CompleteGraphGenerator<>(SIZE);

        // Use the CompleteGraphGenerator object to make completeGraph a
        // complete graph with [size] number of vertices
//        completeGenerator.generateGraph(completeGraph);

        // Print out the graph to be sure it's really complete
//        Iterator<String> iter = new DepthFirstIterator<>(completeGraph);
//        while (iter.hasNext()) {
//            String vertex = iter.next();
//            log.info("Vertex " + vertex + " is connected to: " + completeGraph.edgesOf(vertex).toString());
//        }
    }
}
