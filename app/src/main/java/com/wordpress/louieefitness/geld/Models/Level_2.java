package com.wordpress.louieefitness.geld.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Level_2 {
    private String username;
    private static Level_2 level2_user;
    private String no_received = String.valueOf(0);
    final public static String ref = "Level_2";
    final public static String name = "Amateur";
    private Boolean Reached_limit = getLimit();
    final public static String color = "#7103f7";

    public Level_2(){

    }

    public Level_2(String username, String no_received) {
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
        if (Integer.parseInt(no_received)< 4){
            result = false;
        }else if (Integer.parseInt(no_received) == 4){
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
    public static Level_2 Retrieve_1_by_Id(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(Level_2.ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                level2_user = dataSnapshot.getValue(Level_2.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                level2_user = null;
            }
        });

        return level2_user;
    }
}
