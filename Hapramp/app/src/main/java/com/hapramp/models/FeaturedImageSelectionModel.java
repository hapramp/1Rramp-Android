package com.hapramp.models;

/**
 * Created by Ankit on 2/3/2018.
 */

public class FeaturedImageSelectionModel {

    public boolean isSelected;
    public String url;

    public FeaturedImageSelectionModel(boolean isSelected, String url) {
        this.isSelected = isSelected;
        this.url = url;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FeaturedImageSelectionModel{" +
                "isSelected=" + isSelected +
                ", url='" + url + '\'' +
                '}';
    }

}
