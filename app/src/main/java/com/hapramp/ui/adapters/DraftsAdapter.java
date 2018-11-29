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
import com.hapramp.ui.activity.CreatePostActivity;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.ui.activity.CompetitionCreatorActivity.EXTRA_KEY_DRAFT_JSON;

public class DraftsAdapter extends RecyclerView.Adapter<DraftsAdapter.DraftListItemViewHolder> {

  private List<DraftListItemModel> drafts;
  private Context mContext;
  private DraftsHelper draftsHelper;
  private EmptyDraftsAdapterCallback emptyDraftsAdapterCallback;

  public DraftsAdapter(Context context) {
    this.mContext = context;
    drafts = new ArrayList<>();
    draftsHelper = new DraftsHelper();
  }

  public void setDrafts(List<DraftListItemModel> draftModels) {
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

  private void showDeleteAlert(final long draftID) {
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
      .setTitle("Delete this draft?")
      .setMessage("The draft will be deleted permanently.")
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
    for (int i = 0; i < drafts.size(); i++) {
      if (drafts.get(i).getDraftId() == draftId) {
        drafts.remove(i);
        break;
      }
    }
    if (drafts.size() == 0) {
      if (emptyDraftsAdapterCallback != null) {
        emptyDraftsAdapterCallback.onAllDraftsDeleted();
      }
    }
    notifyDataSetChanged();
  }

  private void navigateToCompetitionCreatePage(long draftId, String draftJson) {
    Intent intent = new Intent(mContext, CompetitionCreatorActivity.class);
    intent.putExtra(CompetitionCreatorActivity.EXTRA_KEY_DRAFT_ID, draftId);
    intent.putExtra(EXTRA_KEY_DRAFT_JSON, draftJson);
    mContext.startActivity(intent);
  }

  private void navigateToShortPostPage(long draftId, String draftJson) {
    Intent intent = new Intent(mContext, CreatePostActivity.class);
    intent.putExtra(CreatePostActivity.EXTRA_KEY_DRAFT_ID, draftId);
    intent.putExtra(EXTRA_KEY_DRAFT_JSON, draftJson);
    mContext.startActivity(intent);
  }

  private void navigateToBlogEditor(long draftId, String draftJson) {
    Intent intent = new Intent(mContext, CreateArticleActivity.class);
    intent.putExtra(CreateArticleActivity.EXTRA_KEY_DRAFT_ID, draftId);
    intent.putExtra(EXTRA_KEY_DRAFT_JSON, draftJson);
    mContext.startActivity(intent);
  }

  public void setEmptyDraftsAdapter(EmptyDraftsAdapterCallback emptyDraftsAdapter) {
    this.emptyDraftsAdapterCallback = emptyDraftsAdapter;
  }

  public interface EmptyDraftsAdapterCallback {
    void onAllDraftsDeleted();
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
      createdTime.setText(" | Last updated: " + MomentsUtils.getFormattedTime(draft.getLastModified()));
      if (draft.getDraftType().equals(DraftType.BLOG)) {
        draftIcon.setImageResource(R.drawable.blog_icon_filled);
        draftType.setText("Blog Draft");
      } else if (draft.getDraftType().equals(DraftType.CONTEST)) {
        draftIcon.setImageResource(R.drawable.competition);
        draftType.setText("Competition Draft");
      } else {
        //short post draft
        draftIcon.setImageResource(R.drawable.image);
        draftType.setText("Short Post Draft");
      }
      draftTitle.setText(draft.getTitle().length() > 0 ? draft.getTitle() : "Untitled Draft");
      draftTitle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (draft.getDraftType().equals(DraftType.BLOG)) {
            navigateToBlogEditor(draft.getDraftId(), draft.getJson());
          } else if (draft.getDraftType().equals(DraftType.CONTEST)) {
            navigateToCompetitionCreatePage(draft.getDraftId(), draft.getJson());
          } else {
            navigateToShortPostPage(draft.getDraftId(), draft.getJson());
          }
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
