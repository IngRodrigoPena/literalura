package com.aluracursos.literalura;

import com.aluracursos.literalura.dto.DatosAutor;
import com.aluracursos.literalura.dto.DatosLibro;
import com.aluracursos.literalura.dto.DatosGutendex;
import com.aluracursos.literalura.service.ConsumoApi;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Iniciando aplicación...");

		var url = "https://gutendex.com/books";
		System.out.println("URL: " + url);

		var consumoApi = new ConsumoApi();
		System.out.println("ConsumoApi creada");

		var json = consumoApi.obtenerDatos(url);
		System.out.println("Respuesta obtenida:");
		System.out.println(json);

		//deserealizando el JSON
		ConvierteDatos conversor = new ConvierteDatos();
		//trae los datos JSON y los convierte en tipo GutendexResponse

		var datos = conversor.obtenerDatos(json, DatosGutendex.class);
		System.out.println("Libros encontrados:");
//		datos.results().forEach(book -> System.out.println(book.title()));

		for (DatosLibro book : datos.results()) {
			System.out.println("📖 " + book.title());
			for (DatosAutor author : book.authors()) {
					System.out.println("   ✍ Autor: " + author.name() +
							" (Nacido: " + author.birthYear() +
							", Fallecido: " + author.deathYear() + ")");
			}
		}
	}
}
