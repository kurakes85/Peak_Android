package com.pick;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by ccei on 2016-08-05.
 * 콘서트 그리드 페이지에 관련된 BaseAdaper 입니다.
 */
public abstract class BaseActivity extends ActionBarActivity {

    /*protected ArrayList<String> getDataList(int dataSize) {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 1; i <= dataSize; i++) {
            dataList.add("포스터" + i);
        }
        return dataList;
    }
*/
    protected void startNewActivity(Class<Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
