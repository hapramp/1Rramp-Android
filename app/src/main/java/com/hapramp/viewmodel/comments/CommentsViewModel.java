package com.hapramp.viewmodel.comments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.hapramp.repository.CommentsRepository;
import com.hapramp.steem.SteemCommentModel;

import java.util.List;

/**
 * Created by Ankit on 5/9/2018.
 */

public class CommentsViewModel extends AndroidViewModel {

  CommentsRepository commentsRepository;
  private MutableLiveData<List<SteemCommentModel>> steemComments;

  public CommentsViewModel(Application application) {
    super(application);
    commentsRepository = new CommentsRepository(application);
  }

  public MutableLiveData<List<SteemCommentModel>> getSteemComments(String author, String postPermlink) {
    if (steemComments == null) {
      steemComments = new MutableLiveData<>();
      getComments(author, postPermlink);
    }
    return steemComments;
  }

  private void getComments(String author, String postPermlink) {
    commentsRepository.getComments(author, postPermlink).observeForever(new Observer<List<SteemCommentModel>>() {
      @Override
      public void onChanged(@Nullable List<SteemCommentModel> steemCommentModels) {
        steemComments.setValue(steemCommentModels);
      }
    });
  }

  public void addComments(SteemCommentModel steemCommentModel, String postPermlink) {
    SteemCommentModel _commentToAdd = steemCommentModel;
    _commentToAdd.setPostPermlink(postPermlink);
    commentsRepository.addComment(_commentToAdd);
  }

}
