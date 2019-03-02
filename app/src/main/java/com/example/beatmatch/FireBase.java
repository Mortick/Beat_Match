package com.example.beatmatch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBase {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    public FireBase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("beatmatch-1c911");

    }
}
