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
//
//    private static void requestVestingShares(SteemPowerCallback steemPowerCallback, DynamicGlobalProperty dynamicGlobalProperty) {
//        SteemJ steemJ = SteemHelper.getSteemInstance();
//        try {
//            ExtendedAccount extendedAccount = steemJ.getUserAccount(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
//            steemPowerCallback.onSteemPowerFetched(calculateSteemPower(dynamicGlobalProperty, extendedAccount));
//            steemPowerCallback.onSteemBalance(extendedAccount.getSbdBalance().getAmount());
//        } catch (SteemCommunicationException e) {
//            e.printStackTrace();
//        } catch (SteemResponseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static float calculateSteemPower(DynamicGlobalProperty dynamicGlobalProperty, ExtendedAccount extendedAccount) {
//        float totalVestingShares = dynamicGlobalProperty.getTotalVestingShares().getAmount();
//        float totalVestingFundSteem = dynamicGlobalProperty.getTotalVestingFundSteem().getAmount();
//        float userVestingShare = extendedAccount.getVestingShares().getAmount();
//        return ((totalVestingFundSteem * userVestingShare) / totalVestingShares)/1000;
//    }

    public interface SteemPowerCallback {
        void onSteemPowerFetched(float steemPower);

        void onSteemBalance(float balance);

        void onSteemPowerFetchFailed();
    }
}
