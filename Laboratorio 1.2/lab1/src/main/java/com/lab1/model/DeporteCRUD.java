package com.lab1.model;

import java.util.ArrayList;
import java.util.List;

public class DeporteCRUD implements CRUD<Deporte> {
    private List<Deporte> deportes = new ArrayList<>();
    private EntrenadorCRUD entrenadorCRUD;
    private SesionEntrenamientoCRUD sesionEntrenamientoCRUD;

    public DeporteCRUD(EntrenadorCRUD entrenadorCRUD, SesionEntrenamientoCRUD sesionEntrenamientoCRUD) {
        this.entrenadorCRUD = entrenadorCRUD;
        this.sesionEntrenamientoCRUD = sesionEntrenamientoCRUD;
    }

    public DeporteCRUD(){
        
    }

    public void setEntrenadorCRUD(EntrenadorCRUD entrenadorCRUD) {
        this.entrenadorCRUD = entrenadorCRUD;
    }
    
    public void setSesionEntrenamientoCRUD(SesionEntrenamientoCRUD sesionEntrenamientoCRUD) {
        this.sesionEntrenamientoCRUD = sesionEntrenamientoCRUD;
    }

    @Override
    public void crear(Deporte deporte) {

        for (Deporte d : deportes) {
            if (d.getDificultad().equals(deporte.getDificultad()) && d.getNombre().toLowerCase().equals(deporte.getNombre().toLowerCase())) {
                System.out.println("Error: Ya existe un deporte con la misma dificultad.");
                return;
            }
        }
        deportes.add(deporte);
        System.out.println("Deporte creado exitosamente.");
    }

    @Override
    public List<Deporte> listar() {
        return deportes;
    }

    @Override
    public void actualizar(Deporte deporte) {
        for (int i = 0; i < deportes.size(); i++) {
            if (deportes.get(i).equals(deporte)) {
                deportes.set(i, deporte);
                return;
            }
        }
    }

    @Override
    public void eliminar(Deporte deporte) {
        List<Entrenador> entrenadores = entrenadorCRUD.listar();
        List<SesionEntrenamiento> sesionesEntrenamientos = sesionEntrenamientoCRUD.listar();

        // Recopilar sesiones a eliminar
        List<SesionEntrenamiento> sesionesAEliminar = new ArrayList<>();
        for (SesionEntrenamiento sesion : sesionesEntrenamientos) {
            if (sesion.getEntrenador().getEspecialidad().equals(deporte)) {
                sesionesAEliminar.add(sesion);
            }
        }
    
        // Eliminar sesiones
        for (SesionEntrenamiento sesion : sesionesAEliminar) {
            sesionEntrenamientoCRUD.eliminar(sesion);
            System.out.println("La sesión " + sesion.getFecha() + " eliminada.");
        }
    
        // Recopilar entrenadores a eliminar
        List<Entrenador> entrenadoresAEliminar = new ArrayList<>();
        for (Entrenador entrenador : entrenadores) {
            if (entrenador.getEspecialidad().equals(deporte)) {
                entrenadoresAEliminar.add(entrenador);
            }
        }
    
        // Eliminar entrenadores
        for (Entrenador entrenador : entrenadoresAEliminar) {
            entrenadorCRUD.eliminar(entrenador);
            System.out.println("Entrenador " + entrenador.getNombre() + " eliminado junto con el deporte " + deporte.getNombre());
        }
    
        deportes.remove(deporte);
        System.out.println("Deporte eliminado exitosamente.");
    }
    
}