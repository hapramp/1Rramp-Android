package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.SkillsConverter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateArticleActivity extends AppCompatActivity{


    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.nextButton)
    TextView postButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.postMedia)
    ImageView postMedia;
    @BindView(R.id.removeImageBtn)
    TextView removeImageBtn;
    @BindView(R.id.postMediaUploadProgress)
    ProgressBar postMediaUploadProgress;
    @BindView(R.id.postMediaContainer)
    FrameLayout postMediaContainer;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.textSizeBtn)
    TextView textSizeBtn;
    @BindView(R.id.quoteBtn)
    TextView quoteBtn;
    @BindView(R.id.bulletBtn)
    TextView bulletBtn;
    @BindView(R.id.linkBtn)
    TextView linkBtn;
    @BindView(R.id.bottom_options_container)
    RelativeLayout bottomOptionsContainer;
    @BindView(R.id.skillsTagView)
    TextView skillsTagView;
    @BindView(R.id.addSkillBtn)
    TextView addSkillBtn;
    @BindView(R.id.skills_wrapper)
    RelativeLayout skillsWrapper;
    @BindView(R.id.characterLimit)
    TextView characterLimit;
    private Typeface typeface;
    private ArrayList<Integer> selectedSkills;

    private boolean isSkillSelected = false;
    private ProgressDialog progressDialog;
    final String[] skills = {"Art", "Dance", "Music", "Literature", "Action", "Photography"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        ButterKnife.bind(this);
        init();
        attachListeners();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDraft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDraft();
    }

    private void loadDraft(){
        content.setText(HaprampPreferenceManager.getInstance().getArticleDraft());
    }

    private void clearDraft(){
        HaprampPreferenceManager.getInstance().saveArticleDraft("");
    }

    private void saveDraft(){
        HaprampPreferenceManager.getInstance().saveArticleDraft(content.getText().toString());
    }

    private void init() {

        typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        textSizeBtn.setTypeface(typeface);
        quoteBtn.setTypeface(typeface);
        bulletBtn.setTypeface(typeface);
        linkBtn.setTypeface(typeface);
        closeBtn.setTypeface(typeface);
        selectedSkills = new ArrayList<>();
        initProgressDialog();

    }

    private void attachListeners() {

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                characterLimit.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSkillsDialog();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareArticle();
            }
        });

    }

    private void prepareArticle() {

        if (!validatePostContent())
            return;

        if (!isSkillSelected()) {
            toast("You Should Select Skills Regarding Your Post");
            return;
        }

        uploadArticle();
    }

    private boolean isSkillSelected() {
        return selectedSkills.size()>0;
    }

    public void uploadArticle() {

        showProgressDialog(true);
        PostCreateBody body = new PostCreateBody(
                content.getText().toString(),
                "",
                Constants.CONTENT_TYPE_ARTICLE,
                selectedSkills,
                1);


    }

    private boolean validatePostContent() {

        if (content.getText().toString().length() < 140) {
            Toast.makeText(this, "Your Content is Small. Minimum Article Size is 140", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private void initProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Post Upload");
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Your Post...");

    }

    private void showProgressDialog(boolean show) {
        if (progressDialog != null) {
            if (show) {
                progressDialog.show();
            } else {
                progressDialog.hide();
            }
        }
    }

    private void showAddSkillsDialog() {

        boolean[] checked = getSelectedSkills();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Skills");

        builder.setMultiChoiceItems(skills, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                int index = selectedSkills.indexOf(SkillsConverter.getSkillIdFromName(skills[which]));
                if (index == -1) {
                    // do not exists
                    if (isChecked) {
                        if (selectedSkills.size() > 2) {
                            toast("Maximum 3 Skills");
                        } else {
                            selectedSkills.add(SkillsConverter.getSkillIdFromName(skills[which]));
                        }
                    }
                } else {
                    // exists
                    if (!isChecked) {
                        selectedSkills.remove(index);
                    }
                }
            }
        });

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSelectedSkills();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    private boolean[] getSelectedSkills() {

        boolean[] selected = new boolean[6];

        for (int i = 0; i < selected.length; i++) {
            selected[i] = selectedSkills.contains(SkillsConverter.getSkillIdFromName(skills[i]));
            Log.d("POST", SkillsConverter.getSkillIdFromName(skills[i]) + " vs " + Arrays.toString(selectedSkills.toArray()));
        }
        return selected;

    }

    private void showSelectedSkills() {
        StringBuilder builder = new StringBuilder();

        for (Integer skillId : selectedSkills) {
            builder.append(" #").append(SkillsConverter.getSkillTitleFromId(skillId));
        }

        skillsTagView.setText(builder.toString());

    }

}
