package com.chmelar.jozef.bcfiredroid.API;

import com.chmelar.jozef.bcfiredroid.API.Model.AddPeopleToProjectRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.Project;
import com.chmelar.jozef.bcfiredroid.API.Model.ProjectRequest;
import com.chmelar.jozef.bcfiredroid.API.Model.User;
import com.chmelar.jozef.bcfiredroid.API.Model.Comment;
import com.chmelar.jozef.bcfiredroid.API.Model.SubmitComment;
import com.chmelar.jozef.bcfiredroid.API.Model.SubmitCommentResponse;
import com.chmelar.jozef.bcfiredroid.API.Model.Trip;
import com.chmelar.jozef.bcfiredroid.Screens.Project.TripRequest;
import com.chmelar.jozef.bcfiredroid.Screens.Project.TripResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IApiRoutes {

    @GET("employee/{id}")
    Observable<User> getUser(@Path("id") int id);

    @GET("employee/")
    Observable<List<User>> getUsers();

    @GET("project/{id}")
    Observable<Project> getProject(@Path("id")int id);

    @GET("project/{id}/comment")
    Observable<List<Comment>> getComments(@Path("id")String id);

    @GET("project/{id}/trip")
    Observable<List<Trip>> getTrips (@Path("id") String projectId);

    @POST("project/{id}/comment")
    Observable<SubmitCommentResponse> submitComment(@Body SubmitComment comment, @Path("id")String id);

    @POST("project/{id}/trip")
    Observable<TripResponse> postTrip (@Body TripRequest tripRequest, @Path("id") String projectId);

    @POST("project/")
    Observable<Project> postProject (@Body ProjectRequest project);

    @POST("project/{id}/add")
    Observable<Boolean> addPeopleToProject (@Body AddPeopleToProjectRequest ppl, @Path("id") String projectId);

    @POST("login")
    Observable<LoginResponse> login(@Body LoginRequest loginResponse);
    @POST("register")
    Observable<LoginResponse> register(@Body LoginRequest loginResponse);
}