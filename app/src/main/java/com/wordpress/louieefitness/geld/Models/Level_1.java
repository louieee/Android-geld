package com.wordpress.louieefitness.geld.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class Level_1 {
    private String username;
    private String no_received = String.valueOf(0);
    final public static  String  ref = "Level_1";
    final public static String name = "Beginner";
    private Boolean Reached_limit = getLimit();
    final public static String color = "#ff0404";
    public Level_1(){

    }

    public Level_1(String username, String no_received) {
        this.username = username;
        this.no_received = no_received;
    }

    public Boolean getReached_limit() {
        return Reached_limit;
    }

    public void setReached_limit(Boolean reached_limit) {
        Reached_limit = reached_limit;
    }

    public String getUsername() {
        return username;
    }

    public String getNo_received() {
        return no_received;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo_received(String no_received) {
        this.no_received = no_received;
    }


    private Boolean getLimit(){
        Boolean result = false;
        if (Integer.parseInt(no_received)< 2){
            result = false;
        }else if (Integer.parseInt(no_received) == 2){
            result = true;
        }
        return result;
    }

}
