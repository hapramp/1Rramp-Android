package com.hapramp.chat.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hapramp.logger.L;
import com.hapramp.preferences.HaprampPreferenceManager;

/**
 * Created by Ankit on 08-07-2017.
 */

public class HaprampFCMClientInstanceService extends FirebaseInstanceIdService {

    private static final String TAG = HaprampFCMClientInstanceService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        L.D.m(TAG," token :"+refreshedToken);
        HaprampPreferenceManager.getInstance().setDeviceId(refreshedToken);
    }

}
