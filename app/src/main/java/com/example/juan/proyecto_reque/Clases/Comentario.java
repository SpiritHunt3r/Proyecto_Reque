package com.example.juan.proyecto_reque.Clases;

public class Comentario {

    private String email;
    private String username;
    private String comentario;

    public Comentario(String email, String username, String comentario) {
        this.username = username;
        this.comentario = comentario;
        this.email = email;
    }

    public Comentario() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
