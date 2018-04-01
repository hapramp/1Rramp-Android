package com.hapramp.views.post;

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

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.views.extraa.CategoryTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/25/2017.
 */

public class PostCategoryView extends FrameLayout {

    private ViewGroup rootView;
    private ProgressBar communityLoadingProgressBar;
    private Context mContext;
    private List<CommunityModel> communities;
    private ArrayList<String> selectedTags;

    public PostCategoryView(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PostCategoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public PostCategoryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.community_view_container, this);
        rootView = view.findViewById(R.id.viewWrapper);
        communityLoadingProgressBar = view.findViewById(R.id.communityLoadingProgressBar);
        selectedTags = new ArrayList<>();

    }

    private void addViews() {

        for (int i = 0; i < communities.size(); i++) {

            final CategoryTextView view = new CategoryTextView(mContext);
            view.setText(communities.get(i).getmName());
            view.setTag(communities.get(i).getmTag());

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = selectedTags.indexOf(view.getTag());
                    if (index == -1) {
                        // select it
                        if (selectedTags.size() > 2) {
                            Toast.makeText(mContext, "Maximum 3 Skills", Toast.LENGTH_LONG).show();
                        } else {
                            view.setSelected(true);
                            selectedTags.add((String) view.getTag());
                        }
                    } else {
                        // de-select it
                        view.setSelected(false);
                        selectedTags.remove(index);
                    }

                    if (communitySelectionChangeListener != null) {
                        communitySelectionChangeListener.onCommunitySelectionChanged(selectedTags);
                    }

                }
            });

            rootView.addView(view, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        communityLoadingProgressBar.setVisibility(GONE);

    }

    public List<String> getSelectedTags() {
        return selectedTags;
    }


    public void initCategory() {

        CommunityListWrapper cr = new Gson().fromJson(HaprampPreferenceManager.getInstance().getAllCommunityAsJson(), CommunityListWrapper.class);
        communities = cr.getCommunityModels();
        addViews();

    }

    private CommunitySelectionChangeListener communitySelectionChangeListener;

    public void setCommunitySelectionChangeListener(CommunitySelectionChangeListener communitySelectionChangeListener) {
        this.communitySelectionChangeListener = communitySelectionChangeListener;
    }

    public interface CommunitySelectionChangeListener {
        void onCommunitySelectionChanged(List<String> communities);
    }

}
