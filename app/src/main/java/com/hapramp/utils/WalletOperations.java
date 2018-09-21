package com.hapramp.utils;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;

import java.util.HashMap;
import java.util.Map;

import static com.hapramp.utils.WalletOperationMethods.OPERATION_DELEGATE;
import static com.hapramp.utils.WalletOperationMethods.OPERATION_POWER_DOWN;
import static com.hapramp.utils.WalletOperationMethods.OPERATION_POWER_UP;
import static com.hapramp.utils.WalletOperationMethods.OPERATION_TRANSFER;

public class WalletOperations {
  public static String getTransferUrl(String username, String memo, String amount) {
    SteemConnect steemConnect = SteemConnectUtils
      .getSteemConnectInstance();
    Map<String, String> tr = new HashMap<>();
    tr.put("to", username);
    tr.put("memo", memo);
    tr.put("amount", amount);
    return steemConnect.sign(OPERATION_TRANSFER, tr, "");
  }

  public static String getPowerUpUrl(String username, String amount) {
    SteemConnect steemConnect = SteemConnectUtils
      .getSteemConnectInstance();
    Map<String, String> pu = new HashMap<>();
    pu.put("to", username);
    pu.put("amount", amount);
    return steemConnect.sign(OPERATION_POWER_UP, pu, "");
  }

  public static String getPowerDownUrl(String vests){
    SteemConnect steemConnect = SteemConnectUtils
      .getSteemConnectInstance();
    Map<String,String> pd = new HashMap<>();
    pd.put("vesting_shares",vests);
   return steemConnect.sign(OPERATION_POWER_DOWN,pd,"");
  }

  public static String getDelegateUrl(String sender, String receiver, String vests) {
    SteemConnect steemConnect = SteemConnectUtils
      .getSteemConnectInstance();
    Map<String, String> pd = new HashMap<>();
    pd.put("delegator", sender);
    pd.put("delegatee", receiver);
    pd.put("vesting_shares", vests);
    return steemConnect.sign(OPERATION_DELEGATE, pd, "");
  }
}
