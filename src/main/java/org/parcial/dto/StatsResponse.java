package org.parcial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StatsResponse {

    @JsonProperty("count_human_dna")
    private long countHumanDna;

    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    private double ratio;

}
