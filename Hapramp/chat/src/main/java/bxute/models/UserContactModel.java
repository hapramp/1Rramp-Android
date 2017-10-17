package bxute.models;

/**
 * Created by Ankit on 10/17/2017.
 */

public class UserContactModel {

    private String userId;
    private String displayName;
    private String dpUrl;
    private String email;
    private String joinedOn;

    public UserContactModel(String userId, String displayName, String dpUrl, String email, String joinedOn) {
        this.userId = userId;
        this.displayName = displayName;
        this.dpUrl = dpUrl;
        this.email = email;
        this.joinedOn = joinedOn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoinedOn() {
        return joinedOn;
    }

    public void setJoinedOn(String joinedOn) {
        this.joinedOn = joinedOn;
    }

    @Override
    public String toString() {
        return "UserContactModel{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dpUrl='" + dpUrl + '\'' +
                ", email='" + email + '\'' +
                ", joinedOn='" + joinedOn + '\'' +
                '}';
    }
}
