package com.hapramp.datastore;

public class CompetitionCreationEligibilityChecker {
  public static void checkEligibilityForCompetitionCreation() {
    new DataStore().performCompetitionEligibilitySync();
  }
}
