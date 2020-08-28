package com.waruna.notes2.data.network;

import com.waruna.notes2.data.network.models.Posts;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyApi {

    @GET("posts")
    Observable<List<Posts>> getPosts();

}
