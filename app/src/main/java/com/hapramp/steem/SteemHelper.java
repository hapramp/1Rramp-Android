package com.hapramp.steem;

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
        } catch (SteemResponseException e) {
            e.printStackTrace();
        }
        return steemJ;

    }

}
