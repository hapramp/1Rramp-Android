package com.hapramp.search;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ankit on 3/8/2018.
 */

public class IndexNode {

    @SerializedName("character")
    String character;
    @SerializedName("nextNodes")
    HashMap<String, IndexNode> nextIndexs;
    @SerializedName("listOfWords")
    ArrayList<String> listOfWords;

    public IndexNode(String character) {
        this.character = character;
        listOfWords = new ArrayList<>();
        nextIndexs = new HashMap<>();
    }

    public void addNode(IndexNode indexNode) {
        nextIndexs.put(indexNode.getCharacter(), indexNode);
    }

    public void addWord(String word) {
        listOfWords.add(word);
    }

    public String getCharacter() {
        return character;
    }

    public ArrayList<String> getListOfWords() {
        return listOfWords;
    }

    public boolean hasNodeWith(String currentCharacter) {
        return nextIndexs.get(currentCharacter) != null;
    }

    public IndexNode getNode(String currentCharacter) {
        return nextIndexs.get(currentCharacter);
    }

//    @Override
//    public String toString() {
//        return "IndexNode{" +
//                "character='" + character + '\'' +
//                ", nextIndexs=" + getJson(nextIndexs) +
//                ", listOfWords=" + listOfWords +
//                '}';
//    }

//    private String getJson(HashMap<String, IndexNode> node){
//
//        JSONObject jsonObject= new JSONObject(node);
//        return jsonObject.toString();
//
//    }

}
