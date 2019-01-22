package com.wordpress.louieefitness.geld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.louieefitness.geld.Models.User;
import com.wordpress.louieefitness.geld.Models.Wallet;
import com.wordpress.louieefitness.geld.Utilities.Downloader;

public class Payment extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private String the_key;
    private String geld_wallet_address ="";
    private Wallet my_wallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_payment);
        current_user = mAuth.getCurrentUser();
        assert current_user != null;
        Wallet wallet = Retrieve_by_Id(retrieve_object_key(Wallet.Ref,"email",current_user.getEmail()));
        String wallet_details = "Wallet Address: "+wallet.getAddress()+"/n"+
                "Wallet Email: "+wallet.getEmail()+"/n"+
                "Wallet Main Address: "+wallet.getMain_Address()+"/n"+
                "Wallet Password: "+wallet.getPassword()+"/n";
    }

    public String retrieve_object_key(String ref,String child, String Query){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(ref);
        Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    the_key = childSnapshot.getKey();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Payment.this, " Database error occured when retrieving data",
                        Toast.LENGTH_LONG).show();
            }
        });
        return the_key;
    }
    public Wallet Retrieve_by_Id(String db_id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(Wallet.Ref);
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                my_wallet = dataSnapshot.getValue(Wallet.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Payment.this, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return my_wallet;
    }
    public void get_wallet_balance(Wallet wallet){
        wallet.setBalance(0);
    }
    public void make_investment(View v){
        Wallet be = new Wallet();
        String key = retrieve_object_key(User.ref,"email",current_user.getEmail());
        User u = new User();
        Downloader invest = new Downloader(Payment.this, "https://blockchain.info/merchant/"+
                my_wallet.getGuid()+"/payment?password="+
                my_wallet.getPassword()+"&to="+geld_wallet_address+
                "&amount="+"0.000000000025",u,be,"send bitcoin");
        invest.execute();

    }


}
