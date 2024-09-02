package com.lab1.model;

import java.util.List;

public class Administrador {
    private String nombre;
    private int numeroId;
    
    public Administrador(String nombre, int numeroId) {
        this.nombre = nombre;
        this.numeroId = numeroId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroId() {
        return numeroId;
    }

    public void setNumeroId(int numeroId) {
        this.numeroId = numeroId;
    }

    public void eliminarDeporte(DeporteCRUD DeporteCRUD, Deporte deporte) {
        DeporteCRUD.eliminar(deporte);
        System.out.println("Se elimino el deporte");
    }

    public void actualizarDeporte(DeporteCRUD DeporteCRUD, Deporte deporte) {
        DeporteCRUD.actualizar(deporte);
    }

    // Métodos para gestionar deportes
    public void crearDeporte(DeporteCRUD deporteCRUD, Deporte deporte) {
        deporteCRUD.crear(deporte);
    }
    public void mostrarDeportes(DeporteCRUD DeporteCRUD) {
        List<Deporte> deportes = DeporteCRUD.listar();
        if (deportes.isEmpty()) {
            System.out.println("No hay deportes registrados.");
        } else {
            System.out.println("Deportes:");
            for (Deporte deporte : deportes) {
                System.out.println(deporte.toString());
            }
        }
    }

    // Métodos para gestionar entrenadores
    public void crearEntrenador(EntrenadorCRUD entrenadorCRUD, Entrenador entrenador) {
        entrenadorCRUD.crear(entrenador);
    }

    public void eliminarEntrenador(EntrenadorCRUD EntrenadorCRUD, Entrenador entrenador) {
        EntrenadorCRUD.eliminar(entrenador);
    }

    public void actualizarEntrenador(EntrenadorCRUD EntrenadorCRUD, Entrenador entrenador) {
        EntrenadorCRUD.actualizar(entrenador);
    }

    public void mostrarEntrenadores(EntrenadorCRUD crudEntrenador) {
        List<Entrenador> entrenadores = crudEntrenador.listar();
        if (entrenadores.isEmpty()) {
            System.out.println("No hay entrenadores registrados.");
        } else {
            System.out.println("Entrenadores:");
            for (Entrenador entrenador : entrenadores) {
                System.out.println(entrenador.toString());
            }
        }
    }

    // Métodos para gestionar miembros
    public void crearMiembro(MiembroCRUD miembroCRUD, Miembro miembro) {
        miembroCRUD.crear(miembro);
    }

    public void eliminarMiembro(MiembroCRUD MiembroCRUD, Miembro miembro) {
        MiembroCRUD.eliminar(miembro);
    }

    public void actualizarMiembro(MiembroCRUD MiembroCRUD, Miembro miembro) {
        MiembroCRUD.actualizar(miembro);
    }

    public void mostrarMiembros(MiembroCRUD MiembroCRUD) {
        List<Miembro> miembros = MiembroCRUD.listar();
        if (miembros.isEmpty()) {
            System.out.println("No hay miembros registrados.");
        } else {
            System.out.println("Miembros:");
            for (Miembro miembro : miembros) {
                System.out.println(miembro.toString());
            }
        }
    }
    //Metodos para gestionar sesion
    public void programarSesion(SesionEntrenamientoCRUD sesionEntrenamientoCRUD, SesionEntrenamiento sesion) {
        sesionEntrenamientoCRUD.crear(sesion);
        sesion.getEntrenador().agregarSesion(sesion);
    }

    public void eliminarSesion(SesionEntrenamientoCRUD crudSesionEntrenamiento, SesionEntrenamiento sesion) {
        crudSesionEntrenamiento.eliminar(sesion);
    }

    public void actualizarSesion(SesionEntrenamientoCRUD crudSesionEntrenamiento, SesionEntrenamiento sesion) {
        crudSesionEntrenamiento.actualizar(sesion);
    }

    public void mostrarSesiones(SesionEntrenamientoCRUD crudSesionEntrenamiento) {
        List<SesionEntrenamiento> sesiones = crudSesionEntrenamiento.listar();
        if (sesiones.isEmpty()) {
            System.out.println("No hay sesiones de entrenamiento registradas.");
        } else {
            System.out.println("Sesiones:");
            for (SesionEntrenamiento sesion : sesiones) {
                System.out.println(sesion.toString());
            }
        }
    }

}