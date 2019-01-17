package com.wordpress.louieefitness.geld;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {
    private String ref;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(ref);
    private Context con;
    private Object obj;
    private ArrayList<Object> db_list;
    private String oldest_key;
    private String no_received;

    public Database(Context con, String ref) {
        this.ref = ref;
        this.con = con;
    }

    public void Add_New(Object obj, String db_Name) {
        String db_id = myRef.push().getKey();
        myRef.child(db_id).setValue(obj);
        Toast.makeText(con, "Added new " + db_Name + " successfully", Toast.LENGTH_LONG).show();
    }

    public Object Retrieve_by_Id(String db_id) {
        myRef.child(db_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                obj = dataSnapshot.getValue(Object.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(con, "Item was not found in database", Toast.LENGTH_LONG).show();
            }
        });

        return obj;
    }

    public ArrayList<Object> Retrieve_All(final String db_Name) {
        db_list.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot db_snapshot : dataSnapshot.getChildren()) {
                    Object db_obj = db_snapshot.getValue(Object.class);
                    db_list.add(db_obj);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(con, "Could not retrieve all " + db_Name + "(s)", Toast.LENGTH_LONG).show();
            }
        });

        return db_list;
    }

    public void Delete(String db_id, String db_Name) {
        myRef.child(db_id).removeValue();
        Toast.makeText(con, "Deleted " + db_Name + " Successfully", Toast.LENGTH_LONG).show();
    }

    public void Update(Object obj, String db_id, String db_Name) {
        myRef.child(db_id).setValue(obj);
        Toast.makeText(con, "Updated " + db_Name + " details Successfully", Toast.LENGTH_LONG).show();
    }
    public String get_oldest_key(){
        Query oldest = myRef.orderByKey().limitToFirst(1);
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




    }

