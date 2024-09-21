package com.lab1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.lab1.model.Utilidades.Utilidades;


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
        Utilidades.getInstance().escribirLog(SesionEntrenamientoCRUD.class, "Se creo la sesión", Level.INFO );
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
                Utilidades.getInstance().escribirLog(SesionEntrenamientoCRUD.class, "Se actualizo la sesión", Level.INFO );
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
                Utilidades.getInstance().escribirLog(SesionEntrenamientoCRUD.class, "Se elimino la sesión "+ sesion + " del entrenador "+ entrenador, Level.INFO );
            }
        }
    
        // Eliminar la sesión del CRUD de sesiones
        sesiones.remove(sesion);
        Utilidades.getInstance().escribirLog(SesionEntrenamientoCRUD.class, "Se elimino la sesión", Level.INFO );
    }

}