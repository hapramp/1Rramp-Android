package bxute;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Ankit on 10/13/2017.
 */

public class LocalTimeFormatter {
    private int DIFF_TYPE_TIME = 1;
    private int DIFF_TYPE_DAY = 2;
    private int DIFF_TYPE_MONTH = 3;
    private int DIFF_TYPE_YEAR = 4;

    private int YEAR = 0;
    private int MONTH = 1;
    private int DAY = 2;
    private int HOUR = 3;
    private int MINUTE = 4;
    private int SECOND = 5;
    private int AM_PM = 6;

    public static int FORMAT_CHAT_WALL = 12;
    public static int FORMAT_TOOLBAR = 13;
    public static int FORMAT_MESSAGE_TIP = 14;

    private String currentTimeStamp;
    private String initialTimeStamp;
    private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private String[] am_pm = {"AM","PM"};
    private ArrayList<String> current;
    private ArrayList<String> initial;

    public LocalTimeFormatter() {

    }

    public String getFormattedTimeStamp(String timestamp,int format) {

        this.initialTimeStamp = timestamp;
        this.currentTimeStamp = getDateTime();

        initial = getParts(initialTimeStamp, ':');
        current = getParts(currentTimeStamp, ':');

        if (format == FORMAT_CHAT_WALL) {
            return formatForChatWall();
        }
        if (format == FORMAT_MESSAGE_TIP) {
            return formatForMessageTip();
        }
        if (format == FORMAT_TOOLBAR) {
            return formatForToolbar();
        }
        return "Unknown";
    }

    private String formatForMessageTip() {

        int dayDiff = getDayDiff();

        if (dayDiff == 0) {
            return getTime();
        }
        if (dayDiff == 1) {
            return "Yes " + getTime();
        }
        if (dayDiff > 1 && dayDiff < 7) {
            return getDayName() + " At " + getTime();
        }
        if (dayDiff >= 7 && dayDiff <= 30) {
            return initial.get(DAY) + "." + initial.get(MONTH) + " At " + getTime();
        }
        if (dayDiff > 30 && dayDiff <= 365) {
            return initial.get(DAY) + "." + initial.get(MONTH) + "." + initial.get(YEAR) + " At " + getTime();
        }

        return "";

    }

    private String formatForToolbar() {

        int dayDiff = getDayDiff();

        if (dayDiff == 0) {
            return "Today At " + getTime();
        }
        if (dayDiff == 1) {
            return "Yesterday At " + getTime();
        }
        if (dayDiff > 1 && dayDiff < 7) {
            return getDayName() + " At " + getTime();
        }
        if (dayDiff >= 7 && dayDiff <= 30) {
            return initial.get(DAY) + "." + initial.get(MONTH) + " At " + getTime();
        }
        if (dayDiff > 30 && dayDiff <= 365) {
            return initial.get(DAY) + "." + initial.get(MONTH) + "." + initial.get(YEAR) + " At " + getTime();
        }

        return "";
    }

    private String formatForChatWall() {

        int dayDiff = getDayDiff();

        if (dayDiff == 0) {
            return getTime();
        }
        if (dayDiff == 1) {
            return "Yes " + getTime();
        }
        if (dayDiff > 1 && dayDiff < 7) {
            return getDayName();
        }
        if (dayDiff >= 7 && dayDiff <= 30) {
            return initial.get(DAY) + "." + initial.get(MONTH);
        }
        if (dayDiff > 30 && dayDiff <= 365) {
            return initial.get(DAY) + "." + initial.get(MONTH) + "." + initial.get(YEAR);
        }
        return "";
    }

    private String getDayName() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(initial.get(YEAR)), Integer.parseInt(initial.get(MONTH)), Integer.parseInt(initial.get(DAY)));
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1];

    }

    private String getTime() {

        // Log.d("TimeFormatter"," hour "+initial.get(HOUR));
        String hour = (Integer.parseInt(initial.get(HOUR))==0)?"12":((initial.get(HOUR).length()==1)?("0"+initial.get(HOUR)):initial.get(HOUR));
        String min = ((initial.get(MINUTE).length()==1)?("0"+initial.get(MINUTE)):initial.get(MINUTE));
        return hour+":"+min+" "+am_pm[Integer.parseInt(initial.get(AM_PM))];

    }

    private int getDayDiff() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Integer.parseInt(initial.get(YEAR)), Integer.parseInt(initial.get(MONTH)), Integer.parseInt(initial.get(DAY)));
        int initialDayNumber = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.set(Integer.parseInt(current.get(YEAR)), Integer.parseInt(current.get(MONTH)), Integer.parseInt(current.get(DAY)));
        int currentDayNumber = calendar.get(Calendar.DAY_OF_YEAR);
        // Log.d("Formatter", "curr DNO " + currentDayNumber + " ini DNO " + initialDayNumber);
        return currentDayNumber - initialDayNumber;
    }

    private ArrayList<String> getParts(String str, char seperator) {
        ArrayList<String> all = new ArrayList<>();
        getAll(str, seperator, all);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).equals("")) all.remove(i);
        }
        return all;
    }

    private String getAll(String str, char seperator, ArrayList<String> list) {
        if (str.indexOf(seperator) == -1) {
            list.add(str);
            return str;
        } else {
            list.add(str.substring(0, str.indexOf(seperator)));
            return getAll(str.substring(str.indexOf(seperator) + 1), seperator,
                    list);
        }
    }


    private String getDateTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        return calendar.get(Calendar.YEAR) + ":" + calendar.get(Calendar.MONTH) + ":" + calendar.get(Calendar.DAY_OF_MONTH) + ":" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)+":"+calendar.get(Calendar.AM_PM);

    }
}

