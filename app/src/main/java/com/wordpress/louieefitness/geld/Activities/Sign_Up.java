package com.wordpress.louieefitness.geld.Activities;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Utilities.Database;
import com.wordpress.louieefitness.geld.Models.Level_1;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.R;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

import java.util.Objects;

import static com.wordpress.louieefitness.geld.Models.User.retrieve_user;

public class Sign_Up extends AppCompatActivity{
    public static String API_KEY = "";
    public static String GUID = "";
    public static String ADDRESS = "";
    public static String PASSWORD = "";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private User new_user;
    private DatabaseReference UserRef;
    private String user_email,user_fn,user_ln,first_password, second_password;
    private String rec_que, rec_ans, refer_username, user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        UserRef = database.getReference(User.ref);
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        TextInputEditText username = findViewById(R.id.username);
        TextInputEditText fn = findViewById(R.id.first_name);
        TextInputEditText ln = findViewById(R.id.last_name);
        TextInputEditText email = findViewById(R.id.email);
        TextInputEditText pass1 = findViewById(R.id.password);
        TextInputEditText pass2 = findViewById(R.id.confirm_password);
        TextInputEditText question = findViewById(R.id.Question);
        TextInputEditText answer = findViewById(R.id.answer);
        TextInputEditText referer_v = findViewById(R.id.referer);
        setContentView(R.layout.activity_sign_up);
        user_email = email.getText().toString();user_ln = ln.getText().toString();
        user_fn = fn.getText().toString();first_password = pass1.getText().toString();
        second_password = pass2.getText().toString();rec_que = question.getText().toString();
        rec_ans = answer.getText().toString();refer_username = referer_v.getText().toString();
        user_name = username.getText().toString();
    }

    public void Sign_up(View v){
        if ((user_email.length() > 0) && (user_fn.length() > 0) && (user_ln.length() > 0) && (first_password.length() > 0) &&
                (second_password.length() > 0)  &&
                (rec_que.length() > 0) && (rec_ans.length() > 0) && (refer_username.length() > 0) &&
                user_name.length() > 0){
            User get_user = User.retrieve_user("username",user_name);
            if (get_user == null) {
                if (first_password.equals(second_password)) {
                    mAuth.createUserWithEmailAndPassword(user_email, first_password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                                        Log.d("Message", "createUserWithEmail:success");
                                        new_user = new User();
                                        new_user.setAnswer(rec_ans);
                                        new_user.setEmail(user_email);
                                        new_user.setQuestion(rec_que);
                                        new_user.setFirst_name(user_fn);
                                        new_user.setLast_name(user_ln);
                                        new_user.setPassword(first_password);
                                        new_user.setUsername(user_name);
                                        User referer = retrieve_user("username",
                                                refer_username);
                                        if (referer == null) {
                                            complete_sign_up();
                                        } else {
                                            if (referer.getLevel().equals(Level_1.name)) {
                                                New_Users newUser = new New_Users(new_user.getUsername(), referer.getUsername());
                                                String db_id = UserRef.push().getKey();
                                                new_user.setReferer(referer.getUsername());
                                                UserRef.child(db_id).setValue(new_user);
                                                DatabaseReference New_userRef = database.getReference(New_Users.ref);
                                                String d_id = New_userRef.push().getKey();
                                                New_userRef.child(d_id).setValue(newUser);
                                                //create wallet start
                                                create_wallet();
                                                //create wallet stop
                                                Toast.makeText(Sign_Up.this, "User Account Created Successfully", Toast.LENGTH_LONG).show();
                                                Intent payment = new Intent(Sign_Up.this, Sign_In.class);
                                                startActivity(payment);
                                            } else {
                                                complete_sign_up();
                                            }
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Message: ", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Sign_Up.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Sign_Up.this, "The Two Passwords Must Match", Toast.LENGTH_LONG).show();

                }
            }else{
                Toast.makeText(Sign_Up.this, "Username is already taken", Toast.LENGTH_LONG).show();
            }
            }else{
            Toast.makeText(Sign_Up.this, "All Fields Must Be Filled", Toast.LENGTH_LONG).show();

        }
        }
        public void complete_sign_up(){
            Database level_1 = new Database(Sign_Up.this, Level_1.ref);
            String refer_key = level_1.get_oldest_key();
            String old_referer = Level_1.Retrieve_1_by_Id(refer_key).getUsername();
            New_Users newUser = new New_Users(new_user.getUsername(), old_referer);
            String db_id = UserRef.push().getKey();
            UserRef.child(db_id).setValue(new_user);
            DatabaseReference New_userRef = database.getReference(New_Users.ref);
            String d_id = New_userRef.push().getKey();
            New_userRef.child(d_id).setValue(newUser);
            create_wallet();
            Toast.makeText(Sign_Up.this, "User Account Created Successfully", Toast.LENGTH_LONG).show();
            Intent payment = new Intent(Sign_Up.this, Sign_In.class);
            startActivity(payment);
        }
        public void create_wallet(){
            Wallet be = new Wallet();
            Downloader b = new Downloader(Sign_Up.this,"https://blockchain.info/api/v2/create?" +
                    "password="+first_password+
                    "&api_code="+API_KEY,
                    new_user, be,"create wallet");
            b.execute();
        }

    }