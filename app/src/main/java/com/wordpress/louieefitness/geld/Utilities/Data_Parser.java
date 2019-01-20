package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import com.wordpress.louieefitness.geld.Models.Wallet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import static java.net.URLDecoder.decode;

public class Data_Parser extends AsyncTask<Void,Void,Wallet> {
    @SuppressLint("StaticFieldLeak")
    private Context c; private String JsonData; @SuppressLint("StaticFieldLeak")
    private RecyclerView rv;
     private ArrayList<Wallet>News = new ArrayList<>();

    Data_Parser(Context c, String JsonData, RecyclerView rv){
        this.c = c;
        this.JsonData = JsonData;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Wallet doInBackground(Void... voids) {
            return this.parseData();
    }

    @Override
    protected void onPostExecute(Wallet result) {
        super.onPostExecute(result);

    }


    private Wallet parseData (){
        JSONArray jh;
        try{
            JSONObject j = new JSONObject(JsonData);
            jh = j.getJSONArray("content");
            JSONObject jo;
            Wallet my_wallet = new Wallet();
            for (int i = 0; i<jh.length(); i++){
                jo = jh.getJSONObject(i);
                String guid = decode(jo.getString("guid"), "UTF-8");
                String address = decode(jo.getString("address"), "UTF-8");
                String label = decode(jo.getString("label"), "UTF-8");
                my_wallet.setAddress(address);
                my_wallet.setGuid(guid);
                my_wallet.setMain_Address(label);
            }
            return my_wallet;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
