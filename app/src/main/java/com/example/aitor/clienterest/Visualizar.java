package com.example.aitor.clienterest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private final static String URL_BASE = "http://ieszv.x10.bz/restful/api/";
    private Actividad actividad,actividadModificada;
    private Profesor profesor;
    private Grupo grupo;
    private EditText nombre,edgrupo,departamento,tipo,fechaInicio,fechaFin,descripcion,lugarInicio,lugarFin;
    private ArrayList<Profesor> profesores;
    private ArrayList<Grupo>grupos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        actividad=getIntent().getParcelableExtra("actividad");
        profesor=getIntent().getParcelableExtra("profesor");
        grupo=getIntent().getParcelableExtra("grupo");
        profesores = (ArrayList<Profesor>) getIntent().getExtras().get("todosprofesores");
        grupos = (ArrayList<Grupo>) getIntent().getExtras().get("todosgrupos");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualizar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editar) {

            editar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public String daIdGrupo(String nombregrupo) {
        for (Grupo grupo : grupos) {
            if (nombregrupo.compareTo(grupo.getGrupo()) == 0) {
                return grupo.getId();
            }
        }
        return null;
    }

    public String daIdProfesor(String nombreProfe) {
        for (Profesor profe : profesores) {
            if (nombreProfe.compareTo(profe.getNombre()) == 0) {
                return profe.getId();
            }
        }
        return null;
    }

    public void editar(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Modificar elemento");
        LayoutInflater inflater = LayoutInflater.from(this);
        alert.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String n=nombre.getText().toString();
                String[] s=n.split(" ");
                actividadModificada = new Actividad(daIdProfesor(s[0]), tipo.getText().toString(),
                        fechaInicio.getText().toString(), fechaFin.getText().toString()
                        , lugarInicio.getText().toString(), lugarFin.getText().toString(),
                        descripcion.getText().toString(), "Aitor");
                tostada(actividadModificada+"");
                String [] datos={URL_BASE,"actividad/"+actividad.getId()};
                new DeleteRestful().execute(datos);

            }
        });
        alert.setNegativeButton("Cancelar", null);
        alert.show();


    }

    private class DeleteRestful extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String r="" ;
                r = ClienteRestFul.delete(params[0] + params[1]);

                return r;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            JSONObject objetoActividad = actividadModificada.getJSON();
            tostada(objetoActividad+"");
            PasarParametrosPost parametrosPost = new PasarParametrosPost();
            parametrosPost.url = URL_BASE + "actividad";
            parametrosPost.objeto = objetoActividad;
            PostRestful get = new PostRestful();
            get.execute(parametrosPost);
        }
    }

    private class PostRestful extends AsyncTask<PasarParametrosPost, Void, String>{

        @Override
        protected String doInBackground(PasarParametrosPost... params) {
            Log.v("parametros", params[0].url + " " + params[0].objeto.toString());
            try {
                String e=ClienteRestFul.post(params[0].url,params[0].objeto);
                tostada(e);
                return e;
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("ERRORRR", e.toString());
            }
            return "hola caracola";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject id=null;
            try {
                id=new JSONObject(s);
                tostada(id+"");
                JSONObject objetogrupo=new JSONObject();
                objetogrupo.put("idactividad",id.getString("r"));
                objetogrupo.put("idgrupo",daIdGrupo(edgrupo.getText().toString()));
                PasarParametrosPost paso=new PasarParametrosPost();
                paso.url=URL_BASE+"actividadgrupo";
                paso.objeto=objetogrupo;
                new PostActividadGrupo().execute(paso);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //----------------------------------------------HEBRA PARA INSERTAR LA ACTIVIDAD EN ACTIVIDADGRUPOS----------------------------------*/
    private class PostActividadGrupo extends AsyncTask<PasarParametrosPost,String,String>{

        @Override
        protected String doInBackground(PasarParametrosPost... params) {
            try {
                return ClienteRestFul.post(params[0].url,params[0].objeto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hola caracola";
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            try {
                JSONObject obj=new JSONObject(strings);
                if(!obj.getString("r").equals("0")){
                    tostada("Actividad insertada");
                    Intent intent=new Intent(Visualizar.this,Principal.class);
                    startActivity(intent);
                    finish();
                }else{
                    tostada("Actividad no insertada");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent=new Intent(Visualizar.this,Principal.class);
            startActivity(intent);
            finish();
        }
    }
    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    static class PasarParametrosPost{
        JSONObject objeto;
        String url;
    }
}
