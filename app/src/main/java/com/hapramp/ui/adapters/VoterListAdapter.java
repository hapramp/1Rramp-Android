package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.VoterData;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoterListAdapter extends RecyclerView.Adapter<VoterListAdapter.VoterItemViewHolder> {
  private ArrayList<VoterData> votersData;

  public void setVotersData(ArrayList<VoterData> votersData) {
    this.votersData = votersData;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VoterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voter_item_view,
      parent,
      false);
    return new VoterItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull VoterItemViewHolder holder, int position) {
    holder.bind(votersData.get(position));
  }

  @Override
  public int getItemCount() {
    return votersData.size();
  }

  class VoterItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.voter_imageview)
    ImageView voterImageview;
    @BindView(R.id.voter_name)
    TextView voterName;
    @BindView(R.id.vote_value)
    TextView voteValue;
    @BindView(R.id.vote_percent)
    TextView votePercent;

    public VoterItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(VoterData voterData) {
      Context context = itemView.getContext();
      ImageHandler.loadCircularImage(context, voterImageview,
        String.format(context.getResources().getString(R.string.steem_user_profile_pic_format),
          voterData.getUsername()));
      voterName.setText(voterData.getUsername());
      voteValue.setText(voterData.getVoteValue());
      votePercent.setText(voterData.getPerecent());
    }
  }
}
