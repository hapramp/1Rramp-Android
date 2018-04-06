package com.hapramp.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 3/9/2018.
 */

public class SearchManager {

    private IndexTreeBuilder indexTreeBuilder;
    HashMap<String, IndexNode> tree;
    private SearchListener searchListener;

    public SearchManager(SearchListener searchListener) {

        this.searchListener = searchListener;

    }

    public void requestSuggestionsFor(final String segment) {

        //callback
        onSearching();
        onPrepared();

        final ArrayList<String> empty_suggestion = new ArrayList<>();
        //loop through characters
        int len = segment.length();
        if (len == 0) {
            onSearched(empty_suggestion);
            return;
        }

        //perform steem search and result
        new Thread() {
            @Override
            public void run() {
                try {

                    onSearching();
                    SteemJ steemJ = new SteemJ();
                    final List<String> users = steemJ.lookupAccounts(segment, 10);
                    onSearched((ArrayList<String>) users);

                } catch (SteemCommunicationException e) {
                    onSearched(empty_suggestion);
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    onSearched(empty_suggestion);
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private void onPreparing() {
        if (searchListener != null) {
            searchListener.onPreparing();
        }
    }

    private void onPrepared() {
        if (searchListener != null) {
            searchListener.onPrepared();
        }
    }

    private void onSearching() {
        if (searchListener != null) {
            searchListener.onSearching();
        }
    }

    private void onSearched(final ArrayList<String> suggs) {
        if (searchListener != null) {
            searchListener.onSearched(suggs);
        }
    }

    public interface SearchListener {

        void onPreparing();

        void onPrepared();

        void onSearching();

        void onSearched(ArrayList<String> suggestions);

    }

}
