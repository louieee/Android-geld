package com.wordpress.louieefitness.geld.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.R;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Forgot_Password extends AppCompatActivity{
    private Context c;
    private TextView password,question,pass_info;
    private TextInputEditText email,pass_answer;
    private LinearLayout pass_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        c = Forgot_Password.this;
        question = findViewById(R.id.pass_question);
        password = findViewById(R.id.pass_retrieve);
        pass_info = findViewById(R.id.pass_info);
        email = findViewById(R.id.email_re);
        pass_answer = findViewById(R.id.pass_answer);
        pass_ = findViewById(R.id.pass_info_main);
        pass_info.setVisibility(GONE);
        pass_.setVisibility(GONE);
        password.setVisibility(GONE);
        question.setVisibility(GONE);
        pass_answer.setVisibility(GONE);
        setContentView(R.layout.activity_forgot_password);

    }
    public void Send(View v){
        if (email.getText().toString().isEmpty() ){
            Toast.makeText(c,"No Email Address Detected",Toast.LENGTH_LONG).show();
        }else{
            final User the_user = User.retrieve_user("email",email.getText().toString());
            if (the_user == null){
                Toast.makeText(c,"No User with this Email Address",Toast.LENGTH_LONG).show();
                email.setText("");
            }else{
                email.setVisibility(GONE);
                question.setText(the_user.getQuestion());
                question.setVisibility(VISIBLE);
                pass_answer.setText("");
                pass_answer.setVisibility(VISIBLE);
                 v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pass_answer.getText().toString().isEmpty()){
                            Toast.makeText(c,"This Field must be Filled",Toast.LENGTH_LONG).show();
                        }else{
                            if (pass_answer.getText().toString().equalsIgnoreCase(the_user.getAnswer())){
                                pass_.setVisibility(VISIBLE);
                                pass_info.setVisibility(VISIBLE);
                                password.setText(the_user.getPassword());
                                password.setVisibility(VISIBLE);
                            }else{
                                Toast.makeText(c,"Your Input is Incorrect",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        }

    }


}
