package hapramp.walletinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

  @Expose
  @SerializedName("user")
  private User user;

  public User getUser() {
    return user;
  }
  public static class User {
    @Expose
    @SerializedName("savings_sbd_balance")
    private String savings_sbd_balance;
    @Expose
    @SerializedName("sbd_balance")
    private String sbd_balance;
    @Expose
    @SerializedName("savings_balance")
    private String savings_balance;
    @Expose
    @SerializedName("balance")
    private String balance;
    @Expose
    @SerializedName("vesting_shares")
    private String vesting_share;

    public String getVesting_share() {
      return vesting_share;
    }

    public String getSavings_sbd_balance() {
      return savings_sbd_balance;
    }

    public String getSbd_balance() {
      return sbd_balance;
    }

    public String getSavings_balance() {
      return savings_balance;
    }

    public String getBalance() {
      return balance;
    }
  }
}
