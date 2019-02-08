package com.wordpress.louieefitness.geld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Models.CONSTANTS;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.Utilities.Downloader;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Utilities.utilities.emailValid;
public class Sign_Up extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static String API_KEY = CONSTANTS.API_KEY;
    public static String GUID;
    public static String ADDRESS;
    public static String PASSWORD;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String user_email,first_password, second_password;
    private String rec_que, rec_ans, refer_username, user_name;
    private TextInputEditText username,email,pass1,pass2,question,answer, referer_v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupSharedPreferences();
        database = FirebaseDatabase.getInstance("https://geld-f5989.firebaseio.com");
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass1 = findViewById(R.id.password);
        pass2 = findViewById(R.id.confirm_password);
        question = findViewById(R.id.Question);
        answer = findViewById(R.id.answer);
        referer_v = findViewById(R.id.referer);
        Wallet geld_wallet =  Wallet.retrieve_wallet(database, "Email","louis.paul9095@gmail.com");
        if (geld_wallet != null) {
            GUID = geld_wallet.getGuid();
            ADDRESS = geld_wallet.getAddress();
            PASSWORD = geld_wallet.getPassword();
        }else{
            GUID = "";
            ADDRESS = "";
            PASSWORD = "";
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
            }else {
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
                    database.getReference("User").orderByChild("Email").equalTo(user_email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.exists()) {
                                pd.dismiss();
                                Toast.makeText(Sign_Up.this, "This Email is in Use by Another Account", Toast.LENGTH_SHORT).show();
                            } else {
                                database.getReference("Users").orderByChild("Username").equalTo(user_name).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            pd.dismiss();
                                            Toast.makeText(Sign_Up.this, "This Username is in Use by Another Account", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (first_password.equals(second_password)) {
                                                mAuth.createUserWithEmailAndPassword(user_email, first_password)
                                                        .addOnCompleteListener(Sign_Up.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    final User new_user = new User();
                                                                    new_user.setLevel("Newbie");
                                                                    new_user.setPassword(first_password);
                                                                    new_user.setUsername(user_name);
                                                                    new_user.setQuestion(rec_que);
                                                                    new_user.setAnswer(rec_ans);
                                                                    new_user.setEmail(user_email);
                                                                    database.getReference("Level_1").orderByChild("Username").equalTo(refer_username).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()) {
                                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                    if (snapshot.child("No_Received").getValue(Integer.class) < 2) {
                                                                                        new_user.setReferer(refer_username);
                                                                                    }
                                                                                }
                                                                            }
                                                                            String key = database.getReference("User").push().getKey();
                                                                            database.getReference().child(key).setValue(new_user).addOnCompleteListener(Sign_Up.this, new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        pd.dismiss();
                                                                                        Wallet user_wallet = new Wallet();
                                                                                        user_wallet.setEmail(user_email);
                                                                                        create_wallet(user_wallet, new_user);
                                                                                    } else {
                                                                                        task.addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                pd.dismiss();
                                                                                                Toast.makeText(Sign_Up.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {
                                                                            Toast.makeText(Sign_Up.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                } else {
                                                                    task.addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            pd.dismiss();
                                                                            Toast.makeText(Sign_Up.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(Sign_Up.this, "The two Passwords must be equal", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(Sign_Up.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Sign_Up.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(Sign_Up.this, "Check Data Connection", Toast.LENGTH_SHORT).show();
                }
            }
            }else{
            Toast.makeText(Sign_Up.this, "All Fields Must Be Filled", Toast.LENGTH_LONG).show();
        }
        }


        public void create_wallet(Wallet a_wallet, User user){
            Downloader b = new Downloader(Sign_Up.this,"https://blockchain.info/api/v2/create?" +
                    "password="+first_password+
                    "&api_code="+API_KEY, user, a_wallet,"create wallet");
            b.execute(database);
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