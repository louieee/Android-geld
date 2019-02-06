package com.wordpress.louieefitness.geld;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.CONSTANTS;
import com.wordpress.louieefitness.geld.Models.User;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Utilities.utilities.emailValid;

public class Forgot_Password extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{
    private Context c;
    private TextView password,question,pass_info;
    private TextInputEditText email,pass_answer;
    private LinearLayout pass_;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        setContentView(R.layout.activity_forgot_password);
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        c = Forgot_Password.this;
        question = findViewById(R.id.pass_question);
        password = findViewById(R.id.pass_retrieve);
        pass_info = findViewById(R.id.pass_info);
        email = findViewById(R.id.email_re);
        pass_answer = findViewById(R.id.pass_answer);
        pass_ = findViewById(R.id.pass_info_main);

    }
    public void Send(View v){
        String email_ = email.getText().toString();
        User the_user = User.retrieve_user(database, CONSTANTS.email, email.getText().toString());
        switch (email.getVisibility()) {
            case VISIBLE:
                if (email_.isEmpty()) {
                    Toast.makeText(c, "No Email Address Detected", Toast.LENGTH_LONG).show();
                } else {
                    if (!emailValid(email_)){
                        email.setError("Please Enter a Valid Email Address");
                    }else {
                        if (the_user == null) {
                            Toast.makeText(c, "No User with this Email Address", Toast.LENGTH_LONG).show();
                            email.setText("");
                        } else {
                            email.setVisibility(GONE);
                            question.setText(the_user.getQuestion());
                            question.setVisibility(VISIBLE);
                            pass_answer.setText("");
                            pass_answer.setVisibility(VISIBLE);
                        }
                    }
                }
                break;
            case GONE:
                if (pass_answer.getText().toString().isEmpty()){
                    Toast.makeText(c,"This Field must be Filled",Toast.LENGTH_LONG).show();
                }else{
                    if (pass_answer.getText().toString().equalsIgnoreCase(the_user.getAnswer())){
                        pass_.setVisibility(VISIBLE);
                        pass_info.
                                setVisibility(VISIBLE);
                        password.setText(the_user.getPassword());
                        password.setVisibility(VISIBLE);
                    }else{
                        Toast.makeText(c,"Your Input is Incorrect",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case INVISIBLE:
                break;

        }

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

