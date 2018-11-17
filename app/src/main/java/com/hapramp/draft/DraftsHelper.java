package com.hapramp.draft;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import xute.markdeditor.models.DraftModel;

public class DraftsHelper {
  DraftDatabaseHelper draftDatabaseHelper;
  private Handler handler;
  private BlogDraftsDatabaseCallbacks blogDraftCallback;
  private ContestDraftsDatabaseCallbacks contestDraftCallback;


  public DraftsHelper(Context context) {
    handler = new Handler();
    draftDatabaseHelper = new DraftDatabaseHelper(context);
  }

  /*
   * Inserts asynchronously
   * */
  public void insertDraft(final DraftModel draft) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.insertBlogDraft(draft);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (blogDraftCallback != null) {
                blogDraftCallback.onDraftInserted(success);
              }
            }
          }
        );
      }
    }.start();
  }

  public void insertDraft(final ContestDraftModel draft) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.insertContestDraft(draft);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (contestDraftCallback != null) {
                contestDraftCallback.onDraftInserted(success);
              }
            }
          }
        );
      }
    }.start();
  }

  public void fetchBlogDraftById(final long id) {
    new Thread() {
      @Override
      public void run() {
        final DraftModel draft = draftDatabaseHelper.findBlogDraftById(id);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (blogDraftCallback != null) {
                blogDraftCallback.onSingleBlogDraftRead(draft);
              }
            }
          }
        );
      }
    }.start();
  }

  /**
   * Read Contest draft
   *
   * @param id contest id
   */
  public void fetchContestDraftById(final long id) {
    new Thread() {
      @Override
      public void run() {
        final ContestDraftModel draft = draftDatabaseHelper.findContestDraftById(id);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (contestDraftCallback != null) {
                contestDraftCallback.onSingleContestDraftRead(draft);
              }
            }
          }
        );
      }
    }.start();
  }

  /**
   * @param draft model of Blog Draft
   */
  public void updateBlogDraft(final DraftModel draft) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.updateBlogDraft(draft);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (blogDraftCallback != null) {
                blogDraftCallback.onDraftUpdated(success);
              }
            }
          }
        );
      }
    }.start();
  }

  /**
   * @param draft model of Contest draft.
   */
  public void updateContestDraft(final ContestDraftModel draft) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.updateContestDraft(draft);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (contestDraftCallback != null) {
                contestDraftCallback.onDraftUpdated(success);
              }
            }
          }
        );
      }
    }.start();
  }


  public void deleteBlogDraft(final long id) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.deleteDraft(id);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (blogDraftCallback != null) {
                blogDraftCallback.onDraftDeleted(success);
              }
            }
          }
        );
      }
    }.start();
  }

  public void deleteContestDraft(final long id) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.deleteDraft(id);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (contestDraftCallback != null) {
                contestDraftCallback.onDraftDeleted(success);
              }
            }
          }
        );
      }
    }.start();
  }

  public void setBlogDraftCallbacks(BlogDraftsDatabaseCallbacks databaseCallbacks) {
    this.blogDraftCallback = databaseCallbacks;
  }

  public void setContestDraftCallbacks(ContestDraftsDatabaseCallbacks contestDraftCallbacks) {
    this.contestDraftCallback = contestDraftCallbacks;
  }

  public void fetchAllTypeOfDrafts() {
    new Thread() {
      @Override
      public void run() {
        final ArrayList<ContestDraftModel> contestDrafts = draftDatabaseHelper.getAllContestDrafts();
        final ArrayList<DraftModel> blogDrafts = draftDatabaseHelper.getAllBlogDrafts();
        final ArrayList<DraftListItemModel> listItemModels = new ArrayList<>();
        for (int i = 0; i < contestDrafts.size(); i++) {
          DraftListItemModel draftListItemModel = new DraftListItemModel();
          draftListItemModel.setDraftId(contestDrafts.get(i).getDraftId());
          draftListItemModel.setDraftType(DraftType.COMPETITION);
          draftListItemModel.setTitle(contestDrafts.get(i).getCompetitionTitle().length() > 0 ? contestDrafts.get(i).getCompetitionTitle() : "Untitled Competition");
          listItemModels.add(draftListItemModel);
        }
        for (int i = 0; i < blogDrafts.size(); i++) {
          DraftListItemModel draftListItemModel = new DraftListItemModel();
          draftListItemModel.setDraftId(blogDrafts.get(i).getDraftId());
          draftListItemModel.setDraftType(DraftType.BLOG);
          draftListItemModel.setTitle(blogDrafts.get(i).getDraftTitle().length() > 0 ? blogDrafts.get(i).getDraftTitle() : "Untitled Blog");
          listItemModels.add(draftListItemModel);
        }
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (contestDraftCallback != null) {
                contestDraftCallback.onAllDraftsRead(listItemModels);
              }
            }
          }
        );
      }
    }.start();
  }

  public interface BlogDraftsDatabaseCallbacks {
    void onDraftInserted(boolean success);

    void onSingleBlogDraftRead(DraftModel draft);

    void onAllDraftsRead(ArrayList<DraftListItemModel> drafts);

    void onDraftUpdated(boolean success);

    void onDraftDeleted(boolean success);
  }

  public interface ContestDraftsDatabaseCallbacks {
    void onDraftInserted(boolean success);

    void onSingleContestDraftRead(ContestDraftModel draft);

    void onAllDraftsRead(ArrayList<DraftListItemModel> drafts);

    void onDraftUpdated(boolean success);

    void onDraftDeleted(boolean success);
  }

}
