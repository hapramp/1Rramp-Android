package com.hapramp.datastore;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hapramp.preferences.HaprampPreferenceManager;

public class CompetitionCreationEligibilityChecker {
  public static void checkEligibilityForCompetitionCreation() {
    String username = "bxute";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    firebaseDatabase
      .getReference()
      .child("competitions")
      .child("admins")
      .child(username)
      .child("active")
      .addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.exists()) {
            Object value = dataSnapshot.getValue();
            if (value instanceof Boolean) {
              HaprampPreferenceManager.getInstance().setCompetitionCreateEligibility((Boolean) value);
            } else {
              HaprampPreferenceManager.getInstance().setCompetitionCreateEligibility(false);
            }
          } else {
            HaprampPreferenceManager.getInstance().setCompetitionCreateEligibility(false);
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
          HaprampPreferenceManager.getInstance().setCompetitionCreateEligibility(false);
        }
      });
  }
}
