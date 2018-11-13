package com.hapramp.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.draft.DraftsHelper;
import com.hapramp.ui.activity.CreateArticleActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import xute.markdeditor.models.DraftModel;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.DraftListItemViewHolder> {
  private ArrayList<DraftModel> draftModels;
  private Context mContext;
  private DraftsHelper draftsHelper;

  public DraftsAdapter(Context context) {
    this.mContext = context;
    draftModels = new ArrayList<>();
    draftsHelper = new DraftsHelper(mContext);
  }

  public void setDrafts(ArrayList<DraftModel> draftModels) {
    this.draftModels = draftModels;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public DraftListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_list_item_view, parent, false);
    return new DraftListItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull DraftListItemViewHolder holder, int position) {
    holder.bind(draftModels.get(position));
  }

  @Override
  public int getItemCount() {
    return draftModels.size();
  }

  private void navigateToEditor(long draftId) {
    Intent intent = new Intent(mContext, CreateArticleActivity.class);
    intent.putExtra(CreateArticleActivity.EXTRA_KEY_DRAFT_ID, draftId);
    mContext.startActivity(intent);
  }

  private void showDeleteAlert(final long draftID) {
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
      .setTitle("Delete ?")
      .setMessage("Draft will be permanently deleted!")
      .setPositiveButton("Ok, Delete", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          deleteDraft(draftID);
        }
      })
      .setNegativeButton("No", null);
    builder.show();
  }

  private void deleteDraft(long draftId) {
    draftsHelper.deleteDraft(draftId);
    removeItemWithId(draftId);
  }

  private void removeItemWithId(long draftId) {
    for (int i = 0; i < draftModels.size(); i++) {
      if (draftModels.get(i).getDraftId() == draftId) {
        draftModels.remove(i);
        break;
      }
    }
    notifyDataSetChanged();
  }

  class DraftListItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.draft_title)
    TextView draftTitle;
    @BindView(R.id.delete_icon)
    ImageView deleteIcon;
    @BindView(R.id.delete_icon_container)
    FrameLayout deleteIconContainer;

    public DraftListItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final DraftModel draft) {
      draftTitle.setText(draft.getDraftTitle());
      draftTitle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToEditor(draft.getDraftId());
        }
      });
      deleteIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          showDeleteAlert(draft.getDraftId());
        }
      });
    }
  }
}
