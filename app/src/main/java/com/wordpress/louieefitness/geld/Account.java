package com.wordpress.louieefitness.geld;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Models.CONSTANTS;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.Level_2;
import com.wordpress.louieefitness.geld.Models.Level_3;
import com.wordpress.louieefitness.geld.Models.Level_4;
import com.wordpress.louieefitness.geld.Models.Level_5;
import com.wordpress.louieefitness.geld.Models.Level_6;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.Utilities.Make_Pay;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wordpress.louieefitness.geld.Models.User.Update_user;
import static com.wordpress.louieefitness.geld.Models.User.retrieve_user;

public class Account extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private FirebaseAuth mAuth;
    private TextView cash, upgrade;
    private String the_key;
    private User my_user;
    private Wallet the_wallet;
    public final static String Key = "4";
    private Boolean checked;
    final Double Bal_residue = 0.001;
    private String my_money;
    private Double minimum, upgrade_money;
    private Double withdraw;
    private Boolean withdraw_ready = false;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSharedPreferences();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        FirebaseUser current_user = mAuth.getCurrentUser();
        cash = findViewById(R.id.cash_out);
        upgrade = findViewById(R.id.upgrade);
        ImageView icon = findViewById(R.id.icon_a);
        ProgressBar progress = findViewById(R.id.no_received_);
        TextView u_n = findViewById(R.id.username_a);
        TextView e_m = findViewById(R.id.email_a);
        TextView l_v = findViewById(R.id.level_a);
        TextView t_p = findViewById(R.id.total_payout);
        setContentView(R.layout.activity_account);
        //display user details
            //get current user
            //get user through email
        assert current_user != null;
        my_user = retrieve_user(CONSTANTS.email, current_user.getEmail());
        u_n.setText(my_user.getUsername());
        e_m.setText(my_user.getEmail());
        l_v.setText(my_user.getLevel());
        switch (my_user.getLevel()){
            case Level_1.name:
                minimum = 0.001;
                upgrade_money = 0.004;
                Level_1 my1 = Level_1.retrieve_object(CONSTANTS.username,my_user.getUsername());
                int value = (my1.getNo_received() / 2) * 100;
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
                minimum = 0.005;
                upgrade_money = 0.006;
                Level_2 my2 = Level_2.retrieve_object(CONSTANTS.username,my_user.getUsername());
                value = (my2.getNo_received()/4) * 100;
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
                minimum = 0.01;
                upgrade_money = 0.018;
                Level_3 my3 = Level_3.retrieve_object(CONSTANTS.username,my_user.getUsername());
                value = (my3.getNo_received()/8) * 100;
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
                minimum = 0.05;
                upgrade_money = 0.088;
                Level_4 my4 = Level_4.retrieve_object(CONSTANTS.username,my_user.getUsername());
                value = (my4.getNo_received()/16) * 100;
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
                minimum = 0.3;
                upgrade_money = 0.816;
                Level_5 my5 = Level_5.retrieve_object(CONSTANTS.username,my_user.getUsername());
                value = (my5.getNo_received()/32) * 100;
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
                minimum = 2.0;
                upgrade_money = 2.224;
                Level_6 my6 = Level_6.retrieve_object(CONSTANTS.username,my_user.getUsername());
                value = (my6.getNo_received()/64) * 100;
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
            case "Finished":
                minimum = 0.0025;
                progress.setVisibility(GONE);
                icon.setVisibility(GONE);
                l_v.setTextColor(getResources().getColor(R.color.black));
                upgrade.setText(R.string.sign_off);
                upgrade.setVisibility(VISIBLE);
                break;
        }
        Double your_cash = my_user.getBalance();
        withdraw = your_cash - Bal_residue-upgrade_money;
        if (withdraw < 0.0){
            t_p.setText(String.valueOf(0));
        }else {
            t_p.setText(String.valueOf(withdraw));
        }
        my_money = String.valueOf(withdraw * 100000000);
        if (withdraw >= minimum){
            withdraw_ready = true;
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
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
                the_key = "";
            }
        });
        return the_key;
    }

    public void cash_out(View v){
        my_money = String.valueOf(withdraw * 100000000);
        if (withdraw >= minimum && withdraw_ready){
            Wallet user_wallet = Retrieve_wallet_by_Id(retrieve_object_key(Wallet.Ref,CONSTANTS.email,my_user.getEmail()));
            Make_Pay pay = new Make_Pay(Account.this, "https://blockchain.info/merchant/"+
                    Sign_Up.GUID+"/payment?password="+
                    Sign_Up.PASSWORD+"&to="+user_wallet.getAddress()+
                    "&amount="+my_money,my_user, withdraw);
            pay.execute();

        }else{
            Toast.makeText(Account.this,"Your Balance is insufficient",Toast.LENGTH_LONG).show();
        }
        cash.setVisibility(GONE);
    }
    public void Upgrade(View v){
        switch (my_user.getLevel()){
            case Level_1.name:
                Delete(retrieve_object_key(Level_1.ref,CONSTANTS.username,my_user.getUsername()),Level_1.ref);
                Level_2 n_l = new Level_2(my_user.getUsername());
                Level_2 r_1 = Level_2.get_oldest_object();
                if (r_1 != null){
                    Level_2.Update_no_received(r_1.getUsername());
                    User.Increment_Balance(r_1.getUsername(),0.004);
                }
                Level_2.Add(n_l);
                my_user.setLevel(Level_2.name);
                Update_user(retrieve_object_key(User.ref,CONSTANTS.email,my_user.getEmail()),my_user);
                break;
            case Level_2.name:
                Delete(retrieve_object_key(Level_2.ref,CONSTANTS.username,my_user.getUsername()),Level_2.ref);
                Level_3 n_2 = new Level_3(my_user.getUsername());
                Level_3 r_2 = Level_3.get_oldest_object();
                if (r_2 != null){
                    Level_3.Update_no_received(r_2.getUsername());
                    User.Increment_Balance(r_2.getUsername(),0.006);
                }
                Level_3.Add(n_2);
                my_user.setLevel(Level_3.name);
                Update_user(retrieve_object_key(User.ref,CONSTANTS.email,my_user.getEmail()),my_user);
                break;
            case Level_3.name:
                Delete(retrieve_object_key(Level_3.ref,CONSTANTS.username,my_user.getUsername()),Level_3.ref);
                Level_4 n_3 = new Level_4(my_user.getUsername());
                Level_4 r_3 = Level_4.get_oldest_object();
                if (r_3 != null){
                    Level_4.Update_no_received(r_3.getUsername());
                    User.Increment_Balance(r_3.getUsername(),0.018);
                }
                Level_4.Add(n_3);
                my_user.setLevel(Level_4.name);
                Update_user(retrieve_object_key(User.ref,CONSTANTS.email,my_user.getEmail()),my_user);
                break;
            case Level_4.name:
                Delete(retrieve_object_key(Level_4.ref,CONSTANTS.username,my_user.getUsername()),Level_4.ref);
                Level_5 n_4 = new Level_5(my_user.getUsername());
                Level_5 r_4 = Level_5.get_oldest_object();
                if (r_4 != null){
                    Level_5.Update_no_received(r_4.getUsername());
                    User.Increment_Balance(r_4.getUsername(),0.088);
                }
                Level_5.Add(n_4);
                my_user.setLevel(Level_5.name);
                Update_user(retrieve_object_key(User.ref,CONSTANTS.email,my_user.getEmail()),my_user);
                break;
            case Level_5.name:
                Delete(retrieve_object_key(Level_5.ref,CONSTANTS.username,my_user.getUsername()),Level_5.ref);
                Level_6 n_5 = new Level_6(my_user.getUsername());
                Level_6 r_5 = Level_6.get_oldest_object();
                if (r_5 != null){
                    Level_6.Update_no_received(r_5.getUsername());
                    User.Increment_Balance(r_5.getUsername(),0.816);
                }
                Level_6.Add(n_5);
                my_user.setLevel(Level_6.name);
                Update_user(retrieve_object_key(User.ref,CONSTANTS.email,my_user.getEmail()),my_user);
                break;
            case Level_6.name:
                Delete(retrieve_object_key(Level_6.ref,CONSTANTS.username,my_user.getUsername()),Level_6.ref);
                my_user.setLevel("Finished");
                Update_user(retrieve_object_key(User.ref,CONSTANTS.email,my_user.getEmail()),my_user);
                break;
            case "Finished":
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                        .setTitle("Sign Off")
                        .setMessage("Before Signing off, get all your Wallet Details, " +
                                "Because your account will be deleted." +
                                "Are you ready to sign off ? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User.Delete_user("Email",my_user.getEmail());
                                Wallet.delete_wallet("Email",my_user.getEmail());
                                Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();break;
        }
        upgrade.setVisibility(GONE);

    }
    public void Delete(String db_id, String ref) {
        DatabaseReference myRef = database.getReference(ref);
        myRef.child(db_id).removeValue();
    }

    public Wallet Retrieve_wallet_by_Id(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(Wallet.Ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                the_wallet = dataSnapshot.getValue(Wallet.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Message: ",databaseError.getMessage(),databaseError.toException());
                the_wallet = null;
            }
        });

        return the_wallet;
    }


    public void sharedPreferenceTheme(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(getString(R.string.Theme_key), getString(R.string.level_1));
        if (value.equalsIgnoreCase("1")){
            getApplicationContext().setTheme(R.style.Light);
        }else if (value.equalsIgnoreCase("2")){
            getApplicationContext().setTheme(R.style.Dark);
        }
    }
    public void sharedPreferenceLock(SharedPreferences sharedPreferences){
        checked = sharedPreferences.getBoolean(getString(R.string.Lock_key),true);
    }
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceTheme(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.Theme_key))){
            sharedPreferenceTheme(sharedPreferences);
        }else if (key.equals(getString(R.string.Lock_key))){
            sharedPreferenceLock(sharedPreferences);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checked){
            getApplicationContext().startActivity(new Intent(getApplicationContext(),Sign_In.class)
            .putExtra("key",Key));
        }
    }
    public void open_settings (View v){
        startActivity(new Intent(this,SettingsActivity.class));
    }
    public void open_wallet (View v){
        startActivity(new Intent(this,User_Wallet.class));
    }
    public void sign_out (View v){
        mAuth.signOut();
        startActivity(new Intent(this,Sign_In.class));
    }
    public void exit_app (View v){
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit App? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
    public void toggle_nav (View v){
        final LinearLayout nav = findViewById(R.id.nav_bar);
        switch (nav.getVisibility()) {
            case VISIBLE:
                nav.setVisibility(GONE);
                break;
            case GONE:
                nav.setVisibility(VISIBLE);
                break;
            case View.INVISIBLE:
                break;
        }
    }
}