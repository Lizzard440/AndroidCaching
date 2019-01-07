package com.example.fabiouceda.gui_test;

/*
* This Class contains additional information about the user in addition to an instance of
* FirebaseUser ti store the corresponding object.
* Created by: Fabio
*/

import com.google.firebase.auth.FirebaseUser;

public class ACUser {
    private FirebaseUser fb_user;
    private String username;
    private int score;
    private String user_ID;
    private String eMail;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }


}
