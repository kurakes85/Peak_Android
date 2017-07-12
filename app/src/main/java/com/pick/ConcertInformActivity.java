package com.pick;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *  공연포스터에 관련된 화면
 */
public class ConcertInformActivity extends BaseActivity {

    ConcertRecyclerViewAdapter concertRecyclerViewAdapter;
    RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout; // 스와이프 변수 선언

    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concert_poster);
        setLayouts();

        //toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.concert_inform_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar conInform = getSupportActionBar();
        conInform.setDisplayHomeAsUpEnabled(true);
        conInform.setTitle("공연 정보");


       concertRecyclerViewAdapter = new ConcertRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(concertRecyclerViewAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //리플레시가 되면 들어올 데이터를 넣어준다.
                //new AsyncConcertJSONList().execute(String.valueOf(page));
                mSwipeRefreshLayout.setRefreshing(false);
                //데이터가 들어오면 리플레시 스톱, 지금은 그냥 정지 시킴.
            }
        });

        new AsyncConcertJSONList().execute("1");
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

    private void setLayouts() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setGridRecyclerView();// 초기에 그리드뷰로 나온다.
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(concertRecyclerViewAdapter);
    }

    private void setGridRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    //Ansync Task 시작.
    // 댓글목록 가져오기
    public class AsyncConcertJSONList extends AsyncTask<String, Integer, ConcertEntity> {
        @Override
        protected ConcertEntity doInBackground(String... args) {
            Response response = null;
            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(String.format(
                                NetworkDefineConstant.GET_CONCERT_LIST,
                                args[0]))
                        .build();
                response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                int responseCode = response.code();
                if (responseCode >= 400) return null;
                if (flag) {
                    return ParseDataParseHandler.getJSONConcertEntity(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                Log.e("connectionFail", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("connectionFail", uee.toString());
            } catch (Exception e) {
                Log.e("connectionFail", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ConcertEntity result) {
            if (result != null && result.data.size() > 0) {
               // page = result.page;

                concertRecyclerViewAdapter.add(result.data);
                concertRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}