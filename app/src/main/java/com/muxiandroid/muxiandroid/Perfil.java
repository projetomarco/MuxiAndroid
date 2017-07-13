package com.muxiandroid.muxiandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {

    public ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        TextView name   = (TextView) findViewById(R.id.name);
        TextView priceD = (TextView) findViewById(R.id.priceD);
        TextView priceR = (TextView) findViewById(R.id.priceR);
        ImageView image = (ImageView)findViewById(R.id.image);
        Button   voltar = (Button)   findViewById(R.id.Voltar);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params!=null) {
            imageLoader = new ImageLoader( getApplicationContext() );
            imageLoader.DisplayImage(params.getString("image"), image);
            name.setText  ( params.getString("name") );
            double valor = Double.valueOf( params.getString("price") );
            double valorR;
            valorR = (valor * 3.5);

            priceD.setText("$ " + String.valueOf(valor) );
            priceR.setText("R$ "+ String.valueOf(valorR) );
        }

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
