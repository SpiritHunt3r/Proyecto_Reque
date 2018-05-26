package com.example.juan.proyecto_reque.Clases;

public class Pelicula {
    private String nombre;
    private String director;
    private String anno;
    private String genero;
    private String actores;
    private String descripcion;
    private String foto = null;
    private Voto[] votos = null;
    private String keywords;

    public Pelicula() {

    }

    public Pelicula(String foto,String nombre, String director, String anno, String genero,String descripcion, String actores, String keywords) {
        this.foto = foto;
        this.nombre = nombre;
        this.director = director;
        this.anno = anno;
        this.genero = genero;
        this.descripcion =descripcion;
        this.actores = actores;
        this.keywords = keywords;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public Voto[] getVotos() {
        return votos;
    }

    public void setVotos(Voto[] votos) {
        this.votos = votos;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public float genCalification(){
        float cal = 0.0f;
        if (this.votos != null) {
            if (this.votos.length != 0) {
                for (int i = 0; i < this.votos.length; i++) {
                    cal += this.votos[i].getCalificacion();
                }
                cal = cal / this.votos.length;
            }
        }
        return cal;
    }
}
