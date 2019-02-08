package com.wordpress.louieefitness.geld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Utilities.utilities.emailValid;

public class Sign_In extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener {
    private FirebaseAuth mAuth;
    private TextInputEditText email_t, password_t;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        database = FirebaseDatabase.getInstance("https://geld-f5989.firebaseio.com");
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_in);
        email_t = findViewById(R.id.email_t);
        password_t = findViewById(R.id.password_t);


    }
    public void SignIn(View v){
        if (email_t.getText().toString().length() > 0 && password_t.getText().toString().length()>0) {
            if (!emailValid(email_t.getText().toString())){
                   email_t.setError("Enter a Valid Email");
            }else {
                final ProgressDialog pd = new ProgressDialog(Sign_In.this);
                ConnectivityManager cm =
                        (ConnectivityManager) Sign_In.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    pd.setMessage("Signing "+email_t.getText().toString()+" in...");
                    pd.show();
                    database.getReference("Users").orderByChild("Email").equalTo(email_t.getText().toString()).
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            if (dataSnapshot1.child("Password").hasChild(password_t.getText().toString())) {
                                                mAuth.signInWithEmailAndPassword(email_t.getText().toString(),
                                                        password_t.getText().toString())
                                                        .addOnCompleteListener(Sign_In.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    pd.dismiss();
                                                                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                                                                        Toast.makeText(Sign_In.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                                                                        switch (getIntent().getStringExtra("Key")) {
                                                                            case Account.Key:
                                                                                startActivity(new Intent(Sign_In.this, Account.class));
                                                                                break;
                                                                            case New_Account.Key:
                                                                                startActivity(new Intent(Sign_In.this, New_Account.class));
                                                                                break;
                                                                            case Payment.Key:
                                                                                startActivity(new Intent(Sign_In.this, Payment.class));
                                                                                break;
                                                                            case User_Wallet.Key:
                                                                                startActivity(new Intent(Sign_In.this, User_Wallet.class));
                                                                                break;
                                                                            default:
                                                                                switch (Objects.requireNonNull(dataSnapshot1.child("Level").getValue(String.class))) {
                                                                                    case "Newbie":
                                                                                        if (!dataSnapshot1.child("Paid").getValue(Boolean.class)) {
                                                                                            startActivity(new Intent(Sign_In.this, Payment.class));
                                                                                            break;
                                                                                        } else {
                                                                                            startActivity(new Intent(Sign_In.this, New_Account.class));
                                                                                        }
                                                                                    default:
                                                                                        startActivity(new Intent(Sign_In.this, Account.class));
                                                                                }

                                                                        }
                                                                    }
                                                                } else {
                                                                    mAuth.signOut();
                                                                    task.addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(Sign_In.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                            } else {
                                                pd.dismiss();
                                                Toast.makeText(Sign_In.this, "Your Password is Incorrect", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(Sign_In.this, "You Don't Have an Account", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(Sign_In.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(Sign_In.this,"Check your Data Connection",Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(Sign_In.this, "All Fields must be Filled", Toast.LENGTH_SHORT).show();
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
