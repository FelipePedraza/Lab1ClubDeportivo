package com.lab1.model.Utilidades;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private static final String DIRECTORIO_INFORMACION = "C://Reportes_Java/";
    
    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    private static Utilidades instancia;
    private FileHandler archivo;

    // Singleton: Constructor privado
    @SuppressWarnings("deprecation")
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
    // Verificar si existe el directorio y si no, crearlo
    private void verificarYCrearDirectorio() throws IOException {
        File directorio = new File(DIRECTORIO_INFORMACION);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                escribirLog(Utilidades.class,"Directorio creado: " + DIRECTORIO_INFORMACION, Level.INFO);
            } else {
                escribirLog(Utilidades.class, "No se pudo crear el directorio: " + DIRECTORIO_INFORMACION, Level.SEVERE);
                throw new IOException("No se pudo crear el directorio.");
            }
        }
    }

    public <T> void escribirReporteEnArchivo(List<T> lista, String nombreArchivo) throws IOException {
        verificarYCrearDirectorio();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIRECTORIO_INFORMACION+nombreArchivo+".txt"))) {
            for (int i = 0; i < lista.size(); i++) {
                writer.write(lista.get(i).toString());
                writer.newLine();
                
                // Escribe en el archivo cada 10 elementos
                if ((i + 1) % 10 == 0) {
                    writer.flush();
                }
            }
            // Asegura que el archivo se escriba completamente al final
            writer.flush();
            escribirLog(Utilidades.class, "Reporte escrito completamente en el archivo: " + archivo, Level.INFO);
        }catch (IOException e) {
            escribirLog(Utilidades.class, "Error al escribir el reporte en el archivo: " + archivo + " - " + e.getMessage(), Level.SEVERE);
            throw e;
        }
    }

    // Serializar y Deserializar objetos Xml
    public void serializarObjetoXML(Object objeto, String nombre) throws IOException {
        XMLEncoder codificador = null;
        try {
            escribirLog(Utilidades.class, "Iniciando serialización a XML: " + nombre, Level.INFO);  // Log al inicio
            codificador = new XMLEncoder(new FileOutputStream(nombre));
            codificador.writeObject(objeto);
            escribirLog(Utilidades.class, "Objeto serializado exitosamente en archivo XML: " + nombre, Level.INFO);  // Log de éxito
        } catch (FileNotFoundException e) {
            escribirLog(Utilidades.class, "Error al crear el archivo XML para serialización: " + nombre + " - " + e.getMessage(), Level.SEVERE);  // Log de error
        } finally {
            if (codificador != null) {
                codificador.close();
            }
        }
    }
    
    public Object deserializarObjetoXML(String nombre) throws IOException, ClassNotFoundException {
        XMLDecoder decodificador = null;
        Object objeto = null;
        try {
            escribirLog(Utilidades.class, "Iniciando deserialización desde archivo XML: " + nombre, Level.INFO);  // Log al inicio
            decodificador = new XMLDecoder(new FileInputStream(nombre));
            objeto = decodificador.readObject();
            escribirLog(Utilidades.class, "Objeto deserializado exitosamente desde archivo XML: " + nombre, Level.INFO);  // Log de éxito
        }catch (IOException e) {
            escribirLog(Utilidades.class, "Error de IO durante la deserialización del archivo XML: " + nombre + " - " + e.getMessage(), Level.SEVERE);  // Log de error
        }finally {
            if (decodificador != null) {
                decodificador.close();
            }
        }
        return objeto;
    }
    



    // Binarios
    public void serializarObjetoBinario(Object objeto, String nombre) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombre))) {
            oos.writeObject(objeto);
            escribirLog(Utilidades.class, "Serialización binaria exitosa para el archivo: {0}", Level.INFO);  // Log de info
        } catch (IOException e) {
            escribirLog(Utilidades.class, "Error al serializar el objeto en el archivo: {0}", Level.SEVERE); // Log de error
            throw e;
        }
    }

    public Object deserializarObjetoBinario(String nombre) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombre))) {
            Object objeto = ois.readObject();
            escribirLog(Utilidades.class, "Deserialización binaria exitosa para el archivo: {0}", Level.INFO);
            return objeto;
        } catch (IOException | ClassNotFoundException e) {
            escribirLog(Utilidades.class, "Error al deserializar el archivo: {0}", Level.SEVERE);
            throw e;
        }
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
