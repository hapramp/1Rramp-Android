package bxute.chat.chat.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import bxute.fcm.FirebaseDatabaseManager;

/**
 * Created by Ankit on 08-07-2017.
 */

public class FCMClientInstanceService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabaseManager.registerDevice();
    }

}
