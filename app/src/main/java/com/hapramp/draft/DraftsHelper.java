package com.hapramp.draft;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import xute.markdeditor.models.DraftModel;

public class DraftsHelper {
  DraftDatabaseHelper draftDatabaseHelper;
  private Handler handler;
  private DraftsDatabaseCallbacks databaseCallbacks;


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
        final boolean success = draftDatabaseHelper.insertDraft(draft);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (databaseCallbacks != null) {
                databaseCallbacks.onDraftInserted(success);
              }
            }
          }
        );
      }
    }.start();
  }

  public void fetchDraftById(final long id) {
    new Thread() {
      @Override
      public void run() {
        final DraftModel draft = draftDatabaseHelper.findDraftById(id);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (databaseCallbacks != null) {
                databaseCallbacks.onDraftRead(draft);
              }
            }
          }
        );
      }
    }.start();
  }

  public void fetchAllDraft() {
    new Thread() {
      @Override
      public void run() {
        final ArrayList<DraftModel> drafts = draftDatabaseHelper.getAllDrafts();
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (databaseCallbacks != null) {
                databaseCallbacks.onDraftsRead(drafts);
              }
            }
          }
        );
      }
    }.start();
  }

  public void updateDraft(final DraftModel draft) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.updateDraft(draft) != -1;
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (databaseCallbacks != null) {
                databaseCallbacks.onDraftUpdated(success);
              }
            }
          }
        );
      }
    }.start();
  }

  public void deleteDraft(final long id) {
    new Thread() {
      @Override
      public void run() {
        final boolean success = draftDatabaseHelper.deleteDraft(id);
        handler.post(
          new Runnable() {
            @Override
            public void run() {
              if (databaseCallbacks != null) {
                databaseCallbacks.onDraftDeleted(success);
              }
            }
          }
        );
      }
    }.start();
  }

  public void setDatabaseCallbacks(DraftsDatabaseCallbacks databaseCallbacks) {
    this.databaseCallbacks = databaseCallbacks;
  }

  public interface DraftsDatabaseCallbacks {
    void onDraftInserted(boolean success);

    void onDraftRead(DraftModel draft);

    void onDraftsRead(ArrayList<DraftModel> drafts);

    void onDraftUpdated(boolean success);

    void onDraftDeleted(boolean success);
  }
}
