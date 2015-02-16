package com.example.aitor.clienterest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 2dam on 28/01/2015.
 */
public class Adaptador extends ArrayAdapter {

    static class ViewHolder{
        public TextView tvGrupo,tvFecha,tvProfe;
        public int position;
    }

    private Context contexto;
    private ArrayList<DatosAdaptador> lista;
    private int recurso;
    private LayoutInflater i;
    public Adaptador(Context context, int resource, ArrayList<DatosAdaptador> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i =(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("LOG", "numero de elementos en el ArrayList: " + lista.size());

        ViewHolder vh = null;
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvGrupo = (TextView)convertView.findViewById(R.id.tvGrupo);
            vh.tvFecha=(TextView)convertView.findViewById(R.id.tvFecha);
            vh.tvProfe=(TextView)convertView.findViewById(R.id.tvProfesor);
            vh.position = position;
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvGrupo.setText(lista.get(position).getNombreGrupo());
        vh.tvFecha.setText(lista.get(position).getFechainico());
        vh.tvProfe.setText(lista.get(position).getProfesor());
        return convertView;
    }



}