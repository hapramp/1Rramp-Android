package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hapramp.R;

import java.util.ArrayList;


public class PayoutSelectionAdapter extends ArrayAdapter<String> {
  public static final int HALF_PAYOUT_POS = 0;
  public static final int FULL_POWER_UP_POS = 1;
  public static final int PAYOUT_DECLINED_POS = 2;
  private final Context mContext;
  ArrayList<String> items;

  public PayoutSelectionAdapter(@NonNull Context context) {
    super(context, 0);
    this.mContext = context;
    items = new ArrayList<>();
    items.add("50% SBD/ 50% SP");
    items.add("Power Up 100%");
    items.add("Decline Payout");
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  // It gets a View that displays in the drop down popup the data at the specified position
  @Override
  public View getDropDownView(int position, View convertView,
                              ViewGroup parent) {
    return getCustomView(position, convertView, parent);
  }

  private View getCustomView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = LayoutInflater.from(mContext).inflate(R.layout.payout_selector_item_view, null);
    }
    TextView title = view.findViewById(R.id.title);
    title.setText(items.get(position));
    return view;
  }
}
