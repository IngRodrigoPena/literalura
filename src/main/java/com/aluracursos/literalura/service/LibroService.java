package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.DatosGutendex;
import com.aluracursos.literalura.dto.DatosLibro;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;

import static com.aluracursos.literalura.util.FormatUtils.*;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;

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

    //lista los autores registrados en la BD
    public void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("📭 No hay autores registrados.");
            return;
        }

        System.out.println("\n📚 Autores registrados (sin duplicados):");
        autores.stream()
                .collect(Collectors.toMap(
                        // clave: nombre en minúsculas para evitar duplicados
                        autor -> autor.getNombre().toLowerCase(),
                        // en caso de duplicado, conservar el primero
                        autor -> autor,
                        (a1, a2) -> a1
                ))
                .values().stream()
                .sorted(Comparator.comparing(Autor::getNombre, String.CASE_INSENSITIVE_ORDER))
                .forEach(a -> {
                    String nacimiento = (a.getAnioNacimiento() != null) ? a.getAnioNacimiento().toString() : "¿?";
                    String muerte = (a.getAnioFallecimiento() != null) ? a.getAnioFallecimiento().toString() : "¿?";
                    System.out.println("👤 " + a.getNombre() + " (Nac.: " + nacimiento + ", Falleció: " + muerte + ")");
                });
    }

    //Lista autores vivos en un año específico
    public void listarAutoresVivosEnAnio(int anio) {
        List<Autor> autoresVivos =
          autorRepository.findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqualOrAnioFallecimientoIsNull(anio, anio);
        //List<Autor> findByFechaNacimientoLessThanEqualAndFechaMuerteGreaterThanEqualOrFechaMuerteIsNull(Integer anio1, Integer anio2);
        if (autoresVivos.isEmpty()) {
            System.out.println("📭 No se encontraron autores vivos en el año " + anio);
            return;
        }

        System.out.println("\n👤 Autores que estaban vivos en el año " + anio + ":");
        autoresVivos.stream()
                .sorted(Comparator.comparing(Autor::getNombre, String.CASE_INSENSITIVE_ORDER))
                .forEach(a -> {
                    String nacimiento = (a.getAnioNacimiento() != null) ? a.getAnioNacimiento().toString() : "¿?";
                    String muerte = (a.getAnioFallecimiento() != null) ? a.getAnioFallecimiento().toString() : "¿?";
                    System.out.println("👤 " + a.getNombre() + " (Nac.: " + nacimiento + ", Falleció: " + muerte + ")");
                });
    }

    public void mostrarTop10LibrosMasDescargados() {
        //Recupera todos los libros desde la BD.
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("⚠️ No hay libros registrados en la base de datos.");
            return;
        }

        System.out.println("\n📚 TOP 10 LIBROS MÁS DESCARGADOS:\n");

        libros.stream()
                //Ordena por número de descargas (getNumeroDescargas()), manejando posibles null.
                .sorted(Comparator.comparing(Libro::getNumeroDescargas, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                //limita la lista a los 10 primeros.
                .limit(10)
                //4.	Imprime los detalles (titulo, primer autor, idioma, descargas).
                .forEach(libro -> {
                    System.out.println("📘 Título: " + libro.getTitulo());
                    System.out.println("👤 Autor: " + (libro.getAutores().isEmpty() ? "Desconocido" : libro.getAutores().get(0).getNombre()));
                    System.out.println("🌐 Idioma: " + libro.getIdioma());
                    System.out.println("⬇️ Descargas: " + libro.getNumeroDescargas());
                    System.out.println("------------------------------");
                });
    }

    public void buscarAutorPorNombre(String nombreParcial) {
        List<Autor> autores = autorRepository.findByNombreContainingIgnoreCase(nombreParcial);

        if (autores.isEmpty()) {
            System.out.println("⚠️ No se encontraron autores con ese nombre.");
            return;
        }

        System.out.println("\n👤 AUTORES ENCONTRADOS:\n");

        for (Autor autor : autores) {
            System.out.println("📌 Nombre: " + autor.getNombre());
            System.out.println("📆 Año de nacimiento: " + (autor.getAnioNacimiento() != null ? autor.getAnioNacimiento() : "Desconocido"));
            System.out.println("🪦 Año de fallecimiento: " + (autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento() : "Desconocido"));

            List<Libro> libros = libroRepository.findAll()
                    .stream()
                    //Por cada libro, revisa si algún autor (anyMatch) tiene un nombre que coincida
                    // con el autor buscado (sin importar mayúsculas).
                    //Si hay coincidencia, ese libro se incluye en la lista final.
                    .filter(libro -> libro.getAutores().stream()
                            .anyMatch(a -> a.getNombre().equalsIgnoreCase(autor.getNombre())))
                    .collect(Collectors.toList());

            if (libros.isEmpty()) {
                System.out.println("📕 Libros registrados: Ninguno");
            } else {
                System.out.println("📚 Libros registrados:");
                libros.forEach(libro -> System.out.println("   - " + libro.getTitulo()));
            }

            System.out.println("------------------------------");
        }
    }


    // 1. Estadísticas de descargas
    public void mostrarEstadisticasDescargas() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("⚠️ No hay libros registrados para generar estadísticas.");
            return;
        }

        DoubleSummaryStatistics estadisticas = libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();

        System.out.println("\n📊 ESTADÍSTICAS DE DESCARGAS DE LIBROS:");
        System.out.println("Total de libros: " + formatLong(estadisticas.getCount()));
        System.out.println("Suma total de descargas: " + formatLong((long) estadisticas.getSum()));
        System.out.println("Promedio de descargas: " + formatDouble(estadisticas.getAverage()));
        System.out.println("Mínimo de descargas: " + formatLong((long) estadisticas.getMin()));
        System.out.println("Máximo de descargas: " + formatInt((int) estadisticas.getMax()));
        System.out.println("------------------------------");
    }

    // 2. Cantidad de libros por idioma
    public void contarLibrosPorIdioma() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("⚠️ No hay libros registrados para contar por idioma.");
            return;
        }

        Map<String, Long> conteoPorIdioma = libros.stream()
                .collect(Collectors.groupingBy(
                        libro -> libro.getIdioma() != null ? libro.getIdioma() : "Desconocido",
                        Collectors.counting()
                ));

        System.out.println("\n📚 Cantidad de libros por idioma:");
        conteoPorIdioma.forEach((idioma, cantidad) ->
                System.out.println("Idioma: " + idioma + " - Libros: " + cantidad)
        );
        System.out.println("------------------------------");
    }

    // 3. Total de libros registrados
    public void mostrarTotalLibrosRegistrados() {
        long total = libroRepository.count();
        System.out.println("\n📖 Total de libros registrados: " + total);
        System.out.println("------------------------------");
    }
}
