package com.wordpress.louieefitness.geld;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.CONSTANTS;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Models.User.retrieve_user;
import static com.wordpress.louieefitness.geld.Utilities.utilities.emailValid;

public class Sign_Up extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static String API_KEY = CONSTANTS.API_KEY;
    public static String GUID;
    public static String ADDRESS;
    public static String PASSWORD;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private User new_user;
    private DatabaseReference UserRef;
    private String user_email,first_password, second_password;
    private String rec_que, rec_ans, refer_username, user_name;
    private TextInputEditText username,email,pass1,pass2,question,answer, referer_v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupSharedPreferences();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UserRef = database.getReference(User.ref);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass1 = findViewById(R.id.password);
        pass2 = findViewById(R.id.confirm_password);
        question = findViewById(R.id.Question);
        answer = findViewById(R.id.answer);
        referer_v = findViewById(R.id.referer);
        Wallet geld_wallet =  Wallet.retrieve_wallet(database, "Email","louis.paul9095@gmail.com");
        if (geld_wallet == null) {
            Toast.makeText(this, "Please Check Your Data Connection", Toast.LENGTH_SHORT).show();
        }else{
            GUID = geld_wallet.getGuid();
            ADDRESS = geld_wallet.getAddress();
            PASSWORD = geld_wallet.getPassword();
        }

    }

    public void Sign_up(View v){
        user_email = email.getText().toString();first_password = pass1.getText().toString();
        second_password = pass2.getText().toString();rec_que = question.getText().toString();
        rec_ans = answer.getText().toString();refer_username = referer_v.getText().toString().toLowerCase();
        user_name = username.getText().toString().toLowerCase();
        if ((user_email.length() > 0) && (first_password.length() > 0) &&
                (second_password.length() > 0)  &&
                (rec_que.length() > 0) && (rec_ans.length() > 0) && user_name.length() > 0){
            if (!emailValid(user_email)){
                email.setError("Please Enter a Valid Email Address");
            }else if (first_password.length() < 8) {
                pass1.setError("Password must be at least 8 characters");
            }else if(user_name.length() <6){
                username.setError("Username must be at least 6 characters");
            }else{
                User get_user = User.retrieve_user(database, CONSTANTS.username, user_name);
                if (get_user == null) {
                    if (first_password.equals(second_password)) {
                        mAuth.createUserWithEmailAndPassword(user_email, first_password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                                            Log.d("Message", "createUserWithEmail:success");
                                            new_user = new User();
                                            new_user.setAnswer(rec_ans);
                                            new_user.setEmail(user_email);
                                            new_user.setQuestion(rec_que);
                                            new_user.setPassword(first_password);
                                            new_user.setUsername(user_name);
                                            if (refer_username.isEmpty()) {
                                                complete_sign_up();
                                            } else {
                                                User referer = retrieve_user(database, CONSTANTS.username,
                                                        refer_username);
                                                if (referer == null) {
                                                    complete_sign_up();
                                                } else {
                                                    if (referer.getLevel().equals(Level_1.name)) {
                                                        New_Users newUser = new New_Users(new_user.getUsername(), referer.getUsername());
                                                        String db_id = UserRef.push().getKey();
                                                        new_user.setReferer(referer.getUsername());
                                                        UserRef.child(db_id).setValue(new_user);
                                                        DatabaseReference New_userRef = database.getReference(New_Users.ref);
                                                        String d_id = New_userRef.push().getKey();
                                                        New_userRef.child(d_id).setValue(newUser);
                                                        //create wallet start
                                                        create_wallet();
                                                        //create wallet stop
                                                        Toast.makeText(Sign_Up.this, "User Account Created Successfully", Toast.LENGTH_LONG).show();
                                                        Intent payment = new Intent(Sign_Up.this, Sign_In.class);
                                                        startActivity(payment);
                                                    } else {
                                                        complete_sign_up();
                                                    }
                                                }
                                            }
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("Message: ", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(Sign_Up.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(Sign_Up.this, "The Two Passwords Must Match", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(Sign_Up.this, "Username is already taken", Toast.LENGTH_LONG).show();
                }
            }
            }else{
            Toast.makeText(Sign_Up.this, "All Fields Must Be Filled", Toast.LENGTH_LONG).show();

        }
        }
        public void complete_sign_up(){
            Level_1 referer = Level_1.get_oldest_object(database);
            New_Users newUser;
            if (referer == null){
                new_user.setReferer("");
                newUser = new New_Users(new_user.getUsername(), "");
            }else{
                newUser = new New_Users(new_user.getUsername(), referer.getUsername());
                new_user.setReferer(referer.getUsername());
            }
            String db_id = UserRef.push().getKey();
            UserRef.child(db_id).setValue(new_user);
            DatabaseReference New_userRef = database.getReference(New_Users.ref);
            String d_id = New_userRef.push().getKey();
            New_userRef.child(d_id).setValue(newUser);
            create_wallet();
            Toast.makeText(Sign_Up.this, "User Account Created Successfully", Toast.LENGTH_LONG).show();
            Intent payment = new Intent(Sign_Up.this, Sign_In.class);
            startActivity(payment);
        }
        public void create_wallet(){
            Wallet be = new Wallet();
            Downloader b = new Downloader(Sign_Up.this,"https://blockchain.info/api/v2/create?" +
                    "password="+first_password+
                    "&api_code="+API_KEY,
                    new_user, be,"create wallet");
            b.execute();
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

    public void exit (View v){
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