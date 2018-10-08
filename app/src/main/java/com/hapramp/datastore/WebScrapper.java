package com.hapramp.datastore;

import android.os.Handler;

import com.hapramp.datastore.callbacks.ResourceCreditCallback;
import com.hapramp.models.ResourceCreditModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScrapper {
  public void scrapResourceCreditInfo(final String username, final ResourceCreditCallback resourceCreditCallback) {
    final Handler handler = new Handler();
    new Thread() {
      @Override
      public void run() {
        String url = "https://steemd.com/@" + username;
        final ResourceCreditModel resourceCreditModel = new ResourceCreditModel();
        Document doc = null;
        try {
          doc = Jsoup.connect(url).get();
          Elements newsHeadlines = doc.getElementsByTag("tr");
          for (Element e : newsHeadlines) {
            Elements ths = e.getElementsByTag("th");
            Elements tds = e.getElementsByTag("td");
            if (ths.size() > 0 && tds.size() > 0) {
              if (ths.text().equals("max_mana")) {
                resourceCreditModel.setMaxMana(tds.text());
              } else if (ths.text().equals("current_mana")) {
                resourceCreditModel.setCurrentMana(tds.text());
              } else if (ths.text().equals("current_pct")) {
                resourceCreditModel.setResourceCreditPercentage(tds.text());
              } else if (ths.text().equals("comments")) {
                resourceCreditModel.setCommentPrice(tds.text());
              } else if (ths.text().equals("votes")) {
                resourceCreditModel.setVotesPrice(tds.text());
              } else if (ths.text().equals("transfers/powerups")) {
                resourceCreditModel.setTransferPrice(tds.text());
              } else if (ths.text().equals("current_mana_pct")) {
                resourceCreditModel.setVotingPowerPercentage(tds.text());
              }
            }
          }
        }
        catch (final Exception e) {
          handler.post(
            new Runnable() {
              @Override
              public void run() {
                if (resourceCreditCallback != null) {
                  resourceCreditCallback.onResourceCreditError(e.toString());
                }
              }
            }
          );
        }
        handler.post(new Runnable() {
          @Override
          public void run() {
            if (resourceCreditCallback != null) {
              resourceCreditCallback.onResourceCreditAvailable(resourceCreditModel);
            }
          }
        });
      }
    }.start();
  }
}
