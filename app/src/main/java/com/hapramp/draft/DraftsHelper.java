package com.hapramp.draft;

import com.google.gson.Gson;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.DraftPostModel;
import com.hapramp.models.DraftUploadResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xute.markdeditor.models.DraftModel;

public class DraftsHelper {
  private DraftsHelperCallback draftsHelperCallback;

  public DraftsHelper() {
  }

  /*
   * Inserts asynchronously
   * */
  public void saveBlogDraft(final DraftModel draft) {
    postDraftToServer(
      DraftType.BLOG,
      draft.getDraftTitle(),
      new Gson().toJson(draft)
    );
  }

  private void postDraftToServer(String draftType, String title, String draftJson) {
    DraftPostModel draftPostModel = new DraftPostModel();
    draftPostModel.setmBody(draftJson);
    draftPostModel.setmTitle(title);
    draftPostModel.setDraftType(draftType);
    draftPostModel.setmJsonMetadata("");
    RetrofitServiceGenerator.getService().postDraft(draftPostModel).enqueue(new Callback<DraftUploadResponse>() {
      @Override
      public void onResponse(Call<DraftUploadResponse> call, Response<DraftUploadResponse> response) {
        if (draftsHelperCallback != null) {
          draftsHelperCallback.onNewDraftSaved(response.isSuccessful());
        }
      }

      @Override
      public void onFailure(Call<DraftUploadResponse> call, Throwable t) {
        if (draftsHelperCallback != null) {
          draftsHelperCallback.onNewDraftSaved(false);
        }
      }
    });
  }

  /*
   * Inserts asynchronously
   * */
  public void saveShortPostDraft(final ShortPostDraftModel draft) {
    postDraftToServer(
      DraftType.SHORT_POST,
      draft.getTitle(),
      new Gson().toJson(draft)
    );
  }

  public void saveContestDraft(final ContestDraftModel draft) {
    postDraftToServer(
      DraftType.CONTEST,
      draft.getCompetitionTitle(),
      new Gson().toJson(draft)
    );
  }

  /**
   * @param draft model of Blog Draft
   */
  public void updateBlogDraft(final DraftModel draft) {
    updateDraftAtServer(
      DraftType.BLOG,
      draft.getDraftId(),
      draft.getDraftTitle(),
      new Gson().toJson(draft)
    );
  }

  private void updateDraftAtServer(String draftType, long draftId, String title, String draftJson) {
    DraftPostModel draftPostModel = new DraftPostModel();
    draftPostModel.setmBody(draftJson);
    draftPostModel.setmTitle(title);
    draftPostModel.setDraftType(draftType);
    draftPostModel.setmJsonMetadata("");
    RetrofitServiceGenerator.getService().updateDraft(draftId, draftPostModel).enqueue(new Callback<DraftUploadResponse>() {
      @Override
      public void onResponse(Call<DraftUploadResponse> call, Response<DraftUploadResponse> response) {
        if (draftsHelperCallback != null) {
          draftsHelperCallback.onDraftUpdated(response.isSuccessful());
        }
      }

      @Override
      public void onFailure(Call<DraftUploadResponse> call, Throwable t) {
        draftsHelperCallback.onDraftUpdated(false);
      }
    });
  }

  public void updateShortPostDraft(final ShortPostDraftModel draft) {
    updateDraftAtServer(
      DraftType.SHORT_POST,
      draft.getDraftId(),
      draft.getTitle(),
      new Gson().toJson(draft)
    );
  }

  public void updateContestDraft(final ContestDraftModel draft) {
    updateDraftAtServer(
      DraftType.CONTEST,
      draft.getDraftId(),
      draft.getCompetitionTitle(),
      new Gson().toJson(draft)
    );
  }

  public void deleteDraft(long draftId) {
    RetrofitServiceGenerator
      .getService()
      .deleteDraft(draftId)
      .enqueue(new Callback<DraftUploadResponse>() {
        @Override
        public void onResponse(Call<DraftUploadResponse> call, Response<DraftUploadResponse> response) {
          if (draftsHelperCallback != null) {
            draftsHelperCallback.onDraftDeleted(response.isSuccessful());
          }
        }

        @Override
        public void onFailure(Call<DraftUploadResponse> call, Throwable t) {
          draftsHelperCallback.onDraftDeleted(false);
        }
      });
  }

  public void setDraftsHelperCallback(DraftsHelperCallback draftsHelperCallback) {
    this.draftsHelperCallback = draftsHelperCallback;
  }

  public interface DraftsHelperCallback {
    void onNewDraftSaved(boolean success);

    void onDraftUpdated(boolean success);

    void onDraftDeleted(boolean success);
  }
}
