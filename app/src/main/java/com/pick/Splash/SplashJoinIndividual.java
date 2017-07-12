package com.pick.Splash;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

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

/*
*
 *  개인 회원가입 부분,
 */
public class SplashJoinIndividual extends AppCompatActivity {


    EditText bandNameEdit;
    ToggleButton genderToggle;
    AppCompatSeekBar seekbar;
    Spinner genreSpinner;
    Spinner citySpinner;
    Spinner doSpinner;
    Spinner partSpinner;

    Button  memberInsertBtn;

    //올릴 정보 세팅
    PersonTeamValueObject personObj = new PersonTeamValueObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_individual);

        Intent intent = getIntent();
        personObj.type = Integer.parseInt(intent.getStringExtra("pORb"));
        bandNameEdit = (EditText) findViewById(R.id.bandNameEdit);

        //토글버튼 액션 부분
        genderToggle = (ToggleButton) findViewById(R.id.toggleButton);
        genderToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Resources res = getResources();
                    if(b){ //남자
                        genderToggle.setBackgroundDrawable(res.getDrawable(R.drawable.gen2));
                        personObj.gender = "m";
                    }else{ //여자
                        genderToggle.setBackgroundDrawable(res.getDrawable(R.drawable.gen));
                        personObj.gender = "f";
                    }
            }
        });

        // Seekbar 클릭 선언
        seekbar = (AppCompatSeekBar) findViewById(R.id.seekbar_individual);
        seekbar.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
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

        //장르 스피너
        genreSpinner = (Spinner) findViewById(R.id.genreSpinner);
        genreSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGenre = (String)adapterView.getItemAtPosition(i);
                int inputedSize = personObj.genres.size();
                if(inputedSize > 0){
                    for(int j = 0 ; i < inputedSize ; j++){
                        String item = personObj.genres.get(j);
                        if(item.equals(selectedGenre)){
                            return;
                        }else{
                            personObj.genres.add(selectedGenre);
                        }
                    }

                }else{
                    personObj.genres.add(selectedGenre);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String genre = (String) adapterView.getItemAtPosition(0);
                if( !(personObj.genres.size() > 0)){
                    personObj.genres.add(genre);
                }
            }
        });

        //도시 스피너
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        citySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                personObj.areaDo = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                personObj.areaDo = (String)adapterView.getItemAtPosition(0);
            }
        });

        //동구,용산구 할때 구 스피너
        doSpinner = (Spinner) findViewById(R.id.DoSpinner);
        doSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
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
        partSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPart = (String)adapterView.getItemAtPosition(i);
                int inputedSize = personObj.parts.size();
                if(inputedSize > 0){
                    for(int j = 0 ; i < inputedSize ; j++){
                        String item = personObj.parts.get(j);
                        if(item.equals(selectedPart)){
                            return;
                        }else{
                            personObj.parts.add(selectedPart);
                        }
                    }

                }else{
                    personObj.parts.add(selectedPart);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String part = (String) adapterView.getItemAtPosition(0);
                if( !(personObj.genres.size() > 0)){
                    personObj.genres.add(part);
                }
            }
        });

        //저장하기 버튼
        memberInsertBtn = (Button) findViewById(R.id.btn_join_person);
        memberInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                personObj.name = bandNameEdit.getText().toString().trim();

               //유효성 체크를 이런식으로 꼭 해주기 바람.
                if(personObj.genres.size() <= 0){
                    Toast.makeText(SplashJoinIndividual.this, "장르를 선택해 주세요", Toast.LENGTH_SHORT).show();
                    genreSpinner.requestFocus();
                    return;
                }
                if(personObj.parts.size() <= 0){
                    Toast.makeText(SplashJoinIndividual.this, "파트를 선택해 주세요", Toast.LENGTH_SHORT).show();
                    partSpinner.requestFocus();
                    return;
                }
                if(personObj.name == null || personObj.name.length() <= 0){
                    Toast.makeText(SplashJoinIndividual.this, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    bandNameEdit.requestFocus();
                    return;
                }
                if(personObj.areaDo == null || personObj.areaDo.length() <= 0){
                    Toast.makeText(SplashJoinIndividual.this, "사시는 곳을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    citySpinner.requestFocus();
                    return;
                }
                //입력한다.
                new PersonalInsertAsyncTask().execute(personObj);
            }
        });
    }

    class PersonalInsertAsyncTask extends AsyncTask<PersonTeamValueObject, Void, String>{
        String uuid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(PersonTeamValueObject... personTeamValueObjects) {
            PersonTeamValueObject insertObj = personTeamValueObjects[0];

            Response response = null; //응답이 도착
            String userID = "";
            try {
                //연결
                OkHttpClient toServer = PickCommon.getOkHttpClient();

                //POST방식의 요청보디를 구현
                FormBody.Builder builder = new FormBody.Builder();

                uuid = UUID.randomUUID().toString().replace('-', 'A');

                builder.add("uuid", uuid);
                builder.add("type",String.valueOf(personObj.type));
                builder.add("name", insertObj.name);
                builder.add("age", insertObj.age);
                builder.add("gender", insertObj.gender);

                String genres = "";
                for(int i = 0 ; i < insertObj.genres.size(); i++){
                    genres += ("," + insertObj.genres.get(i));
                }
                builder.add("genre", genres);

                String parts = "";
                for(int i = 0 ; i < insertObj.parts.size(); i++){
                    parts += ("," + insertObj.parts.get(i));
                }
                builder.add("part", parts);

                builder.add("area_do", insertObj.areaDo);
                builder.add("area_gu",insertObj.areaGu);

                RequestBody reqBody = builder.build();

                //최종 요청구성
                Request request = new Request.Builder()
                                  .url(PickCommon.TARGET_URL + PickCommon.pORbJoin)
                                  .post(reqBody)
                                  .build();

                 response = toServer.newCall(request).execute();
                 String  jsonResult = response.body().string();
                 boolean flag = response.isSuccessful();
                 if(flag){

                     JSONObject returedJSON = new JSONObject(jsonResult);
                     Log.e("member", jsonResult);
                     if(returedJSON.getString("msg").equalsIgnoreCase("success")){
                        JSONObject data = returedJSON.getJSONObject("data");
                        userID = data.getString("_id");
                     }
                 }else{

                 }
                return userID;
            } catch (Exception e) {
                Log.e("개인회원 입력 중 에러", e.toString(), e);
            } finally {
                if(response != null){
                    response.close();
                }
            }
            return userID;
        }

        @Override
        protected void onPostExecute(String userID) {
            super.onPostExecute(userID);

            if(!userID.equals("")){
                PropertyPickManager.getPickInstance().setUUID(uuid);
                PropertyPickManager.getPickInstance().setUserID(userID);
                PropertyPickManager.getPickInstance().setBandORPersonal(String.valueOf(personObj.type));
            }
            Intent intent = new Intent(SplashJoinIndividual.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
