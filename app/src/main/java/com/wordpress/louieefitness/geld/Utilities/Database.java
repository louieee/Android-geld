package com.wordpress.louieefitness.geld.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    public static void Delete(String Ref, String db_id) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Ref);
        myRef.child(db_id).removeValue();
    }





    }

