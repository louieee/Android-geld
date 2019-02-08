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
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Account;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.User;
import static com.wordpress.louieefitness.geld.Models.Level_1.Add;
import static com.wordpress.louieefitness.geld.Models.Level_1.Update_no_received;


    public class Pay_Verifier extends AsyncTask<FirebaseDatabase,Void,Boolean> {
    @SuppressLint("StaticFieldLeak")
    private Context c; private String JsonData; @SuppressLint("StaticFieldLeak")
     private User u;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String the_key;
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
    protected Boolean doInBackground(FirebaseDatabase... DBs) {
        return verify_payment(DBs[0]);
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
    private Boolean verify_payment(FirebaseDatabase db) {
        Boolean result = false;
        Double Bal = Double.parseDouble(JsonData);
        Double Bal_btc = Bal / 100000000;
        if (Bal_btc >= 0.0025) {
            //transfer user to Level 1
            Level_1 n_u = new Level_1();
            n_u.setUsername(u.getUsername());
            if (u.getReferer().isEmpty()){
                Level_1 ref_ = Level_1.get_oldest_object(db);
                if (ref_ != null) {
                    Update_no_received(db,ref_.getUsername());
                    update_user(ref_.getUsername());
                }

            }else {
                //Check if referer no < 2 && if referer is in level1
                Level_1 ref_ = Level_1.retrieve_object(db,"Username",u.getReferer());
                if (ref_ == null) {
                    Level_1 ref__ = Level_1.get_oldest_object(db);
                    if (ref__ != null) {
                        Update_no_received(db,ref__.getUsername());
                        update_user(ref__.getUsername());
                    }
                } else {
                    if (ref_.getNo_received() < 2) {
                        Update_no_received(db,ref_.getUsername());
                    } else {
                        Level_1 ref__ = Level_1.get_oldest_object(db);
                        if (ref__ != null) {
                            Update_no_received(db,ref__.getUsername());
                            update_user(ref__.getUsername());
                        }
                    }
                }
                result = true;
            }
            Add(db,n_u);
        } else {
            result = false;
        }
        return result;
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
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());

            }
        });
        return the_key;
    }
    private void Delete_New_User(String ref,String id){
        DatabaseReference dr = database.getReference(ref).child(id);
        dr.removeValue();
    }
    private void update_user(String ref){
        u.setReferer(ref);
        DatabaseReference user_ref = database.getReference(User.ref);
        String k = retrieve_object_key(User.ref,"email",u.getEmail());
        user_ref.child(k).setValue(u);
    }




}
