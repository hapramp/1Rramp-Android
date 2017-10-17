package bxute.config;

import bxute.models.UserContactModel;

/**
 * Created by Ankit on 9/9/2017.
 */

public class UserPreference {
    private UserContactModel myContact;

    public void setMyContact(UserContactModel myContact){
        this.myContact = myContact;
    }

    public UserContactModel getMyContact(){
        return myContact;
    }

    public static String getUserId(){
        return "ank123";
    }

}
