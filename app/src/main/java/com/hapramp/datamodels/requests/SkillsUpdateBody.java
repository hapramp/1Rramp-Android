package com.hapramp.datamodels.requests;

import java.util.Arrays;

/**
 * Created by Ankit on 10/23/2017.
 */

public class SkillsUpdateBody {
  Integer[] skills;

  public SkillsUpdateBody(Integer[] skills) {
    this.skills = skills;
  }

  public Integer[] getSkills() {
    return skills;
  }

  public void setSkills(Integer[] skills) {
    this.skills = skills;
  }

  @Override
  public String toString() {
    return "SkillsUpdateBody{" +
      "skills=" + Arrays.toString(skills) +
      '}';
  }
}
