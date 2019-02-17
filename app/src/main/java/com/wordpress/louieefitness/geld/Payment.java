package com.wordpress.louieefitness.geld;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.Utilities.Connector;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Payment extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public final static String Key = "2";
    private Boolean checked;
    private Context c = Payment.this;
    TextView address, balance;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        setContentView(R.layout.activity_payment);
        address = findViewById(R.id.wallet_info);
        balance = findViewById(R.id.wallet_info2);
        String user_username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
        String url = "http://127.0.0.1:8000/api/wallet/"+user_username+"/";
        PaymentAsync getWallet = new PaymentAsync();
        getWallet.execute(url);



    }


    public void make_investment(View v) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("Invest")
                .setMessage("Ensure that you have Funded your wallet with more than 0.003BTC." +
                        "Invest ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user_username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
                        String url = "http://127.0.0.1:8000/api/payment/"+user_username+"/";
                        PaymentAsync pay = new PaymentAsync();
                        pay.execute(url);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
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
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PaymentAsync logout = new PaymentAsync();
                        String url = "http://127.0.0.1:8000/logout/";
                        logout.execute(url);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
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
    @SuppressLint("StaticFieldLeak")
    public class PaymentAsync extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            return GetData(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(c);
            pd.setMessage("Retrieving Data...");
            pd.show();

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {
            super.onPostExecute(result);
            if (result.containsKey("Message")) {
                pd.setMessage(result.get("Message"));
                pd.dismiss();
                Toast.makeText(c, result.get("Message"),Toast.LENGTH_SHORT).show();
                if (result.get("Message").equals("You are currently logged out")){
                    c.startActivity(new Intent(c, Sign_In.class));
                }else if (result.containsKey("Notice")){
                    c.startActivity(new Intent(c,New_Account.class));
                }
            }else {
                pd.dismiss();
                address.setText(result.get("address"));
                balance.setText(result.get("balance"));
            }

        }

        private HashMap<String, String> GetData(String urlAddress) {
            HttpURLConnection conn = Connector.connect_get(urlAddress);
            if (conn == null) {
                return null;
            }
            try {
                InputStream is = new BufferedInputStream(conn.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuilder jsonData = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    jsonData.append(line).append("\n");

                }
                br.close();
                is.close();
                return parseData(jsonData.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        HashMap<String, String> parseData(String JsonData) {
            try {
                JSONObject j = new JSONObject(JsonData);
                HashMap<String, String> data = new HashMap<>();
                if (j.has("Message")){
                    if (j.has("Notice")){
                        String message = j.getString("Message");
                        String notice = j.getString("Notice");
                        data.put("Message", message);
                        data.put("Notice", notice);
                        return data;
                    }else {
                        String message = j.getString("Message");
                        data.put("Message", message);
                        return data;
                    }
                }else {
                    String username = j.getString("username");
                    String email = j.getString("email");
                    data.put("username", username);
                    data.put("email", email);
                    return data;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
