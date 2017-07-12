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
 *    즐겨찾기 리스트
 */
public class BookMarkListActivity extends AppCompatActivity {

    ImageView switchTAB1;
    ImageView switchTAB2;

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_list_main);

        //툴바 뒤로가기 버튼
//        Toolbar toolbar = (Toolbar)findViewById(R.id.book_mark_toolbar);
//        setSupportActionBar(toolbar);
//        final ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle("즐겨찾기 리스트");

        Toolbar toolbar = (Toolbar)findViewById(R.id.book_mark_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar conInform = getSupportActionBar();
        conInform.setDisplayHomeAsUpEnabled(true);
        conInform.setTitle("즐겨찾기 리스트");


        viewPager = (ViewPager) findViewById(R.id.book_mark_pager);

        if (viewPager != null) {
            setupFragementViewPager();
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.book_mark_menu_tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_1), 0,true);
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_2),1);

        switchTAB1 = (ImageView)tabLayout.findViewById(R.id.tab1);
        switchTAB2 = (ImageView)tabLayout.findViewById(R.id.tab2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if(i == 0){
                    switchTAB1.setImageResource(R.drawable.tab2);
                    switchTAB2.setImageResource(R.drawable.tab1_2);
                }else{
                    switchTAB2.setImageResource(R.drawable.tab1);
                    switchTAB1.setImageResource(R.drawable.tab2_2);
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

    // ToolBar optionItem,
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    private BookMarkListAdapter  pagerAdapter;

    private void setupFragementViewPager(){

        pagerAdapter = new BookMarkListAdapter(getSupportFragmentManager());
        pagerAdapter.appendFragment(BookMarkFragmentMember.newInstance());
        pagerAdapter.appendFragment(BookMarkFragmentBand.newInstance());
        if( viewPager != null){
            viewPager.setAdapter(pagerAdapter);
        }

    }
    private  class BookMarkListAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();
        public BookMarkListAdapter(FragmentManager fm){
            super(fm);
        }

        public void appendFragment(Fragment fragment){
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position){
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}
