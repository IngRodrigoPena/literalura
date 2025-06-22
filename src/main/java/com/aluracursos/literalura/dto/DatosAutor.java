package com.aluracursos.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DatosAutor(
        String name,
        @JsonAlias("birth_year") Integer birthYear,
        @JsonAlias("death_year") Integer deathYear
) {
}