package com.lab1.model;

import java.util.ArrayList;
import java.util.List;

public class Entrenador {

    private String nombre;
    private Deporte especialidad;
    private List<SesionEntrenamiento> sesionesEntrenamientos;
    
    public Entrenador(String nombre, Deporte especialidad, List<SesionEntrenamiento> sesionEntrenamineto) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.sesionesEntrenamientos = sesionEntrenamineto;
    }
    public Entrenador(String nombre, Deporte especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.sesionesEntrenamientos = new ArrayList<SesionEntrenamiento>(); 
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Deporte getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Deporte especialidad) {
        this.especialidad = especialidad;
    }

    public void agregarSesion(SesionEntrenamiento sesion) {
        sesionesEntrenamientos.add(sesion);
    }
    @Override
    public String toString() {
        return "[Nombre: " + nombre + " Especialidad: " + especialidad.getNombre() + " " +especialidad.getDificultad() + "]";
    }
    public List<SesionEntrenamiento> getSesionesEntrenaminetos() {
        return sesionesEntrenamientos;
    }
    public void setSesionesEntrenamientos(List<SesionEntrenamiento> sesionesEntrenamientos) {
        this.sesionesEntrenamientos = sesionesEntrenamientos;
    }
    
    
}