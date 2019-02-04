package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class JudgeModel implements Parcelable {
  public static final Creator<JudgeModel> CREATOR = new Creator<JudgeModel>() {
    @Override
    public JudgeModel createFromParcel(Parcel source) {
      return new JudgeModel(source);
    }

    @Override
    public JudgeModel[] newArray(int size) {
      return new JudgeModel[size];
    }
  };
  @Expose
  @SerializedName("username")
  private String mUsername;
  private boolean isSelected;

  public JudgeModel() {
  }

  protected JudgeModel(Parcel in) {
    this.mUsername = in.readString();
    this.isSelected = in.readByte() != 0;
  }

  public static ArrayList<JudgeModel> getJudgeModelsFrom(ArrayList<String> usernames) {
    ArrayList<JudgeModel> judgeModels = new ArrayList<>();
    for (int i = 0; i < usernames.size(); i++) {
      JudgeModel judgeModel = new JudgeModel();
      judgeModel.setmUsername(usernames.get(i));
      judgeModels.add(judgeModel);
    }
    return judgeModels;
  }

  public static List<String> getJudgesStringArrayListOf(@NonNull ArrayList<JudgeModel> usernames) {
    List<String> stringList = new ArrayList<>();
    for (int i = 0; i < usernames.size(); i++) {
      stringList.add(usernames.get(i).getmUsername());
    }
    return stringList;
  }

  public String getmUsername() {
    return mUsername;
  }

  public void setmUsername(String mUsername) {
    this.mUsername = mUsername;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mUsername);
    dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
  }
}
