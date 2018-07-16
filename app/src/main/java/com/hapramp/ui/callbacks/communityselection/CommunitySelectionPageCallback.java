package com.hapramp.ui.callbacks.communityselection;

import com.hapramp.datamodels.CommunityModel;

import java.util.List;

/**
 * Created by Ankit on 5/7/2018.
 */

public interface CommunitySelectionPageCallback {
  void onCommunityFetchFailed();

  void onCommunityUpdated(List<CommunityModel> communities);

  void onCommunityUpdateFailed();
}
