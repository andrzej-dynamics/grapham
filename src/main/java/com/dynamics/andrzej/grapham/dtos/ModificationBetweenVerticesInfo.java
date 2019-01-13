package com.dynamics.andrzej.grapham.dtos;

import lombok.*;

@Data
@AllArgsConstructor
public class ModificationBetweenVerticesInfo {
    private boolean canAddEdge;
    private boolean canSwitchGraphs;
}
