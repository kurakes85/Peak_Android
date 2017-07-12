package com.pick;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 상우씨의 Parse 예제
 */
public class ParseDataParseHandler {
    public static ConcertEntity getJSONConcertEntity(StringBuilder buf) {

        // 전체
        JSONObject jsonObject = null;

        // data
        JSONArray jsonArray = null;
        ConcertEntity jsonPostData;
        ArrayList<ConcertDataEntity> jsonPostList = null;

        try {
            jsonObject = new JSONObject(buf.toString());

            jsonArray = jsonObject.getJSONArray("data");
            jsonPostList = new ArrayList<ConcertDataEntity>();
            int jsonArrSize = jsonArray.length();
            for (int i = 0; i < jsonArrSize; i++) {
                ConcertDataEntity data = new ConcertDataEntity();
                JSONObject jData = jsonArray.getJSONObject(i);

                  data._id = jData.getString("_id");
                  data.type = jData.getString("type");
                  data.subj = jData.getString("subj");
                  data.addr = jData.getString("addr");
                  data.date = jData.getString("date");
                  data.img_url = jData.getString("img_url");

                jsonPostList.add(data);
            }

            jsonPostData = new ConcertEntity(jsonPostList);
            jsonPostData.total = jsonObject.getInt("total");
            jsonPostData.page = jsonObject.getString("page");

            return jsonPostData;
        } catch (JSONException je) {
            Log.e("GET:getJSONConcert", "JSON파싱 중 에러발생", je);
        }
        return null;
    }



}