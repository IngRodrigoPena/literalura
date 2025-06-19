package com.aluracursos.literalura;

import com.aluracursos.literalura.service.ConsumoApi;
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
//		var url = "https://gutendex.com/books";
//		//var url = "https://catfact.ninja/fact";
//
//		var consumoApi = new ConsumoApi();
//		var json = consumoApi.obtenerDatos(url);
//		System.out.println("Solicitud enviada, esperando respuesta...");
//		System.out.println(json);
//		System.out.println("fin del consumo al API");

		System.out.println("Iniciando aplicación...");

		var url = "https://gutendex.com/books";
		System.out.println("URL: " + url);

		var consumoApi = new ConsumoApi();
		System.out.println("ConsumoApi creada");

		var json = consumoApi.obtenerDatos(url);
		System.out.println("Respuesta obtenida:");
		System.out.println(json);
	}
}
