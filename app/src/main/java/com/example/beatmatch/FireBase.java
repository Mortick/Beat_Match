package com.example.beatmatch;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBase {

    private DatabaseReference mReference;
    private String url;

    public FireBase() {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://beatmatch-1c911.firebaseio.com/");
    }

        public String getBPM (String key)
        {
            mReference.child("BPM").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            try {
                                url = dataSnapshot.getValue().toString();
                                Log.e("scrubs", "This is the value of" + key + " : " + url);
                                return url;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("scrubs", " it's null.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

