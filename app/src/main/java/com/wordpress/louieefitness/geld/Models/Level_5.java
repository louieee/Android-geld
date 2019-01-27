package com.wordpress.louieefitness.geld.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Level_5 {
    private String username;
    private static Level_5 level5_user;
    private String no_received = String.valueOf(0);
    final public static String ref = "Level_5";
    final public static String name = "Master";
    private Boolean Reached_limit = getLimit();
    final public static String color = "#ff6f00";

    public Level_5(){

    }

    public Level_5(String username, String no_received) {
        this.username = username;
        this.no_received = no_received;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo_received(String no_received) {
        this.no_received = no_received;
    }


    public String getUsername() {
        return username;
    }

    public String getNo_received() {
        return no_received;
    }

    private Boolean getLimit(){
        Boolean result = false;
        if (Integer.parseInt(no_received)< 32){
            result = false;
        }else if (Integer.parseInt(no_received) == 32){
            result = true;
        }
        return result;
    }

    public Boolean getReached_limit() {
        return Reached_limit;
    }

    public void setReached_limit(Boolean reached_limit) {
        Reached_limit = reached_limit;
    }
    public static Level_5 Retrieve_1_by_Id(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(Level_5.ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                level5_user = dataSnapshot.getValue(Level_5.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                level5_user = null;
            }
        });

        return level5_user;
    }
}
