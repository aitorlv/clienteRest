package com.example.aitor.clienterest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Visualizar extends Activity {
    private Actividad actividad;
    private Profesor profesor;
    private Grupo grupo;
    private EditText nombre,edgrupo,departamento,tipo,fechaInicio,fechaFin,descripcion,lugarInicio,lugarFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);
        if(getResources().getBoolean(R.bool.tablet))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        actividad=getIntent().getParcelableExtra("actividad");
        profesor=getIntent().getParcelableExtra("profesor");
        grupo=getIntent().getParcelableExtra("grupo");
        nombre=(EditText)findViewById(R.id.spinnerNombre);
        edgrupo=(EditText)findViewById(R.id.spinnerGrupo);
        departamento=(EditText)findViewById(R.id.tvDepartamento);
        tipo=(EditText)findViewById(R.id.tvTipo);
        fechaInicio=(EditText)findViewById(R.id.tvInicio);
        fechaFin=(EditText)findViewById(R.id.tvFin);
        descripcion=(EditText)findViewById(R.id.tvDescripcionDetalle);
        lugarInicio=(EditText)findViewById(R.id.tvLugarI);
        lugarFin=(EditText)findViewById(R.id.tvLugarF);
        cargarDatos();

    }


    public void cargarDatos(){
            nombre.setText(profesor.getNombre()+" "+profesor.getApellidos());
            edgrupo.setText(grupo.getGrupo());
            departamento.setText(profesor.getDepartamento());
            tipo.setText(actividad.getTipo());
            fechaInicio.setText(actividad.getFechaI());
            fechaFin.setText(actividad.getFechaF());
            descripcion.setText(actividad.getDescripcion());
            lugarInicio.setText(actividad.getLugarI());
            lugarFin.setText(actividad.getLugarF());
    }




}
