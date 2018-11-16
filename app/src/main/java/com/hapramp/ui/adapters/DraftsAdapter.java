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
import com.hapramp.draft.DraftListItemModel;
import com.hapramp.draft.DraftType;
import com.hapramp.draft.DraftsHelper;
import com.hapramp.ui.activity.CompetitionCreatorActivity;
import com.hapramp.ui.activity.CreateArticleActivity;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.DraftListItemViewHolder> {

  private ArrayList<DraftListItemModel> drafts;
  private Context mContext;
  private DraftsHelper draftsHelper;

  public DraftsAdapter(Context context) {
    this.mContext = context;
    drafts = new ArrayList<>();
    draftsHelper = new DraftsHelper(mContext);
  }

  public void setDrafts(ArrayList<DraftListItemModel> draftModels) {
    this.drafts = draftModels;
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
    holder.bind(drafts.get(position));
  }

  @Override
  public int getItemCount() {
    return drafts.size();
  }

  private void navigateToEditor(long draftId) {
    Intent intent = new Intent(mContext, CreateArticleActivity.class);
    intent.putExtra(CreateArticleActivity.EXTRA_KEY_DRAFT_ID, draftId);
    mContext.startActivity(intent);
  }

  private void showDeleteAlert(final long draftID, final DraftType draftType) {
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
      .setTitle("Delete ?")
      .setMessage("Draft will be permanently deleted!")
      .setPositiveButton("Ok, Delete", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          deleteDraft(draftID, draftType);
        }
      })
      .setNegativeButton("No", null);
    builder.show();
  }

  private void deleteDraft(long draftId, DraftType draftType) {
    if (draftType == DraftType.BLOG) {
      draftsHelper.deleteBlogDraft(draftId);
      removeItemWithId(draftId);
    } else {
      draftsHelper.deleteContestDraft(draftId);
      removeItemWithId(draftId);
    }
  }

  private void removeItemWithId(long draftId) {
    for (int i = 0; i < drafts.size(); i++) {
      if (drafts.get(i).getDraftId() == draftId) {
        drafts.remove(i);
        break;
      }
    }
    notifyDataSetChanged();
  }

  private void navigateToCompetitionCreatePage(long draftId) {
    Intent intent = new Intent(mContext, CompetitionCreatorActivity.class);
    intent.putExtra(CompetitionCreatorActivity.EXTRA_KEY_DRAFT_ID, draftId);
    mContext.startActivity(intent);
  }

  class DraftListItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.draft_icon)
    ImageView draftIcon;
    @BindView(R.id.draft_type)
    TextView draftType;
    @BindView(R.id.created_time)
    TextView createdTime;
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

    public void bind(final DraftListItemModel draft) {
      createdTime.setText(" | " + MomentsUtils.getFormattedTime(MomentsUtils.getTimeFromMillis(draft.getDraftId())));
      if (draft.getDraftType() == DraftType.BLOG) {
        draftIcon.setImageResource(R.drawable.blog_icon_filled);
        draftType.setText("Blog Draft");
      } else {
        draftIcon.setImageResource(R.drawable.competition_filled);
        draftType.setText("Competition Draft");
      }
      draftTitle.setText(draft.getTitle());
      draftTitle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (draft.getDraftType() == DraftType.BLOG) {
            navigateToEditor(draft.getDraftId());
          } else {
            navigateToCompetitionCreatePage(draft.getDraftId());
          }
        }
      });
      deleteIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          showDeleteAlert(draft.getDraftId(), draft.getDraftType());
        }
      });
    }
  }
}
