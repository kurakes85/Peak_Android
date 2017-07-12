package com.pick;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Explode;
import android.transition.Transition;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pick.common.PersonTeamValueObject;

/**
 * Created by pyoinsoo on 2016-08-14.
 */
public class CustomerItemDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    ViewPager pager;
    FloatingActionMenu fab;
    //개인 또는 팀의 디테일한 정보
    PersonTeamValueObject detailValueObject;

    private YouTubePlayerView youTubePlayerView;

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        if (!b) {
            youTubePlayer.cueVideo(detailValueObject.videoKey);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_layout);

        //뒤로가기 툴바 넣기,



        Button applyBtn = (Button) findViewById(R.id.apply_button);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomerItemDetailActivity.this, "신청리스트로 이동", Toast.LENGTH_SHORT).show();
                PopUpChooseDialog choose = new PopUpChooseDialog();
                choose.show(getFragmentManager(),"");
            }
        });

        Button bookmark = (Button) findViewById(R.id.item_book_mark_button);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomerItemDetailActivity.this, "즐겨찾기 리스트로 이동", Toast.LENGTH_SHORT).show();
            }
        });








        Intent fromMainFragment = getIntent();

        detailValueObject = (PersonTeamValueObject) fromMainFragment.getSerializableExtra("personOrTeamInfo");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Transition exitTrans = new Explode();
            Transition reenterTrans = new Explode();

            window.setExitTransition(exitTrans);
            window.setEnterTransition(reenterTrans);
            window.setReenterTransition(reenterTrans);
        }
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        TextView musitionName = (TextView) findViewById(R.id.musition_name);
        musitionName.setText(detailValueObject.name);
        TextView age = (TextView) findViewById(R.id.age);
        age.setText(detailValueObject.age);

        TextView parts = (TextView) findViewById(R.id.parts);
        int partSize = detailValueObject.parts.size();

        if (partSize > 0) {
            String writePart = "";
            for (int i = 0; i < partSize; i++) {
                if ((i + 1) == partSize) {
                    writePart += detailValueObject.parts.get(i);
                } else {
                    writePart += "," + detailValueObject.parts.get(i);
                }
            }
            parts.setText(writePart);
        } else {
            parts.setText("파트없음");
        }

        TextView genre = (TextView) findViewById(R.id.genre);
        int genreSize = detailValueObject.genres.size();
        if (genreSize > 0) {
            String writeGenre = "";
            for (int i = 0; i < genreSize; i++) {
                if ((i + 1) == genreSize) {
                    writeGenre += detailValueObject.genres.get(i);
                } else {
                    writeGenre += "," + detailValueObject.genres.get(i);
                }
            }
            genre.setText(writeGenre);
        } else {
            genre.setText("장르없음");
        }

        TextView areaAddress = (TextView) findViewById(R.id.area_address);
        areaAddress.setText(detailValueObject.areaDo + " " + detailValueObject.areaGu);


        //페이저 붙이기
        pager = (ViewPager) findViewById(R.id.detail_view_pager);
        pager.setAdapter(new CustomerDetailAdatper(this));
        fab = (FloatingActionMenu) findViewById(R.id.fab);
    }



    // 툴바 관련된 부분,
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public class CustomerDetailAdatper extends PagerAdapter {
        private int layoutCount = 2;
        private Context context;

        public CustomerDetailAdatper(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return layoutCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            int pageResource = 0;
            switch (position) {
                case 0:
                    pageResource = R.id.pager1;
                    break;
                case 1:
                    pageResource = R.id.pager2;
                    break;
            }
            return findViewById(pageResource);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        //갤러리 형식으로 만들기
        @Override
        public float getPageWidth(int position) {
            return 0.94f;
        }
    }
}
