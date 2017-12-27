package com.hapramp.utils;

/**
 * Created by Ankit on 11/13/2017.
 */

public class SkillsUtils {

    public static final int ART = 1;
    public static final int DANCE = 2;
    public static final int TRAVEL = 3;
    public static final int LITERATURE = 4;
    public static final int ACTION = 5;
    public static final int PHOTOGRAPHY = 6;
    public static final int MUSIC = 8;

    final static String[] skills = {"Art", "Dance", "Music","Travel", "Literature", "Action", "Photography"};

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
            case ACTION:
                return "A";
            case PHOTOGRAPHY:
                return "P";
            case MUSIC:
                return "M";
        }
        return "O";
    }

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
            case "action":
                return ACTION;
            case "photography":
                return PHOTOGRAPHY;
            case "music":
                return MUSIC;
        }

        return -1;
    }

    public static String getSkillTitleFromId(int id) {
        switch (id) {
            case ART:
                return "Art";

            case DANCE:
                return "Dance";

            case TRAVEL:
                return "Travel";

            case LITERATURE:
                return "Literature";

            case ACTION:
                return "Action";

            case PHOTOGRAPHY:
                return "Photography";

            case MUSIC:
                return "Music";

        }

        return "None";

    }

    public static String[] getSkillsSet(){
        return skills;
    }

}