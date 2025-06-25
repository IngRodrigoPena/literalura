package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//id local, autogenerado

    @Column(unique=true)
    private Long idGutendex;//Clave unica para evitar duplicados

    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor>  autores;

    // Constructor por defecto y getters/setters
    public Libro() {}

    public Libro(Long idGutendex,String titulo, String idioma, Integer numeroDescargas, List<Autor> autores) {
        this.idGutendex = idGutendex;
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
        this.autores = autores;
    }

    // Getters y Setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public Long getIdGutendex() {
        return idGutendex;
    }

    public void setIdGutendex(Long idGutendex) {
        this.idGutendex = idGutendex;
    }
}
