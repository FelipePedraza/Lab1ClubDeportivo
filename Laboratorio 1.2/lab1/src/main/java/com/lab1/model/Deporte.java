package com.lab1.model;
import java.io.Serializable;

public class Deporte implements Serializable{
    
    private static final long serialVersionUID = 1L;
    public String nombre;
    public String descripcion;
    public NivelDificultad dificultad;
    

    public Deporte() {
    }

    public Deporte(String nombre, String descripcion, NivelDificultad dificultad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public NivelDificultad getDificultad() {
        return dificultad;
    }

    @Override
    public String toString() {
        return "["+ "Nombre: " + nombre + " Dificultad: " + dificultad + " ]";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDificultad(NivelDificultad dificultad) {
        this.dificultad = dificultad;
    }
    
}   