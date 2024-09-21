package com.lab1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.lab1.model.Utilidades.Utilidades;

public class MiembroCRUD implements CRUD<Miembro> {
    private List<Miembro> miembros = new ArrayList<>();
    private SesionEntrenamientoCRUD sesionEntrenamientoCRUD;

    public MiembroCRUD(SesionEntrenamientoCRUD sesionEntrenamientoCRUD) {
        this.sesionEntrenamientoCRUD = sesionEntrenamientoCRUD;
    }

    public MiembroCRUD(){
        
    }

    public void setSesionEntrenamientoCRUD(SesionEntrenamientoCRUD sesionEntrenamientoCRUD) {
        this.sesionEntrenamientoCRUD = sesionEntrenamientoCRUD;
    }

    @Override
    public void crear(Miembro miembro) {
        
        for (Miembro m : miembros) {
            if (m.getNumeroId() == miembro.getNumeroId()) {
                Utilidades.getInstance().escribirLog(MiembroCRUD.class, "Ya existe un miembro con la misma identificación." , Level.WARNING);
                return; // Salir sin agregar el miembro
            }
        }
        miembros.add(miembro);
        Utilidades.getInstance().escribirLog(MiembroCRUD.class, "Miembro creado exitosamente." , Level.INFO);
    }

    @Override
    public List<Miembro> listar() {
        return miembros;
    }

    @Override
    public void actualizar(Miembro miembro) {
        for (int i = 0; i < miembros.size(); i++) {
            if (miembros.get(i).equals(miembro)) {
                miembros.set(i, miembro);
                Utilidades.getInstance().escribirLog(MiembroCRUD.class, "Se actualizo el miembro", Level.INFO );
                return;
            }
        }
    }

    @Override
    public void eliminar(Miembro miembro) {
        List<SesionEntrenamiento> sesiones = sesionEntrenamientoCRUD.listar();
        for(SesionEntrenamiento s : sesiones){
            for (int i = 0; i < s.getMiembros().size(); i++) {
                if(s.getMiembros().get(i).equals(miembro)){
                    s.getMiembros().remove(i);
                    Utilidades.getInstance().escribirLog(MiembroCRUD.class, "Se elimino el miembro " + miembro + " de la sesion " + s.getFecha() +" con el entrenador"+ s.getEntrenador() , Level.INFO);

                }
            }
        }
        miembros.remove(miembro);
        Utilidades.getInstance().escribirLog(MiembroCRUD.class, "Miembro eliminado exitosamente." , Level.INFO);
    }
}
