package com.wordpress.louieefitness.geld.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_splash_screen);
        ImageView appIcon = findViewById(R.id.iconDisplay);
        TextView appName = findViewById(R.id.iconName);
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        appIcon.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);
        int splashTimeOut = 7000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Toast.makeText(getApplicationContext(),"Welcome to UNN Info",Toast.LENGTH_SHORT).show();
                Intent start = new Intent (SplashScreen.this,Sign_Up.class);
                startActivity(start);
                finish();
            }
        }, splashTimeOut);
    }



}
