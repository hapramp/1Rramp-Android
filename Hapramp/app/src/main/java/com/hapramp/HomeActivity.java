package com.hapramp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.fragments.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements CategoryRecyclerAdapter.OnCategoryItemClickListener {


    @BindView(R.id.search_icon)
    TextView searchIcon;
    @BindView(R.id.bottomBar_home)
    TextView bottomBarHome;
    @BindView(R.id.bottomBar_competition)
    TextView bottomBarCompetition;
    @BindView(R.id.bottom_bar_more)
    TextView bottomBarMore;
    @BindView(R.id.bottomBar_profile)
    TextView bottomBarProfile;
    @BindView(R.id.bottomBar_settings)
    TextView bottomBarSettings;
    @BindView(R.id.notification_icon)
    TextView notificationIcon;
    @BindView(R.id.notification_count)
    TextView notificationCount;
    @BindView(R.id.notification_container)
    RelativeLayout notificationContainer;
    @BindView(R.id.action_bar_container)
    RelativeLayout actionBarContainer;
    @BindView(R.id.sectionsRv)
    RecyclerView sectionsRv;

    CategoryRecyclerAdapter categoryRecyclerAdapter;

    private final int FRAGMENT_HOME = 12;
    private final int FRAGMENT_COMPETITION = 13;
    private final int FRAGMENT_PROFILE = 14;
    private final int FRAGMENT_SETTINGS = 15;

    private Typeface materialTypface;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupToolbar();
        setCategory();
        initObjects();
        attachListeners();
    }

    private void initObjects(){
        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
    }

    private void setupToolbar() {
        materialTypface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        searchIcon.setTypeface(materialTypface);
        notificationIcon.setTypeface(materialTypface);
        bottomBarHome.setTypeface(materialTypface);
        bottomBarCompetition.setTypeface(materialTypface);
        bottomBarProfile.setTypeface(materialTypface);
        bottomBarSettings.setTypeface(materialTypface);
        bottomBarMore.setTypeface(materialTypface);
        notificationCount.setVisibility(View.VISIBLE);
        notificationCount.setText("23");
    }

    private void attachListeners(){

        bottomBarHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                transactFragment(FRAGMENT_HOME);
            }
        });


        bottomBarCompetition.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                transactFragment(FRAGMENT_COMPETITION);
            }
        });


        bottomBarProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                transactFragment(FRAGMENT_PROFILE);
            }
        });


        bottomBarSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                transactFragment(FRAGMENT_SETTINGS);
            }
        });
    }

    private void setCategory(){
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(this,this);
        sectionsRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        sectionsRv.setAdapter(categoryRecyclerAdapter);
    }

    @Override
    public void onCategoryClicked(int id) {
        // TODO: 10/25/2017 fetch posts on category basis
    }

    private void transactFragment(int fragment){

        switch (fragment){
            case FRAGMENT_HOME:
                fragmentManager.beginTransaction()
                        .replace(R.id.contentPlaceHolder,homeFragment)
                        .addToBackStack(null)
                        .commit();

                break;
            case FRAGMENT_COMPETITION:

                fragmentManager.beginTransaction()
                        .replace(R.id.contentPlaceHolder,homeFragment)
                        .addToBackStack(null)
                        .commit();

                break;
            case FRAGMENT_PROFILE:

                fragmentManager.beginTransaction()
                        .replace(R.id.contentPlaceHolder,homeFragment)
                        .addToBackStack(null)
                        .commit();

                break;
            case FRAGMENT_SETTINGS:

                fragmentManager.beginTransaction()
                        .replace(R.id.contentPlaceHolder,homeFragment)
                        .addToBackStack(null)
                        .commit();

                break;
            default:
                break;
        }
    }
}

