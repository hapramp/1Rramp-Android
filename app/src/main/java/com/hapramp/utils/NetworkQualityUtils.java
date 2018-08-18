package com.hapramp.utils;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;

public class NetworkQualityUtils {
  public static void startNetworkSampling() {
    DeviceBandwidthSampler.getInstance().startSampling();
  }

  public static void stopstartNetworkSampling() {
    DeviceBandwidthSampler.getInstance().stopSampling();
    ConnectionQuality cq = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
    ImageLoadQualityDecider.invalidateImageLoadQuality(cq);
  }
  // UNKNOWN - not known
  //  POOR - under 18 KBps
  //  MODERATE -between 18 and 70 KBps
  //  GOOD - between 70 and 250 KBps.
  //  EXCELLENT - Bandwidth over 250 KBps.
}
