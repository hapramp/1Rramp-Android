package com.hapramp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ankit on 11/12/2017.
 */

public class Validator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static boolean validateUsername(String username){
        return username.trim().length()>2;
    }

    public static boolean validatePassword(String password){
        return password.trim().length()>5;
    }

}
