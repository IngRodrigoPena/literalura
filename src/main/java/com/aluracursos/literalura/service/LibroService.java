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
        // Paso 1: Consultar Gutendex
        String url = "https://gutendex.com/books/?search=" + tituloBuscado.replace(" ", "%20");
        String json = consumoApi.obtenerDatos(url);
        DatosGutendex respuesta = conversor.obtenerDatos(json, DatosGutendex.class);

        // Paso 2: Validar que haya resultados
        if (respuesta.results().isEmpty()) {
            System.out.println("❌ Libro no encontrado en Gutendex.");
            return;
        }

        // Paso 3: Tomar el primer resultado (más relevante)
        DatosLibro libroAPI = respuesta.results().get(0);
        //verificamos si ya esta en la BD por idGutendex
        Optional<Libro> existente = libroRepository.findByIdGutendex(libroAPI.idGutendex());

        if (existente.isPresent()) {
            System.out.println("✅ El libro ya existe en la base de datos: " + existente.get().getTitulo());
            return;
        }

        // Paso 5: Convertir y guardar
        List<Autor> autores = libroAPI.authors().stream()
                .map(a -> new Autor(a.name(), a.birthYear(), a.deathYear()))
                .collect(Collectors.toList());

        Libro nuevoLibro = new Libro(
                libroAPI.idGutendex(),
                libroAPI.title(),
                libroAPI.languages().isEmpty() ? "desconocido" : libroAPI.languages().get(0),
                libroAPI.downloadCount(),
                autores
        );

        //Evita guardar libros sin idGutendex, o cuando venga como null
        if (libroAPI.idGutendex() == null) {
            System.out.println("❌ El libro no tiene un ID válido en Gutendex, no se puede guardar.");
            return;
        }

        libroRepository.save(nuevoLibro);
        System.out.println("📚 Libro registrado exitosamente: " + nuevoLibro.getTitulo());
    }


    public void listarLibrosRegistrados() {
        //Recupera todos los libros desde la BD
        List<Libro> libros = libroRepository.findAll();
        //verificamos si la lista de libros esta vacia
        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros registrados.");
            return;
        }

        System.out.println("📚 Libros registrados en la base de datos:");
        //muestra los datos de cada libro
        for (Libro libro : libros) {
            System.out.println("-----------------------------");
            System.out.println("📖 Título: " + libro.getTitulo());
            System.out.println("🌐 Idioma: " + libro.getIdioma());
            System.out.println("⬇️ Descargas: " + libro.getNumeroDescargas());
            System.out.println("👤 Autor(es):");
            for (Autor autor : libro.getAutores()) {
                String datosAutor = " - " + autor.getNombre();
                if (autor.getAnioNacimiento() != null) {
                    datosAutor += " (Nac. " + autor.getAnioNacimiento();
                    if (autor.getAnioFallecimiento() != null) {
                        datosAutor += " - Muerte " + autor.getAnioFallecimiento();
                    }
                    datosAutor += ")";
                }
                System.out.println(datosAutor);
            }
        }
    }

    //busca libros por Idioma
    public void listarLibrosPorIdioma(String codigoIdioma) {
        // Normalizar entrada
        String idioma = codigoIdioma.trim().toLowerCase();

        // Buscar libros por idioma (ignorando mayúsculas/minúsculas)
        List<Libro> libros = libroRepository.findByIdiomaIgnoreCase(idioma);

        if (libros.isEmpty()) {
            // Comprobamos si el idioma existe entre los libros registrados
            List<String> idiomasRegistrados = libroRepository.findAll()
                    .stream()
                    .map(Libro::getIdioma)
                    .distinct()
                    .collect(Collectors.toList());

            if (idiomasRegistrados.stream().noneMatch(i -> i.equalsIgnoreCase(idioma))) {
                System.out.println("❌ El idioma '" + idioma + "' no está registrado en ningún libro.");
            } else {
                System.out.println("📭 No hay libros registrados en el idioma '" + idioma + "'.");
            }
            return;
        }

        System.out.println("📚 Libros en idioma '" + idioma + "':");

        for (Libro libro : libros) {
            System.out.println("-----------------------------");
            System.out.println("📖 Título: " + libro.getTitulo());
            System.out.println("⬇️ Descargas: " + libro.getNumeroDescargas());
            System.out.println("👤 Autor(es):");
            for (Autor autor : libro.getAutores()) {
                System.out.println(" - " + autor.getNombre());
            }
        }
    }

}
