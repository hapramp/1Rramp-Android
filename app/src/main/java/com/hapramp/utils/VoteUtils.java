package com.hapramp.utils;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Voter;

import java.util.List;

public class VoteUtils {

  public static long getNonZeroVoters(List<Voter> votes) {
    long sum = 0;
    if (votes != null)
      for (int i = 0; i < votes.size(); i++) {
        if (votes.get(i).getPercent() > 0) {
          sum++;
        }
      }
    return sum;
  }

  public static long getVotePercentSum(List<Voter> activeVotes) {
    long sum = 0;
    if (activeVotes != null)
      for (int i = 0; i < activeVotes.size(); i++) {
        int vp = activeVotes.get(i).getPercent();
        if (vp > 0) {
          sum += activeVotes.get(i).getPercent();
        }
      }

    return sum;
  }

  public static long getMyVotePercent(List<Voter> votes) {
    if (votes != null)
      for (int i = 0; i < votes.size(); i++) {
        if (votes.get(i).getVoter().equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
          return votes.get(i).getPercent();
        }
      }
    return 0;
  }

  public static boolean checkForMyVote(List<Voter> votes) {
    if (votes != null)
      for (int i = 0; i < votes.size(); i++) {
        if (votes.get(i).getVoter().equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())
          &&
          votes.get(i).getPercent() > 0) {
          return true;
        }
      }
    return false;
  }
}
