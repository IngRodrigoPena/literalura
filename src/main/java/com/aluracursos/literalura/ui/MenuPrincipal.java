package com.aluracursos.literalura.ui;

import com.aluracursos.literalura.service.LibroService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuPrincipal {

    private final LibroService libroService;
    private final ApplicationContext context;
    private final Scanner scanner = new Scanner(System.in);

    public MenuPrincipal(LibroService libroService, ApplicationContext context) {
        this.libroService = libroService;
        this.context = context;
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
            System.out.println("6. Mostrar Top 10 libros mas descargados");
            System.out.println("7. Buscar autor por nombre");
            System.out.println("8. Estadisticas");
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
                    case 3 -> libroService.listarAutoresRegistrados();
                    case 4 -> {
                        System.out.print("Ingrese el año a consultar:");
                        String input = scanner.nextLine();
                        try{
                            int anio = Integer.parseInt(input);
                            libroService.listarAutoresVivosEnAnio(anio);
                        }catch (NumberFormatException e){
                            System.out.println("⚠\uFE0F Año inválido. Ingrese un número válido (por ejemplo, 1900).");
                        }
                    }
                    case 5 -> {
                        System.out.print("🌐 Ingresa el código de idioma (por ejemplo: 'en', 'es', 'fr'): ");
                        String idioma = scanner.nextLine();
                        libroService.listarLibrosPorIdioma(idioma);
                    }
                    case 6 -> libroService.mostrarTop10LibrosMasDescargados();
                    case 7 -> {
                        System.out.print("🔍 Ingrese el nombre (o parte) del autor: ");
                        String nombre = scanner.nextLine();
                        libroService.buscarAutorPorNombre(nombre);
                    }
                    case 8 -> mostrarMenuEstadisticas(); 
                    case 0 -> {
                        System.out.println("👋 Saliendo de la aplicación...");
                        SpringApplication.exit(context, () -> 0);
                    }
                    default -> System.out.println("⚠️ Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada no válida. Por favor, ingrese un número.");
            }
        }

        scanner.close();
    }

    public void mostrarMenuEstadisticas() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menú Estadísticas ---");
            System.out.println("1. Estadísticas de descargas");
            System.out.println("2. Cantidad de libros por idioma");
            System.out.println("3. Total de libros registrados");
            System.out.println("0. Volver al menú principal");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> libroService.mostrarEstadisticasDescargas();
                case 2 -> libroService.contarLibrosPorIdioma();
                case 3 -> libroService.mostrarTotalLibrosRegistrados();
                case 0 -> {
                    System.out.println("Volviendo al menú principal...");
                    return;
                }
                default -> System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }
    }

}