package com.muxiandroid.muxiandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


public class CustomizedListView extends AppCompatActivity {
    ListView list;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> lista = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        list = (ListView) findViewById( R.id.list );

        if(savedInstanceState != null) {
            ExSaveInstanceState  l = (ExSaveInstanceState) savedInstanceState.getSerializable(ExSaveInstanceState.KEY);
            lista = l.lista;
        }

        if(lista.size() >0  ) {
            adapter = new LazyAdapter(getApplicationContext(), lista);
            list.setAdapter(adapter);
        }
        else {
            new JsonAsyncTask().execute();
        }

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> item = new HashMap<String, String>();

                item = lista.get(position);
                Intent intent = new Intent(getApplicationContext(), Perfil.class);
                Bundle params = new Bundle();
                params.putString("name",  item.get("name"));
                params.putString("image", item.get("image"));
                params.putString("price", item.get("price"));
                intent.putExtras(params);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putSerializable(ExSaveInstanceState.KEY, new ExSaveInstanceState(lista) );
    }

    private class JsonAsyncTask extends AsyncTask<String, Integer, String > {

        String msgErro="Não ha frutas para pegar...";

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("https://raw.githubusercontent.com/muxidev/desafio-android/master/fruits.json");

            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String json = getStringFromInputStream(instream);
                    instream.close();

                    JSONObject obj = new JSONObject(json);
                    if (obj.getString("fruits").length() > 0) {
                        try {
                            JSONArray jsonArray = new JSONArray( obj.getString("fruits") );

                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                JSONObject obj2 = new JSONObject( jsonArray.getString(i) );
                                map.put("name",  obj2.getString("name"));
                                map.put("image", obj2.getString("image"));
                                map.put("price",String.valueOf( obj2.getInt("price")));
                                lista.add(map);
                            }

                            msgErro=null;
                        } catch (Exception e) { }
                    }
                }
            }
            catch (SocketTimeoutException e ) { msgErro="Timeout - Tempo de conexao esgotado!";                  e.printStackTrace(); }
            catch (ConnectException e)        { msgErro="Servidor em manutenção. Por favor, tente mais tarde!";  e.printStackTrace(); }
            catch (Exception e)               { msgErro="Servidor em manutenção. Por favor, tente mais tarde!";  e.printStackTrace(); }

            return msgErro;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            
            adapter = new LazyAdapter(getApplicationContext(), lista);
            list.setAdapter(adapter);

        }
    }

    public static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder  sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
