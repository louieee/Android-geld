package com.wordpress.louieefitness.geld;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.Level_2;
import com.wordpress.louieefitness.geld.Models.Level_3;
import com.wordpress.louieefitness.geld.Models.Level_4;
import com.wordpress.louieefitness.geld.Models.Level_5;
import com.wordpress.louieefitness.geld.Models.Level_6;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Account extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private TextView u_n,e_m,l_v,t_p;
    private ProgressBar progress;
    private ImageView icon;
    private Button cash, upgrade;
    private String the_key,oldest_key;
    private User the_user, my_user;
    private int value;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        current_user = mAuth.getCurrentUser();
        cash = findViewById(R.id.cash_out);
        upgrade = findViewById(R.id.upgrade);
        icon = findViewById(R.id.icon_a);
        progress = findViewById(R.id.no_received_);
        u_n = findViewById(R.id.username_a);
        e_m = findViewById(R.id.email_a);
        l_v = findViewById(R.id.level_a);
        t_p = findViewById(R.id.total_payout);
        setContentView(R.layout.activity_account);
        //display user details
            //get current user
            //get user through email
        my_user = Retrieve_user_by_Id(retrieve_object_key(User.ref,"email",current_user.getEmail()));
        u_n.setText(my_user.getUsername());
        e_m.setText(my_user.getEmail());
        l_v.setText(my_user.getLevel());
        t_p.setText(my_user.getAccumulated_payout());
        switch (my_user.getLevel()){
            case Level_1.name:
                Level_1 my1 = Level_1.Retrieve_1_by_Id(retrieve_object_key(Level_1.ref,"username",my_user.getUsername()));
                value = (Integer.parseInt(my1.getNo_received())/2) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_1.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_1);
                l_v.setTextColor(Color.parseColor(Level_1.color));
                if (my1.getReached_limit()){
                   upgrade.setVisibility(VISIBLE);
                }else{
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_2.name:
                Level_2 my2 = Level_2.Retrieve_1_by_Id(retrieve_object_key(Level_2.ref,"username",my_user.getUsername()));
                value = (Integer.parseInt(my2.getNo_received())/4) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_2.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_2);
                l_v.setTextColor(Color.parseColor(Level_2.color));
                if (my2.getReached_limit()){
                    upgrade.setVisibility(VISIBLE);
                }else{
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_3.name:
                Level_3 my3 = Level_3.Retrieve_1_by_Id(retrieve_object_key(Level_3.ref,"username",my_user.getUsername()));
                value = (Integer.parseInt(my3.getNo_received())/8) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_3.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_3);
                l_v.setTextColor(Color.parseColor(Level_3.color));
                if (my3.getReached_limit()){
                    upgrade.setVisibility(VISIBLE);
                }else{
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_4.name:
                Level_4 my4 = Level_4.Retrieve_1_by_Id(retrieve_object_key(Level_4.ref,"username",my_user.getUsername()));
                value = (Integer.parseInt(my4.getNo_received())/16) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_4.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_4);
                l_v.setTextColor(Color.parseColor(Level_4.color));
                if (my4.getReached_limit()){
                    upgrade.setVisibility(VISIBLE);
                }else{
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_5.name:
                Level_5 my5 = Level_5.Retrieve_1_by_Id(retrieve_object_key(Level_5.ref,"username",my_user.getUsername()));
                value = (Integer.parseInt(my5.getNo_received())/32) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_5.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_5);
                l_v.setTextColor(Color.parseColor(Level_5.color));
                if (my5.getReached_limit()){
                    upgrade.setVisibility(VISIBLE);
                }else{
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_6.name:
                Level_6 my6 = Level_6.Retrieve_1_by_Id(retrieve_object_key(Level_6.ref,"username",my_user.getUsername()));
                value = (Integer.parseInt(my6.getNo_received())/64) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_6.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_6);
                l_v.setTextColor(Color.parseColor(Level_6.color));
                if (my6.getReached_limit()){
                    upgrade.setVisibility(VISIBLE);
                }else{
                    upgrade.setVisibility(GONE);
                }
                break;
        }
        if (Double.parseDouble(my_user.getAccumulated_payout())>0.001){
            cash.setVisibility(VISIBLE);
        }else{
            cash.setVisibility(GONE);
        }


    }
    public String retrieve_object_key(String ref,String child, String Query){
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
                Toast.makeText(Account.this, " Database error occurred when retrieving data",
                        Toast.LENGTH_LONG).show();
            }
        });
        return the_key;
    }
    public User Retrieve_user_by_Id(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(User.ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                the_user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Account.this, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return the_user;
    }
    //write code for cashout
    //pay required bitcoin to user address
    //decrement accumulated payment for user with the payout

    public void cash_out(View v){

    }
    public void Upgrade(View v){
        switch (my_user.getLevel()){
            case Level_1.name:
                Delete(retrieve_object_key(Level_1.ref,"username",my_user.getUsername()),Level_1.ref);
                Level_2 n_l = new Level_2(my_user.getUsername());
                Level_2.Add(n_l);
                Level_2 r_1 = Level_2.Retrieve_1_by_Id(get_oldest_child_key(Level_2.ref));
                Level_2.Update_no_received(r_1.getUsername());
                my_user.setLevel(Level_2.name);
                Update_user(retrieve_object_key(User.ref,"email",my_user.getEmail()),my_user);
                break;
            case Level_2.name:
                Delete(retrieve_object_key(Level_2.ref,"username",my_user.getUsername()),Level_2.ref);
                Level_3 n_2 = new Level_3(my_user.getUsername());
                Level_3.Add(n_2);
                Level_3 r_2 = Level_3.Retrieve_1_by_Id(get_oldest_child_key(Level_3.ref));
                Level_3.Update_no_received(r_2.getUsername());
                my_user.setLevel(Level_3.name);
                Update_user(retrieve_object_key(User.ref,"email",my_user.getEmail()),my_user);
                break;
            case Level_3.name:
                Delete(retrieve_object_key(Level_3.ref,"username",my_user.getUsername()),Level_3.ref);
                Level_4 n_3 = new Level_4(my_user.getUsername());
                Level_4.Add(n_3);
                Level_4 r_3 = Level_4.Retrieve_1_by_Id(get_oldest_child_key(Level_4.ref));
                Level_4.Update_no_received(r_3.getUsername());
                my_user.setLevel(Level_4.name);
                Update_user(retrieve_object_key(User.ref,"email",my_user.getEmail()),my_user);
                break;
            case Level_4.name:
                Delete(retrieve_object_key(Level_4.ref,"username",my_user.getUsername()),Level_4.ref);
                Level_5 n_4 = new Level_5(my_user.getUsername());
                Level_5.Add(n_4);
                Level_5 r_4 = Level_5.Retrieve_1_by_Id(get_oldest_child_key(Level_5.ref));
                Level_5.Update_no_received(r_4.getUsername());
                my_user.setLevel(Level_5.name);
                Update_user(retrieve_object_key(User.ref,"email",my_user.getEmail()),my_user);
                break;
            case Level_5.name:
                Delete(retrieve_object_key(Level_5.ref,"username",my_user.getUsername()),Level_5.ref);
                Level_6 n_5 = new Level_6(my_user.getUsername());
                Level_6.Add(n_5);
                Level_6 r_5 = Level_6.Retrieve_1_by_Id(get_oldest_child_key(Level_6.ref));
                Level_6.Update_no_received(r_5.getUsername());
                my_user.setLevel(Level_6.name);
                Update_user(retrieve_object_key(User.ref,"email",my_user.getEmail()),my_user);
                break;
            case Level_6.name:
                Delete(retrieve_object_key(Level_6.ref,"username",my_user.getUsername()),Level_6.ref);
                //Implement Finished level
                break;
        }

    }
    public void Delete(String db_id, String ref) {
        DatabaseReference myRef = database.getReference(ref);
        myRef.child(db_id).removeValue();
    }
    public String get_oldest_child_key(String ref){
        DatabaseReference myRef = database.getReference(ref);
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
    public void Update_user(String db_id, User user) {
        DatabaseReference myRef = database.getReference(User.ref);
        myRef.child(db_id).setValue(user);
    }





}
