package com.aaron.zapatillasdb4o;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Main extends ActionBarActivity {

    private ArrayList<Zapatillas> zapas = new ArrayList<Zapatillas>();
    private ListView lv;
    private Adaptador ad;
    private int index;
    private static ObjectContainer bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) +"/bdJuegos.db4o");
        initComponents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bd.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_agregar) {
            edicion(0, "add");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***********Activities************/
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode){
                case ACTIVIDAD_ELIMINAR:
                    index= data.getIntExtra("index",0);
                    eliminar(zapas.get(index));
                    zapas.remove(index);
                    break;
                case ACTIVIDAD_EDITAR:
                    index= data.getIntExtra("index",0);
                    String opcion=data.getStringExtra("opcion");
                    Zapatillas z=data.getParcelableExtra("zapa");
                    if(!zapas.contains(z)) {
                        if (opcion.contains("edit")) {
                            zapas.get(index).setModelo(z.getModelo());
                            zapas.get(index).setCaract(z.getCaract());
                            zapas.get(index).setPeso(z.getPeso());
                            zapas.get(index).setMarca(z.getMarca());
                            modificar(index,z);

                        } else {
                            zapas.add(z);
                            Log.v("z", z.toString());
                            insertar(z);
                        }
                        ordenar();
                    }else{
                        tostada(R.string.mensajeCancel);
                }
                    break;
            }
            ad.notifyDataSetChanged();
        }
        else{
            Log.v("data", (data == null) + "");
            tostada(R.string.mensajeCancel);
        }
    }

    /* Desplegar menú contextual*/
    @Override
    public void onCreateContextMenu(ContextMenu main, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(main, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.contextual, main);
    }

    /* Al seleccionar elemento del menú contextual */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;
        if (id == R.id.action_editar) {
            edicion(index, "edit");
            return true;
        }else if (id == R.id.action_eliminar) {
            eliminar(index);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /*         Auxiliares        */
    /*****************************/

    private void initComponents(){
        ad = new Adaptador(this, R.layout.elemento, zapas);
        lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
        zapas.clear();
        mostrar();
    }

    //Ordena las zapatillas alfabéticamente
    public void ordenar(){
        Collections.sort(zapas);
    }

    /* Mostramos un mensaje flotante a partir de un recurso string*/
    private void tostada(int s){
        Toast.makeText(this, getText(s), Toast.LENGTH_SHORT).show();
    }


    /*        Menús          */
    /*************************/
    private final int ACTIVIDAD_ELIMINAR = 1;
    public void eliminar(int index){
        Intent i = new Intent(this,Eliminar.class);
        Bundle b = new Bundle();
        b.putString("mod", zapas.get(index).getModelo());
        b.putString("car", zapas.get(index).getCaract());
        b.putString("pes", zapas.get(index).getPeso());
        b.putInt("img", zapas.get(index).getMarca());
        b.putInt("index", index);
        i.putExtras(b);
        startActivityForResult(i, ACTIVIDAD_ELIMINAR);
    }

    private final int ACTIVIDAD_EDITAR = 2;
    public void edicion(int index,String opcion){
        Intent i = new Intent(this,Edicion.class);
        Bundle b = new Bundle();
        Zapatillas z;
        if(zapas.size()>0){
            z=zapas.get(index);
        }
        else{
            z=new Zapatillas();
        }
        b.putString("opcion",opcion);
        b.putInt("index", index);
        b.putParcelable("zapa",z);
        i.putExtras(b);
        startActivityForResult(i, ACTIVIDAD_EDITAR);
    }


    /* ******* DB4o *********** */
    private EmbeddedConfiguration dbConfig() throws IOException{
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().add(new AndroidSupport());
        configuration.common().objectClass(Zapatillas.class).objectField("modelo").indexed(true);
        configuration.common().objectClass(Zapatillas.class).cascadeOnUpdate(true);
        configuration.common().objectClass(Zapatillas.class).cascadeOnActivate(true);
        return configuration;
    }

    public void insertar(Zapatillas z) {
        bd.store(z);
        bd.commit();
    }

    public void mostrar() {
        Query consulta = bd.query();
        consulta.constrain(Zapatillas.class);
        ObjectSet<Zapatillas> zapatillas = consulta.execute();

        Log.v("tamaño",zapatillas.size()+"");
        for (Zapatillas z : zapatillas) {
            if (zapatillas != null) {
                zapas.add(z);
            } else{
                tostada(R.string.nohay);
            }
        }
    }

    public void eliminar(Zapatillas z) {
        bd.delete(z);
        bd.commit();
    }

    public void modificar(final int index, final Zapatillas zapa) {
        ObjectSet<Zapatillas> zapatillas = bd.query(new Predicate<Zapatillas>() {
            @Override
            public boolean match(Zapatillas z) {
                return z.equals(zapas.get(index));
            }
        });
        if(zapatillas.hasNext()){
            Zapatillas z = zapatillas.next();
            z.setMarca(zapa.getMarca());
            z.setCaract(zapa.getCaract());
            z.setModelo(zapa.getModelo());
            z.setPeso(zapa.getPeso());
            bd.store(z);
            bd.commit();
        }
    }
}
