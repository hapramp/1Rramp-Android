package com.hapramp.utils;

import com.hapramp.R;

public class CommunityUtils {
  //images
  public static final int FEED_ICON = R.drawable.feed_filled;
  public static final int ART_ICON = R.drawable.art_filled;
  public static final int DANCE_ICON = R.drawable.dance_filled;
  public static final int TRAVEL_ICON = R.drawable.travel;
  public static final int LITERATURE_ICON = R.drawable.literature_filled;
  public static final int FILM_ICON = R.drawable.film_filled;
  public static final int PHOTOGRAPHY_ICON = R.drawable.photography_filled;
  public static final int MUSIC_ICON = R.drawable.music_filled;
  public static final int FASHION_ICON = R.drawable.fashion_filled;
  public static final int DESIGN_ICON = R.drawable.design_filled;
  public static final int EDIT_COMMUNITY_ICON = R.drawable.plus;
  //backgrounds
  public static final int FEED_FILLED_BG = R.drawable.feed_filled_bg;
  public static final int ART_FILLED_BG = R.drawable.art_filled_bg;
  public static final int DANCE_FILLED_BG = R.drawable.dance_filled_bg;
  public static final int TRAVEL_FILLED_BG = R.drawable.travel_filled_bg;
  public static final int LITERATURE_FILLED_BG = R.drawable.literature_filled_bg;
  public static final int FILM_FILLED_BG = R.drawable.film_filled_bg;
  public static final int PHOTOGRAPHY_FILLED_BG = R.drawable.photography_filled_bg;
  public static final int MUSIC_FILLED_BG = R.drawable.music_filled_bg;
  public static final int FASHION_FILLED_BG = R.drawable.fashion_filled_bg;
  public static final int DESIGN_FILLED_BG = R.drawable.design_filled_bg;
  //borders
  public static final int BLACK_BORDER = R.drawable.black_border;
  public static final int FEED_BORDER = R.drawable.feed_border;
  public static final int ART_BORDER = R.drawable.art_border;
  public static final int DANCE_BORDER = R.drawable.dance_border;
  public static final int TRAVEL_BORDER = R.drawable.travel_border;
  public static final int LITERATURE_BORDER = R.drawable.literature_border;
  public static final int FILM_BORDER = R.drawable.film_border;
  public static final int PHOTOGRAPHY_BORDER = R.drawable.photography_border;
  public static final int MUSIC_BORDER = R.drawable.music_border;
  public static final int FASHION_BORDER = R.drawable.fashion_border;
  public static final int DESIGN_BORDER = R.drawable.design_border;
  public static final int EDIT_BORDER = R.drawable.edit_community_border;

  public static int getBorder(int com_id) {
    return BLACK_BORDER;
  }

  public static int getFilledBackground(int com_id) {
    switch (com_id) {
      case 0:
        return FEED_FILLED_BG;
      case 1:
        return ART_FILLED_BG;
      case 2:
        return DANCE_FILLED_BG;
      case 3:
        return TRAVEL_FILLED_BG;
      case 4:
        return LITERATURE_FILLED_BG;
      case 5:
        return FILM_FILLED_BG;
      case 6:
        return PHOTOGRAPHY_FILLED_BG;
      case 7:
        return FASHION_FILLED_BG;
      case 8:
        return DESIGN_FILLED_BG;
      case 404:
        return EDIT_BORDER;
    }
    return -1;
  }

  public static int getCommunityIcon(int com_id) {
    switch (com_id) {
      case 0:
        return FEED_ICON;
      case 1:
        return ART_ICON;
      case 2:
        return DANCE_ICON;
      case 3:
        return TRAVEL_ICON;
      case 4:
        return LITERATURE_ICON;
      case 5:
        return FILM_ICON;
      case 6:
        return PHOTOGRAPHY_ICON;
      case 7:
        return FASHION_ICON;
      case 8:
        return DESIGN_ICON;
      case 404:
        return EDIT_COMMUNITY_ICON;
    }
    return -1;
  }

}
