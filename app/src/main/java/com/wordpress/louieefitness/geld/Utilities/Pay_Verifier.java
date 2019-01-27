package com.wordpress.louieefitness.geld.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static java.net.URLDecoder.decode;

public class Pay_Verifier extends AsyncTask<Void,Void,Boolean> {
    @SuppressLint("StaticFieldLeak")
    private Context c; private String JsonData; @SuppressLint("StaticFieldLeak")
     private User u;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Level_1 ml;
    private String the_key, oldest_key;
    Pay_Verifier(Context c, String JsonData, User u){
        this.c = c;
        this.JsonData = JsonData;
        this.u = u;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return verify_payment();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (!result){
            Toast.makeText(c, "Your Investment has not been Verified", Toast.LENGTH_LONG).show();
        }else{
            Intent account = new Intent(c.getApplicationContext(), Account.class);
            c.startActivity(account);
        }

    }
    private Boolean verify_payment() {
        Boolean result;
        Double Bal = Double.parseDouble(JsonData);
        Double Bal_btc = Bal / 100000000;
        if (Bal_btc >= 0.0025) {
            //transfer user to Level 1
            Level_1 n_u = new Level_1();
            n_u.setUsername(u.getUsername());
            Add_To_Level_1(Level_1.ref, n_u);
            //delete user from new users
            String my_key = retrieve_object_key(New_Users.ref, "username", u.getUsername());
            Delete_New_User(New_Users.ref, my_key);
            //Check if referer no < 2 && if referer is in level1
            String ref_key = retrieve_object_key(Level_1.ref, "username", u.getReferer());
            if (ref_key.isEmpty()) {
                String ref_username = get_the_referer();
                Update_no_received(ref_username);
                update_user(ref_username);
            } else {
                Level_1 r = Retrieve_Referer(ref_key);
                if (Integer.parseInt(r.getNo_received()) < 2) {
                    Update_no_received(u.getReferer());
                } else {
                    String ref_username = get_the_referer();
                    Update_no_received(ref_username);
                    update_user(ref_username);
                }
            }
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    private void Add_To_Level_1(String ref, Level_1 new_u) {
        DatabaseReference d_ref = database.getReference(ref);
        String db_id = d_ref.push().getKey();
        d_ref.child(db_id).setValue(new_u);
    }
    private String retrieve_object_key(String ref,String child, String Query){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    the_key = childSnapshot.getKey();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(c, " Database error occurred when retrieving data",
                        Toast.LENGTH_LONG).show();

            }
        });
        return the_key;
    }
    private void Delete_New_User(String ref,String id){
        DatabaseReference dr = database.getReference(ref).child(id);
        dr.removeValue();
    }
    private void Update_no_received(final String Username) {
        DatabaseReference the_ref = database.getReference(Level_1.ref);
        the_ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Level_1 p = mutableData.getValue(Level_1.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getUsername().equals(Username) ) {
                    int num = Integer.parseInt(p.getNo_received());
                    num = num + 1;
                    p.setNo_received(String.valueOf(num));
                    if (num == 2){
                        p.setReached_limit(true);
                    }
                }
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d("Message: ", "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    private Level_1 Retrieve_Referer(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(Level_1.ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ml = dataSnapshot.getValue(Level_1.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(c, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return ml;
    }
    private String get_oldest_child(){
        DatabaseReference myRef = database.getReference(Level_1.ref);
        Query oldest = myRef.orderByChild("reached_limit").equalTo(false).limitToFirst(1);
        oldest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    oldest_key = childSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Message: ", "onCancelled", databaseError.toException());
            }
        });
        return oldest_key;
    }
    private  String get_the_referer(){
        String my_id = get_oldest_child();
        Level_1 re = Retrieve_Referer(my_id);
        return re.getUsername();
    }
    private void update_user(String ref){
        u.setReferer(ref);
        DatabaseReference user_ref = database.getReference(User.ref);
        String k = retrieve_object_key(User.ref,"email",u.getEmail());
        user_ref.child(k).setValue(u);
    }




}
