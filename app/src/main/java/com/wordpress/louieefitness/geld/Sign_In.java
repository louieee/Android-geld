package com.wordpress.louieefitness.geld;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.wordpress.louieefitness.geld.Utilities.Connector;
import com.wordpress.louieefitness.geld.Utilities.utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Utilities.utilities.emailValid;

public class Sign_In extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private TextInputEditText username_t, password_t;
    ProgressDialog pd; private Context c = Sign_In.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        setContentView(R.layout.activity_sign_in);
        username_t = findViewById(R.id.email_t);
        password_t = findViewById(R.id.password_t);

    }

    public void SignIn(View v) {
        if (username_t.getText().toString().length() > 0 && password_t.getText().toString().length() > 0) {
            if (!emailValid(username_t.getText().toString())) {
                username_t.setError("Enter a Valid Email");
            } else {
                ConnectivityManager cm =
                        (ConnectivityManager) Sign_In.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                assert cm != null;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    HashMap<String,String> post = new HashMap<>();
                    post.put("username",username_t.getText().toString());
                    post.put("password",password_t.getText().toString());
                    String params = utilities.StringBuild(post).toString();
                    String url = "http://127.0.0.1:8000/api/login/";
                    SignInAsync signIn = new SignInAsync();
                    signIn.execute(url,params);
                } else {
                    Toast.makeText(Sign_In.this, "Check your Data Connection", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(Sign_In.this, "All Fields must be Filled", Toast.LENGTH_SHORT).show();
        }
    }

    public void SignUp(View v) {
        startActivity(new Intent(Sign_In.this, Sign_Up.class));
    }

    public void ForgotPassword(View v) {
        startActivity(new Intent(Sign_In.this, Forgot_Password.class));
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

    @SuppressLint("StaticFieldLeak")
    public class SignInAsync extends AsyncTask<String,Void,HashMap<String,String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            if (strings.length == 2){
                return PostData(strings[0], strings[1]);
            }
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
                if (result.get("Message").equalsIgnoreCase("You are Logged in")){
                    if (result.get("Level").equalsIgnoreCase("Newbie"))
                        c.startActivity(new Intent(c,New_Account.class));
                    else{
                        c.startActivity(new Intent(c,Account.class));
                    }

                }
            }else {
                pd.dismiss();
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
        private HashMap<String, String> PostData(String urlAddress, String params) {
            HttpURLConnection conn = Connector.connect_post(urlAddress, params);
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
