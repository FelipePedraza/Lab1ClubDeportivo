package com.lab1.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import com.lab1.model.Utilidades.Utilidades;

public class SesionEntrenamiento implements Inscripcion{

    private LocalDate fecha;
    private int duracion;
    private Estado estado;
    private Deporte deporte;
    private Entrenador entrenador;
    private List<Miembro> miembros;

    public SesionEntrenamiento(LocalDate fecha, int duracion, Estado estado, Deporte deporte, Entrenador entrenador) {
        this.fecha = fecha;
        this.duracion = duracion;
        this.estado = estado;
        this.deporte = deporte;
        this.entrenador = entrenador;
        this.miembros = new ArrayList<Miembro>();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public List<Miembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Miembro> miembros) {
        this.miembros = miembros;
    }

    @Override
    public void inscribirMiembro(Miembro miembro){
        if(miembro.getEtapa() == Etapa.JUVENIL && deporte.getDificultad() == NivelDificultad.ALTO){
            Utilidades.getInstance().escribirLog(SesionEntrenamiento.class, "Ingrese a una categoria menor", Level.WARNING );
            throw new IllegalArgumentException("Juveniles no pueden estar en categorias altas");  
        }
        else{
            miembros.add(miembro);
            Utilidades.getInstance().escribirLog(SesionEntrenamiento.class, "Miembro Inscrito", Level.INFO);
        }
        
    }
    

    @Override
    public String toString() {
        return "[Fecha: " + fecha + "Duracion: " + duracion + "Estado: " + estado + "Deporte: "
                + deporte.getNombre() + ", entrenador=" + entrenador.getNombre() + ", miembros=" + miembros + "]";
    }
    

}