package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import static java.net.URLDecoder.decode;

public class Data_Parser extends AsyncTask<Void,Void,Wallet> {
    @SuppressLint("StaticFieldLeak")
    private Context c; private String JsonData; @SuppressLint("StaticFieldLeak")
     private User u;

    Data_Parser(Context c, String JsonData, User u){
        this.c = c;
        this.JsonData = JsonData;
        this.u = u;
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
        if (JsonData == null){
            Toast.makeText(c,"Check your Data Connection",Toast.LENGTH_SHORT).show();
        }else{
            //PARSER
            Database_Loader loader = new Database_Loader(c,result);
            loader.execute();
        }
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
                my_wallet.setEmail(u.getEmail());
            }
            return my_wallet;

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
