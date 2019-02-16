package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Downloader extends AsyncTask<FirebaseDatabase, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    @SuppressLint("StaticFieldLeak")
    private User u;
    private String urlAddress;
    private String action;
    private Wallet wallet;
    private FirebaseDatabase db;
    private ProgressDialog pd;

    public Downloader(Context c, String url, User u, Wallet wallet, String action) {
        this.c = c;
        this.urlAddress = url;
        this.u = u;
        this.action = action;
        this.wallet = wallet;
    }


    @Override
    protected String doInBackground(FirebaseDatabase... firebaseDatabases) {
        this.db = firebaseDatabases[0];
        return this.downloadData();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String word = "";
        switch (action) {
            case "send bitcon":
                word = "Send Bitcoin";
                break;
            case "send money":
                word = "Send Bitcoin";
                break;
            case "get balance":
                word = "Bitcoin Balance";
                break;
            case "create wallet":
                word = "Sign Up";
                break;
        }

        pd = new ProgressDialog(c);
        pd.setMessage("Initiating Connection...");
        pd.setTitle(word);
        pd.show();

    }


    @Override
    protected void onPostExecute(String JsonData) {
        super.onPostExecute(JsonData);
        pd.dismiss();
        if (JsonData == null) {
            Toast.makeText(c, "Check your Data Connection", Toast.LENGTH_SHORT).show();
        } else {
            if (action.equals("verify payment")) {
                Pay_Verifier pay_verifier = new Pay_Verifier(c, JsonData, u);
                pay_verifier.execute(db);
            } else {
                Data_Parser dataParser = new Data_Parser(c, JsonData, wallet, u, action);
                dataParser.execute(db);
            }
        }

    }

    private String downloadData() {
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
            return jsonData.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
