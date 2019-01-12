package com.dynamics.andrzej.grapham.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraphDTO {
    private List<Integer> v;
    private List<EdgeDTO> e;
}
