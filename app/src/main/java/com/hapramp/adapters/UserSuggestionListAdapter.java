package com.hapramp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hapramp.R;

import java.util.ArrayList;

/**
 * Created by Ankit on 4/5/2018.
 */

public class UserSuggestionListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> words;

    public UserSuggestionListAdapter(Context context) {
        super(context, 0);
        words = new ArrayList<>();
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        ((TextView) v.findViewById(R.id.content)).setText(words.get(position));

        return v;
    }

    @Override
    public int getCount() {
        return words.size();
    }
}