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

  /**
   * Votes having more than 20% vote is considered as rate
   */
  public static int getCountOfVotesConsideredAsRate(List<Voter> votes) {
    int sum = 0;
    if (votes != null)
      for (int i = 0; i < votes.size(); i++) {
        if (votes.get(i).getPercent() >= 2000) {
          sum++;
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

  /**
   TRANSFORMATION LOGIC (for rating display only):
   (0,2000) -> 1 Star
   [2000,4000) -> 2 Star
   [4000,6000) -> 3 Star
   [6000,8000) -> 4 Star
   [8000, 10000) -> 5 Star
   */
  public static int transformToRate(float myVotePercent) {
    return (int) Math.ceil(myVotePercent / 2000);
  }

  public static int getSumOfRatings(List<Voter> votes) {
    int sum = 0;
    if (votes != null)
      for (int i = 0; i < votes.size(); i++) {
        int vp = votes.get(i).getPercent();
        if (vp >= 2000) {
          sum += transformToRate(vp);
        }
      }
    return sum;
  }
}
