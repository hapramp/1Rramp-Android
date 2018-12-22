package com.hapramp.utils;

import java.util.List;
import java.util.Locale;

public class PrizeMoneyFilter {

  public static String getTotalPrize(List<String> prizes) {
    String mixedPrizes = "Multiple prizes";
    String unit = "";
    double totalSum = 0;
    for (int i = 0; i < prizes.size(); i++) {
      String[] item = prizes.get(i).split(" ");
      if (item.length == 2) {
        try{
          if(unit.length()==0){
            //assign the first unit
            unit = item[1].toUpperCase();
            totalSum += Double.valueOf(item[0]);
          }else{
            //unit is already known
            if(unit.equals(item[1].toUpperCase())){
              //add prize amount
              totalSum += Double.valueOf(item[0]);
            }else{
              // there is mixed prizes
              return mixedPrizes;
            }
          }
        }catch (Exception e){
          return mixedPrizes;
        }
      }else{
        return mixedPrizes;
      }
    }
    return String.format(Locale.US, "%.2f %s", totalSum, unit);
  }

}
