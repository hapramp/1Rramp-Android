package com.hapramp.utils;

import java.util.List;
import java.util.Locale;

public class PrizeMoneyFilter {
  public static String getTotalPrize(List<String> prizes) {
    try {
      String totalPrize = "";
      double totalSBD = 0;
      if (prizes.size() > 0) {
        for (int i = 0; i < prizes.size(); i++) {
          String[] splitted = prizes.get(i).split(" ");
          if (splitted.length > 1) {
            if (splitted[1].toLowerCase().equals("sbd")) {
              totalSBD += Double.valueOf(splitted[0]);
            } else {
              return "Multiple prizes";
            }
          } else {
            return "Multiple prizes";
          }
        }
        if (totalSBD == 0) {
          totalPrize = prizes.get(0);
        } else {
          totalPrize = String.format(Locale.US, "%.2f SBD", totalSBD);
        }
      } else {
        totalPrize = "No Prize";
      }
      return totalPrize;
    }
    catch (Exception e) {
      return "";
    }
  }
}
