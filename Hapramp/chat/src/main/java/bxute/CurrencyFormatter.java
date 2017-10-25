package bxute;

import java.util.ArrayList;

/**
 * Created by Ankit on 10/18/2017.
 */

public class CurrencyFormatter {
    private static String[] singles = {
            "Zero", "One", "Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Eleven","Twelve","Thirteen","Fourteen","Fifteen","Sixteen","Seventeen","Eighteen","Nineteen","Twenty"
    };

    private static String[] doubles = {
            "Twenty","Thirty","Forty","Fifty","Sixty","Seventy","Eighty","Ninety"};


    private static boolean isSingles(int num) {
        return num <= 20;
    }

    private static String trimPreceedingZeros(String string){
        int ind = 0;
        while(string.charAt(ind)=='0'){
            ind++;
        }
        return string.substring(ind);
    }

    public static String convert(String num) {
        num = trimPreceedingZeros(num);
        int len = num.length();
        String word = "";
        if (len > 0) {
            if (len >= 1) {
                word += getTensConv(num.substring(len - 2));
                if (len > 2) {
                    //233
                    word = getHunderedConv(num.substring((len - 3), len - 2)) + word;
                    if (len > 3) {
                        if (len > 4) {
                            //34,567
                            word = getTensConv(num.substring((len - 5), len - 3)) + " Thousand " + word;
                        } else {
                            //4,567
                            word = getTensConv(num.substring((len - 4), len - 3)) + " Thousand " + word;
                        }
                        if (len > 5) {
                            if (len > 6) {
                                //12,12,122
                                word = getTensConv(num.substring((len - 7), len - 5)) + " Lakh " + word;
                            } else {
                                //2,12,122
                                word = getTensConv(num.substring((len - 6), len - 5)) + " Lakh " + word;
                            }
                        }
                        if (len > 7) {
                            if (len > 8) {
                                // 22,12,34,567
                                word = getTensConv(num.substring((len - 9), len - 7)) + " Crore " + word;
                            } else {
                                // 2,12,34,567
                                word = getTensConv(num.substring((len - 8), len - 7)) + " Crore " + word;
                            }

                        }

                    }
                }
            }
        }
        return word;
    }

    private static String getTensConv(String num) {
        int n = Integer.parseInt(num);
        if (n == 0)
            return "";
        if (isSingles(n)) {
            return singles[n]; // 01 - 20
        } else {    // 21 - 99
            return doubles[Integer.parseInt(num.substring(0, 1)) - 2] + " " + getTensConv(num.substring(1, 2));
        }

    }

    private static String getHunderedConv(String num) {
        int n = Integer.parseInt(num);
        if (n == 0)
            return "";

        return new StringBuilder()
                .append(singles[n])
                .append(" ")
                .append("Hundred ")
                .toString();
    }

}
