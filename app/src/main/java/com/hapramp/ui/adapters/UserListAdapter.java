package com.hapramp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hapramp.views.UserItemView;

import java.util.ArrayList;

/**
 * Created by Ankit on 4/5/2018.
 */

public class UserListAdapter extends ArrayAdapter<String> {
  private ArrayList<String> usernames;

  public UserListAdapter(Context context) {
    super(context, 0);
    usernames = new ArrayList<>();
  }

  public void setUsernames(ArrayList<String> usernames) {
    this.usernames = usernames;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return usernames.size();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = new UserItemView(getContext());
    ((UserItemView) v).setUsername(usernames.get(position));
    return v;
  }
}
