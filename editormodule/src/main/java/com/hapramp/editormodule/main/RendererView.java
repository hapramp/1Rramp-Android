package com.hapramp.editormodule.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hapramp.editormodule.R;
import com.hapramp.editormodule.model.ContentTypes;
import com.hapramp.editormodule.model.PostStructureModel;
import com.hapramp.editormodule.types.ImageTypeView;
import com.hapramp.editormodule.types.TextTypeView;

import java.util.List;

/**
 * Created by Ankit on 4/8/2018.
 */

public class RendererView extends FrameLayout {

    private Context mContext;
    private LinearLayout parentView;

    public RendererView(@NonNull Context context) {
        super(context);
    }

    public RendererView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RendererView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        this.mContext = context;
        parentView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.renderer_view, this);
    }

    public void render(PostStructureModel postStructureModel) {

        //todo show rendering progress bar

        List<PostStructureModel.Data> dataSeries = postStructureModel.getDataSeries();
        //loop for views
        for (int i = 0; i < dataSeries.size(); i++) {
            //check for view type
            View viewToAdd = getViewType(dataSeries.get(i));
            //add to parent view
            parentView.addView(viewToAdd, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        //todo hide progress bar

    }

    private View getViewType(PostStructureModel.Data data) {

        View view = null;
        switch (data.getmContentType()) {
            case ContentTypes.DataType.TEXT:
                TextTypeView textTypeView = new TextTypeView(mContext);
                textTypeView.setText(data.getmContent());
                return textTypeView;
            case ContentTypes.DataType.IMAGE:
                ImageTypeView imageTypeView = new ImageTypeView(mContext);
                imageTypeView.setImageSource(data.getmContent());
                return imageTypeView;
            default:
                return view;
        }

    }


}
