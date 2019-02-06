package com.wordpress.louieefitness.geld.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class New_Users {
    private String Username, referer;
    public static final String ref = "New_Users";
    public static final String name = "Newbie";
    private static New_Users the_user;

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
    public static New_Users retrieve_user(FirebaseDatabase db,String child, String Query){
        DatabaseReference myRef = db.getReference(ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    the_user = null;
                } else{
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        the_user = childSnapshot.getValue(New_Users.class);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
            }
        });
        return the_user;
    }


    public void setReferer(String referer) {
        this.referer = referer;
    }
}
