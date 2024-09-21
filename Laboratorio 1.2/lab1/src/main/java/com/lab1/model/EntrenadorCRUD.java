package com.lab1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.lab1.model.Utilidades.Utilidades;

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
                Utilidades.getInstance().escribirLog(EntrenadorCRUD.class, "Ya existe un entranador con el mismo nombre.", Level.WARNING);
                return;
            }
        }
        entrenadores.add(entrenador);
        Utilidades.getInstance().escribirLog(EntrenadorCRUD.class, "Entrenador creado exitosamente.", Level.INFO);
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
                Utilidades.getInstance().escribirLog(EntrenadorCRUD.class, "Se actualizo el entrenador", Level.INFO );
                for(SesionEntrenamiento s : sesionEntrenamientoCRUD.listar()){
                    if (s.getEntrenador().equals(entrenadores.get(i))){
                        s.setDeporte(entrenadores.get(i).getEspecialidad());
                        Utilidades.getInstance().escribirLog(EntrenadorCRUD.class, "Se actualizo el deporte de la sesion " + s.getFecha() + " con el entrenador " + entrenador , Level.INFO );
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
            Utilidades.getInstance().escribirLog(EntrenadorCRUD.class, "La sesión " + sesion.getFecha() + " eliminada junto con el entrenador " + entrenador.getNombre(), Level.INFO);
        }
    
        // Eliminar el entrenador
        entrenadores.remove(entrenador);
        Utilidades.getInstance().escribirLog(EntrenadorCRUD.class, "El entrenador " + entrenador.getNombre() + " fue eliminado", Level.INFO);
    }

}