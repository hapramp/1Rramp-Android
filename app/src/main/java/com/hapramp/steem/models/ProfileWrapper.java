package com.hapramp.steem.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/3/2018.
 */

public class ProfileWrapper {
  @Expose
  @SerializedName("profile")
  public Profile profile;

  public ProfileWrapper(Profile profile) {
    this.profile = profile;
  }

  public Profile getProfile() {
    return profile;
  }
}
