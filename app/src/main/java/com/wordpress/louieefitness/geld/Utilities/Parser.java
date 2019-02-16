package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Models.CONSTANTS;
import com.wordpress.louieefitness.geld.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static java.net.URLDecoder.decode;

public class Parser extends AsyncTask<Void, Void, Boolean> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    private String JsonData;
    @SuppressLint("StaticFieldLeak")
    private User u;
    private String the_key;
    private Double amount;

    Parser(Context c, String JsonData, Double amount, User u) {
        this.c = c;
        this.JsonData = JsonData;
        this.amount = amount;
        this.u = u;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return sent_money();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (JsonData == null) {
            Toast.makeText(c, "Check your Data Connection", Toast.LENGTH_SHORT).show();
        } else {
            if (result) {
                Double d_amount = u.getBalance() - amount;
                u.setBalance(d_amount);
                Update_user(retrieve_object_key(User.ref, u.getEmail()), u);
            } else {
                Toast.makeText(c, "Payment was not successful", Toast.LENGTH_LONG).show();
            }
        }

    }


    private Boolean sent_money() {
        Boolean result = false;
        try {
            JSONObject j = new JSONObject(JsonData);
            String guid = decode(j.getString("message"), "UTF-8");
            result = guid.startsWith("sent");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void Update_user(String db_id, User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(User.ref);
        myRef.child(db_id).setValue(user);
    }

    private String retrieve_object_key(String ref, String Query) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(CONSTANTS.email).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    the_key = childSnapshot.getKey();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ", databaseError.getMessage(), databaseError.toException());
            }
        });
        return the_key;
    }


}
