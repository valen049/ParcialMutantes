package org.parcial.dto;

import lombok.Getter;
import lombok.Setter;
import org.parcial.validators.ValidDna;

@Getter
@Setter
public class DnaRequest {

    @ValidDna
    private String[] dna;

}
