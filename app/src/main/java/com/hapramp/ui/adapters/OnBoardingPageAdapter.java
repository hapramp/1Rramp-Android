package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;

public class OnBoardingPageAdapter extends PagerAdapter {
		private Context mContext;
		private int[] imageIds = {R.drawable.onboarding_join, R.drawable.onboarding_share, R.drawable.onboarding_earning};
		private int[] titles = {R.string.onboarding_title_1, R.string.onboarding_title_2, R.string.onboarding_title_3};
		private int[] contents = {R.string.onboarding_content_1, R.string.onboarding_content_2, R.string.onboarding_content_3};

		public OnBoardingPageAdapter(Context context) {
				this.mContext = context;
		}

		@NonNull
		@Override
		public Object instantiateItem(@NonNull ViewGroup container, int position) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.onboarding_itemview, container, false);
				TextView title = layout.findViewById(R.id.onboardingTitle);
				ImageView imageView = layout.findViewById(R.id.imageView);
				TextView content = layout.findViewById(R.id.onboardingContent);
				title.setText(mContext.getString(titles[position]));
				content.setText(mContext.getString(contents[position]));
				imageView.setImageResource(imageIds[position]);
				container.addView(layout);
				return layout;
		}

		@Override
		public int getCount() {
				return 3;
		}

		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
				collection.removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
				return view == object;
		}
}
