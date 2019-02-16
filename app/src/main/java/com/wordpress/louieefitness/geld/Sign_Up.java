package com.wordpress.louieefitness.geld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.wordpress.louieefitness.geld.Models.CONSTANTS;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Utilities.utilities.emailValid;

public class Sign_Up extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static String API_KEY = CONSTANTS.API_KEY;
    public static String GUID;
    public static String ADDRESS;
    public static String PASSWORD;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String user_email, first_password, second_password;
    private String rec_que, rec_ans, refer_username, user_name;
    private TextInputEditText username, email, pass1, pass2, question, answer, referer_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupSharedPreferences();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://geld-f5989.firebaseio.com");
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass1 = findViewById(R.id.password);
        pass2 = findViewById(R.id.confirm_password);
        question = findViewById(R.id.Question);
        answer = findViewById(R.id.answer);
        referer_v = findViewById(R.id.referer);

    }

    public void Sign_up(View v) {
        user_email = email.getText().toString();
        first_password = pass1.getText().toString();
        second_password = pass2.getText().toString();
        rec_que = question.getText().toString();
        rec_ans = answer.getText().toString();
        refer_username = referer_v.getText().toString().toLowerCase();
        user_name = username.getText().toString().toLowerCase();
        if ((user_email.length() > 0) && (first_password.length() > 0) &&
                (second_password.length() > 0) &&
                (rec_que.length() > 0) && (rec_ans.length() > 0) && user_name.length() > 0) {
            if (!emailValid(user_email)) {
                email.setError("Please Enter a Valid Email Address");
            } else if (first_password.length() < 8) {
                pass1.setError("Password must be at least 8 characters");
            } else if (user_name.length() < 6) {
                username.setError("Username must be at least 6 characters");
            } else {
                final ProgressDialog pd = new ProgressDialog(Sign_Up.this);
                ConnectivityManager cm =
                        (ConnectivityManager) Sign_Up.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    pd.setTitle("Sign Up");
                    pd.setMessage("Creating new Account for " + user_name + " ...");
                    pd.show();

                } else {
                    Toast.makeText(Sign_Up.this, "Check Data Connection", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(Sign_Up.this, "All Fields Must Be Filled", Toast.LENGTH_LONG).show();
        }
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