package com.pick.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pyoinsoo on 2016-08-13.
 */
public class PickJSONParsor {

    /*
     Person Or Team 데이터 파싱
     */
    public static ArrayList<PersonTeamValueObject> parsePersonTeamValueObject(String responedJSONData) {
        ArrayList<PersonTeamValueObject> personTeams = null;

        JSONObject jsonRoot = null;

        try {
            jsonRoot = new JSONObject(responedJSONData);
            JSONArray datas = jsonRoot.getJSONArray("data");
            int  size = datas.length();
            if( size > 0) {
                personTeams = new ArrayList<>();
                for (int i = 0; i < size; i++) {

                    PersonTeamValueObject valueObject = new PersonTeamValueObject();

                    JSONObject item = datas.getJSONObject(i);

                    valueObject._id = item.getString("_id");
                    valueObject.type = item.getInt("type");

                    JSONArray genre = item.getJSONArray("genre");
                    int genreSize = genre.length();
                    for (int gSize = 0; gSize < genreSize; gSize++) {
                        valueObject.genres.add(genre.getString(gSize));
                    }

                    JSONArray parts = item.getJSONArray("part");
                    int partSize = parts.length();
                    for (int pSize = 0; pSize < partSize; pSize++) {
                        valueObject.parts.add(parts.getString(pSize));
                    }

                    valueObject.cont = item.getString("cont");
                    valueObject.videoKey = item.getString("v_key");
                    valueObject.uId = item.getString("u_id");
                    valueObject.check = item.getString("check");
                    valueObject.name = item.getString("name");
                    valueObject.gender = item.getString("gender");
                    valueObject.age = item.getString("age");
                    valueObject.areaDo = item.getString("area_do");
                    valueObject.areaGu = item.getString("area_gu");

                    personTeams.add(valueObject);
                }
            }

        } catch (JSONException e) {
            Log.e("parsePersonTeam", "파싱중에러", e);
        }
        return personTeams;
    }
}
