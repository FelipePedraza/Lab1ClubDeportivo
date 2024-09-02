package com.lab1.model;

import java.util.ArrayList;
import java.util.List;

public class EntrenadorCRUD implements CRUD<Entrenador> {

    private List<Entrenador> entrenadores = new ArrayList<>();
    private SesionEntrenamientoCRUD sesionEntrenamientoCRUD;

    public EntrenadorCRUD(SesionEntrenamientoCRUD sesionEntrenamientoCRUD) {
        this.sesionEntrenamientoCRUD = sesionEntrenamientoCRUD;
    }
    public EntrenadorCRUD(){
        
    }

    public void setSesionEntrenamientoCRUD(SesionEntrenamientoCRUD sesionEntrenamientoCRUD) {
        this.sesionEntrenamientoCRUD = sesionEntrenamientoCRUD;
    }
    @Override
    public void crear(Entrenador entrenador) {
        
        for (Entrenador e : entrenadores) {
            if (e.getNombre().toLowerCase().equals(entrenador.getNombre()) && e.getEspecialidad() == entrenador.getEspecialidad()) {
                System.out.println("Error: Ya existe un entranador con el mismo nombre.");
                return;
            }
        }
        entrenadores.add(entrenador);
        System.out.println("Entrenador creado exitosamente.");
    }

    @Override
    public List<Entrenador> listar() {
        return entrenadores;
    }

    @Override
    public void actualizar(Entrenador entrenador) {
        for (int i = 0; i < entrenadores.size(); i++) {
            if (entrenadores.get(i).equals(entrenador)) {
                entrenadores.set(i, entrenador);
                for(SesionEntrenamiento s : sesionEntrenamientoCRUD.listar()){
                    if (s.getEntrenador().equals(entrenadores.get(i))){
                        s.setDeporte(entrenadores.get(i).getEspecialidad());
                    }
                }
                
                return;
            }
        }
        
    }
    
    @Override
    public void eliminar(Entrenador entrenador) {
        List<SesionEntrenamiento> sesionesEntrenamientos = sesionEntrenamientoCRUD.listar();

        // Recopilar sesiones a eliminar
        List<SesionEntrenamiento> sesionesAEliminar = new ArrayList<>();
        for (SesionEntrenamiento sesion : sesionesEntrenamientos) {
            if (sesion.getEntrenador().equals(entrenador)) {
                sesionesAEliminar.add(sesion);
            }
        }

        // Eliminar sesiones
        for (SesionEntrenamiento sesion : sesionesAEliminar) {
            sesionEntrenamientoCRUD.eliminar(sesion);
            System.out.println("La sesión " + sesion.getFecha() + " eliminada junto con el entrenador " + entrenador.getNombre());
        }
    
        // Eliminar el entrenador
        entrenadores.remove(entrenador);
        System.out.println("Entrenador eliminado");
    }

}