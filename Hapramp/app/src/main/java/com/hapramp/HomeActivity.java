package com.hapramp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hapramp.fragments.FeaturedSection;
import com.hapramp.fragments.RecentSection;
import com.hapramp.fragments.TopSection;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.BindView;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.search_icon)
    TextView searchIcon;
    @BindView(R.id.notification_icon)
    TextView notificationIcon;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Typeface materialTypeface;
    @BindView(R.id.bottomBar_home)
    TextView bottomBarHome;
    @BindView(R.id.bottomBar_competition)
    TextView bottomBarCompetition;
    @BindView(R.id.bottomBar_profile)
    TextView bottomBarProfile;
    @BindView(R.id.bottomBar_settings)
    TextView bottomBarSettings;
    @BindView(R.id.bottom_bar_more)
    TextView bottomBarMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupToolbar();
        setupTabs();
        setupViewPager();
        setUpBottomBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupToolbar() {

        materialTypeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        notificationIcon.setTypeface(materialTypeface);
        searchIcon.setTypeface(materialTypeface);

    }

    private void setupViewPager() {
        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    }

    private void setUpBottomBar() {

        bottomBarHome.setTypeface(materialTypeface);
        bottomBarCompetition.setTypeface(materialTypeface);
        bottomBarMore.setTypeface(materialTypeface);
        bottomBarProfile.setTypeface(materialTypeface);
        bottomBarSettings.setTypeface(materialTypeface);

    }

    private void setupTabs() {
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setupWithViewPager(viewpager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> titles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new TopSection());
            fragments.add(new RecentSection());
            fragments.add(new FeaturedSection());
            fragments.add(new TopSection());
            fragments.add(new RecentSection());
            fragments.add(new FeaturedSection());
            fragments.add(new TopSection());
            fragments.add(new RecentSection());
            fragments.add(new FeaturedSection());

            titles.add("Top");
            titles.add("Recent");
            titles.add("Featured");
            titles.add("Top");
            titles.add("Recent");
            titles.add("Featured");
            titles.add("Top");
            titles.add("Recent");
            titles.add("Featured");

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}

