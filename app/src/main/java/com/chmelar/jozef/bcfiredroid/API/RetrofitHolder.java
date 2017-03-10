package com.chmelar.jozef.bcfiredroid.API;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHolder {
    private final String BASE_URL = "https://bcdroid.herokuapp.com/";
    private static RetrofitHolder ourInstance;
    private static Retrofit retrofit;
    private IRoutes bcDroidService;
    private OkHttpClient client;
    String TAG = "retrohodler";

    public static RetrofitHolder getInstance() {
        if (retrofit == null) {
            ourInstance = new RetrofitHolder();
            return ourInstance;
        } else return ourInstance;
    }

    private RetrofitHolder() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bcDroidService = retrofit.create(IRoutes.class);
    }

    public IRoutes getBcDroidService(OkHttpClient client) {
        if (client != this.client) {
            this.client = client;
            bcDroidService = retrofit.newBuilder().client(client).build().create(IRoutes.class);
            return bcDroidService;

        } else {
            return bcDroidService;
        }
    }
}
