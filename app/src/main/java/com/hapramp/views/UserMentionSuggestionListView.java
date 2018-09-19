package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.ImageHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserMentionSuggestionListView extends FrameLayout {
  @BindView(R.id.mentions_container)
  LinearLayout mentionsContainer;
  private Context context;
  private MentionsSuggestionPickListener mentionsSuggestionPickListener;

  public UserMentionSuggestionListView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    View rootView = LayoutInflater.from(context).inflate(R.layout.user_mention_suggestion_container_view, this);
    ButterKnife.bind(this, rootView);
  }

  public UserMentionSuggestionListView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public UserMentionSuggestionListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void addSuggestions(List<String> suggestions) {
    int max = suggestions.size() > 8 ? 8 : suggestions.size();
    mentionsContainer.removeAllViews();
    for (int i = 0; i < max; i++) {
      String u = suggestions.get(i);
      View view = LayoutInflater.from(context).inflate(R.layout.user_mention_suggestion_item_row,
        null);
      view.setTag(u);
      view.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (mentionsSuggestionPickListener != null) {
            mentionsSuggestionPickListener.onUserPicked((String) view.getTag());
          }
        }
      });
      ImageView im = view.findViewById(R.id.user_pic);
      TextView tv = view.findViewById(R.id.username);
      tv.setText(u);
      ImageHandler.loadCircularImage(context, im, String.format(context.getResources().getString(R.string.steem_user_profile_pic_format), u));
      mentionsContainer.addView(view, i, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  public void setMentionsSuggestionPickListener(MentionsSuggestionPickListener mentionsSuggestionPickListener) {
    this.mentionsSuggestionPickListener = mentionsSuggestionPickListener;
  }

  public interface MentionsSuggestionPickListener {
    void onUserPicked(String username);
  }
}
