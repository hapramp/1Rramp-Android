package com.hapramp.views.renderer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hapramp.R;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.views.types.HeadingOneTypeView;
import com.hapramp.views.types.HeadingThreeTypeView;
import com.hapramp.views.types.HeadingTwoTypeView;
import com.hapramp.views.types.HeadingFourTypeView;
import com.hapramp.views.types.ImageTypeView;
import com.hapramp.views.types.TextTypeView;

import java.util.List;

/**
 * Created by Ankit on 4/8/2018.
 */

public class RendererView extends FrameLayout {

    private Context mContext;
    private LinearLayout container;

    public RendererView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RendererView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RendererView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View parentView = LayoutInflater.from(mContext).inflate(R.layout.renderer_view, this);
        container = parentView.findViewById(R.id.container);
    }

    public void render(PostStructureModel postStructureModel) {

        //todo show rendering progress bar

        List<PostStructureModel.Data> dataSeries = postStructureModel.getDataSeries();
        //loop for views
        for (int i = 0; i < dataSeries.size(); i++) {
            //check for view type
            View viewToAdd = getViewType(dataSeries.get(i));
            //add to parent view
            container.addView(viewToAdd, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        //todo hide progress bar

    }


    private View getViewType(PostStructureModel.Data data) {

        View view = null;
        switch (data.getContentType()) {

            case ContentTypes.DataType.TEXT:
                TextTypeView textTypeView = new TextTypeView(mContext);
                textTypeView.setText(data.getContent());
                return textTypeView;

            case ContentTypes.DataType.IMAGE:
                ImageTypeView imageTypeView = new ImageTypeView(mContext);
                imageTypeView.setImageSource(data.getContent());
                return imageTypeView;

            case ContentTypes.DataType.H1:
                HeadingOneTypeView headingTypeView1 = new HeadingOneTypeView(mContext);
                headingTypeView1.setText(data.getContent());
                return headingTypeView1;

            case ContentTypes.DataType.H2:
                HeadingTwoTypeView headingTypeView2 = new HeadingTwoTypeView(mContext);
                headingTypeView2.setText(data.getContent());
                return headingTypeView2;

            case ContentTypes.DataType.H3:
                HeadingThreeTypeView headingTypeView3 = new HeadingThreeTypeView(mContext);
                headingTypeView3.setText(data.getContent());
                return headingTypeView3;

            case ContentTypes.DataType.H4:
                HeadingFourTypeView headingTypeView4 = new HeadingFourTypeView(mContext);
                headingTypeView4.setText(data.getContent());
                return headingTypeView4;

            default:
                return view;

        }

    }


}