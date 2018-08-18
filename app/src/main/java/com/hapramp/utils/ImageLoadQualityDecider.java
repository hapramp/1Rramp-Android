package com.hapramp.utils;

import android.util.Log;

import com.facebook.network.connectionclass.ConnectionQuality;
import com.hapramp.preferences.HaprampPreferenceManager;

public class ImageLoadQualityDecider {
  public static void invalidateImageLoadQuality(ConnectionQuality cq) {
    Log.d("NetworkQuality", cq.toString());
    int downGradeFactor = 1;
    switch (cq) {
      case POOR:
        // 3 times lower res.
        downGradeFactor = 4;
        break;
      case MODERATE:
        // 2 times lower res
        downGradeFactor = 3;
        break;
      case GOOD:
        // 1.5 times lower res
        downGradeFactor = 2;
        break;
      case EXCELLENT:
        // original res
        downGradeFactor = 1;
        break;
      default:
        downGradeFactor = 2;
    }
    Log.d("NetworkQuality", "set image downgrade factor " + downGradeFactor);
    HaprampPreferenceManager.getInstance().setImageLoaddownGradeFactor(downGradeFactor);
  }
  // UNKNOWN - not known
  //  POOR - under 18 KBps
  //  MODERATE -between 18 and 70 KBps
  //  GOOD - between 70 and 250 KBps.
  //  EXCELLENT - Bandwidth over 250 KBps.
}
