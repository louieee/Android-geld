package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Downloader_Post extends AsyncTask<StringBuilder, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    @SuppressLint("StaticFieldLeak")
    private User u;
    private String urlAddress;
    private String action;
    private Wallet wallet;

    public Downloader_Post(Context c, String url, User u, Wallet wallet, String action) {
        this.c = c;
        this.urlAddress = url;
        this.u = u;
        this.action = action;
        this.wallet = wallet;
    }


    @Override
    protected String doInBackground(StringBuilder... stringBuilders) {

        return downloadData(stringBuilders[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String JsonData) {
        super.onPostExecute(JsonData);
        if (JsonData == null) {
            Toast.makeText(c, "Check your Data Connection", Toast.LENGTH_SHORT).show();
        } else {
            Data_Parser dataParser = new Data_Parser(c, JsonData, wallet, u, action);
            dataParser.execute();
        }
    }

    private String downloadData(StringBuilder params) {
        HttpURLConnection conn = Connector.connect_post(urlAddress, params);
        if (conn == null) {
            return null;
        }
        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;
    }
}
