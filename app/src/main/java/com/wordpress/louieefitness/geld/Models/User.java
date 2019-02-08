package com.wordpress.louieefitness.geld.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class User {
    private String username,email;
    private String question, answer, password, referer;
    private static User the_user;
    private Boolean paid = false;
    private Double Balance = 0.0;
    private String level =  "Newbie";
    public  static final String ref = "User";
    public User(){
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        this.Balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public String getLevel() {
        return level;
    }

    public String getEmail() {
        return email;
    }
    public static void Increment_Balance(FirebaseDatabase database, final String username, final Double amount) {
        DatabaseReference the_ref = database.getReference(ref);
        the_ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User p = mutableData.getValue(User.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getUsername().equals(username) ) {
                    Double num = p.getBalance();
                    num = num + amount;
                    p.setBalance((num));
                }
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
            }
        });
    }
    public static void Update_user(FirebaseDatabase db, final String db_id, final User user) {
        final DatabaseReference myRef = db.getReference(User.ref);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(db_id).exists()){
                    myRef.child(db_id).setValue(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
            }
        });
    }
    public static User retrieve_user(FirebaseDatabase db, String child, String Query){
        DatabaseReference myRef = db.getReference(ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    the_user = null;
                } else{
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        the_user = childSnapshot.getValue(User.class);
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
    public static void Delete_user(FirebaseDatabase db, String child, String Query){
        final DatabaseReference myRef = db.getReference(ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        the_user = childSnapshot.getValue(User.class);
                        String key = childSnapshot.getKey();
                        myRef.child(key).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
            }
        });
    }


}
