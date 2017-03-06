package com.chmelar.jozef.bcfiredroid.API;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitHolder {
    private  final String BASE_URL = "https://bcdroid.herokuapp.com/";
    private static RetrofitHolder ourInstance;
    private static Retrofit retrofit;
    private  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private  OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    private IRoutes bcDroidService;

    private RetrofitHolder() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
        bcDroidService = retrofit.create(IRoutes.class);
    }

    public static  RetrofitHolder getClient() {
        if (retrofit == null) {
            ourInstance = new RetrofitHolder();
            return ourInstance;
        } else return ourInstance;
    }

    public IRoutes getBcDroidService(){
        return bcDroidService;
    }

}
