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
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.R;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Payment extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private String the_key;
    public final static String Key = "2";
    private Boolean checked;
    private Wallet my_wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_payment);
        current_user = mAuth.getCurrentUser();
        assert current_user != null;
        Wallet wallet = retrieve_wallet("email",current_user.getEmail());
        set_wallet_balance(wallet);
        String wallet_details = "Wallet Address: "+wallet.getAddress()+"/n"+
                "Wallet Balance: "+wallet.getBalance().toString()+" BTC";
        TextView wallet_info = findViewById(R.id.wallet_info);
        wallet_info.setText(wallet_details);
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
                Toast.makeText(Payment.this, " Database error occurred when retrieving data",
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
                my_wallet = dataSnapshot.getValue(Wallet.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Payment.this, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return my_wallet;
    }
    public void set_wallet_balance(Wallet wallet){
        Downloader get_bal = new Downloader(Payment.this,"https://blockchain.info/merchant/"
                +wallet.getGuid()+"/balance?password="+wallet.getPassword(),null,wallet,"get balance");
        get_bal.execute();
    }
    public void make_investment(View v){
        Wallet be = new Wallet();
        String key = retrieve_object_key(User.ref,"email",current_user.getEmail());
        Downloader invest = new Downloader(Payment.this, "https://blockchain.info/merchant/"+
                my_wallet.getGuid()+"/payment?password="+
                my_wallet.getPassword()+"&to="+ Sign_Up.ADDRESS+
                "&amount="+"250000",null,be,"send bitcoin");
        invest.execute();

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
