package com.hapramp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.DelegationModel;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelegationListAdapter extends RecyclerView.Adapter<DelegationListAdapter.DelegationItemViewHolder> {
  private ArrayList<DelegationModel> delegationModels;

  public DelegationListAdapter() {
    this.delegationModels = new ArrayList<>();
  }


  public void setDelegationModels(ArrayList<DelegationModel> delegationModels) {
    this.delegationModels = delegationModels;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public DelegationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delegations_item_view, parent, false);
    return new DelegationItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull DelegationItemViewHolder holder, int position) {
    holder.bind(delegationModels.get(position));
  }

  @Override
  public int getItemCount() {
    return delegationModels.size();
  }

  class DelegationItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.delegatee_image)
    ImageView delegateeImage;
    @BindView(R.id.delegatee_username)
    TextView delegateeUsername;
    @BindView(R.id.delegated_sp)
    TextView delegatedSp;
    @BindView(R.id.delegation_time)
    TextView delegationTime;

    public DelegationItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(DelegationModel delegation) {
      ImageHandler.loadCircularImage(itemView.getContext(), delegateeImage,
        String.format(itemView.getContext().getResources().getString(R.string.steem_user_profile_pic_format),
          delegation.getDelegatee()));
      delegateeUsername.setText(delegation.getDelegatee());
      delegatedSp.setText(String.format("%.2f SP", delegation.getDelegatedSteemPower()));
      delegationTime.setText(String.format("Delegation Time: %s", MomentsUtils.getFormattedTime(delegation.getTime())));
    }
  }
}
