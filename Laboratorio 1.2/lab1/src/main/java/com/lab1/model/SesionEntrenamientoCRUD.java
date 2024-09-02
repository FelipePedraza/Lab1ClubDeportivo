package com.lab1.model;

import java.util.ArrayList;
import java.util.List;


public class SesionEntrenamientoCRUD implements CRUD<SesionEntrenamiento> {
    private List<SesionEntrenamiento> sesiones = new ArrayList<>();
    private EntrenadorCRUD entrenadorCRUD;

    public SesionEntrenamientoCRUD(EntrenadorCRUD entrenadorCRUD) {
        this.entrenadorCRUD = entrenadorCRUD;
    }
    
    public void setEntrenadorCRUD(EntrenadorCRUD entrenadorCRUD) {
        this.entrenadorCRUD = entrenadorCRUD;
    }
    
    @Override
    public void crear(SesionEntrenamiento sesion) {
        sesiones.add(sesion);
    }

    @Override
    public List<SesionEntrenamiento> listar() {
        return sesiones;
    }

    @Override
    public void actualizar(SesionEntrenamiento sesion) {
        for (int i = 0; i < sesiones.size(); i++) {
            if (sesiones.get(i).equals(sesion)) {
                sesiones.set(i, sesion);
                return;
            }
        }
    }

    @Override
    public void eliminar(SesionEntrenamiento sesion) {
        List<Entrenador> entrenadores = entrenadorCRUD.listar();

        
        for (Entrenador entrenador : entrenadores) {
            if (sesion.getEntrenador().equals(entrenador)) {
                entrenador.getSesionesEntrenaminetos().remove(sesion);
                entrenador.setSesionesEntrenamientos(entrenador.getSesionesEntrenaminetos());
            }
        }
    
        // Eliminar la sesión del CRUD de sesiones
        sesiones.remove(sesion);
    }

}