package com.example.aitor.clienterest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class Anadir extends Activity {

    private Spinner spinertipo, spinerprofe, spinergrupo, spinerdep;
    private View layoutfecha, layouthora;
    private ImageButton btnhsa, btnhre, btnfsa, btnfre;
    private static TextView hsa, hre, fsa, fre;
    private EditText descri, lugarsal, lugarre;
    //private ArrayList<Actividad> actividades;
    private Actividad actividadModificable;
    private ArrayList<Profesor> profesores;
    private ArrayList<Grupo> grupos;
    Actividad actividad = null;
    private final static String URL_BASE = "http://ieszv.x10.bz/restful/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir);
        if (getResources().getBoolean(R.bool.tablet))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //actividades = (ArrayList<Actividad>) getIntent().getExtras().get("actividades");

        /*if(getIntent().getExtras().getString("accion").compareTo("modificar")==0){
            rellenarEdicion();
            actividadModificable=new Actividad();
            actividadModificable= getIntent().getParcelableExtra("actividad");
        }*/

        profesores = (ArrayList<Profesor>) getIntent().getExtras().get("profesores");
        grupos = (ArrayList<Grupo>) getIntent().getExtras().get("grupos");
        layouthora = (View) findViewById(R.id.hora);
        layoutfecha = (View) findViewById(R.id.fecha);
        descri = (EditText) findViewById(R.id.descri);
        lugarsal = (EditText) findViewById(R.id.lugar);
        lugarre = (EditText) findViewById(R.id.regreso);
        spinerprofe = (Spinner) findViewById(R.id.spinner2);
        spinergrupo = (Spinner) findViewById(R.id.spinner3);
        spinerdep = (Spinner) findViewById(R.id.spinner4);
        btnhsa = (ImageButton) findViewById(R.id.btnhsal);
        btnhre = (ImageButton) findViewById(R.id.btnhre);
        btnfsa = (ImageButton) findViewById(R.id.btnfsa);
        btnfre = (ImageButton) findViewById(R.id.btnfre);
        hsa = (TextView) findViewById(R.id.hsal);
        hre = (TextView) findViewById(R.id.hre);
        fre = (TextView) findViewById(R.id.fre);
        fsa = (TextView) findViewById(R.id.fsal);
        //invisible();
        spinertipo = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinertipo.setAdapter(adapter);

        spinertipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    visibleComplementaria();
                }else {
                    visibleExtraordinaria();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> nombreProfesores = new ArrayList();
        ArrayList<String> nombreGrupos = new ArrayList<>();
        for (int i = 0; i < profesores.size(); i++) {
            nombreProfesores.add(profesores.get(i).getNombre() + " " + profesores.get(i).getApellidos());
        }
        for (Grupo g : grupos) {
            nombreGrupos.add(g.getGrupo());
        }
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, nombreProfesores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerprofe.setAdapter(adapter2);


        ArrayAdapter<CharSequence> adapter4 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, nombreGrupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinergrupo.setAdapter(adapter4);
    }

   /* public void rellenarEdicion(){
        if(actividadModificable.getTipo().equals("complementaria"))
            spinertipo.setSelection(0);
        else{
            spinertipo.setSelection(1);
        }
        for(int i=0;i< profesores.size();i++){
            if(profesores.get(i).getId().equals(actividadModificable.getIdProfesor())){

            }

        }
    }*/

    public void visibleExtraordinaria() {
        spinerprofe.setVisibility(View.VISIBLE);
        spinergrupo.setVisibility(View.VISIBLE);
        descri.setVisibility(View.VISIBLE);
        lugarsal.setVisibility(View.VISIBLE);
        lugarre.setVisibility(View.VISIBLE);
        btnfre.setVisibility(View.VISIBLE);
        fre.setVisibility(View.VISIBLE);
        layoutfecha.setVisibility(View.VISIBLE);
        layouthora.setVisibility(View.VISIBLE);
    }

    public void visibleComplementaria() {
        spinerprofe.setVisibility(View.VISIBLE);
        spinergrupo.setVisibility(View.VISIBLE);
        descri.setVisibility(View.VISIBLE);
        lugarsal.setVisibility(View.VISIBLE);
        lugarre.setVisibility(View.INVISIBLE);
        btnfre.setVisibility(View.INVISIBLE);
        fre.setVisibility(View.INVISIBLE);
        layoutfecha.setVisibility(View.VISIBLE);
        layouthora.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anadir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            crearActividad();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* -------------------------------- OBTENEMOS EL ID DEL GRUPO Y PROFESOR SELECCIONADO EN EL SPINNER-----------------------------------*/

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
            if (nombreProfe.compareTo(profe.getNombre() + " " + profe.getApellidos()) == 0) {
                return profe.getId();
            }
        }
        return null;
    }


    /*-----------------------------------METODO PARA CREAR EL OBJETO ACTIVIDAD Y OBTENER EL JSONOBJET-------------------------------------*/

    public void crearActividad() {
        if (spinertipo.getSelectedItem().toString().compareTo("complementaria") == 0) {
            actividad = new Actividad(daIdProfesor(spinerprofe.getSelectedItem().toString()), spinertipo.getSelectedItem().toString(),
                    fsa.getText().toString() + " " + hsa.getText().toString(), fsa.getText().toString() + " " + hre.getText().toString()
                    , lugarsal.getText().toString(), lugarsal.getText().toString(),
                    descri.getText().toString(), "Aitor");
        } else {
            actividad = new Actividad(daIdProfesor(spinerprofe.getSelectedItem().toString()), spinertipo.getSelectedItem().toString(),
                    fsa.getText().toString() + " " + hsa.getText().toString(), fre.getText().toString() + " " + hre.getText().toString()
                    , lugarsal.getText().toString(), lugarre.getText().toString(),
                    descri.getText().toString(), "Aitor");
        }
        JSONObject objetoActividad = actividad.getJSON();
        PasarParametrosPost parametrosPost = new PasarParametrosPost();
        parametrosPost.url = URL_BASE + "actividad";
        parametrosPost.objeto = objetoActividad;
        PostRestful get = new PostRestful();
        get.execute(parametrosPost);
    }


    /*-------------------------------------HEBRA PARA INSERTAR LAS ACTIVIDADES----------------------------------------------*/

   private class PostRestful extends AsyncTask<PasarParametrosPost, Void, String>{

    @Override
    protected String doInBackground(PasarParametrosPost... params) {
        Log.v("parametros",params[0].url + " " + params[0].objeto.toString());
        try {
            return ClienteRestFul.post(params[0].url,params[0].objeto);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject objetogrupo=new JSONObject();
            objetogrupo.put("idactividad",id.getString("r"));
            objetogrupo.put("idgrupo",daIdGrupo(spinergrupo.getSelectedItem().toString()));
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

                }else{
                    tostada("Actividad no insertada");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent=new Intent(Anadir.this,Principal.class);
            startActivity(intent);
            finish();
        }
    }
     static class PasarParametrosPost{
        JSONObject objeto;
        String url;
    }



    /*-------------------------------------------ESTO SON SOLO LOS PICKER---------------------------------------------------------*/
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        int tvRef;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hora, minuto;

            if(String.valueOf(hourOfDay).length() == 1)
                hora = "0" + hourOfDay;
            else
                hora = hourOfDay+"";
            if(String.valueOf(minute).length() == 1)
                minuto = "0" + minute;
            else
                minuto = minute+"";

            if(getTag().toString().equals("horaInicio"))
                hsa.setText(hora + ":" + minuto);
            else
                hre.setText(hora + ":" + minuto);
        }
    }

    public void hora(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "horaInicio");
    }

    public void hora2(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "horaFin");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dia, mes, anio;

            if(String.valueOf(day).length() == 1)
                dia = "0" + day;
            else
                dia = day+"";
            if(String.valueOf(month).length() == 1)
                mes = "0" + (month+1);
            else
                mes = (month+1)+"";

            if(getTag().toString().equals("fechaInicio"))
                fsa.setText(year + "-" + mes + "-" + dia);
            else
                fre.setText(year + "-" + mes + "-" + dia);
        }
    }

    public void fecha(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "fechaInicio");
    }

    public void fecha2(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "fechaFin");
    }




    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}

