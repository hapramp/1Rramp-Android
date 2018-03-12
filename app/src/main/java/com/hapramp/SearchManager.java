package com.hapramp;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ankit on 3/9/2018.
 */

public class SearchManager {

    private IndexTreeBuilder indexTreeBuilder;
    HashMap<String, IndexNode> tree;
    private SearchListener searchListener;
    private Handler mHandler;

    public SearchManager(final ArrayList<String> dataList, SearchListener searchListener) {

        mHandler = new Handler();

        this.searchListener = searchListener;

        new Thread() {
            @Override
            public void run() {

                //callback
                onPreparing();

                if (dataList != null) {
                    indexTreeBuilder = new IndexTreeBuilder(dataList);
                }
                tree = indexTreeBuilder.buildTree();

                //callback
                onPrepared();

            }
        }.start();

    }

    public void requestSuggestionsFor(String segment) {

        //callback
        onSearching();

        ArrayList<String> suggs = new ArrayList<>();
        //loop through characters
        int len = segment.length();

        //first character
        String s = segment.substring(0, 1);

        //get the first node from tree
        IndexNode currNode = tree.get(s);
        // check for existence
        if (currNode == null) {
            //return empty arrayList
            onSearched(suggs);
            return;
        }

        for (int i = 1; i < len; i++) {
            currNode = currNode.getNode(String.valueOf(segment.charAt(i)));
        }

        onSearched(currNode.getListOfWords());

    }

    private void onPreparing() {
        if (searchListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    searchListener.onPreparing();
                }
            });
        }
    }

    private void onPrepared() {
        if (searchListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    searchListener.onPrepared();
                }
            });
        }
    }

    private void onSearching() {
        if (searchListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    searchListener.onSearching();
                }
            });
        }
    }

    private void onSearched(final ArrayList<String> suggs) {
        if (searchListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    searchListener.onSearched(suggs);
                }
            });
        }
    }

    public static SearchBuilder Builder() {
        return new SearchBuilder();
    }

    public static class SearchBuilder {

        ArrayList<String> words;
        SearchListener searchListener;
        SearchManager searchManager;

        public SearchBuilder() {

        }

        public SearchBuilder feedWords(ArrayList<String> words) {
            this.words = words;
            return this;
        }

        public SearchBuilder setSearchCallback(SearchListener searchCallback) {
            this.searchListener = searchCallback;
            return this;
        }

        public SearchManager build() {
            searchManager = new SearchManager(words, searchListener);
            return searchManager;
        }

    }

    public interface SearchListener {

        void onPreparing();

        void onPrepared();

        void onSearching();

        void onSearched(ArrayList<String> suggestions);

    }

}
