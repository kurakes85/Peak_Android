package com.pick;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.pick.common.PersonTeamValueObject;
import com.pick.common.PickCommon;
import com.pick.common.PickJSONParsor;
import com.pick.common.PropertyPickManager;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by ccei on 2016-07-20.
 */
public class MainFragment extends android.support.v4.app.Fragment {

    RecyclerView rv;
    SwipeRefreshLayout swiper;
    private MainActivity owner;
    private int type = 0; //리사이클러뷰의 항목보이기 type
    private Button listViewButton, videoViewButton;
    private RecyclerViewAdapter viewAdapter;
    private int personOrTeam; //URL 타입(개인, 밴드)
    private int pageState = 1; //현재 페이지 상태

    public MainFragment() {

    }

    public static MainFragment newInstance(int uType) {
        MainFragment fragment = new MainFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("personOrBand", uType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle received = getArguments();
        if (received != null) {
            personOrTeam = received.getInt("personOrBand");
        }
    }

    //RecyclerView가 보여지는 방법
    public void setType(int changeType) {
        type = changeType;
        rv.setAdapter(viewAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_video_list, container, false);
        swiper = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_wrapper);
        //////////////////////////////
        //  Setup Swipe To Refresh  //
        //////////////////////////////
        /*
          Swipe Icon 크기 및 컬러설정
         */
        swiper.setSize(SwipeRefreshLayout.LARGE);
        swiper.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light
        );
        //Swipe 발생시
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*
                  발생시(2.5초)
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   new  PersonTeamAsyncTask().execute(personOrTeam,pageState++);
                        swiper.setRefreshing(false);
                    }
                }, 2500);
            }
        });


        rv = (RecyclerView) v.findViewById(R.id.recyclerviewList);
        owner = (MainActivity) getActivity();
        listViewButton = (Button) v.findViewById(R.id.view_block_list);
        videoViewButton = (Button) v.findViewById(R.id.view_video_list);

        //리스트 화면 변환 이벤트
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setType(0);
            }
        });
        videoViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setType(1);
            }
        });

        owner = (MainActivity) getActivity();

        rv.setLayoutManager(new LinearLayoutManager(owner));
        viewAdapter = new RecyclerViewAdapter(new ArrayList<PersonTeamValueObject>());
        rv.setAdapter(viewAdapter);

        return v;
    }

    //Fragment의 View가 완성됐을 때 호출
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new PersonTeamAsyncTask().execute(personOrTeam, pageState);
    }

    //동영상 썸네일을 반드시 해제한다.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewAdapter != null) {
            viewAdapter.releaseLoaders();
        }
    }
    //어댑터에서 구인 구직에 대한 ValueObject객체를 담고 있는 배열객체

    class PersonTeamAsyncTask extends AsyncTask<Integer, Void, ArrayList<PersonTeamValueObject>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<PersonTeamValueObject> doInBackground(Integer... types) {
            int urlType = types[0];
            int pageValue = types[1];
            String requestURL = "";
            Response response = null;
            try {
                //GET방식
                if (urlType == 0) { //밴드요청
                    requestURL = PickCommon.TARGET_URL + String.format(PickCommon.teamPath, pageValue);
                } else { //개인요청
                    requestURL = PickCommon.TARGET_URL + String.format(PickCommon.personsPath, pageValue);
                }
                //연결
                OkHttpClient toServer = PickCommon.getOkHttpClient();
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("u_id", PropertyPickManager.getPickInstance().getUserID());
                builder.add("u_type", String.valueOf(urlType));
                FormBody formBody = builder.build();
                //요청
                Request request = new Request.Builder()
                        .url(requestURL)
                        .post(formBody)
                        .build();
                //응답
                response = toServer.newCall(request).execute();
                boolean flag = response.isSuccessful();
                ResponseBody resBody = response.body();

                if (flag) { //http req/res 성공
                    return PickJSONParsor.parsePersonTeamValueObject(resBody.string());
                } else { //실패시 정의

                }
            } catch (Exception e) {
                Log.e("요청중에러", "팀/개인리스트", e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }

            return null;
        }


        // 강사님 페이지 네이션 하신 부분,
        // 이 예제 중요함~!!
        @Override
        protected void onPostExecute(ArrayList<PersonTeamValueObject> personTeamValueObjects) {
            super.onPostExecute(personTeamValueObjects);
            if (personTeamValueObjects != null && personTeamValueObjects.size() > 0) {
                viewAdapter.addItemsData(personTeamValueObjects);
                viewAdapter.notifyDataSetChanged();
                rv.setAdapter(viewAdapter);
            }
        }
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<PersonTeamValueObject> items;
        HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
        VideoThumbnailListener thumbnailListener;
        boolean isBookMark;

        public RecyclerViewAdapter(ArrayList<PersonTeamValueObject> returnedItems) {
            items = returnedItems;
            thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
            thumbnailListener = new VideoThumbnailListener();
        }

        public void addItemsData(ArrayList<PersonTeamValueObject> valueObjects) {
            if (valueObjects != null && valueObjects.size() > 0) {
                items.addAll(valueObjects);
            }
        }

        //프래그먼트가 종료되면 반드시 자원을 반납한다.
        public void releaseLoaders() {
            for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
                loader.release();
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder viewHolder = null;
            switch (viewType) {
                /* 보는 방식 변하게 하기 위해 구분 */
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_video_main_fragment, parent, false);
                    return new ListViewHolder(view);

                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_list_main_fragment, parent, false);
                    return new ListViewHolder(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            switch (type) {
                case 0:
                    return 0;
                case 1:
                    return 1;
            }
            return 0;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

            final PersonTeamValueObject entityObject = items.get(position);
            Log.e("onBindvideoKey", entityObject.videoKey + "");

            final ListViewHolder holder = (ListViewHolder) viewHolder;
            String videoTag = (String) holder.youTubeThumbnailView.getTag();

            //비디오 태그가 존재하지 않으면 강제로 초기화 하여 Load한다.
            if (videoTag == null || videoTag.length() <= 0) {
                holder.youTubeThumbnailView.setTag(entityObject.videoKey);
                holder.youTubeThumbnailView.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
            }

            YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.youTubeThumbnailView);

            if (loader == null) { //태그로 부착함.
                holder.youTubeThumbnailView.setTag(entityObject.videoKey);
            } else {
                holder.youTubeThumbnailView.setImageResource(R.drawable.loading_thumbnail);
                //유튜브에서 썸네일을 가져와 로드 한다.
                loader.setVideo(entityObject.videoKey);
            }
            if (entityObject.type == 0) {
                holder.mType.setText("밴드");
            } else {
                holder.mType.setText("개인");
            }
            holder.mBandName.setText(entityObject.name);
            int partSize = entityObject.parts.size();
            if (partSize > 0) {
                String writePart = "";
                for (int i = 0; i < partSize; i++) {
                    if ((i + 1) == partSize) {
                        writePart += (entityObject.parts.get(i));
                    } else {
                        writePart += (entityObject.parts.get(i) + ",");
                    }
                }
                holder.mPart.setText(writePart);
            } else {
                holder.mPart.setText("파트없음");
            }
            holder.mLocation.setText(entityObject.areaDo + " " + entityObject.areaGu); //주소가 넘어오지 않음

            final ImageButton tempBookMark = holder.mBookMark;
            holder.mBookMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isBookMark) {
                        tempBookMark.setBackgroundResource(R.drawable.fav_select_btn);
                        isBookMark = false;
                    } else {
                        tempBookMark.setBackgroundResource(R.drawable.star_fav_btn);
                        isBookMark = true;
                    }
                }
            });

            holder.youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(owner, CustomerItemDetailActivity.class);
                    intent.putExtra("personOrTeamInfo", entityObject);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        //유튜브영상에서 썸네일을 가져오기 위함.
        private final class VideoThumbnailListener implements
                YouTubeThumbnailView.OnInitializedListener,
                YouTubeThumbnailLoader.OnThumbnailLoadedListener {

            @Override
            public void onInitializationSuccess(
                    YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader loader) {

                loader.setOnThumbnailLoadedListener(this);
                thumbnailViewToLoaderMap.put(thumbnailView, loader);
                thumbnailView.setImageResource(R.drawable.loading_l);
                String videoId = (String) thumbnailView.getTag();
                loader.setVideo(videoId);
            }

            @Override
            public void onInitializationFailure(
                    YouTubeThumbnailView thumbnailView, YouTubeInitializationResult loader) {
                thumbnailView.setImageResource(R.drawable.error_l);
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView thumbnailView, String videoId) {
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                thumbnailView.setImageResource(R.drawable.error_s);
            }
        }

        public class ListViewHolder extends RecyclerView.ViewHolder {
            public final YouTubeThumbnailView youTubeThumbnailView;
            public final TextView mType;
            public final TextView mBandName;
            public final TextView mPart;
            public final TextView mLocation;
            public final ImageButton mBookMark;

            public ListViewHolder(View view) {
                super(view);
                youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youthumbnail_thumbnail);
                mType = (TextView) view.findViewById(R.id.item_type);
                mBandName = (TextView) view.findViewById(R.id.item_name);
                mPart = (TextView) view.findViewById(R.id.item_part);
                mLocation = (TextView) view.findViewById(R.id.item_location);
                mBookMark = (ImageButton) view.findViewById(R.id.book_mark_icon);
            }
        }
    }
}