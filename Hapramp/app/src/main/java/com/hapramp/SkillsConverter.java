package com.hapramp;

/**
 * Created by Ankit on 11/13/2017.
 */

public class SkillsConverter {

    public static final int ART = 1;
    public static final int DANCE = 2;
    public static final int TRAVEL = 3;
    public static final int LITERATURE = 4;
    public static final int ACTION = 5;
    public static final int PHOTOGRAPHY = 6;
    public static final int MUSIC = 8;

    public static String getSkillCharacter(int id){

        switch (id){
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
}
