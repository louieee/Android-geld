package com.wordpress.louieefitness.geld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.New_Users;
import com.wordpress.louieefitness.geld.Models.User;

import java.util.Objects;

public class Sign_In extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText email_t, password_t;
    private String user_email, user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.actitvity_sign_in);
        email_t = findViewById(R.id.email_t);
        password_t = findViewById(R.id.password_t);
        user_email = email_t.getText().toString();
        user_password = password_t.getText().toString();

    }

    public void SignIn(View v) {
        if (!(user_email.isEmpty() || user_password.isEmpty())) {
            final User user = User.retrieve_user("email", user_email);
            if (user == null) {
                Toast.makeText(Sign_In.this, "You Don't Have an account", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Sign_In.this, Sign_Up.class));
            } else {
                mAuth.signInWithEmailAndPassword(user_email, user_password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Sign_In.this, "Sign Up Successfull", Toast.LENGTH_LONG).show();
                                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                                        if (user.getLevel().equals(New_Users.name)) {
                                            Toast.makeText(Sign_In.this, "Welcome " + user.getFirst_name(), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Sign_In.this, Payment.class));
                                        } else {
                                            Toast.makeText(Sign_In.this, "Welcome " + user.getFirst_name(), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Sign_In.this, Account.class));
                                        }
                                    } else {
                                        email_t.setText("");
                                        password_t.setText("");
                                        mAuth.getCurrentUser().sendEmailVerification();
                                        Toast.makeText(Sign_In.this, getString(R.string.verification_message), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    email_t.setText("");
                                    password_t.setText("");
                                    Toast.makeText(Sign_In.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

        } else {
            Toast.makeText(Sign_In.this, "All Fields must be Filled", Toast.LENGTH_LONG).show();
        }
    }
    public void SignUp(View v){
        startActivity(new Intent(Sign_In.this, Sign_Up.class));
    }
    public void ForgotPassword(View v){
        startActivity(new Intent(Sign_In.this, Forgot_Password.class));
    }
}
