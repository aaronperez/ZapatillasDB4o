package com.aaron.zapatillasdb4o;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Aaron on 24/11/2014.
 */
public class Edicion extends Activity {

    private Spinner spinner;
    private int index;
    private EditText etMod,etCar,etPes;
    private Zapatillas z;
    private String opcion;
    private TextView titulo;
    private Button bOK;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion);
        initComponent();
    }

    public void initComponent(){
        etMod=(EditText)findViewById(R.id.etModelo);
        etCar=(EditText)findViewById(R.id.etUsos);
        etPes=(EditText)findViewById(R.id.etPeso);
        titulo=(TextView)findViewById(R.id.tvTitulo);
        bOK=(Button)findViewById(R.id.bOK);
        iniciarSpinner();
        Bundle b = getIntent().getExtras();
        opcion=b.getString("opcion");
        if(b !=null){
            if(opcion.contains("edit")){
                z=b.getParcelable("zapa");
                titulo.setText(getText(R.string.tituloEditar));
                bOK.setText(getText(R.string.tituloEditar));
                etMod.setText(z.getModelo());
                etCar.setText(z.getCaract());
                etPes.setText(z.getPeso());
                for (int i = 0; i < 7; i++) {
                    if (spinner.getItemAtPosition(i).toString().substring(0, 3).equals(z.getModelo().substring(0, 3))) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            }
            else{
                titulo.setText(getText(R.string.tituloAgregar));
                bOK.setText(getText(R.string.tituloAgregar));
                etMod.setText("");
                etCar.setText("");
                etPes.setText("");
            }
            index=b.getInt("index");
        }
    }

    public void aceptar(View v){
        Intent i = new Intent();
        Bundle bundle= new Bundle();
        bundle.putInt("index", index);
        bundle.putString("opcion", opcion);
        Zapatillas z=rellena();
        bundle.putParcelable("zapa",z);
        i.putExtras(bundle);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public Zapatillas rellena(){
        int img=spinnerDraw();
        String c= spinner.getSelectedItem().toString()+" "+etMod.getText().toString();
        return new Zapatillas(c,etCar.getText().toString(),etPes.getText().toString(),img);
    }

    public void cancelar(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /*  Método para iniciar Spinner y escucharlo  */
    private void iniciarSpinner(){
        ArrayAdapter<CharSequence> stringArrayAdapter=ArrayAdapter.createFromResource(this,R.array.Marca,android.R.layout.simple_spinner_dropdown_item);
        spinner =(Spinner)findViewById(R.id.spinnerM);
        spinner.setAdapter(stringArrayAdapter);
    }


    /* Método que convierte la posición en
    * el spinner en la imagen que le corresponde*/
    private int spinnerDraw(){
        Context n=Edicion.this.getApplicationContext();
        Resources resources = n.getResources();
        String nombre=spinner.getSelectedItem().toString().toLowerCase();
        final int resourceId = resources.getIdentifier(prepararNombre(nombre), "drawable",
                n.getPackageName());
        return resourceId;
    }

    //Recoge el nombre del spinner y lo deja sin espacios
    public String prepararNombre(String n){
        if(n.contains(" ")){
            n=n.substring(0,n.indexOf(" "))+n.substring(n.indexOf(" ")+1,n.length());
        }
        return n;
    }
}