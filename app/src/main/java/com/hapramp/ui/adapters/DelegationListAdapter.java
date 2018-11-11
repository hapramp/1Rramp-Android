package com.hapramp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.DelegationModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.WalletOperations;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelegationListAdapter extends RecyclerView.Adapter<DelegationListAdapter.DelegationItemViewHolder> {
  private ArrayList<DelegationModel> delegationModels;
  private String mCurrentUsername;
  private Context mContext;
  private boolean shouldShowDelegationCancellationButton;

  public DelegationListAdapter(Context context) {
    this.mContext = context;
    this.delegationModels = new ArrayList<>();
    mCurrentUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
  }


  public void setDelegationModels(ArrayList<DelegationModel> delegationModels) {
    this.delegationModels = delegationModels;
    if (delegationModels.size() > 0) {
      shouldShowDelegationCancellationButton = delegationModels.get(0).getDelegator().equals(mCurrentUsername);
      notifyDataSetChanged();
    }
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

  private void navigateToCancelDelegation(String delegatee) {
    String url = WalletOperations.getDelegateUrl(
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
      delegatee,
      "0 VESTS");
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    mContext.startActivity(browserIntent);
  }

  class DelegationItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.delegatee_image)
    ImageView delegateeImage;
    @BindView(R.id.delegatee_username)
    TextView delegateeUsername;
    @BindView(R.id.delegated_sp)
    TextView delegatedSp;
    @BindView(R.id.cancel_delegation_btn)
    TextView cancelDelegationBtn;

    public DelegationItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final DelegationModel delegation) {
      ImageHandler.loadCircularImage(itemView.getContext(), delegateeImage,
        String.format(itemView.getContext().getResources().getString(R.string.steem_user_profile_pic_format),
          delegation.getDelegatee()));
      delegateeUsername.setText(delegation.getDelegatee());
      delegatedSp.setText(String.format(Locale.US, "%,.2f", delegation.getDelegatedSteemPower()));
      if (shouldShowDelegationCancellationButton) {
        cancelDelegationBtn.setVisibility(View.VISIBLE);
        cancelDelegationBtn.setClickable(true);
        cancelDelegationBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            navigateToCancelDelegation(delegation.getDelegatee());
          }
        });
      } else {
        cancelDelegationBtn.setVisibility(View.GONE);
        cancelDelegationBtn.setClickable(false);
      }
    }
  }
}
