package com.chmelar.jozef.bcfiredroid.API;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IRoutes {
    @POST("login")
    Observable<LoginResponse> login(@Body LoginRequest loginResponse);
    @POST("register")
    Observable<LoginResponse> register(@Body LoginRequest loginResponse);
}