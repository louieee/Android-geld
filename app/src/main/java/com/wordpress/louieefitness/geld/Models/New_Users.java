package com.wordpress.louieefitness.geld.Models;

public class New_Users {
    private String Username, referer;
    public static final String ref = "New_Users";
    public static final String name = "Newbie";
    private static New_User the_user;

    public New_Users(){

    }

    public New_Users(String username, String referer) {
        Username = username;
        this.referer = referer;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getReferer() {
        return referer;
    }
    public static New_Users retrieve_user(String child, String Query){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(ref);
        com.google.firebase.database.Query m_query = myRef.orderByChild(child).equalTo(Query);
        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    the_user = childSnapshot.getValue(New_Users.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                the_user = null;
            }
        });
        return the_user;
    }


    public void setReferer(String referer) {
        this.referer = referer;
    }
}
