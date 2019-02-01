package com.wordpress.louieefitness.geld.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.R;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

public class User_Wallet extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{
    private Context c;
    private Wallet myWallet;
    public final static String Key = "1";
    private Boolean checked;
    private TextInputEditText amount,receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        c = User_Wallet.this;
        TextView e_mail = findViewById(R.id.wallet_email);
        TextView address = findViewById(R.id.wallet_address);
        TextView balance = findViewById(R.id.wallet_balance);
        amount = findViewById(R.id.send_amount);
        receiver = findViewById(R.id.receiver_address);
        setContentView(R.layout.activity_wallet);
        FirebaseUser current_user = mAuth.getCurrentUser();
        if (current_user == null){
            startActivity(new Intent(c,Sign_Up.class));
        }else{
            User fake = new User();
            String email = current_user.getEmail();
            myWallet = Wallet.retrieve_wallet("email", email);
            Downloader get_bal = new Downloader(c,"https://blockchain.info/merchant/"
                    + myWallet.getGuid()+"/balance?password="+ myWallet.getPassword(),fake, myWallet,"get balance");
            get_bal.execute();
            e_mail.setText(myWallet.getEmail());
            address.setText(myWallet.getAddress());
            balance.setText(String.valueOf(myWallet.getBalance()));
        }

    }

    public void Send_Bit_coin(View v){
        if (amount.getText().toString().isEmpty() || receiver.getText().toString().isEmpty()){
            Toast.makeText(c,"All Fields must be Filled",Toast.LENGTH_LONG).show();
        }else{
            String money = String.valueOf(Double.parseDouble(amount.getText().toString())*100000000);
            Downloader sender = new Downloader(c, "https://blockchain.info/merchant/"+
                    myWallet.getGuid()+"/payment?password="+
                    myWallet.getPassword()+"&to="+receiver.getText().toString()+
                    "&amount="+money,null, myWallet,"send money");
            sender.execute();

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

}
