package com.aluracursos.literalura.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
//ignora los campos que no hemos mapeado dentro de esta clase
@JsonIgnoreProperties(ignoreUnknown = true)
public record Book(
        Long id,
        String title,
        List<Author> authors,
        List<String> languages,
        @JsonAlias("download_count") Integer downloadCount
       ) {
}
