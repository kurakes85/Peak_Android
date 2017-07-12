package com.pick.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.pick.MainActivity;
import com.pick.R;
import com.pick.common.PickCommon;
import com.pick.common.PropertyPickManager;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 스플레쉬 화면 진영이는 바나나우유 랑 썸탄다!
 * 2017.07.12, 진영이 바나나랑 헤어짐...ㅠㅠ
 */
public class SpalshLayout extends Activity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        //회원가입이 되어있는지 체크
        final String userID = PropertyPickManager.getPickInstance().getUserID();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!userID.equals("")) {  //회원이 등록되어 있다면
                    new LoginAsynkTast().execute(userID);
                } else {
                    Intent targetIntent = new Intent(SpalshLayout.this, SplashJoin.class);
                    startActivity(targetIntent);
                    finish();
                }

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    //로그인
    class LoginAsynkTast extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... userID) {

            String result = "";
            Response response = null;

            try {
                Log.e("회원ObjectID", userID[0]);
                OkHttpClient toServer = PickCommon.getOkHttpClient();
                FormBody formBody = new FormBody.Builder()
                        .add("u_id", userID[0])
                        .build();
                Request request = new Request.Builder()
                        .url(PickCommon.TARGET_URL + PickCommon.login)
                        .post(formBody)
                        .build();
                response = toServer.newCall(request).execute();
                boolean flag = response.isSuccessful();
                String returedJSON = response.body().string();
                if (flag) {
                    JSONObject jsonObject = new JSONObject(returedJSON);
                    result = jsonObject.getString("msg");
                    return result;
                }
            } catch (Exception e) {
                Log.e("로그인 중 에러", e.toString(), e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("success")) {
                Intent intent = new Intent(SpalshLayout.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(SpalshLayout.this, " 로그인에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
