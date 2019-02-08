package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.Payment;

public class Database_Loader extends AsyncTask<FirebaseDatabase,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    @SuppressLint("StaticFieldLeak")
    private Wallet my_wallet;
    private ProgressDialog pd;
    private String result;


    Database_Loader(Context c, Wallet wallet) {
        this.c = c;
        this.my_wallet = wallet;
    }

    @Override
    protected String doInBackground(FirebaseDatabase... firebaseDatabases) {
        FirebaseDatabase db = firebaseDatabases[0];
        return this.LoadData(db);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Sign Up");
        pd.setMessage("Creating Wallet...");
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result.equals("Successful")) {
            pd.setMessage("Sign Up was Successful");
            pd.dismiss();
            c.startActivity(new Intent(c.getApplicationContext(), Payment.class));
        }else{
            pd.setMessage(result);
            pd.dismiss();
        }
    }

    private String LoadData(FirebaseDatabase db) {
        DatabaseReference mRef = db.getReference(Wallet.Ref);
        String wallet_id = mRef.push().getKey();
        mRef.child(wallet_id).setValue(my_wallet)
        .addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    result = "Successful";
                }else{
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            result = e.getMessage();
                        }
                    });
                }
            }
        });
      return result;
    }

}
