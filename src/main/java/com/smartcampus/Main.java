package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;

public class Main {

    private static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) {

        HttpServer server = initializeServer();

        System.out.println("=================================");
        System.out.println(" Smart Campus API is running ");
        System.out.println(" URL: " + BASE_URI);
        System.out.println("=================================");

        try {
            // keeps server running
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("Server stopped.");
        }
    }

    private static HttpServer initializeServer() {

        // configure resources
        final ResourceConfig config = new ResourceConfig()
                .packages("com.smartcampus");

        // create server
        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                config
        );
    }
}