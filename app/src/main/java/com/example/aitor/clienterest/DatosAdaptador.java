package com.example.aitor.clienterest;

/**
 * Created by aitor on 15/02/2015.
 */
public class DatosAdaptador {
    private String idActividad ,nombreGrupo,fechainico,profesor;


    public DatosAdaptador(String idActividad,String nombreGrupo, String fechainico, String profesor) {
        this.idActividad=idActividad;

        this.nombreGrupo = nombreGrupo;
        this.fechainico = fechainico;
        this.profesor = profesor;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getFechainico() {
        return fechainico;
    }

    public void setFechainico(String fechainico) {
        this.fechainico = fechainico;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}
