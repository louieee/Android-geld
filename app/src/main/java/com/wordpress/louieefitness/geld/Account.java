package com.wordpress.louieefitness.geld;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.Level_2;
import com.wordpress.louieefitness.geld.Models.Level_3;
import com.wordpress.louieefitness.geld.Models.Level_4;
import com.wordpress.louieefitness.geld.Models.Level_5;
import com.wordpress.louieefitness.geld.Models.Level_6;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Utilities.Connector;

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

public class Account extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public final static String Key = "4";
    private Boolean checked;
    ProgressBar progress;
    ProgressDialog pd;
    TextView u_n;
    TextView e_m;
    TextView l_v ;
    TextView t_p;
    ImageView icon ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        setContentView(R.layout.activity_account);
        icon = findViewById(R.id.icon_a);
        progress = findViewById(R.id.no_received_);
        u_n = findViewById(R.id.username_a);
        e_m = findViewById(R.id.email_a);
        l_v = findViewById(R.id.level_a);
        t_p = findViewById(R.id.total_payout);
        String username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
        String url = "http://127.0.0.1:8000/api/account/"+username+"/";
        AccountAsync getAccountDetails = new AccountAsync();
        getAccountDetails.execute(url);
    }

    public void Upgrade(View v) {
        AccountAsync upgrade = new AccountAsync();
        String username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
        String url = "http://127.0.0.1:8000/api/upgrade/"+username+"/";
        upgrade.execute(url);
    }
    public void Cashout(View v) {
        AccountAsync cashout = new AccountAsync();
        String username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
        String url = "http://127.0.0.1:8000/api/cashout/"+username+"/";
        cashout.execute(url);
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

    public void open_wallet(View v) {
        startActivity(new Intent(this, User_Wallet.class));
    }

    public void sign_out(View v) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountAsync logout = new AccountAsync();
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
    public class AccountAsync extends AsyncTask<String,Void,HashMap<String,String>>{
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            return GetData(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Account.this);
            pd.setMessage("Retrieving Data...");
            pd.show();

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {
            super.onPostExecute(result);
            if (result.containsKey("Message")) {
                pd.setMessage(result.get("Message"));
                pd.dismiss();
                Toast.makeText(Account.this, result.get("Message"),Toast.LENGTH_SHORT).show();
                if (result.get("Message").equals("You are currently logged out")){
                    Account.this.startActivity(new Intent(Account.this, Sign_In.class));
                }
            }else {
                pd.dismiss();
                User my_user = new User();
                my_user.setEmail(result.get("email"));
                my_user.setUsername(result.get("username"));
                my_user.setLevel(result.get("level"));
                my_user.setBalance(Double.parseDouble(result.get("Balance")));
                int value = Integer.parseInt(result.get("percent"));
                u_n.setText(my_user.getUsername());
                e_m.setText(my_user.getEmail());
                l_v.setText(my_user.getLevel());
                t_p.setText(String.valueOf(my_user.getBalance()));
                switch (my_user.getLevel()) {
                    case Level_1.name:
                        progress.setProgress(value);
                        progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_1.color), PorterDuff.Mode.SRC_IN);
                        icon.setImageResource(R.drawable.stage_1);
                        l_v.setTextColor(Color.parseColor(Level_1.color));
                        break;
                    case Level_2.name:
                        progress.setProgress(value);
                        progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_2.color), PorterDuff.Mode.SRC_IN);
                        icon.setImageResource(R.drawable.stage_2);
                        l_v.setTextColor(Color.parseColor(Level_2.color));
                        break;
                    case Level_3.name:
                        progress.setProgress(value);
                        progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_3.color), PorterDuff.Mode.SRC_IN);
                        icon.setImageResource(R.drawable.stage_3);
                        l_v.setTextColor(Color.parseColor(Level_3.color));
                        break;
                    case Level_4.name:
                        progress.setProgress(value);
                        progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_4.color), PorterDuff.Mode.SRC_IN);
                        icon.setImageResource(R.drawable.stage_4);
                        l_v.setTextColor(Color.parseColor(Level_4.color));
                        break;
                    case Level_5.name:
                        progress.setProgress(value);
                        progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_5.color), PorterDuff.Mode.SRC_IN);
                        icon.setImageResource(R.drawable.stage_5);
                        l_v.setTextColor(Color.parseColor(Level_5.color));
                        break;
                    case Level_6.name:
                        progress.setProgress(value);
                        progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_6.color), PorterDuff.Mode.SRC_IN);
                        icon.setImageResource(R.drawable.stage_6);
                        l_v.setTextColor(Color.parseColor(Level_6.color));
                        break;
                }
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
                    String message = j.getString("Message");
                    data.put("Message",message);
                    return data;

                }else {
                    String username = j.getString("username");
                    String email = j.getString("email");
                    String level = j.getString("level");
                    String percentage = j.getString("percentage");
                    String Balance = j.getString("Balance");
                    data.put("username", username);
                    data.put("email", email);
                    data.put("level", level);
                    data.put("percentage", percentage);
                    data.put("Balance", Balance);
                    return data;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
