package com.wordpress.louieefitness.geld.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.R;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class New_Account extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Wallet mWallet;
    private String the_key;
    private User the_user;
    private Boolean checked;
    public final static String Key = "3";
    private New_Users d_user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseUser current_user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_new_account);
        TextView username = findViewById(R.id.New_user_name);
        TextView email = findViewById(R.id.New_user_email);
        String user_id = retrieve_object_key(User.ref,"email", current_user.getEmail());
        User u = Retrieve_user_by_Id(user_id);
        String _id = retrieve_object_key(Wallet.Ref,"email", current_user.getEmail());
        Wallet w = Retrieve_by_Id(_id);
        Downloader verify = new Downloader(this,"https://blockchain.info/q/getsentbyaddress/"
                +w.getAddress()+"?confirmations=6",u,w,"verify payment");
        verify.execute();
        String the_id = retrieve_object_key(New_Users.ref,"username",u.getUsername());
        New_Users newUsers = Retrieve_new_user(the_id);
        username.setText(newUsers.getUsername());
        email.setText(current_user.getEmail());
    }

    public String retrieve_object_key(String ref,String child, String Query){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(ref);
        Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    the_key = childSnapshot.getKey();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(New_Account.this, " Database error occurred when retrieving data",
                        Toast.LENGTH_LONG).show();
            }
        });
        return the_key;
    }
    public Wallet Retrieve_by_Id(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(Wallet.Ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mWallet = dataSnapshot.getValue(Wallet.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(New_Account.this, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return mWallet;
    }
    public New_Users Retrieve_new_user(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(New_Users.ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                d_user = dataSnapshot.getValue(New_Users.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(New_Account.this, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return d_user;
    }
    public User Retrieve_user_by_Id(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(User.ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                the_user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(New_Account.this, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return the_user;
    }


    public void sharedPreferenceTheme(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(getString(R.string.Theme_key), getString(R.string.level_1));
        if (value.equalsIgnoreCase("1")){
            getApplicationContext().setTheme(R.style.Light);
        }else if (value.equalsIgnoreCase("2")){
            getApplicationContext().setTheme(R.style.Dark);
        }
    }
    public void sharedPreferenceLock(SharedPreferences sharedPreferences){
        checked = sharedPreferences.getBoolean(getString(R.string.Lock_key),true);
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
        }else if (key.equals(getString(R.string.Lock_key))){
            sharedPreferenceLock(sharedPreferences);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checked){
            getApplicationContext().startActivity(new Intent(getApplicationContext(),Sign_In.class)
                    .putExtra("key",Key));
        }
    }
    public void open_settings (View v){
        startActivity(new Intent(this,SettingsActivity.class));
    }

    public void sign_out (View v){
        mAuth.signOut();
        startActivity(new Intent(this,Sign_In.class));
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

