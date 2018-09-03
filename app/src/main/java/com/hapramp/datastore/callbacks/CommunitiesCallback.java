package com.hapramp.datastore.callbacks;

import com.hapramp.models.CommunityModel;

import java.util.List;

public interface CommunitiesCallback {
  void onWhileWeAreFetchingCommunities();

  void onCommunitiesAvailable(List<CommunityModel> communityModelList, boolean isFreshData);

  void onCommunitiesFetchError(String err);
}
