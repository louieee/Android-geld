package com.wordpress.louieefitness.geld;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.wordpress.louieefitness.geld.Models.Wallet;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class User_Wallet extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Context c;
    private Wallet myWallet;
    public final static String Key = "1";
    private Boolean checked;
    private FirebaseAuth mAuth;
    private TextInputEditText amount, receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        c = User_Wallet.this;
        setContentView(R.layout.activity_wallet);
        TextView e_mail = findViewById(R.id.wallet_email);
        TextView address = findViewById(R.id.wallet_address);
        TextView balance = findViewById(R.id.wallet_balance);
        amount = findViewById(R.id.send_amount);
        receiver = findViewById(R.id.receiver_address);
        FirebaseUser current_user = mAuth.getCurrentUser();
        if (current_user == null) {
            startActivity(new Intent(c, Sign_Up.class));
        } else {
            e_mail.setText(myWallet.getEmail());
            address.setText(myWallet.getAddress());
            balance.setText(String.valueOf(myWallet.getBalance()));
        }

    }

    public void Send_Bit_coin(View v) {
        if (amount.getText().toString().isEmpty() || receiver.getText().toString().isEmpty()) {
            Toast.makeText(c, "All Fields must be Filled", Toast.LENGTH_LONG).show();
        } else {
            String money = String.valueOf(Double.parseDouble(amount.getText().toString()) * 100000000);

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

    public void sharedPreferenceLock(SharedPreferences sharedPreferences) {
        checked = sharedPreferences.getBoolean(getString(R.string.Lock_key), true);
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
        } else if (key.equals(getString(R.string.Lock_key))) {
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
        if (checked) {
            getApplicationContext().startActivity(new Intent(getApplicationContext(), Sign_In.class)
                    .putExtra("key", Key));
        }
    }

    public void open_settings(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void sign_out(View v) {
        mAuth.signOut();
        startActivity(new Intent(this, Sign_In.class));
    }

    public void exit(View v) {
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

    public void open_account(View v) {
        startActivity(new Intent(this, Account.class));
    }

}
