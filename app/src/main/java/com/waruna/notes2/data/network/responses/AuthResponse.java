package com.waruna.notes2.data.network.responses;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("login")
    private Boolean isLogin;
    @SerializedName("registered")
    private boolean isRegistered;
    @SerializedName("userID")
    private int userID;
    @SerializedName("message")
    private String message;

    public AuthResponse(boolean isLogin, boolean isRegistered, int userID, String message) {
        this.isLogin = isLogin;
        this.isRegistered = isRegistered;
        this.userID = userID;
        this.message = message;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
