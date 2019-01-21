package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.wordpress.louieefitness.geld.Models.User;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Downloader extends AsyncTask<Void,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context c; @SuppressLint("StaticFieldLeak")
    private StringBuilder sb;
    private User u;

    public Downloader(Context c, StringBuilder rv, User u){
        this.c = c;
        this.sb = rv;
        this.u = u;
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
        }else{
            //PARSER
            Data_Parser dataParser = new Data_Parser(c,JsonData, u);
            dataParser.execute();
        }
    }
    private String downloadData(){
        String urlAddress =  "https://blockchain.info/api/v2/create";
        HttpURLConnection conn = Connector.connect(urlAddress, sb);
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
