package com.dynamics.andrzej.grapham;

import org.jgrapht.graph.DefaultEdge;

public class Edge extends DefaultEdge {
    public int source() {
        return (int) getSource();
    }

    public int target() {
        return (int) getTarget();
    }
}
