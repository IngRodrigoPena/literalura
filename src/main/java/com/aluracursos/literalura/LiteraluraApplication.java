package com.aluracursos.literalura;

import com.aluracursos.literalura.dto.DatosAutor;
import com.aluracursos.literalura.dto.DatosLibro;
import com.aluracursos.literalura.dto.DatosGutendex;
import com.aluracursos.literalura.service.ConsumoApi;
import com.aluracursos.literalura.service.ConvierteDatos;
import com.aluracursos.literalura.ui.MenuPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private MenuPrincipal menu;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		menu.mostrar();
	}
}
