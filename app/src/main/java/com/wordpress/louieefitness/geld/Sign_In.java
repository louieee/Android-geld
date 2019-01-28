package com.wordpress.louieefitness.geld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class Sign_In extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.actitvity_sign_in);
        //TODO create sign in activity
        //through sign in
        //check user level
        //if user level is finished reset user to new
        // take user to investment page
    }
}
