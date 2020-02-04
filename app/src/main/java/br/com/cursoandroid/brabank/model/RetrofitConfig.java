package br.com.cursoandroid.brabank.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static Retrofit retrofit;

    // Url base da consulta da nossa API
    // O que utilizamos é o padrão definido para o emulador poder acessar o nosso localhost

    private static final String BASE_URL = "http://10.0.2.2:8080";

    //Criamos o método no padrão Singletown
    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }


        return retrofit;
    }




}
