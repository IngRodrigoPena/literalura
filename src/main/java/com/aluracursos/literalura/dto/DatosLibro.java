package com.aluracursos.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//ignora los campos que no hemos mapeado dentro de esta clase
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        //Long id,
        String title,
        List<DatosAutor> authors,
        List<String> languages,
        @JsonAlias("download_count") Integer downloadCount
) {
}