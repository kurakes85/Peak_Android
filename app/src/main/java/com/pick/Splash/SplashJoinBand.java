package com.pick.Splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.pick.MainActivity;
import com.pick.R;
import com.pick.common.PersonTeamValueObject;
import com.pick.common.PickCommon;
import com.pick.common.PropertyPickManager;

import org.json.JSONObject;

import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 밴드 회원가입
 */
public class SplashJoinBand extends AppCompatActivity {
    EditText bandNameEdit;
    EditText bandTeamGroupEdit;
    AppCompatSeekBar seekbarBand;
    Spinner genreSpinner;
    Spinner citySpinner;
    Spinner doSpinner;
    Spinner partSpinner;

    Button bandInserJoinBtn;

    // 정보를 올리기 전에 셋팅을 한다.
    PersonTeamValueObject personObj = new PersonTeamValueObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_band);

        Intent intent = getIntent();
        personObj.type = Integer.parseInt(intent.getStringExtra("pORb"));
        bandNameEdit = (EditText) findViewById(R.id.bandNameEdit);
        bandTeamGroupEdit = (EditText) findViewById(R.id.bandTeamText);

        //seekbar 들어가는 부분,

        seekbarBand = (AppCompatSeekBar) findViewById(R.id.join_seek_bar_band);
        seekbarBand.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                personObj.age = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        genreSpinner = (Spinner) findViewById(R.id.genreSpinner);
        genreSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGenre = (String) adapterView.getItemAtPosition(i);
                int inputedSize = personObj.genres.size();
                if (inputedSize > 0) {
                    for (int j = 0; i < inputedSize; j++) {
                        String item = personObj.genres.get(j);
                        if (item.equals(selectedGenre)) {
                            return;
                        } else {
                            personObj.genres.add(selectedGenre);
                        }
                    }

                } else {
                    personObj.genres.add(selectedGenre);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String genre = (String) adapterView.getItemAtPosition(0);
                if (!(personObj.genres.size() > 0)) {
                    personObj.genres.add(genre);
                }
            }
        });

        //도시 스피너
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        citySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                personObj.areaDo = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                personObj.areaDo = (String) adapterView.getItemAtPosition(0);
            }
        });

        //동구,용산구 할때 구 스피너
        doSpinner = (Spinner) findViewById(R.id.DoSpinner);
        doSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                personObj.areaGu = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                personObj.areaGu = (String) adapterView.getItemAtPosition(0);
            }
        });

        // 파트 스피너
        partSpinner = (Spinner) findViewById(R.id.partSpinner);
        partSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPart = (String) adapterView.getItemAtPosition(i);
                int inputedSize = personObj.parts.size();
                if (inputedSize > 0) {
                    for (int j = 0; i < inputedSize; j++) {
                        String item = personObj.parts.get(j);
                        if (item.equals(selectedPart)) {
                            return;
                        } else {
                            personObj.parts.add(selectedPart);
                        }
                    }

                } else {
                    personObj.parts.add(selectedPart);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String part = (String) adapterView.getItemAtPosition(0);
                if (!(personObj.genres.size() > 0)) {
                    personObj.genres.add(part);
                }
            }
        });


        // 밴드 저장하기 버튼
        bandInserJoinBtn = (Button) findViewById(R.id.bandInserJoinBtn);
        bandInserJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                personObj.name = bandNameEdit.getText().toString().trim();
                personObj.bandTeamGroup = bandTeamGroupEdit.getText().toString().trim();


                // 유효성 체크
                if (personObj.genres.size() <= 0) {
                    Toast.makeText(SplashJoinBand.this, "장르를 선택해 주세요", Toast.LENGTH_SHORT).show();
                    genreSpinner.requestFocus();
                    return;
                }
                if (personObj.parts.size() <= 0) {
                    Toast.makeText(SplashJoinBand.this, "파트를 선택해 주세요", Toast.LENGTH_SHORT).show();
                    partSpinner.requestFocus();
                    return;
                }
                if (personObj.name == null || personObj.name.length() <= 0) {
                    Toast.makeText(SplashJoinBand.this, "팀명을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    bandNameEdit.requestFocus();
                    return;
                }
                if (personObj.areaDo == null || personObj.areaDo.length() <= 0) {
                    Toast.makeText(SplashJoinBand.this, "사시는 곳을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    citySpinner.requestFocus();
                    return;
                }
                if (personObj.bandTeamGroup == null || personObj.bandTeamGroup.length() <= 0) {
                    Toast.makeText(SplashJoinBand.this, "밴드맴버 구성을 입력해주세요", Toast.LENGTH_SHORT).show();
                    bandTeamGroupEdit.requestFocus();
                }
                new BandInsertAsyncTask().execute(personObj);
            }
        });

    }

    class BandInsertAsyncTask extends AsyncTask<PersonTeamValueObject, Void, String> {

        String uuid;
        Response response = null; //응답이 도착

        @Override
        protected String doInBackground(PersonTeamValueObject... personTeamValueObjects) {
            PersonTeamValueObject insertObj = personTeamValueObjects[0];

            Response response = null;
            String userID = "";
            try {
                //OKHTTP 연결
                OkHttpClient toServer = PickCommon.getOkHttpClient();

                //POST방식의 요청보디를 구현
                FormBody.Builder builder = new FormBody.Builder();

                uuid = UUID.randomUUID().toString().replace('-', 'A');

                builder.add("uuid", uuid);
                builder.add("type", String.valueOf(personObj.type));
                builder.add("name", insertObj.name);
                builder.add("member", insertObj.bandTeamGroup);
                builder.add("age", insertObj.age);

                String genres = "";
                for (int i = 0; i < insertObj.genres.size(); i++) {
                    genres += ("," + insertObj.genres.get(i));
                }
                builder.add("genre", genres);

                String parts = "";
                for (int i = 0; i < insertObj.parts.size(); i++) {
                    parts += ("," + insertObj.parts.get(i));
                }
                builder.add("part", parts);

                builder.add("area_do", insertObj.areaDo);
                builder.add("area_gu", insertObj.areaGu);

                RequestBody reqBody = builder.build();

                Request request = new Request.Builder()
                        .url(PickCommon.TARGET_URL + PickCommon.pORbJoin)
                        .post(reqBody)
                        .build();

                response = toServer.newCall(request).execute();
                String jsonResult = response.body().string();
                boolean flag = response.isSuccessful();
                if (flag) {

                    JSONObject returedJSON = new JSONObject(jsonResult);
                    Log.e("member", jsonResult);
                    if (returedJSON.getString("msg").equalsIgnoreCase("success")) {
                        JSONObject data = returedJSON.getJSONObject("data");
                        userID = data.getString("_id");
                    }
                } else {

                }
                return userID;
            } catch (Exception e) {
                Log.e("개인회원 입력 중 에러", e.toString(), e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return userID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String userId) {
            super.onPostExecute(userId);
            // 유저아이디 설정 세팅
            PropertyPickManager.getPickInstance().setUserID(userId);
            Log.e("상우잘생김", PropertyPickManager.getPickInstance().getUserID()+" ");
            Intent intent = new Intent(SplashJoinBand.this, MainActivity.class);
            startActivity(intent);
            // 유저아이디 불러올때
//            PropertyPickManager.getPickInstance().getUserID()
        }
    }
}


