package com.waruna.notes2.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    private static OkHttpClient okHttpClient;

    public static Retrofit getInstance(NetworkConnectionInterceptor interceptor){
        if (instance == null)
            okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(interceptor)
                    .build();
            instance = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }

    private RetrofitClient() {
    }
}
