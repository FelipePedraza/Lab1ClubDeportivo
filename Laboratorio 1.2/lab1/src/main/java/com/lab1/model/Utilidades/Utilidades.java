package com.lab1.model.Utilidades;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.Locale;
import java.util.ResourceBundle;

public class Utilidades {
    
    private ResourceBundle bundle;

    
    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    private static Utilidades instancia;
    private FileHandler archivo;

    // Singleton: Constructor privado
    private Utilidades(){
        Locale locale = new Locale("es", "CO");
        bundle = ResourceBundle.getBundle("MiRecurso", locale);
    }

    // Método para obtener la única instancia de la clase
    public static Utilidades getInstance(){
        if (instancia == null) {
            instancia = new Utilidades();
        }
        return instancia;
    }
    
    // Inicialización del logger
    public void inicializarLogger() throws IOException {
        
        try {
            archivo = new FileHandler("logClubDeportivo.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            archivo.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Escribir un log en el archivo
    public void escribirLog(Class<?> clase, String mensaje, Level nivel) {
        Logger logger = Logger.getLogger(clase.getName());  // Crear logger por clase

        // Asociar el FileHandler al logger si no se ha hecho antes
        if (logger.getHandlers().length == 0) {
            logger.addHandler(archivo);
        }

        logger.log(nivel, mensaje);
    }

    //traduccion
    public void setLocale(Locale locale) {
        // Cambia el idioma en tiempo de ejecución
        bundle = ResourceBundle.getBundle("MiRecurso", locale);
    }
    


    
}
