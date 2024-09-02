package com.lab1.model;

import java.util.ArrayList;
import java.util.List;

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
                System.out.println("Error: Ya existe un miembro con la misma identificación.");
                return; // Salir sin agregar el miembro
            }
        }
        miembros.add(miembro);
        System.out.println("Miembro creado exitosamente.");
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

                }
            }
        }
        miembros.remove(miembro);
    }
}
