package com.muxiandroid.muxiandroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ExSaveInstanceState implements Serializable {

    public static final String KEY = "VETOR";

    public ArrayList<HashMap<String, String>> lista = new ArrayList<HashMap<String, String>>();

    public ExSaveInstanceState(ArrayList<HashMap<String, String>> lista) {
        this.lista = lista;
    }
}
