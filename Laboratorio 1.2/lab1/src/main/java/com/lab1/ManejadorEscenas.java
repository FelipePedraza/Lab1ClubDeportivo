package com.lab1;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManejadorEscenas {

    private static Stage primaryStage;

    public static void inicializar(Stage stage) {
        if (primaryStage == null) {
            primaryStage = stage;
        } else {
            throw new IllegalStateException("Stage ya ha sido inicializado.");
        }
    }

    public static void cambiarEscena(String fxml) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("Stage no ha sido inicializado.");
        }

        FXMLLoader loader = new FXMLLoader(ManejadorEscenas.class.getResource(fxml + ".fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        primaryStage.setScene(newScene);

        // Como la ventana del club es muy amplia, esta condición aplica a esa ventana para recortarla.
        if(fxml == "clubDeportivo"){
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);
        }
        primaryStage.show();
    }
}
