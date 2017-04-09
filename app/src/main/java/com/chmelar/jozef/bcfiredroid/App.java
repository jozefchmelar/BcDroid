package com.chmelar.jozef.bcfiredroid;

import android.app.Application;
import com.chmelar.jozef.bcfiredroid.API.IApiRoutes;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.io.IOException;
import lombok.val;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String TAG = "App";
    private final String BASE_URL = "https://bcdroid.herokuapp.com/";

    private HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private OkHttpClient client;
    private IApiRoutes bcDroidService;


    @Override
    public void onCreate() {
        super.onCreate();
        val cache = new Cache(getCacheDir(), 2048 /*bytes*/);
        //region client and retrofit
        client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(cache)
                .build();
        createApiService(client);
    }

    public void addToken(final String token) {
        client = client.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request oldRequest = chain.request();
                Request newRequest = oldRequest.newBuilder()
                        .header("x-access-token", token)
                        .header("Content-Type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        createApiService(client);
    }
    public void clearCache(){
        try {
            client.cache().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IApiRoutes getApi() {
        return bcDroidService;
    }

    private void createApiService(OkHttpClient client) {
        bcDroidService = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(IApiRoutes.class);
    }
}