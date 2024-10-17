package com.example.prn231.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public String URL_BASE ;
    private static Retrofit retrofit;

    public ApiClient(String URL_BASE) {
        this.URL_BASE = URL_BASE;
    }

    public Retrofit getClient() {
        if(retrofit == null){
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
