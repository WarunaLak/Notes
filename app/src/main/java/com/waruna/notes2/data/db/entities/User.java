package com.waruna.notes2.data.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    public static final int CURRENT_USER_ID = 0;

    @PrimaryKey(autoGenerate = false)
    private int id;
    private int userID;
    private String email;
    private boolean isLogin;

    public User(int userID, String email, boolean isLogin) {
        this.userID = userID;
        this.email = email;
        this.isLogin = isLogin;
        this.id = CURRENT_USER_ID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
