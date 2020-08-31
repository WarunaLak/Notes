package com.waruna.notes2.data.network.responses;

public class PostsResponse {
    public int userId;
    public int id;
    public String title;
    public String body;

    public PostsResponse() {
    }

    public PostsResponse(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }
}
