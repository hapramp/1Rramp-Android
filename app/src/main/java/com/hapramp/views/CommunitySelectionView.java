package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.views.post.PostCategoryViewGroup;
import com.hapramp.views.skills.CommunityItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunitySelectionView extends FrameLayout {

    @BindView(R.id.viewWrapper) PostCategoryViewGroup viewWrapper;
    @BindView(R.id.communityLoadingProgressBar) ProgressBar communityLoadingProgressBar;

    private Context mContext;
    private ViewGroup parentView;
    private ArrayList<Integer> selectedCommunityIds;
    private List<CommunityModel> mCommunityList;

    public CommunitySelectionView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CommunitySelectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommunitySelectionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.community_view_container, this);
        parentView = view.findViewById(R.id.viewWrapper);
        selectedCommunityIds = new ArrayList<>();
        ButterKnife.bind(this,view);

    }

    public void setCommunityList(List<CommunityModel> communityList) {

        this.mCommunityList = communityList;

        if (communityLoadingProgressBar != null) {
            communityLoadingProgressBar.setVisibility(GONE);
        }

        addViews();

    }

    private void addViews() {

        for (int i = 0; i < mCommunityList.size(); i++) {
    //        Log.d("View","Addding "+mCommunityList.get(i).getmName());

            final CommunityItemView view = new CommunityItemView(mContext);
           // view.setCommunityItemTitle(String.valueOf(mCommunityList.get(i).getmName()));
            view.setCommunityDetails(mCommunityList.get(i));
            // set selection
            view.setSelection((selectedCommunityIds.indexOf(mCommunityList.get(i).getmId()) > -1));

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // try to get the index of current clicked view in selection list, if its index >-1 then it is already selected
                    int index = selectedCommunityIds.indexOf(view.getCommunityId());
                    if (index == -1) {
                        // select it
                        view.setSelection(true);

                        selectedCommunityIds.add(view.getCommunityId());

                    } else {
                        // de-select it
                        if (selectedCommunityIds.size() < 2) {
                            // warn for selecting atleast one
                            Toast.makeText(mContext, "Atleast One Skill Should be There", Toast.LENGTH_LONG).show();
                            return;
                        }
                        view.setSelection(false);
                        selectedCommunityIds.remove(index);

                    }
                }
            });

            parentView.addView(view, i,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }

    }

    public List<Integer> getSelectionList() {
        return selectedCommunityIds;
    }

}
