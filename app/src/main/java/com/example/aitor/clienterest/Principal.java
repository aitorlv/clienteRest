package com.example.aitor.clienterest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;


public class Principal extends Activity {

    private TextView tvInicial;
    private final static String URL_BASE = "http://ieszv.x10.bz/restful/api/";
    private ListView lv;
    private Adaptador ad;
    private ArrayList<Actividad> actividades;
    private ArrayList<Profesor> profesores;
    private ArrayList<Grupo> grupos;
    private ArrayList<ActividadGrupo> actividadGrupos;
    private ArrayList<DatosAdaptador>arrayParaAdaptador;
    private static int VISU=13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        actividades = new ArrayList();
        profesores = new ArrayList<>();
        grupos = new ArrayList<>();
        actividadGrupos=new ArrayList<>();
        arrayParaAdaptador=new ArrayList<>();
        cargarActividades();
        if(getResources().getBoolean(R.bool.tablet))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lv = (ListView) findViewById(R.id.lvActividades);
        ad = new Adaptador(Principal.this, R.layout.detalle, arrayParaAdaptador);
        //lv.setAdapter(ad);
       // ad.notifyDataSetChanged();


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                borrar(actividades.get(position).getId(),position);
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent visualiza =new Intent(Principal.this,Visualizar.class);
                    visualiza.putExtra("actividad",actividades.get(position));
                    visualiza.putExtra("profesor",daObjetoProfesor(actividades.get(position).getIdProfesor()));
                    visualiza.putExtra("grupo", daObjetoGrupo(actividades.get(position).getId()));
                    visualiza.putExtra("todosprofesores", profesores);
                    visualiza.putExtra("todosgrupos", grupos);
                    startActivityForResult(visualiza,VISU);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            Intent intent = new Intent(this, Anadir.class);
            intent.putExtra("profesores", profesores);
            intent.putExtra("grupos", grupos);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    /*--------------------------------------------------metodos pra borrar---------------------------------------------------------*/
    public void borrar(final String id, final int posicion){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Borrar elemento");
        LayoutInflater inflater = LayoutInflater.from(this);
        alert.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String [] datos={URL_BASE,"actividad/"+id};
               new DeleteRestful().execute(datos);
                cargarActividades();
            }
        });
        alert.setNegativeButton("Cancelar", null);
        alert.show();
    }



    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
    public Profesor daObjetoProfesor(String id){
        String profe="";
        for (Profesor p:profesores){
            if(p.getId().compareTo(id)==0){
                return p;
            }
        }
        return null;
    }
    public Grupo daObjetoGrupo(String id){
        String grupo="";
        for (ActividadGrupo ac:actividadGrupos){
            if(ac.getIdActividad().compareTo(id)==0){
                String idgrupo=ac.getIdGrupo();
                for (Grupo g:grupos){
                    if(g.getId().compareTo(idgrupo)==0){
                        return g;
                    }
                }
            }
        }
        return null;
    }

/*---------------------------------------- SACAR EL GRUPO YE L PORFESOR A PARTIR DEL ID DE LA ACTIVIDAD-------------------------------------*/
    public String daProfesor(String id){
        String profe="";
        for (Profesor p:profesores){
            if(p.getId().compareTo(id)==0){
                return p.getNombre()+" "+p.getApellidos();
            }
        }
        return null;
    }
    public String daGrupo(String id){
        String grupo="";
        for (ActividadGrupo ac:actividadGrupos){
            if(ac.getIdActividad().compareTo(id)==0){
                String idgrupo=ac.getIdGrupo();
                for (Grupo g:grupos){
                    if(g.getId().compareTo(idgrupo)==0){
                        return g.getGrupo();
                    }
                }
            }
        }
        return null;
    }

    /*----------------------------------CARGAMOS TODOS LOS DATOS NECESARIOS ---------------------------------------------------*/

    private void cargarActividades() {
        /*petición get al servidor restful para obtener las actividades, recibo una lista en formato json, lo paso a arraylist y cargo el listview EN UNA HEBRA
         a la que le paso la URL*/
        actividades.clear();
        profesores.clear();
        grupos.clear();
        arrayParaAdaptador.clear();
        String[] peticiones = {"actividad/Aitor", "profesor", "grupo","actividadgrupo"};
        GetRestful get = new GetRestful();
        get.execute(peticiones);
    }
    private class GetRestful extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            try {
                JSONTokener tokenerActividad = new JSONTokener(ClienteRestFul.get(URL_BASE + params[0]));
                JSONTokener tokenerProfesor = new JSONTokener(ClienteRestFul.get(URL_BASE + params[1]));
                JSONTokener tokenerGrupo = new JSONTokener(ClienteRestFul.get(URL_BASE + params[2]));
                JSONTokener tokenerActividadGrupo = new JSONTokener(ClienteRestFul.get(URL_BASE + params[3]));
                JSONArray jsonArray = new JSONArray(tokenerActividad);
                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Actividad actividad = new Actividad(jsonObject);
                    actividades.add(actividad);
                }
                jsonArray = new JSONArray(tokenerProfesor);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Profesor profesor = new Profesor(jsonObject);
                    profesores.add(profesor);
                }
                jsonArray = new JSONArray(tokenerGrupo);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Grupo grupo = new Grupo(jsonObject);
                    grupos.add(grupo);
                }

                jsonArray = new JSONArray(tokenerActividadGrupo);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ActividadGrupo ag = new ActividadGrupo(jsonObject);
                    actividadGrupos.add(ag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] r) {
            super.onPostExecute(r);
            Log.v("tamaño",actividades.size()+"");


                    for (int i = 0; i < actividades.size(); i++) {
                        arrayParaAdaptador.add(new DatosAdaptador(actividades.get(i).getId(), daGrupo(actividades.get(i).getId()), actividades.get(i).getFechaI(),daProfesor(actividades.get(i).getIdProfesor()) ));
                        Log.v("adaptador",arrayParaAdaptador.size()+"");
                    }
                    lv.setAdapter(ad);



        }
    }



    /*--------------------------------------------------------HEBRA PARA BORRAR DATOS-------------------------------------------------------*/
    private class DeleteRestful extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String r="" ;
                arrayParaAdaptador.clear();
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
            for (int i = 0; i < actividades.size(); i++) {
                arrayParaAdaptador.add(new DatosAdaptador(actividades.get(i).getId(), daGrupo(actividades.get(i).getId()), actividades.get(i).getFechaI(),daProfesor(actividades.get(i).getId()) ));
                Log.v("adaptador",arrayParaAdaptador.size()+"");
            }
            lv.setAdapter(ad);

        }
    }

}
