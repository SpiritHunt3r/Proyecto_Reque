package com.example.juan.proyecto_reque.Clases;

public class Voto {
    private String username;
    private Double Calificacion;

    public Voto(String username, Double calificacion) {

        this.username = username;
        this.Calificacion = calificacion;
    }

    public Voto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(Double calificacion) {
        Calificacion = calificacion;
    }


}
