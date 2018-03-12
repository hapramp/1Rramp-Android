package com.hapramp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ankit on 3/8/2018.
 */

public class IndexTreeBuilder {

    @SerializedName("tree")
    HashMap<String, IndexNode> tree;
    private ArrayList<String> words;
    String currentWord = null;
    String currentCharacter = null;
    IndexNode currentNode = null;

    public IndexTreeBuilder(ArrayList<String> words) {
        this.tree = new HashMap<>();
        this.words = words;
    }

    public HashMap<String, IndexNode> buildTree() {

        int wordLen;
        // i is index of word in list of words
        for (int wordIndex = 0; wordIndex < words.size(); wordIndex++) {

            currentWord = words.get(wordIndex);
            //p("Current Word: " + currentWord);
            wordLen = currentWord.length();

            //p("====================================================");
            //loop for characters
            for (int characterIndex = 0; characterIndex < wordLen; characterIndex++) {
               // p("===============================");
                // get current character
                currentCharacter = String.valueOf(currentWord.charAt(characterIndex));
               // p("Current Character : " + currentCharacter);

                // create or find the node
                if (characterIndex == 0) {

                    if (exists(currentCharacter)) {
                       // p(currentCharacter + " - exists [Getting Old Node]");
                        //TODO: 3/8/2018  get node from tree, add word and make it current Node
                        IndexNode oldNode = tree.get(currentCharacter);
                        oldNode.addWord(currentWord);
                        currentNode = oldNode;

                    } else {
                       // p("Creating New Root Node for: " + currentCharacter);
                        // create new node
                        currentNode = new IndexNode(currentCharacter);
                        // add word
                        currentNode.addWord(currentWord);
                        // add this to tree
                        tree.put(currentCharacter, currentNode);
                    }

                } else {
                    // check for the currentCharacter in currentNode
                    if (hasChild(currentCharacter)) {

                       // p(currentCharacter + " - exists [Getting Old Node]");
                        // TODO: 3/8/2018 it has already node, just add the word and make it current Node
                        // get old node
                        IndexNode oldNode = currentNode.getNode(currentCharacter);
                        //add word to this
                        oldNode.addWord(currentWord);
                        //make this node current node
                        currentNode = oldNode;

                    } else {
                       // p("Creating New Inter. Node for: " + currentCharacter);
                        // create a new node
                        IndexNode tempNode = new IndexNode(currentCharacter);
                        //add word
                        tempNode.addWord(currentWord);
                        //add node to currentNode
                        currentNode.addNode(tempNode);
                        //make this current Node
                        currentNode = tempNode;
                        //release node
                        tempNode = null;

                    }

                }

            }

        }


        return tree;

    }

    private void printTree() {

        p("============================");
        p("Final Tree");
        p("============================");
        String json = new Gson().toJson(tree);
        p(json);

    }

    private boolean hasChild(String currentCharacter) {
        return currentNode.hasNodeWith(currentCharacter);
    }

    private boolean exists(String currentCharacter) {
        return tree.get(currentCharacter) != null;
    }

    public HashMap<String, IndexNode> getTree() {
        return tree;
    }

    private void p(String s) {
        Log.d("TAG",s);
    }



}
