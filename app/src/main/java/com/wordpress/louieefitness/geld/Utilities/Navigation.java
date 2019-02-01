package com.wordpress.louieefitness.geld.Utilities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.wordpress.louieefitness.geld.Activities.Account;
import com.wordpress.louieefitness.geld.Activities.SettingsActivity;
import com.wordpress.louieefitness.geld.Activities.Sign_In;
import com.wordpress.louieefitness.geld.Activities.User_Wallet;

public class Navigation{
    public static void open_settings (View v){
        Context c = v.getContext();
        c.startActivity(new Intent(c,SettingsActivity.class));
    }
    public static void open_wallet (View v){
        Context c = v.getContext();
        c.startActivity(new Intent(c,User_Wallet.class));
    }
    public static void sign_out (View v){
        Context c = v.getContext();
        c.startActivity(new Intent(c,Sign_In.class));
    }
    public static void exit (View v){
        Context c = v.getContext();
        c.startActivity(new Intent(c,SettingsActivity.class));
    }
    public static void toggle_nav (View v){
        Context c = v.getContext();
        c.startActivity(new Intent(c,SettingsActivity.class));
    }
    public static void open_account (View v){
        Context c = v.getContext();
        c.startActivity(new Intent(c,Account.class));
    }
    public static void signoff(FirebaseAuth auth){
        auth.signOut();
    }

}
