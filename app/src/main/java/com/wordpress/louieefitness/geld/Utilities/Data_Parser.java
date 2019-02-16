package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.CONSTANTS;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.New_Account;
import com.wordpress.louieefitness.geld.User_Wallet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static java.net.URLDecoder.decode;

public class Data_Parser extends AsyncTask<FirebaseDatabase, Void, Wallet> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    private String JsonData;
    @SuppressLint("StaticFieldLeak")
    private User u;
    private String action;
    private ProgressDialog pd;
    private FirebaseDatabase db;
    private Wallet m_wallet;

    Data_Parser(Context c, String JsonData, Wallet wallet, User u, String action) {
        this.c = c;
        this.JsonData = JsonData;
        this.m_wallet = wallet;
        this.action = action;
        this.u = u;
    }

    @Override
    protected Wallet doInBackground(FirebaseDatabase... firebaseDatabases) {
        this.db = firebaseDatabases[0];
        Wallet res = new Wallet();
        switch (action) {
            case "create wallet":
                res = this.parseData();
                break;
            case "send bitcoin":
                res = this.get_userWallet();
                break;
            case "get balance":
                res = (this.my_wallet_bal());
                break;
            case "send money":
                res = (this.send_bit());
                break;
        }
        return res;
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
        pd.setTitle(word);
        pd.setMessage("Parsing Data");
        pd.show();
    }


    @Override
    protected void onPostExecute(Wallet result) {
        super.onPostExecute(result);
        pd.dismiss();
        if (JsonData == null) {
            Toast.makeText(c, "Check your Data Connection", Toast.LENGTH_SHORT).show();
        } else {
            switch (action) {
                case "create wallet":
                    Database_Loader loader = new Database_Loader(c, result);
                    loader.execute(db);
                    break;
                case "send bitcoin":
                    if (result.getGuid().startsWith("Sent")) {
                        Toast.makeText(c, result.getGuid(), Toast.LENGTH_LONG).show();
                        Intent n = new Intent(c, New_Account.class);
                        c.startActivity(n);
                    } else {
                        Toast.makeText(c, result.getGuid(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case "get balance":
                    Wallet.update_wallet(db, CONSTANTS.email, result.getEmail(), result);
                    if (u == null) {
                        Toast.makeText(c, "Your balance is " + result.getBalance().toString() + " BTC",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        c.startActivity(new Intent(c, User_Wallet.class));
                    }
                    break;
                case "send money":
                    if (result.getGuid().startsWith("Sent")) {
                        Toast.makeText(c, result.getGuid(), Toast.LENGTH_LONG).show();
                        Intent n = new Intent(c, User_Wallet.class);
                        c.startActivity(n);
                    } else {
                        Toast.makeText(c, result.getGuid(), Toast.LENGTH_LONG).show();
                    }
                    break;
            }

        }
    }


    private Wallet parseData() {
        try {
            JSONObject j = new JSONObject(JsonData);
            String guid = decode(j.getString("guid"), "UTF-8");
            String address = decode(j.getString("address"), "UTF-8");
            String label = decode(j.getString("label"), "UTF-8");
            m_wallet.setAddress(address);
            m_wallet.setGuid(guid);
            m_wallet.setMain_Address(label);
            m_wallet.setEmail(u.getEmail());
            return m_wallet;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Wallet get_userWallet() {

        Wallet c = new Wallet();
        try {
            JSONObject j = new JSONObject(JsonData);
            Wallet my_wallet = new Wallet();
            String guid = decode(j.getString("message"), "UTF-8");
            my_wallet.setGuid(guid);
            return c;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Wallet my_wallet_bal() {
        try {
            JSONObject j = new JSONObject(JsonData);
            String balance = decode(j.getString("balance"), "UTF-8");
            Double bal = Double.parseDouble(balance) / 100000000;
            m_wallet.setBalance(bal);
            return m_wallet;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Wallet send_bit() {
        Wallet c = new Wallet();
        try {
            JSONObject j = new JSONObject(JsonData);
            Wallet my_wallet = new Wallet();
            String guid = decode(j.getString("message"), "UTF-8");
            my_wallet.setGuid(guid);
            return c;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
