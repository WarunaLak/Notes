package com.waruna.notes2.data.network;

import com.waruna.notes2.data.network.responses.AuthResponse;
import com.waruna.notes2.data.network.responses.NotesResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyApi {

    @GET("?notes")
    Observable<Response<NotesResponse>> getNotes(
            int u
    );

    @FormUrlEncoded
    @POST("?notes")
    Observable<Response<NotesResponse>> saveNote(
            int u,
            String title,
            String description,
            int priority
    ) ;

    @FormUrlEncoded
    @POST("?user&login")
    Observable<Response<AuthResponse>> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("?user&register")
    Observable<Response<AuthResponse>> register(
            @Field("email") String email,
            @Field("password") String password
    );

}
