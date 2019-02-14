package com.hapramp.utils;

import android.os.CountDownTimer;

import java.util.Locale;

public class CountDownTimerUtils {
  final static long MILLIS_IN_DAY = 86400000;
  final static long MILLIS_IN_HOUR = 3600000;
  final static long MILLIS_IN_MIN = 60000;
  final static long MILLIS_IN_SEC = 1000;
  private CountDownTimer timer;

  public void setTimerWith(int targetId, long finishTime, long tickInterval, final TimerUpdateListener timerUpdateListener) {
    timer = new CountDownTimer(finishTime, tickInterval) {
      public void onTick(long millisLeft) {
        if (timerUpdateListener != null) {
          timerUpdateListener.onRunningTimeUpdate(targetId, getDescriptiveCountdown(millisLeft));
        }
      }

      public void onFinish() {
        if (timerUpdateListener != null) {
          timerUpdateListener.onFinished(targetId);
        }
      }
    };
  }

  public static String getDescriptiveCountdown(long millisLeft) {
    StringBuilder builder = new StringBuilder();
    long day, hour, min, sec;
    long consumed = 0;
    if (millisLeft >= MILLIS_IN_DAY) {

      day = millisLeft / MILLIS_IN_DAY;
      consumed = day * MILLIS_IN_DAY;

      hour = (millisLeft - consumed) / MILLIS_IN_HOUR;
      consumed += hour * MILLIS_IN_HOUR;

      min = (millisLeft - consumed) / MILLIS_IN_MIN;
      consumed += min * MILLIS_IN_MIN;

      sec = (millisLeft - consumed) / MILLIS_IN_SEC;

      builder
        .append(String.format(Locale.US, "%02d : ", day))
        .append(String.format(Locale.US, "%02d : ", hour))
        .append(String.format(Locale.US, "%02d : ", min))
        .append(String.format(Locale.US, "%02d", sec));

    } else if (millisLeft >= MILLIS_IN_HOUR) {

      hour = (millisLeft - consumed) / MILLIS_IN_HOUR;
      consumed = hour * MILLIS_IN_HOUR;
      min = (millisLeft - consumed) / MILLIS_IN_MIN;
      consumed += min * MILLIS_IN_MIN;
      sec = (millisLeft - consumed) / MILLIS_IN_SEC;

      builder
        .append(String.format(Locale.US, "%02d Hr. ", hour))
        .append(String.format(Locale.US, "%02d Min ", min))
        .append(String.format(Locale.US, "%02d Sec ", sec));

    } else if (millisLeft >= MILLIS_IN_MIN) {

      min = (millisLeft - consumed) / MILLIS_IN_MIN;
      consumed = min * MILLIS_IN_MIN;
      sec = (millisLeft - consumed) / MILLIS_IN_SEC;

      builder
        .append(String.format(Locale.US, "%02d Min ", min))
        .append(String.format(Locale.US, "%02d Sec ", sec));

    } else if (millisLeft >= MILLIS_IN_SEC) {

      sec = millisLeft / MILLIS_IN_SEC;

      builder
        .append(String.format(Locale.US, "%02d Second", sec));
    }
    return builder.toString();
  }

  public void start() {
    if (timer != null) {
      timer.start();
    }
  }

  public void cancel() {
    if (timer != null) {
      timer.cancel();
    }
  }

  public interface TimerUpdateListener {
    void onFinished(int targetId);

    void onRunningTimeUpdate(int targetId, String updateTime);
  }
}
