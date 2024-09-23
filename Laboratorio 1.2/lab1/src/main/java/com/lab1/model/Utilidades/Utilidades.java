package com.lab1.model.Utilidades;

import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Utilidades {
    
    private ResourceBundle bundle;
    private static final String DIRECTORIO_REPORTES = "C://Reportes_Java/";
    
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

    //Traduccion
    public void setLocale(Locale locale) {
        // Cambia el idioma en tiempo de ejecución
        bundle = ResourceBundle.getBundle("MiRecurso", locale);
    }

    // Gestor de archivos
    public void crearArchivos(List<String> lista1, List<String> lista2) throws IOException {
        File dir = new File(DIRECTORIO_REPORTES);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                escribirLog(Utilidades.class,"Directorio creado: " + DIRECTORIO_REPORTES, Level.INFO);
            } else {
                escribirLog(Utilidades.class, "No se pudo crear el directorio: " + DIRECTORIO_REPORTES, Level.SEVERE);
                throw new IOException("No se pudo crear el directorio.");
            }
        }

        escribirListaEnArchivo(lista1, DIRECTORIO_REPORTES + "lista1.txt");
        escribirListaEnArchivo(lista2, DIRECTORIO_REPORTES + "lista2.txt");
    }

    private void escribirListaEnArchivo(List<String> lista, String archivoDatos) throws IOException {
        try (BufferedWriter escribir = new BufferedWriter(new FileWriter(archivoDatos))) {
            for (int i = 0; i < lista.size(); i++) {
                escribir.write(lista.get(i));
                escribir.newLine();
                if ((i + 1) % 10 == 0) {
                    escribir.flush();
                }
            }
        }
    }

    // Serializar y Deserializar objetos

        // Binarios
    public void serializarObjetoBinario(Object objeto, String ruta) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(objeto);
        }
    }

    public Object deserializarObjetoBinario(String ruta) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return ois.readObject();
        }
    }

    public void serializarObjetoXML(Object objeto, String ruta) throws IOException {
        XMLEncoder codificador;
        codificador = new XMLEncoder(new FileOutputStream(nombre));
        codificador.writeObject(objeto);
        codificador.close();
    }

    // Carga de archivos serializados.
    public void cargarYMostrarObjeto(String ruta) {
        try {
            Object objeto = deserializarObjetoBinario(ruta);
            System.out.println("Objeto cargado: " + objeto);
        } catch (IOException | ClassNotFoundException e) {
            escribirLog(Utilidades.class, "Error al cargar objeto: " + e.getMessage(), Level.SEVERE);
        }
    }
    
}
