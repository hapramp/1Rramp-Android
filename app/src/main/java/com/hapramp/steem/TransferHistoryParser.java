package com.hapramp.steem;

import com.hapramp.search.TranserHistoryManager;
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
        .getJSONObject(TranserHistoryManager.KEYS.KEY_RESULT)
        .getJSONObject(TranserHistoryManager.KEYS.KEY_PROPS);

      total_vesting_fund_steem = props.getString(TranserHistoryManager.KEYS.KEY_TOTAL_VESTING_FUND_STEEM);
      total_vesting_share = props.getString(TranserHistoryManager.KEYS.KEY_TOTAL_VESTING_SHARE);

      JSONArray transfer_history = root
        .getJSONObject(TranserHistoryManager.KEYS.KEY_RESULT)
        .getJSONObject(TranserHistoryManager.KEYS.KEY_ACCOUNTS)
        .getJSONObject(user)
        .getJSONArray(TranserHistoryManager.KEYS.KEY_TRANSFER_HISTORY);

      for (int i = 0; i < transfer_history.length(); i++) {
        JSONArray _history_item = transfer_history.getJSONArray(i);
        JSONObject transfer_obj = _history_item.getJSONObject(1);
        JSONArray operationArray = transfer_obj.getJSONArray(TranserHistoryManager.KEYS.KEY_OPERATION);
        String timestamp = transfer_obj.getString(TranserHistoryManager.KEYS.KEY_TIMESTAMP);
        JSONObject meta_data_obj = operationArray.getJSONObject(1);
        String operation = operationArray.getString(0);
        //skipping extra data
        if (operation.equals(TranserHistoryManager.KEYS.OPERATION_CLAIM_REWARD_BALANCE) ||
          operation.equals(TranserHistoryManager.KEYS.OPERATION_AUTHOR_REWARD) ||
          operation.equals(TranserHistoryManager.KEYS.OPERATION_TRANSFER) ||
          operation.equals(TranserHistoryManager.KEYS.OPERATION_CURATION_REWARD)) {
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
        case TranserHistoryManager.KEYS.OPERATION_TRANSFER:
          return parseTransfer(user, operation, timestamp, meta_data_obj);
        case TranserHistoryManager.KEYS.OPERATION_AUTHOR_REWARD:
          return parseAuthorReward(user, operation, timestamp, meta_data_obj);
        case TranserHistoryManager.KEYS.OPERATION_CLAIM_REWARD_BALANCE:
          return parseClaimRewardBalance(user, operation, timestamp, meta_data_obj);
          /*			case TranserHistoryManager.KEYS.OPERATION_COMMENT_BENEFACTOR_REWARD:
                    return parseCommentBenefactor(user, operation, timestamp, meta_data_obj);*/
        case TranserHistoryManager.KEYS.OPERATION_CURATION_REWARD:
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
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_FROM),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_TO),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_AMOUNT),
      meta_data_obj.optString(TranserHistoryManager.KEYS.KEY_MEMO, "")
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
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_AUTHOR),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_PERMLINK),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_SBD_PAYOUT),
      meta_data_obj.optString(TranserHistoryManager.KEYS.KEY_STEEM_PAYOUT, ""),
      meta_data_obj.optString(TranserHistoryManager.KEYS.KEY_VESTING_PAYOUT)
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
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_ACCOUNT),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_REWARD_STEEM),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_REWARD_SBD),
      meta_data_obj.optString(TranserHistoryManager.KEYS.KEY_REWARD_VESTS, "")
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
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_CURATOR),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_REWARD),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_COMMENT_AUTHOR),
      meta_data_obj.optString(TranserHistoryManager.KEYS.KEY_COMMENT_PERMLINK, "")
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
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_BENEFACTOR),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_AUTHOR),
      meta_data_obj.getString(TranserHistoryManager.KEYS.KEY_PERMLINK),
      meta_data_obj.optString(TranserHistoryManager.KEYS.KEY_REWARD, "")
    ));
    return transferHistoryModel;
  }
}
