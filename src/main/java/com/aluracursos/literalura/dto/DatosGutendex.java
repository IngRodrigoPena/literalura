package com.aluracursos.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosGutendex(
//        Integer count,
//        String next,
//        String previous,
        ////List<Book> results
        List<DatosLibro> results
) {}
