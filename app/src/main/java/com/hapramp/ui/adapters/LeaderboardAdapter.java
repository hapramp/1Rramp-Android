package com.hapramp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.LeaderboardModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderItemViewHolder> {
  private ArrayList<LeaderboardModel.Winners> leaders;
  private String myUsername;

  public LeaderboardAdapter() {
    this.leaders = new ArrayList<>();
    myUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
  }

  public void setLeaders(ArrayList<LeaderboardModel.Winners> leaders) {
    this.leaders = leaders;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public LeaderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_item_view,
      parent,
      false);
    return new LeaderItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull LeaderItemViewHolder holder, int position) {
    holder.bind(leaders.get(position), position + 1);
  }

  @Override
  public int getItemCount() {
    return leaders.size();
  }

  class LeaderItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.rank)
    TextView rank;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.earning)
    TextView earning;

    public LeaderItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final LeaderboardModel.Winners winner, int pos) {
      earning.setText(String.format("%s$", winner.getmScore()));
      username.setText(winner.getmAuthor());
      rank.setText(String.format("%d", pos));
      ImageHandler.loadCircularImage(avatar.getContext(), avatar, winner.avatarUrl(avatar.getContext()));
      checkAndMakeMyRankOutstand(winner.getmAuthor());
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToProfile(avatar.getContext(),winner.getmAuthor());
        }
      });
    }

    private void navigateToProfile(Context context, String username) {
      Intent intent = new Intent(context, ProfileActivity.class);
      intent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, username);
      context.startActivity(intent);
    }

    private void checkAndMakeMyRankOutstand(String leader){
      if(leader.equals(myUsername)){
        avatar.setBackgroundResource(R.drawable.leader_avatar_border);
      }else{
        avatar.setBackgroundResource(0);
      }
    }
  }
}

