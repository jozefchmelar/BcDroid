package com.chmelar.jozef.bcfiredroid.API;

import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.API.Model.User;
import com.chmelar.jozef.bcfiredroid.Screens.Project.Comment;
import com.chmelar.jozef.bcfiredroid.Screens.Project.SubmitComment;
import com.chmelar.jozef.bcfiredroid.Screens.Project.SubmitCommentResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRoutes {
    @POST("login")
    Observable<LoginResponse> login(@Body LoginRequest loginResponse);
    @POST("register")
    Observable<LoginResponse> register(@Body LoginRequest loginResponse);

    @GET("employee/{id}")
    Observable<User> getUser(@Path("id") int id);

    @GET("project/{id}")
    Observable<Project> getProject(@Path("id")int id);

    @GET("project/{id}/comment")
    Observable<List<Comment>> getComments(@Path("id")String id);


    @POST("project/{id}/comment")
    Observable<SubmitCommentResponse> submitComment(@Body SubmitComment comment, @Path("id")String id);


}