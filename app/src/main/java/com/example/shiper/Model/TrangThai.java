package com.example.shiper.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrangThai {
    FirebaseAuth auth ;
    DatabaseReference reference;

    public void QLTrangThai(){
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }
}

