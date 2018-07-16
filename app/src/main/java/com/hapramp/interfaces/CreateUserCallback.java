package com.hapramp.interfaces;

import com.hapramp.datamodels.response.CreateUserReponse;

/**
 * Created by Ankit on 10/11/2017.
 */

public interface CreateUserCallback {
  void onUserCreated(CreateUserReponse body);

  void onFailedToCreateUser(String message);
}
