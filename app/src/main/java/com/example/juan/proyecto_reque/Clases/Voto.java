package com.example.juan.proyecto_reque.Clases;

public class Voto {
    private String username;
    private Float Calificacion;

    public Voto(String username, Float calificacion) {

        this.username = username;
        this.Calificacion = calificacion;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(Float calificacion) {
        Calificacion = calificacion;
    }


}
