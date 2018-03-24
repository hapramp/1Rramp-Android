package com.hapramp.steem;

import android.util.Log;

import com.hapramp.preferences.HaprampPreferenceManager;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 2/18/2018.
 */

public class SteemHelper {

    public static SteemJ getSteemInstance(String username,String ppk) {

        SteemJ steemJ = null;

        // Change the default settings if needed:
        SteemJConfig myConfig = SteemJConfig.getInstance();
        myConfig.setResponseTimeout(100000);
        myConfig.setDefaultAccount(new AccountName(username));
        // Add and manage private keys:
        List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
        privateKeys.add(new ImmutablePair<>(PrivateKeyType.POSTING, ppk));
        myConfig.getPrivateKeyStorage().addAccount(myConfig.getDefaultAccount(), privateKeys);
        try {
            steemJ = new SteemJ();
        } catch (SteemCommunicationException e) {
            e.printStackTrace();
            Log.d("SteemHelper","Communication Error : "+e.toString());
        } catch (SteemResponseException e) {
            Log.d("SteemHelper", "Response Error : " + e.toString());
            e.printStackTrace();
        }
        Log.d("SteemHelper",steemJ.toString());
        return steemJ;

    }

    public static SteemJ getSteemInstance(){

        SteemJ steemJ = null;
        String username = HaprampPreferenceManager.getInstance().getSteemUsername();
        String ppk = HaprampPreferenceManager.getInstance().getPPK();
        Log.d("SteemHelper","getSteemInstance() Username:"+username+" ppk:"+ppk);
        if(ppk.length()==0)
            return steemJ;

        // Change the default settings if needed:
        SteemJConfig myConfig = SteemJConfig.getInstance();
        myConfig.setResponseTimeout(100000);
        myConfig.setDefaultAccount(new AccountName(username));
        // Add and manage private keys:
        List<ImmutablePair<PrivateKeyType, String>> privateKeys = new ArrayList<>();
        privateKeys.add(new ImmutablePair<>(PrivateKeyType.POSTING, ppk));
        myConfig.getPrivateKeyStorage().addAccount(myConfig.getDefaultAccount(), privateKeys);
        try {
            steemJ = new SteemJ();
        } catch (SteemCommunicationException e) {
            Log.d("SteemHelper","Communication Error : "+e.toString());
            e.printStackTrace();
        } catch (SteemResponseException e) {
            Log.d("SteemHelper", "Response Error : " + e.toString());
            e.printStackTrace();
        }

        Log.d("SteemHelper",steemJ+"");
        return steemJ;
    }

    public static String getFollowingsRequestString(String username){

       return "{\"id\":2,\"jsonrpc\":\"2.0\",\"method\":\"call\",\"params\":[\"follow_api\",\"get_following\",[\""+username+"\",\"\",\"blog\",100]]}";

    }

}
