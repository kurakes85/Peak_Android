package com.pick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

/**
 * 콘서트 디테일 화면,
 */
public class ConcertDetailActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concert_detail_layout);

        //콘서트 정보 toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.concer_detail_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar conInform = getSupportActionBar();
        conInform.setDisplayHomeAsUpEnabled(true);
        conInform.setTitle("공연 상세 ");


        viewPager = (ViewPager) findViewById(R.id.concert_viewpager);

        if (viewPager != null) {
            setupFragmentViewPager();
        }


    }

    // 툴바 관련된 부분,

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };


    private ConcertAdapter pagerAdapter;

    private void setupFragmentViewPager() {
        pagerAdapter = new ConcertAdapter(getSupportFragmentManager());
        pagerAdapter.appendFragment(ConcertFragment1.newInstance());
        pagerAdapter.appendFragment(ConcertFragment2.newInstance());
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }

    }

    private class ConcertAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();

        public ConcertAdapter(FragmentManager fm) {
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

