package com.chmelar.jozef.bcfiredroid;


import android.app.Application;
import android.util.Log;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class App extends Application {
    private static final String TAG = "App";
    private HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private OkHttpClient client;
    private Cache cache;

    public OkHttpClient getClient() {
        return client;
    }

    public void addToken(final String token) {
        Log.d(TAG, "addToken: pridany token");
        client = client.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request oldRequest = chain.request();
                Request newRequest = oldRequest.newBuilder()
                        .header("x-access-token", token)
                        .header("Content-Type", "application/json")
                        .build();
                Response r = chain.proceed(newRequest);
                return r;
            }
        }).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cache = new Cache(getCacheDir(), 2048);
        client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(cache)
                .build();
    }
}
