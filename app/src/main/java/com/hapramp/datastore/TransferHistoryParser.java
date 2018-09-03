package com.hapramp.datastore;
import com.hapramp.steem.models.TransferHistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransferHistoryParser {
  private String total_vesting_fund_steem;
  private String total_vesting_share;
  public ArrayList<TransferHistoryModel> parseTransferHistory(String response, String user) {
    ArrayList<TransferHistoryModel> transferHistoryList = new ArrayList<>();
    try {
      JSONObject root = new JSONObject(response);
      JSONObject props = root
        .getJSONObject(KEYS.KEY_RESULT)
        .getJSONObject(KEYS.KEY_PROPS);

      total_vesting_fund_steem = props.getString(KEYS.KEY_TOTAL_VESTING_FUND_STEEM);
      total_vesting_share = props.getString(KEYS.KEY_TOTAL_VESTING_SHARE);

      JSONArray transfer_history = root
        .getJSONObject(KEYS.KEY_RESULT)
        .getJSONObject(KEYS.KEY_ACCOUNTS)
        .getJSONObject(user)
        .getJSONArray(KEYS.KEY_TRANSFER_HISTORY);

      for (int i = 0; i < transfer_history.length(); i++) {
        JSONArray _history_item = transfer_history.getJSONArray(i);
        JSONObject transfer_obj = _history_item.getJSONObject(1);
        JSONArray operationArray = transfer_obj.getJSONArray(KEYS.KEY_OPERATION);
        String timestamp = transfer_obj.getString(KEYS.KEY_TIMESTAMP);
        JSONObject meta_data_obj = operationArray.getJSONObject(1);
        String operation = operationArray.getString(0);
        //skipping extra data
        if (operation.equals(KEYS.OPERATION_CLAIM_REWARD_BALANCE) ||
          operation.equals(KEYS.OPERATION_AUTHOR_REWARD) ||
          operation.equals(KEYS.OPERATION_TRANSFER) ||
          operation.equals(KEYS.OPERATION_CURATION_REWARD)) {
          transferHistoryList.add(parse(user, operation, timestamp, meta_data_obj));
        }
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    return transferHistoryList;
  }

  private TransferHistoryModel parse(String user, String operation, String timestamp, JSONObject meta_data_obj) {
    try {
      switch (operation) {
        case KEYS.OPERATION_TRANSFER:
          return parseTransfer(user, operation, timestamp, meta_data_obj);
        case KEYS.OPERATION_AUTHOR_REWARD:
          return parseAuthorReward(user, operation, timestamp, meta_data_obj);
        case KEYS.OPERATION_CLAIM_REWARD_BALANCE:
          return parseClaimRewardBalance(user, operation, timestamp, meta_data_obj);
        case KEYS.OPERATION_CURATION_REWARD:
          return parseCurationReward(user, operation, timestamp, meta_data_obj);
      }
    }
    catch (JSONException e) {
    }
    return null;
  }

  private TransferHistoryModel parseTransfer(String user, String op, String timestamp, JSONObject meta_data_obj) throws JSONException {
    TransferHistoryModel transferHistoryModel = new TransferHistoryModel();
    transferHistoryModel.setOperation(op);
    transferHistoryModel.setUserAccount(user);
    transferHistoryModel.setTimeStamp(timestamp);
    transferHistoryModel.setTotal_vesting_fund_steem(total_vesting_fund_steem);
    transferHistoryModel.setTotal_vesting_shares(total_vesting_share);
    transferHistoryModel.setTransfer(new TransferHistoryModel.Transfer(
      meta_data_obj.getString(KEYS.KEY_FROM),
      meta_data_obj.getString(KEYS.KEY_TO),
      meta_data_obj.getString(KEYS.KEY_AMOUNT),
      meta_data_obj.optString(KEYS.KEY_MEMO, "")
    ));
    return transferHistoryModel;
  }

  private TransferHistoryModel parseAuthorReward(String user, String op, String timestamp, JSONObject meta_data_obj) throws JSONException {
    TransferHistoryModel transferHistoryModel = new TransferHistoryModel();
    transferHistoryModel.setOperation(op);
    transferHistoryModel.setUserAccount(user);
    transferHistoryModel.setTimeStamp(timestamp);
    transferHistoryModel.setTotal_vesting_fund_steem(total_vesting_fund_steem);
    transferHistoryModel.setTotal_vesting_shares(total_vesting_share);
    transferHistoryModel.setAuthorReward(new TransferHistoryModel.AuthorReward(
      meta_data_obj.getString(KEYS.KEY_AUTHOR),
      meta_data_obj.getString(KEYS.KEY_PERMLINK),
      meta_data_obj.getString(KEYS.KEY_SBD_PAYOUT),
      meta_data_obj.optString(KEYS.KEY_STEEM_PAYOUT, ""),
      meta_data_obj.optString(KEYS.KEY_VESTING_PAYOUT)
    ));
    return transferHistoryModel;
  }

  private TransferHistoryModel parseClaimRewardBalance(String user, String op, String timestamp, JSONObject meta_data_obj) throws JSONException {
    TransferHistoryModel transferHistoryModel = new TransferHistoryModel();
    transferHistoryModel.setOperation(op);
    transferHistoryModel.setUserAccount(user);
    transferHistoryModel.setTimeStamp(timestamp);
    transferHistoryModel.setTotal_vesting_fund_steem(total_vesting_fund_steem);
    transferHistoryModel.setTotal_vesting_shares(total_vesting_share);
    transferHistoryModel.setClaimRewardBalance(new TransferHistoryModel.ClaimRewardBalance(
      meta_data_obj.getString(KEYS.KEY_ACCOUNT),
      meta_data_obj.getString(KEYS.KEY_REWARD_STEEM),
      meta_data_obj.getString(KEYS.KEY_REWARD_SBD),
      meta_data_obj.optString(KEYS.KEY_REWARD_VESTS, "")
    ));
    return transferHistoryModel;
  }

  private TransferHistoryModel parseCurationReward(String user, String op, String timestamp, JSONObject meta_data_obj) throws JSONException {
    TransferHistoryModel transferHistoryModel = new TransferHistoryModel();
    transferHistoryModel.setOperation(op);
    transferHistoryModel.setUserAccount(user);
    transferHistoryModel.setTotal_vesting_fund_steem(total_vesting_fund_steem);
    transferHistoryModel.setTotal_vesting_shares(total_vesting_share);
    transferHistoryModel.setTimeStamp(timestamp);
    transferHistoryModel.setCurationReward(new TransferHistoryModel.CurationReward(
      meta_data_obj.getString(KEYS.KEY_CURATOR),
      meta_data_obj.getString(KEYS.KEY_REWARD),
      meta_data_obj.getString(KEYS.KEY_COMMENT_AUTHOR),
      meta_data_obj.optString(KEYS.KEY_COMMENT_PERMLINK, "")
    ));
    return transferHistoryModel;
  }

  private TransferHistoryModel parseCommentBenefactor(String user, String op, String timestamp, JSONObject meta_data_obj) throws JSONException {
    TransferHistoryModel transferHistoryModel = new TransferHistoryModel();
    transferHistoryModel.setOperation(op);
    transferHistoryModel.setUserAccount(user);
    transferHistoryModel.setTimeStamp(timestamp);
    transferHistoryModel.setTotal_vesting_fund_steem(total_vesting_fund_steem);
    transferHistoryModel.setTotal_vesting_shares(total_vesting_share);
    transferHistoryModel.setCommentBenefactor(new TransferHistoryModel.CommentBenefactor(
      meta_data_obj.getString(KEYS.KEY_BENEFACTOR),
      meta_data_obj.getString(KEYS.KEY_AUTHOR),
      meta_data_obj.getString(KEYS.KEY_PERMLINK),
      meta_data_obj.optString(KEYS.KEY_REWARD, "")
    ));
    return transferHistoryModel;
  }

  public static class KEYS {
    public static final String KEY_RESULT = "result";
    public static final String KEY_ACCOUNTS = "accounts";
    public static final String KEY_TRANSFER_HISTORY = "transfer_history";
    public static final String KEY_OPERATION = "op";
    public static final String KEY_TIMESTAMP = "timestamp";

    //operation: Transfer
    public static final String OPERATION_TRANSFER = "transfer";
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_MEMO = "memo";

    //operation: Author Reward
    public static final String OPERATION_AUTHOR_REWARD = "author_reward";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_PERMLINK = "permlink";
    public static final String KEY_SBD_PAYOUT = "sbd_payout";
    public static final String KEY_STEEM_PAYOUT = "steem_payout";
    public static final String KEY_VESTING_PAYOUT = "vesting_payout";

    //operation: Comment Benefactor Reward
    public static final String OPERATION_COMMENT_BENEFACTOR_REWARD = "comment_benefactor_reward";
    public static final String KEY_BENEFACTOR = "benefactor";
    public static final String KEY_REWARD = "reward";

    //operation: claim reward balance
    public static final String OPERATION_CLAIM_REWARD_BALANCE = "claim_reward_balance";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_REWARD_STEEM = "reward_steem";
    public static final String KEY_REWARD_SBD = "reward_sbd";
    public static final String KEY_REWARD_VESTS = "reward_vests";

    //operation : curation reward
    public static final String OPERATION_CURATION_REWARD = "curation_reward";
    public static final String KEY_CURATOR = "curator";
    public static final String KEY_COMMENT_AUTHOR = "comment_author";
    public static final String KEY_COMMENT_PERMLINK = "comment_permlink";

    public static final String KEY_PROPS = "props";
    public static final String KEY_TOTAL_VESTING_FUND_STEEM = "total_vesting_fund_steem";
    public static final String KEY_TOTAL_VESTING_SHARE = "total_vesting_shares";

  }
}
