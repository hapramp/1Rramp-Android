package com.hapramp.steem;

/**
 * Created by Ankit on 2/18/2018.
 */

public class SteemHelper {
//
//    public static SteemJ getSteemInstance(String username,String ppk) {
//
//        SteemJ steemJ = null;
//        SteemJConfig myConfig = SteemJConfig.getInstance();
//        myConfig.setResponseTimeout(100000);
//        myConfig.setDefaultAccount(new AccountName(username));
//        List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
//        privateKeys.add(new ImmutablePair<>(PrivateKeyType.POSTING, ppk));
//        myConfig.getPrivateKeyStorage().addAccount(myConfig.getDefaultAccount(), privateKeys);
//        try {
//            steemJ = new SteemJ();
//        } catch (SteemCommunicationException e) {
//            e.printStackTrace();
//        } catch (SteemResponseException e) {
//            e.printStackTrace();
//        }
//        return steemJ;
//    }

//    public static SteemJ getSteemInstance(){
//
//        SteemJ steemJ = null;
//        String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
//        String ppk = HaprampPreferenceManager.getInstance().getPPK();
//        if(ppk.length()==0)
//            return steemJ;
//        SteemJConfig myConfig = SteemJConfig.getInstance();
//        myConfig.setResponseTimeout(100000);
//        myConfig.setDefaultAccount(new AccountName(username));
//        List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
//        privateKeys.add(new ImmutablePair<>(PrivateKeyType.POSTING, ppk));
//        myConfig.getPrivateKeyStorage().addAccount(myConfig.getDefaultAccount(), privateKeys);
//        try {
//            steemJ = new SteemJ();
//        } catch (SteemCommunicationException e) {
//            e.printStackTrace();
//        } catch (SteemResponseException e) {
//            e.printStackTrace();
//        }
//        return steemJ;
//    }
}
