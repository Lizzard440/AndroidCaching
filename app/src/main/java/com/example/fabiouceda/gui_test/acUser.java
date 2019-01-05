package com.example.fabiouceda.gui_test;

/*
* This Class contains additional information about the user in addition to an instance of
* FirebaseUser ti store the corresponding object.
* Created by: Fabio
*/

import com.google.firebase.auth.FirebaseUser;

public class acUser  {
    private FirebaseUser fb_user;
    private String username;
    private int score;
    private String user_ID;
    private String eMail;


    /**
     * extracts the data from the Firebase user
     * created by: Fabio
     */
    private void extractData (){
        user_ID = fb_user.getUid();
        eMail = fb_user.getEmail();
    }


    /**
     * returns the Firebase user to the calling instance
     * created by: Fabio
     * @return instance of FirebaseUser
     */
    public FirebaseUser getFb_user() {
        return fb_user;
    }


    /**
     * sets the needed Firebase user
     * created by: Fabio
     * @param fb_user_ instance of FirebaseUser
     */
    public void setFb_user(FirebaseUser fb_user_) {
        fb_user = fb_user_;
    }


    /**
     * returns Username to the calling instance
     * created by: Fabio
     * @return returns string username
     */
    public String getUsername() {
        return username;
    }


    /**
     * sets the current username
     * created by: Fabio
     * @param username_ string username
     */
    public void setUsername(String username_) {
        username = username_;
    }


    /**
     * Returns the users Score to the calling instance
     * created by: Fabio
     * @return integer score
     */
    public int getScore() {
        return score;
    }


    /**
     * sets the users score for  syncing with DB
     * created by: Fabio
     * @param score_ integer new score
     */
    public void setScore(int score_) {
        score = score_;
    }


    /**
     * returns the user ID created by FirebaseAuth ti the calling instance
     * created by: Fabio
     * @return string User-ID
     */
    public String getUser_ID() {
        return user_ID;
    }


    /**
     * sets the current user ID to a new value
     * created by: Fabio
     * @param user_ID sting new user ID
     */
    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }


    /**
     * returns the string of the e-Mail address of the current user
     * created by: Fabio
     * @return e-Mail address
     */
    public String geteMail() {
        return eMail;
    }


    /**
     * sets new user e-Mail
     * created by: Fabio
     * @param eMail string e-Mail
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }


}
