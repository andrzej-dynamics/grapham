package com.dynamics.andrzej.grapham.services;

import com.dynamics.andrzej.grapham.Edge;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GraphService {

    @Setter
    private Graph<Integer, Edge> graph;


}
