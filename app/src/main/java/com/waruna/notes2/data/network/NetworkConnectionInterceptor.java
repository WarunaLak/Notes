package com.waruna.notes2.data.network;

import android.content.Context;
import android.net.ConnectivityManager;

import com.waruna.notes2.util.NoInternetException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NetworkConnectionInterceptor implements Interceptor {
    private Context context;

    public NetworkConnectionInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isInternetAvailable()){
            throw new NoInternetException("Internet connection is not available.");
        }

        return chain.proceed(chain.request());
    }

    private boolean isInternetAvailable(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return  manager.getActiveNetworkInfo() != null ;//&& manager.isConnected
    }
}
