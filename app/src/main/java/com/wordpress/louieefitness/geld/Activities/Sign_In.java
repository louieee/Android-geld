package com.wordpress.louieefitness.geld.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.R;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Sign_In extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener {
    private FirebaseAuth mAuth;
    private TextInputEditText email_t, password_t;
    private String user_email, user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_in);
        email_t = findViewById(R.id.email_t);
        password_t = findViewById(R.id.password_t);
        user_email = email_t.getText().toString();
        user_password = password_t.getText().toString();


    }

    public void SignIn(View v) {
        if (!(user_email.isEmpty() || user_password.isEmpty())) {
            final User user = User.retrieve_user("email", user_email);
            if (user == null) {
                Toast.makeText(Sign_In.this, "You Don't Have an account", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Sign_In.this, Sign_Up.class));
            } else {
                mAuth.signInWithEmailAndPassword(user_email, user_password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Sign_In.this, "Sign Up Successfull", Toast.LENGTH_LONG).show();
                                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                                        switch (getIntent().getStringExtra("key")){
                                            case Account.Key :
                                                startActivity(new Intent(Sign_In.this, Account.class));break;
                                            case New_Account.Key :
                                                startActivity(new Intent(Sign_In.this, New_Account.class));break;
                                            case Payment.Key :
                                                startActivity(new Intent(Sign_In.this, Payment.class));break;
                                            case User_Wallet.Key :
                                                startActivity(new Intent(Sign_In.this, User_Wallet.class));break;
                                            default:
                                                if (user.getLevel().equals(New_Users.name)) {
                                                    Toast.makeText(Sign_In.this, "Welcome " + user.getFirst_name(), Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Sign_In.this, Payment.class));
                                                } else {
                                                    Toast.makeText(Sign_In.this, "Welcome " + user.getFirst_name(), Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Sign_In.this, Account.class));
                                                }
                                        }

                                    } else {
                                        email_t.setText("");
                                        password_t.setText("");
                                        mAuth.getCurrentUser().sendEmailVerification();
                                        Toast.makeText(Sign_In.this, getString(R.string.verification_message), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    email_t.setText("");
                                    password_t.setText("");
                                    Toast.makeText(Sign_In.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

        } else {
            Toast.makeText(Sign_In.this, "All Fields must be Filled", Toast.LENGTH_LONG).show();
        }
    }
    public void SignUp(View v){
        startActivity(new Intent(Sign_In.this, Sign_Up.class));
    }
    public void ForgotPassword(View v){
        startActivity(new Intent(Sign_In.this, Forgot_Password.class));
    }

    public void sharedPreferenceTheme(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(getString(R.string.Theme_key), getString(R.string.level_1));
        if (value.equalsIgnoreCase("1")){
            getApplicationContext().setTheme(R.style.Light);
        }else if (value.equalsIgnoreCase("2")){
            getApplicationContext().setTheme(R.style.Dark);
        }
    }
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceTheme(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.Theme_key))){
            sharedPreferenceTheme(sharedPreferences);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void exit_app (View v){
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit App? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
    public void toggle_nav (View v){
        final LinearLayout nav = findViewById(R.id.nav_bar);
        switch (nav.getVisibility()) {
            case VISIBLE:
                nav.setVisibility(GONE);
                break;
            case GONE:
                nav.setVisibility(VISIBLE);
                break;
            case View.INVISIBLE:
                break;
        }
    }


}
