package com.hapramp.views.skills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.SkillsUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Ankit on 12/9/2017.
 */

public class SkillsTagView extends FrameLayout{

    private Context mContext;
    private RecyclerView skillsTagRV;
    private SkillsRVAdapter skillsAdapter;
    private int[] selectionArray;

    private static final String TAG = SkillsTagView.class.getSimpleName();

    public SkillsTagView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SkillsTagView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SkillsTagView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        // initialize skills array
        selectionArray = new int[8];

        View view = LayoutInflater.from(context).inflate(R.layout.skills_tag_view, null);
        skillsTagRV = (RecyclerView) view.findViewById(R.id.skillsTagsRV);
        skillsAdapter = new SkillsRVAdapter();
        skillsTagRV.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));
        skillsTagRV.setAdapter(skillsAdapter);
        this.mContext = context;

    }


    /*
    *  public Method to add Skills
    * */
    public void setSkills(ArrayList<Integer> skill_ids){

        Log.d(TAG," adding skills");
        skillsAdapter.setSkills(skill_ids);

    }

    class SkillsRVAdapter extends RecyclerView.Adapter<SkillsRVAdapter.SkillsTagViewHolder> {

        ArrayList<Integer> skills;
        OnCancelListener cancelListener;

        public void setSkills(ArrayList<Integer> skills) {
            this.skills = skills;
            notifyDataSetChanged();
        }

        public void setCancelListener(OnCancelListener cancelListener) {
            this.cancelListener = cancelListener;
        }

        @Override
        public SkillsTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.skills_tag_view_item, null);
            return new SkillsTagViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SkillsTagViewHolder holder, int position) {

            Log.d(TAG,"binding "+position);

            holder.bind(skills.get(position), new OnCancelListener() {
                @Override
                public void onCancel(String title) {
                    //remove from current list
                    cancelListener.onCancel(title);
                }
            });

        }

        private void removeItem(String title){
            for (int i=0;i<skills.size();i++){
                if(skills.get(i)== SkillsUtils.getSkillIdFromName(title)){
                    //remove
                }
            }
        }

        @Override
        public int getItemCount() {
            return skills != null ? skills.size() : 0;
        }


        class SkillsTagViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.skill_title)
            TextView skillTitle;
            @BindView(R.id.cancelBtn)
            TextView cancelBtn;

            public SkillsTagViewHolder(View itemView) {
                super(itemView);
                cancelBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
            }

            public void bind(final int tagId , final OnCancelListener onCancelListener){

                skillTitle.setText(SkillsUtils.getSkillTitleFromId(tagId));
                cancelBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
                cancelBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onCancelListener!=null){
                            //remove item from list
                            onCancelListener.onCancel(skillTitle.getText().toString());
                        }
                    }
                });
            }

        }


    }

    public interface OnCancelListener {
        void onCancel(String title);
    }

}
