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
import com.hapramp.models.JudgeModel;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JudgeListAdapter extends RecyclerView.Adapter<JudgeListAdapter.JudgeItemViewHolder> {
  private final Context mContext;
  ArrayList<JudgeModel> judges;
  private JudgeListListener judgeListListener;

  public JudgeListAdapter(Context context) {
    this.mContext = context;
    judges = new ArrayList<>();
  }

  public void setJudges(ArrayList<JudgeModel> judges) {
    this.judges = judges;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public JudgeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.judge_item_view,
      parent,
      false);
    return new JudgeItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull JudgeItemViewHolder holder, int position) {
    holder.bind(judges.get(position), judgeListListener);
  }

  @Override
  public int getItemCount() {
    return judges.size();
  }

  public void setJudgeListListener(JudgeListListener judgeListListener) {
    this.judgeListListener = judgeListListener;
  }

  public interface JudgeListListener {
    void onAddJudge(JudgeModel judge);

    void onRemoveJudge(JudgeModel judge);
  }

  public class JudgeItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.judge_image)
    ImageView judgeImage;
    @BindView(R.id.judge_fullname)
    TextView judgeFullname;
    @BindView(R.id.judge_username)
    TextView judgeUsername;
    @BindView(R.id.judge_add_btn)
    TextView judgeAddBtn;

    public JudgeItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final JudgeModel judge, final JudgeListListener judgeListListener) {
      ImageHandler.loadCircularImage(mContext, judgeImage,
        String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
          judge.getmUsername()));
      judgeFullname.setText(judge.getmFullName());
      judgeUsername.setText(judge.getmUsername());
      if (judge.isSelected()) {
        judgeAddBtn.setText("Remove");
        judgeAddBtn.setBackgroundResource(R.drawable.remove_judge_btn_bg);
      } else {
        judgeAddBtn.setText("Add");
        judgeAddBtn.setBackgroundResource(R.drawable.follow_btn_bg);
      }
      judgeAddBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (judgeListListener != null) {
            if (judge.isSelected()) {
              judge.setSelected(false);
              judgeListListener.onRemoveJudge(judge);
            } else {
              judge.setSelected(true);
              judgeListListener.onAddJudge(judge);
            }
          }
        }
      });
    }
  }
}
