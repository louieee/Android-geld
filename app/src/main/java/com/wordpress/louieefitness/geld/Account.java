package com.wordpress.louieefitness.geld;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
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

public class Account extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public final static String Key = "4";
    final Double Bal_residue = 0.001;
    private FirebaseAuth mAuth;
    private TextView cash, upgrade;
    private String the_key;
    private User my_user;
    private Wallet the_wallet;
    private Boolean checked;
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
        database = FirebaseDatabase.getInstance("https://geld-f5989.firebaseio.com");
        FirebaseUser current_user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_account);
        cash = findViewById(R.id.cash_out);
        upgrade = findViewById(R.id.upgrade);
        ImageView icon = findViewById(R.id.icon_a);
        ProgressBar progress = findViewById(R.id.no_received_);
        TextView u_n = findViewById(R.id.username_a);
        TextView e_m = findViewById(R.id.email_a);
        TextView l_v = findViewById(R.id.level_a);
        TextView t_p = findViewById(R.id.total_payout);
        //display user details
        //get current user
        //get user through email
        assert current_user != null;
        my_user = new User();
        u_n.setText(my_user.getUsername());
        e_m.setText(my_user.getEmail());
        l_v.setText(my_user.getLevel());
        switch (my_user.getLevel()) {
            case Level_1.name:
                minimum = 0.001;
                Level_1 my1 = new Level_1();
                int value = (my1.getNo_received() / 2) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_1.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_1);
                l_v.setTextColor(Color.parseColor(Level_1.color));
                if (my1.getReached_limit()) {
                    upgrade.setVisibility(VISIBLE);
                } else {
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_2.name:
                Level_2 my2 = new Level_2();
                value = (my2.getNo_received() / 4) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_2.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_2);
                l_v.setTextColor(Color.parseColor(Level_2.color));
                if (my2.getReached_limit()) {
                    upgrade.setVisibility(VISIBLE);
                } else {
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_3.name:
                Level_3 my3 = new Level_3();
                value = (my3.getNo_received() / 8) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_3.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_3);
                l_v.setTextColor(Color.parseColor(Level_3.color));
                if (my3.getReached_limit()) {
                    upgrade.setVisibility(VISIBLE);
                } else {
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_4.name:
                Level_4 my4 = new Level_4();
                value = (my4.getNo_received() / 16) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_4.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_4);
                l_v.setTextColor(Color.parseColor(Level_4.color));
                if (my4.getReached_limit()) {
                    upgrade.setVisibility(VISIBLE);
                } else {
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_5.name:
                Level_5 my5 = new Level_5();
                value = (my5.getNo_received() / 32) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_5.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_5);
                l_v.setTextColor(Color.parseColor(Level_5.color));
                if (my5.getReached_limit()) {
                    upgrade.setVisibility(VISIBLE);
                } else {
                    upgrade.setVisibility(GONE);
                }
                break;
            case Level_6.name:
                Level_6 my6 = new Level_6();
                value = (my6.getNo_received() / 64) * 100;
                progress.setProgress(value);
                progress.getProgressDrawable().setColorFilter(Color.parseColor(Level_6.color), PorterDuff.Mode.SRC_IN);
                icon.setImageResource(R.drawable.stage_6);
                l_v.setTextColor(Color.parseColor(Level_6.color));
                if (my6.getReached_limit()) {
                    upgrade.setVisibility(VISIBLE);
                } else {
                    upgrade.setVisibility(GONE);
                }
                break;
        }


    }

    public void Upgrade(View v) {
        upgrade.setVisibility(GONE);
    }


    public void sharedPreferenceTheme(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(getString(R.string.Theme_key), getString(R.string.level_1));
        if (value.equalsIgnoreCase("1")) {
            getApplicationContext().setTheme(R.style.Light);
        } else if (value.equalsIgnoreCase("2")) {
            getApplicationContext().setTheme(R.style.Dark);
        }
    }

    public void sharedPreferenceLock(SharedPreferences sharedPreferences) {
        checked = sharedPreferences.getBoolean(getString(R.string.Lock_key), true);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceTheme(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.Theme_key))) {
            sharedPreferenceTheme(sharedPreferences);
        } else if (key.equals(getString(R.string.Lock_key))) {
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
        if (checked) {
            getApplicationContext().startActivity(new Intent(getApplicationContext(), Sign_In.class)
                    .putExtra("key", Key));
        }
    }

    public void open_settings(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void open_wallet(View v) {
        startActivity(new Intent(this, User_Wallet.class));
    }

    public void sign_out(View v) {
        mAuth.signOut();
        startActivity(new Intent(this, Sign_In.class));
    }

    public void exit_app(View v) {
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

    public void toggle_nav(View v) {
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
