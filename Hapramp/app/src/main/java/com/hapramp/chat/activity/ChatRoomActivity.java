package com.hapramp.chat.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hapramp.R;
import com.hapramp.chat.models.ChatRoom;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ChatRoom>> {
    private int LOADER_ID = 191;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this).forceLoad();
    }

    @Override
    public Loader<ArrayList<ChatRoom>> onCreateLoader(int id, Bundle args) {
        return new android.content.AsyncTaskLoader<ArrayList<ChatRoom>>(this) {
            @Override
            public ArrayList<ChatRoom> loadInBackground() {
                // TODO: 9/6/2017 read from data base
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ChatRoom>> loader, ArrayList<ChatRoom> chatRooms) {

    }


    @Override
    public void onLoaderReset(Loader<ArrayList<ChatRoom>> loader) {

    }

}
