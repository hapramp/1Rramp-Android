package com.hapramp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/2/2017.
 */

public class LikeBody {

    @SerializedName("vote")
    public int vote;

    public LikeBody(int vote) {
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "LikeBody{" +
                "vote=" + vote +
                '}';
    }
}
