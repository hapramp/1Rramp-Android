package com.hapramp;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Ankit on 3/23/2018.
 */

public class CommunitiesUnitTest {

    ArrayList<String> communities;
    public static final String PHOTOGRAPHY = "hapramp-photography";
    public static final String ART = "hapramp-art";
    public static final String MUSIC = "hapramp-music";
    public static final String FASHION = "hapramp-fashion";
    public static final String DANCE = "hapramp-dance";
    public static final String DRAMATICS = "hapramp-dramatics";
    public static final String TRAVEL = "hapramp-travel";
    public static final String LITERATURE = "hapramp-literature";

    @Before
    public void populateCommunityList(){

        communities = new ArrayList<>();
        communities.add(PHOTOGRAPHY);
        communities.add(ART);
        communities.add(MUSIC);
        communities.add(FASHION);
        communities.add(DANCE);
        communities.add(DRAMATICS);
        communities.add(TRAVEL);
        communities.add(LITERATURE);

    }

    @Test
    public void testCommunitiesList(){

        //true test
        assert (communities.contains("hapramp-art"));
        assert (communities.contains("hapramp-dance"));
        //false test
        assert (!communities.contains("hapramp"));
        assert (!communities.contains("hapramp-test"));
    }

}
