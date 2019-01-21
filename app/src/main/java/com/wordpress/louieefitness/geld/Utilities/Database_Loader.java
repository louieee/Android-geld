package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.Wallet;

public class Database_Loader extends AsyncTask<Void,Void,Integer> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    @SuppressLint("StaticFieldLeak")
    private Wallet my_wallet;


    Database_Loader(Context c, Wallet wallet) {
        this.c = c;
        this.my_wallet = wallet;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.LoadData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        Toast.makeText(c, "Wallet creation successful", Toast.LENGTH_LONG).show();
    }

    private int LoadData() {
        FirebaseDatabase db  = FirebaseDatabase.getInstance();
        DatabaseReference mRef = db.getReference(Wallet.Ref);
        String wallet_id = mRef.push().getKey();
        mRef.child(wallet_id).setValue(my_wallet);
      return 0;
    }

}
