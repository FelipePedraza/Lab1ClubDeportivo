package com.lab1.model;

public class Deporte{
        
    private String nombre;
    private String descripcion;
    private NivelDificultad dificultad;

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