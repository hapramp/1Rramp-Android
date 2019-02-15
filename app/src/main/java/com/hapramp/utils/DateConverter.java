package com.hapramp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {

  static DateConverter dateConverter;

  String dateTtimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  String datePattern = "yyyy-MM-dd";
  String timePattern = "HH:mm:ss.SSS'Z'";

  public static DateConverter getInstance() {
    if (dateConverter == null) {
      dateConverter = new DateConverter();
    }
    return dateConverter;
  }

  public String toLocalTime(String gmtTime) {
    DateFormat dateFormat = new SimpleDateFormat(dateTtimePattern);
    TimeZone timeZone = TimeZone.getTimeZone("GMT");
    dateFormat.setTimeZone(timeZone);
    //obtain gmt dateTime
    try {
      Date gmtDateTime = dateFormat.parse(gmtTime);
      //convert to local
      TimeZone localTimeZone = TimeZone.getDefault();
      dateFormat.setTimeZone(localTimeZone);
      return dateFormat.format(gmtDateTime);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    return gmtTime;
  }

  public String toGmt(String localTime) {
    try {
      DateFormat dateFormat = new SimpleDateFormat(dateTtimePattern);
      //obtain localDateTime
      Date localDateTime = dateFormat.parse(localTime);
      //prepate for gmt calculation
      TimeZone timeZone = TimeZone.getTimeZone("GMT");
      dateFormat.setTimeZone(timeZone);
      return dateFormat.format(localDateTime);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    return localTime;
  }

  public String getTimeFrom(String dateTime, TimeZone timeZone) {
    try {
      DateFormat dateFormat = new SimpleDateFormat(dateTtimePattern);
      dateFormat.setTimeZone(timeZone);
      Date localDateTime = dateFormat.parse(dateTime);
      DateFormat timeFormat = new SimpleDateFormat(timePattern);
      timeFormat.setTimeZone(timeZone);
      return timeFormat.format(localDateTime);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String getDateFrom(String dateTime, TimeZone timeZone) {
    try {
      DateFormat dateFormat = new SimpleDateFormat(dateTtimePattern);
      dateFormat.setTimeZone(timeZone);
      Date localDateTime = dateFormat.parse(dateTime);
      DateFormat timeFormat = new SimpleDateFormat(datePattern);
      timeFormat.setTimeZone(timeZone);
      return timeFormat.format(localDateTime);
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    return "";
  }
}
