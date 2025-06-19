package com.aluracursos.literalura.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public record Book(
        Long id,
        String title,
        List<Author> authors,
        List<String> languages,
        @JsonAlias("download_count") Integer downloadCount
       ) {
}
