package com.waruna.notes2.data.network;

import com.waruna.notes2.data.network.responses.NotesResponse;
import com.waruna.notes2.data.network.responses.PostsResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface MyApi {

//    @GET("posts")
//    Response<List<PostsResponse>> getPosts();

    @GET("?notes&u=1")
    Observable<Response<NotesResponse>> getNotes();

    @GET("posts")
    Observable<Response<List<PostsResponse>>> getPosts();

}
