package com.hapramp.steem;

import android.provider.Telephony;

import java.util.List;

public class Communities {

    public static final String PHOTOGRAPHY = "hapramp-photography";
    public static final String ART = "hapramp-art";
    public static final String MUSIC = "hapramp-music";
    public static final String FASHION = "hapramp-fashion";
    public static final String DANCE = "hapramp-dance";
    public static final String DRAMATICS = "hapramp-dramatics";
    public static final String TRAVEL = "hapramp-travel";
    public static final String LITERATURE = "hapramp-literature";
    public static final String DESIGN = "hapramp-design";
    public static final String ALL = "all";


    // TODO: 4/1/2018 check community existence from stored list in preference.
    // It enhances the flexibility to change data dynamically from server

    public static boolean doesCommunityExists(String commmunity) {

        switch (commmunity) {
            case PHOTOGRAPHY:
                return true;
            case ART:
                return true;
            case MUSIC:
                return true;
            case FASHION:
                return true;
            case DANCE:
                return true;
            case DRAMATICS:
                return true;
            case TRAVEL:
                return true;
            case LITERATURE:
                return true;
            case DESIGN:
                return true;
            default:
                return false;
        }

    }

}
