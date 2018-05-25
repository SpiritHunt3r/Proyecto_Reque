package com.example.juan.proyecto_reque.Clases;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;

public class Usuario {
    private String email;
    private String nombre;
    private Boolean is_active;
    private Boolean is_admin;


    public Usuario(String email,String nombre, Boolean is_active, Boolean is_admin) {
        this.nombre = nombre;
        this.email = email;
        this.is_active = is_active;
        this.is_admin = is_admin;

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

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }




    }
