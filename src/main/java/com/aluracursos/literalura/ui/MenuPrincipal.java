package com.aluracursos.literalura.ui;

import com.aluracursos.literalura.service.LibroService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuPrincipal {

    private final LibroService libroService;
    private final Scanner scanner = new Scanner(System.in);

    public MenuPrincipal(LibroService libroService) {
        this.libroService = libroService;
    }

    public void mostrar() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n=== 📚 Menú de opciones ===");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar libros registrados");
            System.out.println("3. Listar autores registrados");
            System.out.println("4. Listar autores vivos en un año específico");
            System.out.println("5. Listar libros por idioma");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Ingrese el título del libro: ");
                        String titulo = scanner.nextLine();
                        libroService.buscarYRegistrarLibroPorTitulo(titulo);
                    }
                    case 2 -> libroService.listarLibrosRegistrados();
                    case 3 -> System.out.println("🔧 Función aún no implementada");
                    case 4 -> System.out.println("🔧 Función aún no implementada");
                    case 5 -> System.out.println("🔧 Función aún no implementada");
                    case 0 -> System.out.println("👋 Saliendo de la aplicación...");
                    default -> System.out.println("⚠️ Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada no válida. Por favor, ingrese un número.");
            }
        }

        scanner.close();
    }
}