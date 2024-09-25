package com.lab1.model;

import java.io.Serializable;

public class Miembro implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private String email;
    private int numeroId;
    private Etapa etapa;

    public Miembro(){
        
    }
    public Miembro(String nombre, String email, int numeroId, Etapa etapa) {
        this.nombre = nombre;
        this.email = email;
        this.numeroId = numeroId;
        this.etapa = etapa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumeroId() {
        return numeroId;
    }

    public void setNumeroId(int numeroId) {
        this.numeroId = numeroId;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }
    
    @Override
    public String toString() {
        return "[Nombre: " + nombre + " Email: " + email + " NumeroId: " + numeroId +" Etapa: " + etapa + " ]";
    }


}