package com.hapramp.views.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.views.extraa.CategoryTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/25/2017.
 */

public class PostCategoryView extends FrameLayout {

    private ViewGroup rootView;
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
                         //   Log.d("PostCategoryView","selecting : "+view.getTag());
                            selectedTags.add((String) view.getTag());
                        }
                    } else {
                        // de-select it
                       // Log.d("PostCategoryView","de-selecting : "+view.getTag());
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

    }

    public List<String> getSelectedTags() {

        selectedTags.add(Communities.TAG_HAPRAMP); //communites should be added by default
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
