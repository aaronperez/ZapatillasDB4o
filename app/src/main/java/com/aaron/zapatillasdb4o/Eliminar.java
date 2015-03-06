package com.aaron.zapatillasdb4o;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Eliminar extends Activity {

    private TextView tvMod,tvCar,tvPes;
    private ImageView ivMar;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        initComponent();
        }

    public void initComponent(){
        tvMod=(TextView)findViewById(R.id.tvModeloE);
        tvCar=(TextView)findViewById(R.id.tvCaracteristicasE);
        tvPes=(TextView)findViewById(R.id.tvPesoE);
        ivMar=(ImageView)findViewById(R.id.ivMarcaE);
        Bundle b = getIntent().getExtras();
        if(b !=null){
            tvMod.setHint(b.getString("mod"));
            tvCar.setHint(b.getString("car"));
            tvPes.setHint(b.getString("pes"));
            ivMar.setImageResource(b.getInt("img"));
            index=b.getInt("index");
        }
    }


    public void aceptar(View v){
        Intent i = new Intent();
        Bundle bundle= new Bundle();
        bundle.putInt("index", index);
        i.putExtras(bundle);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void cancelar(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
