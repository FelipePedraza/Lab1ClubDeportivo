package com.lab1.model;

public class ClubDeportivo {
    private Administrador admin;

    //instancia (singleton)
    private static ClubDeportivo instancia;

    //constructor  privado para evitar instancias fuera de la clase.
    private ClubDeportivo() {
        this.admin = new Administrador("Admin", 789);
    }
    //metodo estatico para obtener la instancia(singleton)
    public static ClubDeportivo getInstancia(){
        if (instancia == null) {
            instancia = new ClubDeportivo();
        }
        return instancia;
    }

    public Administrador getAdmin() {
        return admin;
    }

    public void setAdmin(Administrador admin) {
        this.admin = admin;
    }

}
