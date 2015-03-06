package com.aaron.zapatillasdb4o;

/**
 * Created by Aaron Perez on 20/11/2014.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 2dam on 17/10/2014.
 */
public class Adaptador extends ArrayAdapter<Zapatillas> {
    private Context contexto;
    private ArrayList<Zapatillas> zapas;
    private int recurso;
    private LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Zapatillas> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.zapas=objects;
        this.recurso=resource;
        this.i= (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder{
        public TextView tvM,tvC,tvP;
        public ImageView ivM;
        public int posicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh= null;
        //El if entra cuando se crea el ViewHolder por primera vez
        if(convertView == null){
            convertView= i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvM = (TextView)convertView.findViewById(R.id.tvModeloE);
            vh.tvC = (TextView)convertView.findViewById(R.id.tvCaracteristicasE);
            vh.tvP = (TextView)convertView.findViewById(R.id.tvPeso);
            vh.ivM = (ImageView)convertView.findViewById(R.id.ivMarca);
            convertView.setTag(vh);
        }
        else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.tvM.setText(zapas.get(position).getModelo());
        vh.tvC.setText(zapas.get(position).getCaract());
        vh.tvP.setText(zapas.get(position).getPeso());
        vh.ivM.setImageResource(zapas.get(position).getMarca());
        vh.posicion=position;
        return convertView;
    }


}
