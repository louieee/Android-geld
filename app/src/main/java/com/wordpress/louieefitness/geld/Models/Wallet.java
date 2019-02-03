package com.wordpress.louieefitness.geld.Models;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Wallet {
    private String guid;
    private String address;
    private String email;
    private static Wallet the_wallet;
    public final static String Ref = " Wallet";
    private String Main_Address;
    private String password;
    private Double Balance = 0.0;

    public Wallet(){

    }

    public Wallet(String guid, String address, String email, String main_Address, String password, Double balance) {
        this.guid = guid;
        this.address = address;
        this.email = email;
        Main_Address = main_Address;
        this.password = password;
        Balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMain_Address() {
        return Main_Address;
    }

    public void setMain_Address(String main_Address) {
        Main_Address = main_Address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        Balance = balance;
    }
    public static Wallet retrieve_wallet(String child, String Query){
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference myRef = db.getReference(Ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    the_wallet = childSnapshot.getValue(Wallet.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
                the_wallet = null;
            }
        });
        return the_wallet;
    }
    public static void update_wallet(String child, String Query,final Wallet myWallet){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference(Ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    String key = childSnapshot.getKey();
                    myRef.child(key).setValue(myWallet);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
            }
        });
    }
    public static void delete_wallet(String child, String Query){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference(Ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    String key = childSnapshot.getKey();
                    myRef.child(key).removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
            }
        });
    }


}

