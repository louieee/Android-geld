package com.wordpress.louieefitness.geld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;

import java.util.HashMap;

import static com.wordpress.louieefitness.geld.Utilities.Builder.StringBuild;

public class Sign_Up extends AppCompatActivity{
    public static String API_KEY = "";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private HashMap<String, String> params;
    private User a_user;
    private TextInputEditText username, fn,ln,email,pass1,pass2,wallet,question,answer,referer;
    private String user_email,user_fn,user_ln,first_password, second_password;
    private String Bitcoin_wallet, rec_que, rec_ans, refer_username, user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        username = findViewById(R.id.username);
        fn = findViewById(R.id.first_name);
        ln = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        pass1 = findViewById(R.id.password);
        pass2 = findViewById(R.id.confirm_password);
        wallet = findViewById(R.id.bitcoin_wallet);
        question = findViewById(R.id.Question);
        answer = findViewById(R.id.answer);
        referer = findViewById(R.id.referer);
        setContentView(R.layout.activity_sign_up);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

        if (!(currentUser != null && currentUser.equals(null))) {
            Database user_db = new Database(Sign_Up.this, User.ref);
            assert currentUser != null;
            String user_key = user_db.retrieve_object_key("email",
                    currentUser.getEmail());
            if (user_key.length() > 0) {
                DatabaseReference UserRef;
                UserRef = database.getReference(User.ref);
                UserRef.child(user_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        a_user = dataSnapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            if (Integer.parseInt(a_user.getLevel()) == 0) {
                Intent payment = new Intent(Sign_Up.this, Account.class);
                startActivity(payment);
            } else {
                String userEmail = currentUser.getEmail();
                Intent account = new Intent(this, Account.class);
                account.putExtra("email", userEmail);
                startActivity(account);
            }


        } else {
            user_email = email.getText().toString();
            first_password = pass1.getText().toString();
            second_password = pass2.getText().toString();
            user_fn = fn.getText().toString();
            user_ln = ln.getText().toString();
            user_name = username.getText().toString();
            refer_username = referer.getText().toString();
            Bitcoin_wallet = wallet.getText().toString();
            rec_que = question.getText().toString();
            rec_ans = question.getText().toString();
        }
    }

    public void Sign_up(View v){
        if ((user_email.length() > 0) && (user_fn.length() > 0) && (user_ln.length() > 0) && (first_password.length() > 0) &&
                (second_password.length() > 0) && (Bitcoin_wallet.length() > 0) &&
                (rec_que.length() > 0) && (rec_ans.length() > 0) && (refer_username.length() > 0) &&
                user_name.length() > 0){
            if (first_password.equals(second_password)){
                mAuth.createUserWithEmailAndPassword(user_email, first_password)

                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Message", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User new_user = new User();
                                    new_user.setAnswer(rec_ans);
                                    assert user != null;
                                    new_user.setEmail(user.getEmail());
                                    new_user.setQuestion(rec_que);
                                    new_user.setFirst_name(user_fn);
                                    new_user.setWallet_address(Bitcoin_wallet);
                                    new_user.setLast_name(user_ln);
                                    new_user.setUsername(user_name);
                                    Database user_db = new Database(Sign_Up.this, User.ref);
                                    String referer_key = user_db.retrieve_object_key("username",
                                            refer_username);
                                    if (referer_key.length() > 0){
                                        DatabaseReference UserRef;
                                            UserRef = database.getReference(User.ref);
                                        UserRef.child(referer_key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                a_user = dataSnapshot.getValue(User.class);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e("Message: ", "User not Found");
                                            }
                                        });
                                            String referer_level = a_user.getLevel();
                                            if (Integer.parseInt(referer_level) == 1) {
                                                New_Users newUser = new New_Users(new_user.getUsername(), a_user.getUsername());
                                                String db_id = UserRef.push().getKey();
                                                UserRef.child(db_id).setValue(new_user);
                                                DatabaseReference New_userRef = database.getReference(New_Users.ref);
                                                String d_id = New_userRef.push().getKey();
                                                New_userRef.child(d_id).setValue(newUser);
                                                params = new HashMap<>();
                                                params.put("password",first_password);
                                                params.put("api_key", API_KEY);
                                                StringBuilder sb = StringBuild(params);

                                                Toast.makeText(Sign_Up.this, "User Account Created Successfully", Toast.LENGTH_LONG).show();
                                                Intent payment = new Intent(Sign_Up.this, Payment.class);
                                                startActivity(payment);
                                            }else {
                                                Database level_1 = new Database(Sign_Up.this, Level_1.ref);
                                                String refer_key = level_1.get_oldest_key();
                                                DatabaseReference URef;
                                                URef = database.getReference(User.ref);
                                                URef.child(refer_key).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        a_user = dataSnapshot.getValue(User.class);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Log.e("Message: ", "User not Found");
                                                    }
                                                });
                                                New_Users newUser = new New_Users(new_user.getUsername(), a_user.getUsername());
                                                String db_id = UserRef.push().getKey();
                                                UserRef.child(db_id).setValue(new_user);
                                                DatabaseReference New_userRef = database.getReference(New_Users.ref);
                                                String d_id = New_userRef.push().getKey();
                                                New_userRef.child(d_id).setValue(newUser);
                                                Toast.makeText(Sign_Up.this, "User Account Created Successfully", Toast.LENGTH_LONG).show();
                                                Intent payment = new Intent(Sign_Up.this, Payment.class);
                                                startActivity(payment);
                                            }
                                    }else {
                                        Database level_1 = new Database(Sign_Up.this, Level_1.ref);
                                        String refer_key = level_1.get_oldest_key();
                                        DatabaseReference URef;
                                        URef = database.getReference(User.ref);
                                        URef.child(refer_key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                a_user = dataSnapshot.getValue(User.class);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e("Message: ", "User not Found");
                                            }
                                        });
                                        DatabaseReference UserRef = database.getReference(User.ref);
                                        New_Users newUser = new New_Users(new_user.getUsername(), a_user.getUsername());
                                        String db_id = UserRef.push().getKey();
                                        UserRef.child(db_id).setValue(new_user);
                                        DatabaseReference New_userRef = database.getReference(New_Users.ref);
                                        String d_id = New_userRef.push().getKey();
                                        New_userRef.child(d_id).setValue(newUser);
                                        Toast.makeText(Sign_Up.this, "User Account Created Successfully", Toast.LENGTH_LONG).show();
                                        Intent payment = new Intent(Sign_Up.this, Payment.class);
                                        startActivity(payment);
                                            }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Message: ", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Sign_Up.this, "Sign Up failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(Sign_Up.this, "The Two Passwords Must Match", Toast.LENGTH_LONG).show();
            }
            }else{
            Toast.makeText(Sign_Up.this, "All Fields Must Be Filled", Toast.LENGTH_LONG).show();

        }
        }

    }


