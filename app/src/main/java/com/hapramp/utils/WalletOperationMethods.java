package com.hapramp.utils;

import android.util.Log;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.PUT;

public class WalletOperationMethods {
  public static final String OPERATION_POWER_UP = "transfer-to-vesting";
  public static final String OPERATION_POWER_DOWN = "withdraw-vesting";
  public static final String OPERATION_TRANSFER = "transfer";

  public static void testUrls(){
    SteemConnect steemConnect= SteemConnectUtils
      .getSteemConnectInstance(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    Map<String,String> pu = new HashMap<>();
    pu.put("amount","5 STEEM");
    pu.put("to","bxute");
    Log.d("WalletOptions",steemConnect.sign(OPERATION_POWER_UP,pu,""));

    Map<String,String> pd = new HashMap<>();
    pd.put("vesting_shares","5 VESTS");
    Log.d("WalletOptions",steemConnect.sign(OPERATION_POWER_DOWN,pd,""));

    Map<String,String> tr = new HashMap<>();
    tr.put("to","bxute");
    tr.put("memo","hello transfer");
    tr.put("amount","5 STEEM");
    Log.d("WalletOptions",steemConnect.sign(OPERATION_TRANSFER,tr,""));
  }
}

