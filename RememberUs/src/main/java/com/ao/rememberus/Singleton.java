package com.ao.rememberus;

import android.content.Context;

import java.util.ArrayList;


public class Singleton {

    //-----Singleton------------------------------------
    private static Singleton instance = null;

    private Context context;
    private ArrayList<Item> results = new ArrayList<Item>();



    private Singleton(Context context) {
        this.context = context;
    }

    public static synchronized Singleton getInstance(Context context) {
        if(instance == null) {
            instance = new Singleton(context);
        }
        return instance;
    }
    //--------------------------------------------



    public ArrayList<Item> getArrayList(){
        return results;
    }
}

