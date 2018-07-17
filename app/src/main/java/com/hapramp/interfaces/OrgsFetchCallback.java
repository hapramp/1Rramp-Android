package com.hapramp.interfaces;

import com.hapramp.datamodels.response.OrgsResponse;

import java.util.List;

/**
 * Created by Ankit on 10/22/2017.
 */

public interface OrgsFetchCallback {
  void onOrgsFetched(List<OrgsResponse> orgs);

  void onOrgFetchedError();
}
