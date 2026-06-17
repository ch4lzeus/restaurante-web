package com.mycompany.Food_restaurant.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ImageUploadPaths {

    private ImageUploadPaths() {
    }

    public static Path platosDir() {
        return staticDir()
                .resolve("img")
                .resolve("platos")
                .normalize()
                .toAbsolutePath();
    }

    private static Path staticDir() {
        Path base = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

        if (Files.exists(base.resolve("pom.xml"))) {
            return base.resolve(Paths.get("src", "main", "resources", "static"));
        }

        Path proyectoInterno = base.resolve("Food_restaurant");
        if (Files.exists(proyectoInterno.resolve("pom.xml"))) {
            return proyectoInterno.resolve(Paths.get("src", "main", "resources", "static"));
        }

        return base.resolve(Paths.get("src", "main", "resources", "static"));
    }
}
