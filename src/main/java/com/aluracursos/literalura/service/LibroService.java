package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.DatosGutendex;
import com.aluracursos.literalura.dto.DatosLibro;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();

    public void buscarYRegistrarLibroPorTitulo(String tituloBuscado) {
        // Buscar en base de datos
        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(tituloBuscado);

        if (libroExistente.isPresent()) {
            System.out.println("✅ El libro ya existe en la base de datos: "
                                                    + libroExistente.get().getTitulo());
            return;
        }

        // Consultar API de Gutendex
        String url = "https://gutendex.com/books/?search=" + tituloBuscado.replace(" ", "%20");
        String json = consumoApi.obtenerDatos(url);
        DatosGutendex respuesta = conversor.obtenerDatos(json, DatosGutendex.class);

        // Verificar si encontró libros
        if (respuesta.results().isEmpty()) {
            System.out.println("❌ Libro no encontrado en Gutendex.");
            return;
        }

        // Tomamos el primer resultado como el más relevante
        DatosLibro libroAPI = respuesta.results().get(0);

        // Convertimos los autores
        List<Autor> autores = libroAPI.authors().stream()
                .map(a -> new Autor(a.name(), a.birthYear(), a.deathYear()))
                .collect(Collectors.toList());

        // Creamos la entidad Libro y la guardamos
        Libro nuevoLibro = new Libro(
                libroAPI.title(),
                libroAPI.languages().isEmpty() ? "desconocido" : libroAPI.languages().get(0),
                libroAPI.downloadCount(),
                autores
        );

        libroRepository.save(nuevoLibro);
        System.out.println("📚 Libro registrado exitosamente: " + nuevoLibro.getTitulo());
    }
}
