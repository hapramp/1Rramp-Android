package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.response.CompetitionResponse;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 10/28/2017.
 */

public class CompetitionRecyclerAdapter extends RecyclerView.Adapter<CompetitionRecyclerAdapter.CompetitionViewHolder> {

  public Context mContext;
  public List<CompetitionResponse> competitionResponse;
  private OnCompetitionElementsClickListener competitionElementsClickListener;

  public CompetitionRecyclerAdapter(Context mContext) {
    this.mContext = mContext;
  }

  public void setCompetitionResponses(List<CompetitionResponse> competitionResponse) {
    this.competitionResponse = competitionResponse;
    notifyDataSetChanged();
  }

  public void setCompetitionElementsClickListener(OnCompetitionElementsClickListener competitionElementsClickListener) {
    this.competitionElementsClickListener = competitionElementsClickListener;
  }

  @Override
  public CompetitionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.competition_card, null);
    return new CompetitionViewHolder(view);
  }

  @Override
  public void onBindViewHolder(CompetitionViewHolder viewHolder, int i) {
    viewHolder.bind(competitionResponse.get(i), competitionElementsClickListener);
  }

  @Override
  public int getItemCount() {
    return competitionResponse != null ? competitionResponse.size() : 0;
  }

  public interface OnCompetitionElementsClickListener {
    void onKnowMoreTapped(String compId);

    void onJoinNowTapped(String compId);
  }

  class CompetitionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.feed_owner_pic)
    ImageView feedOwnerPic;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.post_header_container)
    RelativeLayout postHeaderContainer;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.entry_fee_icon)
    TextView entryFeeIcon;
    @BindView(R.id.entry_fee_caption)
    TextView entryFeeCaption;
    @BindView(R.id.entry_fee)
    TextView entryFee;
    @BindView(R.id.prize_money_icon)
    TextView prizeMoneyIcon;
    @BindView(R.id.prize_caption)
    TextView prizeCaption;
    @BindView(R.id.prize_money)
    TextView prizeMoney;
    @BindView(R.id.join_now_btn)
    TextView joinNowBtn;
    @BindView(R.id.know_more_btn)
    TextView knowMoreBtn;
    @BindView(R.id.participantIcon)
    TextView participantIcon;
    @BindView(R.id.participantCount)
    TextView participantCount;

    public CompetitionViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      entryFeeIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
      prizeMoneyIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
      participantIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    }

    public void bind(final CompetitionResponse postResponse, final OnCompetitionElementsClickListener competitionElementsClickListener) {
      ImageHandler.load(mContext, feedOwnerPic, postResponse.getLogo_uri());
      //feedOwnerPic.setImageURI(postResponse.getLogo_uri());
      feedOwnerTitle.setText(postResponse.getName());
      feedOwnerSubtitle.setText(postResponse.getHandle());
      entryFee.setText(String.valueOf(postResponse.getEntry_fee()));

      knowMoreBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (competitionElementsClickListener != null) {
            competitionElementsClickListener.onKnowMoreTapped(String.valueOf(postResponse.getId()));
          }
        }
      });

      joinNowBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (competitionElementsClickListener != null) {
            competitionElementsClickListener.onJoinNowTapped(String.valueOf(postResponse.getId()));
          }
        }
      });

    }
  }
}
