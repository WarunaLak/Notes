package com.waruna.notes2.data.network;

import com.google.gson.annotations.SerializedName;
import com.waruna.notes2.data.network.responses.AuthResponse;
import com.waruna.notes2.data.network.responses.NotesResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApi {

    @GET("notes/get.php")
    Observable<Response<NotesResponse>> getNotes(
            @Query("u") int userID
    );

    @FormUrlEncoded
    @POST("notes/save.php")
    Observable<Response<NotesResponse>> saveNote(
            @Field("u") int userID,
            @Field("title") String title,
            @Field("description") String description,
            @Field("priority") int priority
    ) ;

    @FormUrlEncoded
    @POST("notes/update.php")
    Observable<Response<NotesResponse>> updateNote(
            @Field("id") int id,
            @Field("title") String title,
            @Field("description") String description,
            @Field("priority") int priority
    ) ;

    @FormUrlEncoded
    @POST("notes/remove.php")
    Observable<Response<NotesResponse>> removeNote(
            @Field("id") int id
    ) ;

    @FormUrlEncoded
    @POST("user/login.php")
    Observable<Response<AuthResponse>> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/register.php")
    Observable<Response<AuthResponse>> register(
            @Field("email") String email,
            @Field("password") String password
    );

}
