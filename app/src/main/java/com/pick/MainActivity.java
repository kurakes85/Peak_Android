package com.pick;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pick.common.PropertyPickManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static ViewPager pager;
    private final long FINSH_INTERVAL_TIME = 1500;
    ImageView switchTAB1;
    ImageView switchTAB2;
    FloatingActionMenu fabMenu;
    FloatingActionButton minifab1;
    FloatingActionButton minifab2;
    // 네비게이션 헤더를 위한 액티비티
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private long backPressedTime = 0;
    private CircleImageView circleImageView;



    //현재 단말기의 회원이 개인/팀인지 구별
    private String memberType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        memberType = PropertyPickManager.getPickInstance().getKeyBandOrPersonal();
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ham_btn);
        ab.setDisplayHomeAsUpEnabled(true);

        //Floating Action Buttons
        fabMenu = (FloatingActionMenu) findViewById(R.id.fab);
        minifab1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        minifab2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final View headerView = getLayoutInflater().inflate(R.layout.nav_header, mDrawerLayout, false);
        navigationView.addHeaderView(headerView);


        //네비게이션 드로어 마이프로필 부분 클릭 이벤트
        circleImageView = (CircleImageView) headerView.findViewById(R.id.navi_header_image);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "마이메뉴 로 들어가요~!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MenuMineActivity.class);
                startActivity(intent);
            }
        });

        minifab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memberType.equalsIgnoreCase("0")) {
                    Toast.makeText(getApplicationContext(), "밴드회원은 개인글쓰기에 들어가실수 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, UploadPersonaWritteActivity.class);

                startActivity(intent);

            }
        });
        minifab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memberType.equalsIgnoreCase("1")) {
                    Toast.makeText(getApplicationContext(), "개인은 밴드글쓰기에 들어가실수 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, UploadBandWritteActivity.class);
                startActivity(intent);
            }
        });

        // 네비게이션
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent = null;
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.concert_information:
                        intent = new Intent(MainActivity.this, ConcertInformActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.apply_list:
                        intent = new Intent(MainActivity.this, RequestListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bookmark_list:
                        intent = new Intent(MainActivity.this, BookMarkListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.messagebox:
                        intent = new Intent(MainActivity.this, MessageListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.setting:

                        break;
                }

                return true;
            }
        });

        //탭에 필요한 뷰 페이지가 없을 경우 설정
        ViewPager mainViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mainViewPager != null) {
            setupTabViewPager(mainViewPager);
        }

        // Tap Layout 시작
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mainViewPager);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_1), 0, true);
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_2), 1);

        switchTAB1 = (ImageView) tabLayout.findViewById(R.id.tab1);
        switchTAB2 = (ImageView) tabLayout.findViewById(R.id.tab2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                if (i == 0) {
                    switchTAB1.setImageResource(R.drawable.tab2);
                    switchTAB2.setImageResource(R.drawable.tab1_2);
                } else {
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

        // 롤리팝 이상부터 트랜지션 적용
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Transition exitTrans = new Explode();
            Transition reenterTrans = new Explode();

            window.setExitTransition(exitTrans);
            window.setReenterTransition(reenterTrans);
            window.setAllowEnterTransitionOverlap(true);
            window.setAllowReturnTransitionOverlap(true);
        }
    }

    //탭 몇개 필요한지 설정하는 부분
    private void setupTabViewPager(ViewPager pager) {

        PagerAdapter tabAdapter = new PagerAdapter(getSupportFragmentManager());
        tabAdapter.appendFragment(
                MainFragment.newInstance(1)); //개인(person)
        tabAdapter.appendFragment(
                MainFragment.newInstance(0)); //밴드(person)
        pager.setAdapter(tabAdapter);
    }

    // 네비게이션 처음 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로가기 버튼 이벤트
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;

        if (mDrawerLayout.isDrawerOpen(navigationView)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = currentTime;
            Toast.makeText(getApplicationContext(),
                    "'뒤로' 버튼 한번 더 누르시면 종료됩니다.",
                    Toast.LENGTH_SHORT).show();
        }
        // super.onBackPressed();
    }

    //탭 바 설정
    private static class PagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> tabFragment =
                new ArrayList();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment) {
            tabFragment.add(fragment);
        }

        public android.support.v4.app.Fragment getItem(int position) {
            return tabFragment.get(position);
        }

        public int getCount() {
            return tabFragment.size();
        }
    }
}
