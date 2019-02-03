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
    private int no_received = 0;
    final public static String ref = "Level_2";
    final public static String name = "Amateur";
    private Boolean Reached_limit = getLimit();
    final public static String color = "#7103f7";

    public Level_2(){

    }

    public Level_2(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo_received(int no_received) {
        this.no_received = no_received;
    }


    public String getUsername() {
        return username;
    }

    public int getNo_received() {
        return no_received;
    }

    private Boolean getLimit(){
        Boolean result = false;
        if (no_received< 4){
            result = false;
        }else if (no_received == 4){
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
    public static void Add(Level_2 level2_user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ref);
        String key = myRef.push().getKey();
        myRef.child(key).setValue(level2_user);

    }
    public static void Update_no_received(final String username) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference the_ref = database.getReference(ref);
        the_ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Level_2 p = mutableData.getValue(Level_2.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getUsername().equals(username) ) {
                    int num = p.getNo_received();
                    num = num + 1;
                    p.setNo_received(num);
                    if (num == 4){
                        p.setReached_limit(true);
                    }
                }
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d("Message: ", "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    public Level_2 retrieve_object(String child, String Query){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    level2_user = childSnapshot.getValue(Level_2.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                level1_user = null;
            }
        });
        return level2_user;
    }


}
