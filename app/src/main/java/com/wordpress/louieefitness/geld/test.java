package com.wordpress.louieefitness.geld;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class test {
    private Level_1 my_level = new Level_1();
    private void increment_no_received(String ref) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(ref);
        myRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                my_level = mutableData.getValue(Level_1.class);
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
