package com.hapramp.steem;

import com.hapramp.preferences.HaprampPreferenceManager;

public class SteemPowerHelper {

    public static void requestSteemUserEarningInfo(final SteemPowerCallback steemPowerCallback) {
        new Thread() {
            @Override
            public void run() {
//                SteemJ steemJ = SteemHelper.getSteemInstance();
//                try {
//                    DynamicGlobalProperty dynamicGlobalProperty = steemJ.getDynamicGlobalProperties();
//                    requestVestingShares(steemPowerCallback, dynamicGlobalProperty);
//                } catch (SteemCommunicationException e) {
//                    e.printStackTrace();
//                    steemPowerCallback.onSteemPowerFetchFailed();
//                } catch (SteemResponseException e) {
//                    steemPowerCallback.onSteemPowerFetchFailed();
//                    e.printStackTrace();
//                }
//                super.run();
            }
        }.start();
    }
    public interface SteemPowerCallback {
        void onSteemPowerFetched(float steemPower);

        void onSteemBalance(float balance);

        void onSteemPowerFetchFailed();
    }
}
