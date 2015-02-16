package com.example.aitor.clienterest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aitor on 15/02/2015.
 */
public class ActividadGrupo {
    private String idActividad,idGrupo;

    public ActividadGrupo(String idActividad, String idGrupo) {
        this.idActividad = idActividad;
        this.idGrupo = idGrupo;
    }

    public ActividadGrupo(JSONObject object) throws JSONException {
        this.idActividad=object.getString("idactividad");
        this.idGrupo= object.getString("idgrupo");
    }
    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }
}
