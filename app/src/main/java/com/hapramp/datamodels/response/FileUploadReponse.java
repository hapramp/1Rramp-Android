package com.hapramp.datamodels.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 5/14/2018.
 */

public class FileUploadReponse {
  @Expose
  @SerializedName("url")
  private String downloadUrl;

  @Expose
  @SerializedName("error")
  private String error;

  public FileUploadReponse(String downloadUrl, String error) {
    this.downloadUrl = downloadUrl;
    this.error = error;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
