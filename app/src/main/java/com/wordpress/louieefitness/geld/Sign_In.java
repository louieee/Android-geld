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
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Utilities.utilities.emailValid;

public class Sign_In extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
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

    public void SignIn(View v) {
        if (email_t.getText().toString().length() > 0 && password_t.getText().toString().length() > 0) {
            if (!emailValid(email_t.getText().toString())) {
                email_t.setError("Enter a Valid Email");
            } else {
                final ProgressDialog pd = new ProgressDialog(Sign_In.this);
                ConnectivityManager cm =
                        (ConnectivityManager) Sign_In.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    pd.setMessage("Signing " + email_t.getText().toString() + " in...");
                    pd.show();

                } else {
                    Toast.makeText(Sign_In.this, "Check your Data Connection", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(Sign_In.this, "All Fields must be Filled", Toast.LENGTH_SHORT).show();
        }
    }

    public void SignUp(View v) {
        startActivity(new Intent(Sign_In.this, Sign_Up.class));
    }

    public void ForgotPassword(View v) {
        startActivity(new Intent(Sign_In.this, Forgot_Password.class));
    }

    public void sharedPreferenceTheme(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(getString(R.string.Theme_key), getString(R.string.level_1));
        if (value.equalsIgnoreCase("1")) {
            getApplicationContext().setTheme(R.style.Light);
        } else if (value.equalsIgnoreCase("2")) {
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
        if (key.equals(getString(R.string.Theme_key))) {
            sharedPreferenceTheme(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void exit_app(View v) {
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

    public void toggle_nav(View v) {
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
