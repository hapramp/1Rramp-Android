package com.hapramp.viewmodel.common;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.hapramp.utils.ConnectionUtils;

/**
 * Created by Ankit on 5/6/2018.
 */

public class ConnectivityViewModel extends AndroidViewModel {


  MutableLiveData<Boolean> connectivity;

  public ConnectivityViewModel(@NonNull Application application) {
    super(application);
    application.registerReceiver(new NetworkChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
  }

  public MutableLiveData<Boolean> getConnectivityState() {
    if (connectivity == null) {
      connectivity = new MutableLiveData<>();
    }
    return connectivity;
  }

  class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      try {
        connectivity.setValue(ConnectionUtils.isConnected(context));
      }
      catch (NullPointerException e) {
        e.printStackTrace();
      }
    }
  }

}
