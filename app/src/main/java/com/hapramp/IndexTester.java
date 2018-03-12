package com.hapramp;

import java.util.ArrayList;

/**
 * Created by Ankit on 3/8/2018.
 */

public class IndexTester {

    ArrayList<String> word;
    IndexTreeBuilder indexTree;

    public IndexTester(){

        word = new ArrayList<>();
        word.add("Ankit Kumar");
        word.add("Ankit Singh");

    }

    public void test(){
        IndexTreeBuilder t = new IndexTreeBuilder(word);
        t.buildTree();
    }

}
