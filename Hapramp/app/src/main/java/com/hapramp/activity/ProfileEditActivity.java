package com.hapramp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.OrgsFetchCallback;
import com.hapramp.interfaces.UserDpUpdateRequestCallback;
import com.hapramp.logger.L;
import com.hapramp.models.UserDataUpdateBody;
import com.hapramp.models.response.OrgsResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.SelectableInterestsView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileEditActivity extends AppCompatActivity implements UserDpUpdateRequestCallback, FullUserDetailsCallback, OrgsFetchCallback {

    private static final int REQUEST_IMAGE_SELECTOR = 101;
    @BindView(R.id.profile_pic)
    ImageView profilePic;
    @BindView(R.id.editBtn)
    TextView editBtn;
    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.nameEt)
    EditText nameEt;
    @BindView(R.id.usernameEt)
    EditText usernameEt;
    @BindView(R.id.bioEt)
    EditText bioEt;
    @BindView(R.id.org_dropdown)
    Spinner orgDropdown;
    @BindView(R.id.emailEt)
    EditText emailEt;
    @BindView(R.id.interestView)
    SelectableInterestsView interestView;
    @BindView(R.id.backButton)
    TextView backBtn;
    @BindView(R.id.saveButton)
    TextView saveButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.dpUploadingProgress)
    ProgressBar dpUploadingProgress;

    private String dpUrl;
    private FirebaseStorage storage;
    ArrayAdapter<String> spinnerArrayAdapter;
    UserModel userData;
    private List<OrgsResponse> orgs;
    private int selectedOrgId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);
        init();

    }

    private void init() {

        storage = FirebaseStorage.getInstance();
        fetchOrgs();
        fetchUserDetailsFull();
        //back button
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        backBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void attachListeners() {

    }

    private void setSpinnerAdapter(String[] spinnerList) {


        spinnerArrayAdapter = new ArrayAdapter<String>
                (this,
                        android.R.layout.simple_spinner_item,
                        spinnerList);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        orgDropdown.setAdapter(spinnerArrayAdapter);

    }

    private void fetchUserDetailsFull() {
        DataServer.getFullUserDetails(HaprampPreferenceManager.getInstance().getUserId(), this);
    }

    private void fetchOrgs() {
        DataServer.getOrgs(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMAGE_SELECTOR:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex < 0) {
                    L.D.m("Profile", "Photo Url error!");
                } else {
                    ;
                    uploadMedia(cursor.getString(columnIndex));
                }
                cursor.close();
            }
        }
    }

    private void uploadMedia(String uri) {

        showDpProgress();
        // show image
        ImageHandler.loadCircularImage(this, profilePic, uri);

        StorageReference storageRef = storage.getReference();
        StorageReference dpRef = storageRef
                .child(Constants.userDpFolder)
                .child(HaprampPreferenceManager.getInstance().getUserId());

        InputStream stream = null;
        L.D.m("Profile", "Uploading from..." + uri);

        try {
            stream = new FileInputStream(new File(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = dpRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                hideDpProgress();
                Toast.makeText(ProfileEditActivity.this, "Failed To Upload Media", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                hideDpProgress();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                dpUrl = downloadUrl.toString();
                L.D.m("Profile", " uploaded to : " + downloadUrl.toString());
            }
        });

    }

    private void updateAppServerForDpUpdate() {

        UserDataUpdateBody body =  new UserDataUpdateBody(emailEt.getText().toString(),
                usernameEt.getText().toString(),
                nameEt.getText().toString(),
                dpUrl,
                bioEt.getText().toString(),
                selectedOrgId);

        DataServer.updataUserDpUrl(
                HaprampPreferenceManager.getInstance().getUserId(),
               body
               ,this);

    }

    private void openGallery() {

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUserDataUpdated() {
        Toast.makeText(this,"Updated!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserDataUpdateError() {
        //load Previous Image
        bindValues(userData);
        //ImageHandler.loadCircularImage(this, profilePic, dpUrl);
    }

    @Override
    public void onFullUserDetailsFetched(UserModel userModel) {
        bindValues(userModel);
    }

    private void bindValues(UserModel userModel) {

        userData = userModel;
       //dp
        dpUrl = userModel.image_uri;
        ImageHandler.loadCircularImage(this, profilePic, userModel.image_uri);
        //edit Btn
        editBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppServerForDpUpdate();
            }
        });

        //name
        nameEt.setText(userModel.full_name);
        //username
        usernameEt.setText(userModel.username);
        //bio
        bioEt.setText(userModel.bio);
        //interest
        interestView.setInterests(userModel.skills);
        //email
        emailEt.setText(userModel.email);
        //disable for now
        emailEt.setEnabled(false);

        selectedOrgId = userModel.organization.id;

        orgDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrgId = orgs.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onFullUserDetailsFetchError() {

    }

    @Override
    public void onOrgsFetched(List<OrgsResponse> orgs) {

        String[] _org = new String[orgs.size()];
        int i = 0;
        for (OrgsResponse org : orgs) {
            _org[i++] = org.name;
        }
        this.orgs = orgs;
        setSpinnerAdapter(_org);

    }

    @Override
    public void onOrgFetchedError() {
        // TODO: 12/26/2017 do nothing for now :)
    }

    private void showDpProgress(){
        if(dpUploadingProgress!=null){
            dpUploadingProgress.setVisibility(View.VISIBLE);
        }
    }

    private void hideDpProgress(){
        if(dpUploadingProgress!=null){
            dpUploadingProgress.setVisibility(View.GONE);
        }
    }

}
