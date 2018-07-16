package com.hapramp.utils;

import android.graphics.Color;

/**
 * Created by Ankit on 11/13/2017.
 */

public class SkillsUtils {

  public static final int ALL = 0;
  public static final int ART = 1;
  public static final int DANCE = 2;
  public static final int TRAVEL = 3;
  public static final int LITERATURE = 4;
  public static final int DRAMATICS = 5;
  public static final int PHOTOGRAPHY = 6;
  public static final int MUSIC = 8;
  final static String[] skills = {"Art", "Dance", "Music", "Travel", "Literature", "Dramatics", "Photography"};
  // colors
  private static final String L = "#607d8b";
  private static final String T = "#8Bc34a";
  private static final String P = "#2196f3";
  private static final String D = "#607d8b";
  private static final String A = "#FFEB3B";
  private static final String M = "#eF5350";

  public static int getSkillIdFromName(String name) {

    String n = name.toLowerCase();

    switch (n) {
      case "art":
        return ART;
      case "dance":
        return DANCE;
      case "travel":
        return TRAVEL;
      case "literature":
        return LITERATURE;
      case "dramatics":
        return DRAMATICS;
      case "photography":
        return PHOTOGRAPHY;
      case "music":
        return MUSIC;
    }

    return -1;
  }

  public static String getSkillTitleFromId(int id) {

    switch (id) {
      case ALL:
        return "All";

      case ART:
        return "ART";

      case DANCE:
        return "DANCE";

      case TRAVEL:
        return "TRAVEL";

      case LITERATURE:
        return "LITERATURE";

      case DRAMATICS:
        return "DRAMATICS";

      case PHOTOGRAPHY:
        return "PHOTOGRAPHY";

      case MUSIC:
        return "MUSIC";

    }

    return "None";

  }

  public static int getSkillTagColorFromId(int skillId) {

    String c = getSkillCharacter(skillId);
    int color = 0;
    switch (c) {

      case "L":
        color = Color.parseColor(L);
        break;

      case "T":
        color = Color.parseColor(T);
        break;

      case "P":
        color = Color.parseColor(P);
        break;
      case "D":
        color = Color.parseColor(D);
        break;
      case "A":
        color = Color.parseColor(A);
        break;
      case "M":
        color = Color.parseColor(M);
        break;
      default:
        color = Color.parseColor(P);
        break;

    }
    return color;

  }

  public static String getSkillCharacter(int id) {

    switch (id) {
      case ART:
        return "A";
      case DANCE:
        return "D";
      case TRAVEL:
        return "T";
      case LITERATURE:
        return "L";
      case DRAMATICS:
        return "D";
      case PHOTOGRAPHY:
        return "P";
      case MUSIC:
        return "M";
    }
    return "O";
  }

  public static String[] getSkillsSet() {
    return skills;
  }

}
