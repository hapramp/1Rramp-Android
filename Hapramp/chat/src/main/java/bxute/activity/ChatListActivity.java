package bxute.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.adapters.ChatListRecyclerAdapter;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.database.DatabaseHelper;
import bxute.models.ChatRoom;


public class ChatListActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<ArrayList<ChatRoom>> {

    private int LOADER_ID = 191;
    @BindView(R2.id.chat_list)
    RecyclerView chatList;
    DatabaseHelper databaseHelper;
    ChatListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        ButterKnife.bind(this);
        setAdapter();
        getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this).forceLoad();
    }

    private void setAdapter(){
        adapter = new ChatListRecyclerAdapter(this);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        chatList.setAdapter(adapter);
    }

    @Override
    public Loader<ArrayList<ChatRoom>> onCreateLoader(int id, Bundle args) {
        return new android.content.AsyncTaskLoader<ArrayList<ChatRoom>>(this) {
            @Override
            public ArrayList<ChatRoom> loadInBackground() {
                return databaseHelper.getChatRooms();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ChatRoom>> loader, ArrayList<ChatRoom> chatRooms) {
        adapter.setChatRooms(chatRooms);
    }


    @Override
    public void onLoaderReset(Loader<ArrayList<ChatRoom>> loader) {

    }

}
