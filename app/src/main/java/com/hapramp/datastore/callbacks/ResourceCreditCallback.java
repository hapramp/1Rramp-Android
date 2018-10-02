package com.hapramp.datastore.callbacks;

import com.hapramp.models.ResourceCreditModel;

public interface ResourceCreditCallback {
  void onResourceCreditAvailable(ResourceCreditModel resourceCreditModel);

  void onResourceCreditError(String msg);
}
