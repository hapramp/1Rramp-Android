package xute.markdownrenderer;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import xute.markdownrenderer.types.BlockQuoteView;
import xute.markdownrenderer.types.BulletTypeView;
import xute.markdownrenderer.types.HeadingFiveTypeView;
import xute.markdownrenderer.types.HeadingFourTypeView;
import xute.markdownrenderer.types.HeadingOneTypeView;
import xute.markdownrenderer.types.HeadingThreeTypeView;
import xute.markdownrenderer.types.HeadingTwoTypeView;
import xute.markdownrenderer.types.HorizontalDividerTypeView;
import xute.markdownrenderer.types.ImageTypeView;
import xute.markdownrenderer.types.TextTypeView;
import xute.markdownrenderer.utils.MarkdownRendererUtils;

public class RendererView extends FrameLayout {

  private MarkdownRendererUtils markdownDataProcessor;
  private Context mContext;
  private LinearLayout container;
  private Handler mHandler;
  private ProgressBar rendererProgressBar;

  public RendererView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    mHandler = new Handler();
    View parentView = LayoutInflater.from(mContext).inflate(R.layout.renderer_view, this);
    container = parentView.findViewById(R.id.container);
    markdownDataProcessor = new MarkdownRendererUtils();
  }

  public RendererView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public RendererView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void addAllToView(ArrayList<ItemModel> items) {
    int insertIndex = 0;
    for (int i = 0; i < items.size(); i++) {
      View viewToAdd = getViewType(items.get(i));
      if (viewToAdd != null) {
        container.addView(viewToAdd, insertIndex++,
          new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
      }
    }
  }

  private View getViewType(ItemModel itemType) {
    View view = null;
    switch (itemType.getType()) {
      case ItemType.HEADING_ONE:
        HeadingOneTypeView h1v = new HeadingOneTypeView(mContext);
        h1v.setText(itemType.getData());
        return h1v;

      case ItemType.HEADING_TWO:
        HeadingTwoTypeView h2v = new HeadingTwoTypeView(mContext);
        h2v.setText(itemType.getData());
        return h2v;

      case ItemType.HEADING_THREE:
        HeadingThreeTypeView h3v = new HeadingThreeTypeView(mContext);
        h3v.setText(itemType.getData());
        return h3v;

      case ItemType.HEADING_FOUR:
        HeadingFourTypeView h4v = new HeadingFourTypeView(mContext);
        h4v.setText(itemType.getData());
        return h4v;

      case ItemType.HEADING_FIVE:
        HeadingFiveTypeView h5v = new HeadingFiveTypeView(mContext);
        h5v.setText(itemType.getData());
        return h5v;

      case ItemType.NORMAL_TEXT:
        TextTypeView textTypeView = new TextTypeView(mContext);
        textTypeView.setText(itemType.getData());
        return textTypeView;

      case ItemType.UL_BULLET:
        BulletTypeView bulletTypeView_UL = new BulletTypeView(mContext);
        bulletTypeView_UL.setBulletType(BulletTypeView.TYPE_UL);
        bulletTypeView_UL.setText(itemType.getData());
        return bulletTypeView_UL;

      case ItemType.OL_BULLET:
        BulletTypeView bulletTypeView_OL = new BulletTypeView(mContext);
        bulletTypeView_OL.setBulletType(BulletTypeView.TYPE_OL);
        bulletTypeView_OL.setText(itemType.getData());
        return bulletTypeView_OL;

      case ItemType.BLOCKQUOTE:
        BlockQuoteView blockquoteTypeView = new BlockQuoteView(mContext);
        blockquoteTypeView.setText(itemType.getData());
        return blockquoteTypeView;

      case ItemType.IMAGE:
        ImageTypeView imageTypeView = new ImageTypeView(mContext);
        imageTypeView.setImageInfo("", itemType.getData());
        return imageTypeView;

      case ItemType.HORIZONTAL_LINE:
        return new HorizontalDividerTypeView(mContext);
      default:
        return view;
    }
  }
}
