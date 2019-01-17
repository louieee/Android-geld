package com.wordpress.louieefitness.geld.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class Level_5 {
    private String username, no_received, color;
    final public String ref = "Level_5";
    private Level_5 my_level;

    public Level_5(){

    }

    public Level_5(String username, String no_received, String color) {
        this.username = username;
        this.no_received = no_received;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public String getNo_received() {
        return no_received;
    }

    public String getColor() {
        return color;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo_received(String no_received) {
        this.no_received = no_received;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public void increment_no_received() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ref);
        myRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                my_level = mutableData.getValue(Level_5.class);
                if (my_level == null) {
                    return Transaction.success(mutableData);
                }
                int no_received = Integer.parseInt(my_level.getNo_received());
                no_received += 1;
                my_level.setNo_received(String.valueOf(no_received));
                // Set value and report transaction success
                mutableData.setValue(my_level);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("Message: ", "postTransaction:onComplete:" + databaseError);
            }
        });
    }

}
