package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Account;
import com.wordpress.louieefitness.geld.Database;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.New_Account;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.logging.Level;

public class Downloader extends AsyncTask<Void,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context c; @SuppressLint("StaticFieldLeak")
    private User u;
    private String urlAddress;
    private String action;
    private Wallet wallet;

    public Downloader(Context c, String url, User u, Wallet wallet, String action){
        this.c = c;
        this.urlAddress = url;
        this.u = u;
        this.action = action;
        this.wallet = wallet;
    }


    @Override
    protected String doInBackground(Void... voids) {
        return this.downloadData();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String JsonData) {
        super.onPostExecute(JsonData);
        if (JsonData == null){
            Toast.makeText(c,"Check your Data Connection",Toast.LENGTH_SHORT).show();
        }else {
            if (action.equals("verify payment")) {
                Pay_Verifier pay_verifier = new Pay_Verifier(c, JsonData,u);
                pay_verifier.execute();
            } else{
                Data_Parser dataParser = new Data_Parser(c, JsonData, wallet, u, action);
            dataParser.execute();
            }
            }

    }
    private String downloadData(){
        HttpURLConnection conn = Connector.connect(urlAddress);
        if (conn == null){
            return null;
        }
        try{
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder jsonData = new StringBuilder();

            while ((line = br.readLine())!= null){
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
