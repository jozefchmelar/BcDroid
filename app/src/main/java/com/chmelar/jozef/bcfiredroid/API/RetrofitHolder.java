package com.chmelar.jozef.bcfiredroid.API;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitHolder {
    private static final String BASE_URL = "https://bcdroid.herokuapp.com/";
    private static RetrofitHolder ourInstance;
    private static Retrofit retrofit;
    private static Gson gson;
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private static OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    private RetrofitHolder() {
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized IRoutes getClient() {
        if (retrofit == null) {
            ourInstance = new RetrofitHolder();
            return ourInstance.retrofit.create(IRoutes.class);
        } else return ourInstance.retrofit.create(IRoutes.class);
    }
}