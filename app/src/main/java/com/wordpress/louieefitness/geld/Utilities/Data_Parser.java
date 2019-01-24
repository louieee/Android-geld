package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wordpress.louieefitness.geld.Account;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.Payment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import static java.net.URLDecoder.decode;

public class Data_Parser extends AsyncTask<Void,Void,Wallet> {
    @SuppressLint("StaticFieldLeak")
    private Context c; private String JsonData; @SuppressLint("StaticFieldLeak")
     private User u;
    private String action;
    private Wallet m_wallet;
    Data_Parser(Context c, String JsonData, Wallet wallet, User u, String action){
        this.c = c;
        this.JsonData = JsonData;
        this.m_wallet = wallet;
        this.action = action;
        this.u = u;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Wallet doInBackground(Void... voids) {
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
        }
        return res;
    }

    @Override
    protected void onPostExecute(Wallet result) {
        super.onPostExecute(result);
        if (JsonData == null){
            Toast.makeText(c,"Check your Data Connection",Toast.LENGTH_SHORT).show();
        }else{
            if (action.equals("create wallet")){
                Database_Loader loader = new Database_Loader(c,result);
                loader.execute();
            }else if (action.equals("send bitcoin")){
                if (result.getGuid().startsWith("Sent")){
                    Toast.makeText(c,result.getGuid(), Toast.LENGTH_LONG).show();
                    Intent n = new Intent(c, Account.class);
                    c.startActivity(n);
                }else{
                    Toast.makeText(c,result.getGuid(), Toast.LENGTH_LONG).show();
                }
            }else if (action.equals("get balance")){
                Toast.makeText(c, "Your balance is "+result.getBalance().toString()+" BTC",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


    private Wallet parseData (){
        try{
            JSONObject j = new JSONObject(JsonData);
            Wallet my_wallet = new Wallet();
            String guid = decode(j.getString("guid"), "UTF-8");
            String address = decode(j.getString("address"), "UTF-8");
            String label = decode(j.getString("label"), "UTF-8");
            my_wallet.setAddress(address);
            my_wallet.setGuid(guid);
            my_wallet.setMain_Address(label);
            my_wallet.setEmail(u.getEmail());
            return my_wallet;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Wallet get_userWallet(){
        Wallet c = new Wallet();
        try{
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
    private Wallet my_wallet_bal(){
        try{
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

}
