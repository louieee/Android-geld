package com.wordpress.louieefitness.geld.Models;

public class New_Users {
    private String Username, referer;
    public static final String Ref = "New_Users";

    public New_Users(){

    }

    public New_Users(String username, String referer) {
        Username = username;
        this.referer = referer;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
