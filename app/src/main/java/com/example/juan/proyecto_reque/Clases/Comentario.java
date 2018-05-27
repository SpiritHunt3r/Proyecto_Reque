package com.example.juan.proyecto_reque.Clases;

public class Comentario {

    private String username;
    private String comentario;

    public Comentario( String username, String comentario) {
        this.username = username;
        this.comentario = comentario;
    }

    public Comentario() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
