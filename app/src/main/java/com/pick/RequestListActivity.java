package com.pick;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 *          신청 리스트
 */
public class RequestListActivity extends AppCompatActivity {

    ImageView switchTabMe;
    ImageView switchTabMine;

    ViewPager requestViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_list_main);

        // ToolBar Section
        Toolbar toolbar = (Toolbar)findViewById(R.id.request_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar conInform = getSupportActionBar();
        conInform.setDisplayHomeAsUpEnabled(true);
        conInform.setTitle("신청 리스트");




        requestViewPager = (ViewPager) findViewById(R.id.requst_pager);

        if (requestViewPager != null) {
            setupFragmentViewPager();
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.request_menu_tabs);
        tabLayout.setupWithViewPager(requestViewPager);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.request_tab1), 0, true);
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.request_tab2), 1);

        switchTabMe = (ImageView) tabLayout.findViewById(R.id.request_tab1);
        switchTabMine = (ImageView) tabLayout.findViewById(R.id.request_tab2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if (i == 0) {
                    switchTabMe.setImageResource(R.drawable.mine_add_pink_tab1_1);
                    switchTabMine.setImageResource(R.drawable.me_add_purple_tab2_2);
                } else {
                    switchTabMine.setImageResource(R.drawable.me_add_pink_tab2_1);
                    switchTabMe.setImageResource(R.drawable.mine_add_purple_tab1_2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // ToolBar option Item 추가

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };


    private RequestAdapter pagerAdaper;

    private void setupFragmentViewPager() {
        pagerAdaper = new RequestAdapter(getSupportFragmentManager());
        pagerAdaper.appendFragment(RequestFragmentMe.newInstance());
        pagerAdaper.appendFragment(RequestFragmentMine.newInstance());
        if (requestViewPager != null) {
            requestViewPager.setAdapter(pagerAdaper);
        }
    }

    private class RequestAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();

        public RequestAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}


