package com.waruna.notes2.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static MyApi instance;
    private static OkHttpClient okHttpClient;

    public static MyApi getInstance(NetworkConnectionInterceptor interceptor) {

        /*Gson gson = new GsonBuilder()
                .setLenient()
                .create();*/

        /* .addConverterFactory(GsonConverterFactory.create(gson))*/

        /*HttpLoggingInterceptor intc = new HttpLoggingInterceptor();
        intc.setLevel(HttpLoggingInterceptor.Level.BODY);*/

        if (instance == null)
            okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(interceptor)
                    .build();
            instance = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://10.0.3.2/note-api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(MyApi.class);
        return instance;
    }

    private RetrofitClient() {
    }
}
