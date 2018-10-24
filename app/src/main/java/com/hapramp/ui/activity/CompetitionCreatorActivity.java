package com.hapramp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datastore.UrlBuilder;
import com.hapramp.models.CompetitionCreateResponse;
import com.hapramp.models.JudgeModel;
import com.hapramp.models.error.ErrorResponse;
import com.hapramp.models.requests.CompetitionCreateBody;
import com.hapramp.utils.CommunityUtils;
import com.hapramp.utils.ErrorUtils;
import com.hapramp.utils.GoogleImageFilePathReader;
import com.hapramp.views.JudgeSelectionView;
import com.hapramp.views.hashtag.CustomHashTagInput;
import com.hapramp.views.post.PostCommunityView;
import com.hapramp.views.post.PostImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hapramp.ui.activity.JudgeSelectionActivity.EXTRA_SELECTED_JUDGES;

public class CompetitionCreatorActivity extends AppCompatActivity implements JudgeSelectionView.JudgeSelectionCallback {

  private static final int REQUEST_IMAGE_SELECTOR = 1039;
  private static final int REQUEST_JUDGE_SELECTOR = 1037;

  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.toolbar_title)
  TextView toolbarTitle;
  @BindView(R.id.nextButton)
  TextView nextButton;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.competition_title)
  EditText competitionTitle;
  @BindView(R.id.competition_description)
  EditText competitionDescription;
  @BindView(R.id.competition_rules)
  EditText competitionRules;
  @BindView(R.id.backBtnFromCompetionMeta)
  ImageView backBtnFromCompetionMeta;
  @BindView(R.id.publishButton)
  TextView publishButton;
  @BindView(R.id.meta_toolbar_container)
  RelativeLayout metaToolbarContainer;
  @BindView(R.id.community_caption)
  TextView communityCaption;
  @BindView(R.id.competitionCommunityView)
  PostCommunityView competitionCommunityView;
  @BindView(R.id.tagsCaption)
  TextView tagsCaption;
  @BindView(R.id.tagsInputBox)
  CustomHashTagInput tagsInputBox;
  @BindView(R.id.timingCaption)
  TextView timingCaption;
  @BindView(R.id.starts_on_caption)
  TextView startsOnCaption;
  @BindView(R.id.start_time_input)
  EditText startTimeInput;
  @BindView(R.id.start_clock_icon)
  ImageView startClockIcon;
  @BindView(R.id.start_date_input)
  EditText startDateInput;
  @BindView(R.id.start_date_icon)
  ImageView startDateIcon;
  @BindView(R.id.start_timing_container)
  RelativeLayout startTimingContainer;
  @BindView(R.id.ends_on_caption)
  TextView endsOnCaption;
  @BindView(R.id.end_time_input)
  EditText endTimeInput;
  @BindView(R.id.end_clock_icon)
  ImageView endClockIcon;
  @BindView(R.id.end_date_input)
  EditText endDateInput;
  @BindView(R.id.end_date_icon)
  ImageView endDateIcon;
  @BindView(R.id.end_timing_container)
  RelativeLayout endTimingContainer;
  @BindView(R.id.prizeCaption)
  TextView prizeCaption;
  @BindView(R.id.first_prize_caption)
  TextView firstPrizeCaption;
  @BindView(R.id.first_prize_input)
  EditText firstPrizeInput;
  @BindView(R.id.second_prize_caption)
  TextView secondPrizeCaption;
  @BindView(R.id.second_prize_input)
  EditText secondPrizeInput;
  @BindView(R.id.third_prize_caption)
  TextView thirdPrizeCaption;
  @BindView(R.id.third_prize_input)
  EditText thirdPrizeInput;
  @BindView(R.id.prizes_container)
  RelativeLayout prizesContainer;
  @BindView(R.id.bannerCaption)
  TextView bannerCaption;
  @BindView(R.id.choose_banner_image_btn)
  TextView chooseBannerImageButton;
  @BindView(R.id.competition_banner)
  PostImageView competitionBanner;
  @BindView(R.id.skills_wrapper)
  RelativeLayout skillsWrapper;
  @BindView(R.id.metaView)
  RelativeLayout metaView;
  @BindView(R.id.judge_selector)
  JudgeSelectionView judgeSelector;
  private boolean isBannerSelected;
  private String bannerImageDownloadUrl = "";
  private ArrayList<JudgeModel> selectedJudges;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_competition_editor);
    ButterKnife.bind(this);
    initialize();
    attachListeners();
  }

  private void initialize() {
    progressDialog = new ProgressDialog(this);
    selectedJudges = new ArrayList<>();
    competitionCommunityView.initCategory();
    competitionBanner.setImageActionListener(new PostImageView.ImageActionListener() {
      @Override
      public void onImageRemoved() {
        isBannerSelected = false;
        bannerImageDownloadUrl = "";
      }

      @Override
      public void onImageUploaded(String downloadUrl) {
        bannerImageDownloadUrl = downloadUrl;
      }
    });
  }

  private void attachListeners() {
    nextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showMetaView(true);
      }
    });
    backBtnFromCompetionMeta.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showMetaView(false);
      }
    });

    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        close();
      }
    });

    publishButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (validateFields()) {
          prepareCompetition();
        }
      }
    });

    startTimeInput.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showTimePicker("Select competition start time", startTimeInput);
      }
    });
    startClockIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showTimePicker("Select competition start time", startTimeInput);
      }
    });

    startDateInput.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePicker("Select competition start date", startDateInput);
      }
    });
    startDateIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePicker("Select competition start date", startDateInput);
      }
    });

    endTimeInput.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showTimePicker("Select competition end time", endTimeInput);
      }
    });
    endClockIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showTimePicker("Select competition end time", endTimeInput);
      }
    });

    endDateInput.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePicker("Select competition end date", endDateInput);
      }
    });
    endDateIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePicker("Select competition end date", endDateInput);
      }
    });

    chooseBannerImageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openGallery();
      }
    });

    judgeSelector.setJudgeSelectionCallback(this);

  }

  private void showMetaView(boolean show) {
    if (metaView != null) {
      if (show) {
        metaView.setVisibility(View.VISIBLE);
      } else {
        metaView.setVisibility(View.GONE);
      }
    }
  }

  private void close() {
    finish();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
  }

  private boolean validateFields() {
    if (competitionTitle.getText().toString().trim().length() == 0) {
      toast("Title required!");
      return false;
    }
    if (competitionDescription.getText().toString().trim().length() == 0) {
      toast("Competition description required!");
      return false;
    }
    if (competitionCommunityView.getSelectedTags().size() <= 1) {
      toast("Atleast 1 community required!");
      return false;
    }

    if (startTimeInput.getText().length() == 0) {
      toast("Select competition start time!");
      return false;
    }

    if (startDateInput.getText().length() == 0) {
      toast("Select competition start date!");
      return false;
    }

    if (endTimeInput.getText().length() == 0) {
      toast("Select competition end time!");
      return false;
    }

    if (endDateInput.getText().length() == 0) {
      toast("Select competition end date!");
      return false;
    }

    if (firstPrizeInput.getText().toString().trim().length() == 0) {
      toast("First prize is required!");
      return false;
    }

    if (isBannerSelected) {
      if (bannerImageDownloadUrl.length() == 0) {
        toast("Still uploading banner image. Please wait...");
        return false;
      }
    } else {
      toast("Select competition banner image.");
      return false;
    }

    if (selectedJudges.size() == 0) {
      toast("Select Atleast 1 Judge.");
      return false;
    }

    return true;
  }

  private void prepareCompetition() {
    showPublishingProgressDialog(true, "Creating Competition...");
    CompetitionCreateBody competitionCreateBody = new CompetitionCreateBody();
    competitionCreateBody.setmImage(bannerImageDownloadUrl);
    competitionCreateBody.setmTitle(competitionTitle.getText().toString().trim());
    competitionCreateBody.setmDescription(competitionDescription.getText().toString().trim());
    competitionCreateBody.setmStartsAt(getStartTime());
    competitionCreateBody.setmEndsAt(getEndTime());
    competitionCreateBody.setmRules(competitionRules.getText().toString());
    competitionCreateBody.setmJudges(getSelectedJudgesIds());
    competitionCreateBody.setmCommunities(getSelectedCommunityIds());
    competitionCreateBody.setmPrizes(getPrizes());
    Log.d("CompetitionCreate", new Gson().toJson(competitionCreateBody));
    createCompetition(competitionCreateBody);
  }

  private void showTimePicker(String msg, final EditText targetInput) {
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    mTimePicker = new TimePickerDialog(CompetitionCreatorActivity.this, new TimePickerDialog.OnTimeSetListener() {
      @Override
      public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        if (targetInput != null) {
          targetInput.setText(String.format(Locale.US, "%02d:%02d:00.000Z", selectedHour, selectedMinute));
        }
      }
    }, hour, minute, true);//Yes 24 hour time
    mTimePicker.setTitle(msg);
    mTimePicker.show();
  }

  private void showDatePicker(String msg, final EditText targetInput) {
    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
      new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
          if (targetInput != null) {
            targetInput.setText(String.format(Locale.US, "%02d-%02d-%02d", year, 1 + monthOfYear, dayOfMonth));
          }
        }
      }, mYear, mMonth, mDay);
    datePickerDialog.setMessage(msg);
    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    datePickerDialog.show();
  }

  private void openGallery() {
    try {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ||
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
      } else {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  private void showPublishingProgressDialog(boolean show, String msg) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
      } else {
        progressDialog.hide();
      }
    }
  }

  private String getStartTime() {
    return String.format("%sT%s", startDateInput.getText().toString(), startTimeInput.getText().toString());
  }

  private String getEndTime() {
    return String.format("%sT%s", endDateInput.getText().toString(), endTimeInput.getText().toString());
  }

  private List<Integer> getSelectedJudgesIds() {
    List<Integer> sjs = new ArrayList<>();
    for (int i = 0; i < selectedJudges.size(); i++) {
      sjs.add(selectedJudges.get(i).getmId());
    }
    return sjs;
  }

  private List<Integer> getSelectedCommunityIds() {
    List<String> communities = competitionCommunityView.getSelectedTags();
    List<Integer> ids = new ArrayList<>();
    for (int i = 0; i < communities.size(); i++) {
      int _id = CommunityUtils.getCommunityIdFromTitle(communities.get(i));
      if (_id >= 0) {
        ids.add(CommunityUtils.getCommunityIdFromTitle(communities.get(i)));
      }
    }
    return ids;
  }

  private List<String> getPrizes() {
    List<String> prizes = new ArrayList<>();
    prizes.add(firstPrizeInput.getText().toString());
    if (secondPrizeInput.getText().length() > 0) {
      prizes.add(secondPrizeInput.getText().toString());
    }
    if (thirdPrizeInput.getText().length() > 0) {
      prizes.add(thirdPrizeInput.getText().toString());
    }
    return prizes;
  }

  public void createCompetition(CompetitionCreateBody body) {
    String url = UrlBuilder.createCompetitionUrl();
    try {
      RetrofitServiceGenerator.getService().createCompetition(url, body).enqueue(new Callback<CompetitionCreateResponse>() {
        @Override
        public void onResponse(Call<CompetitionCreateResponse> call, Response<CompetitionCreateResponse> response) {
          showPublishingProgressDialog(false, "");
          if (response.isSuccessful()) {
            toast("Competition Created Successfully!");
          } else {
            ErrorResponse er = ErrorUtils.parseError(response);
            toast(er.getmMessage());
          }
        }

        @Override
        public void onFailure(Call<CompetitionCreateResponse> call, Throwable t) {
          showPublishingProgressDialog(false, "");
          toast("Failed to create competition");
        }
      });
    }
    catch (Exception e) {
      e.printStackTrace();
      showPublishingProgressDialog(false, "");
      toast("Failed to create competition");
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
      handleImageResult(data);
    }
    if (requestCode == REQUEST_JUDGE_SELECTOR && resultCode == Activity.RESULT_OK) {
      if (data != null) {
        selectedJudges = data.getParcelableArrayListExtra(EXTRA_SELECTED_JUDGES);
        updateJudgesView();
      }
    }
  }

  private void handleImageResult(final Intent intent) {
    final Handler handler = new Handler();
    new Thread() {
      @Override
      public void run() {
        final String filePath = GoogleImageFilePathReader.getImageFilePath(CompetitionCreatorActivity.this, intent);
        handler.post(new Runnable() {
          @Override
          public void run() {
            selectImage(filePath);
          }
        });
      }
    }.start();
  }

  private void updateJudgesView() {
    judgeSelector.setJudgesList(selectedJudges);
  }

  private void selectImage(String filePath) {
    isBannerSelected = true;
    competitionBanner.setImageSource(filePath);
  }

  @Override
  public void onSelectJudge() {
    Intent i = new Intent(this, JudgeSelectionActivity.class);
    i.putParcelableArrayListExtra(EXTRA_SELECTED_JUDGES, selectedJudges);
    startActivityForResult(i, REQUEST_JUDGE_SELECTOR);
    overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);
  }
}

