package com.aluracursos.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    public String obtenerDatos(String url){
        //crea le cliente para poder hacer solicitudes a la API
        //la API esta devolviendo status 301 (redireccion), no muestra el json
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        //especificamos la URL a la que queremos hacer la solicitud
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            //usamos el cliente para enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Código de estado HTTP: " + response.statusCode());
            //devuelve el json
            return response.body();
            //manejo de excepciones
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.err.println("Error al hacer la solicitud:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
