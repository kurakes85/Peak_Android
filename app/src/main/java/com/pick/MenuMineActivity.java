package com.pick;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 메뉴 마이페이지 관련 부분
 */
public class MenuMineActivity extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    ImageView mSwitchTAB1;
    ImageView mSwitchTAB2;


    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_profile);
        CircleImageView picBtn = (CircleImageView) findViewById(R.id.navi_header_image_btn);
        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuMineActivity.this, "서클 사진 버튼 클릭!", Toast.LENGTH_LONG).show();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.person_menu_pager);
        if (viewPager != null) {
            setupMenuFragmentViewPager();
        }

        tabLayout = (TabLayout) findViewById(R.id.menu_tabs);

        tabLayout.setupWithViewPager(viewPager);
        // 탭 레이아웃 작동 뷰페이져

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.menu_tab1), 0, true);
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.menu_tab2), 1);

        mSwitchTAB1 = (ImageView) tabLayout.findViewById(R.id.menu_tab1);
        mSwitchTAB2 = (ImageView) tabLayout.findViewById(R.id.menu_tab2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if (i == 0) {
                    mSwitchTAB1.setImageResource(R.drawable.menu_pro_pink_1);
                    mSwitchTAB2.setImageResource(R.drawable.menu_my_pur2_2);
                } else {
                    mSwitchTAB2.setImageResource(R.drawable.menu_my_pink_2);
                    mSwitchTAB1.setImageResource(R.drawable.menu_pro_pur1_2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

    }

    //Fragment 아답터 페이져
    private MenuMineAdapter pagerMenuAdapter;

    private void setupMenuFragmentViewPager() {
        pagerMenuAdapter = new MenuMineAdapter(getSupportFragmentManager());
        pagerMenuAdapter.appendFragment(MenuFragmentPerson.newInstance());
        pagerMenuAdapter.appendFragment(MenuFragment2.newInstance());
        if (viewPager != null) {
            viewPager.setAdapter(pagerMenuAdapter);
        }
    }

    private class MenuMineAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();

        public MenuMineAdapter(FragmentManager fm) {
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