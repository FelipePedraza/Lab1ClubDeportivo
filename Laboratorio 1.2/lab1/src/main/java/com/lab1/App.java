package com.lab1;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Inicializa el Stage en el manejador de escenas
        ManejadorEscenas.inicializar(stage);
        
        // Cambia a la escena de login
        ManejadorEscenas.cambiarEscena("login");
        
        // Muestra el escenario
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
