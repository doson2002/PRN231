package com.example.prn231.Api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiClient {
    private static Retrofit commandRetrofit;
    private static Retrofit queryRetrofit;
    private final String URL_BASE;

    public ApiClient(String URL_BASE) {
        this.URL_BASE = URL_BASE;
    }

    public Retrofit getClient() {
        // Create logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create OkHttpClient with interceptors
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("accept", "*/*")
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        // Check which URL is being used and return appropriate Retrofit instance
        if (URL_BASE.equals(ApiEndPoint.BASE_URL_COMMAND)) {
            if (commandRetrofit == null) {
                commandRetrofit = new Retrofit.Builder()
                        .baseUrl(URL_BASE)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return commandRetrofit;
        } else {
            if (queryRetrofit == null) {
                queryRetrofit = new Retrofit.Builder()
                        .baseUrl(URL_BASE)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return queryRetrofit;
        }
    }
}
