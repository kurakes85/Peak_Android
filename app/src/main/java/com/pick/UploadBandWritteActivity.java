package com.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by ccei on 2016-08-12.
 *  밴드 글쓰기 화면
 */
public class UploadBandWritteActivity extends AppCompatActivity {

    private EditText editText;
    private Spinner mSpinner = null;
    private ArrayAdapter<String> mSpinnerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_band_writte);

        ImageView videoBtn = (ImageView) findViewById(R.id.band_write_video_btn);
        Button addBtn = (Button) findViewById(R.id.band_add_button);
        Button cancelBtn = (Button) findViewById(R.id.band_cancel_button);


        Toolbar toolbar = (Toolbar)findViewById(R.id.band_upload_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar conInform = getSupportActionBar();
        conInform.setDisplayHomeAsUpEnabled(true);
        conInform.setTitle("밴드 구인 정보");


        //밴드 비디오버튼 클릭 리스너
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpAddVideoDialog f = new PopUpAddVideoDialog();
                f.show(getSupportFragmentManager(),"");
                Toast.makeText(UploadBandWritteActivity.this, "밴드 버튼 클릭", Toast.LENGTH_LONG).show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UploadBandWritteActivity.this, "밴드 영상 올리기", Toast.LENGTH_SHORT).show();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UploadBandWritteActivity.this,MainActivity.class);
                Toast.makeText(UploadBandWritteActivity.this, "메인으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            }
        });



        editText = (EditText) findViewById(R.id.band_writte_text);
        editText.requestFocus();


        //스피너 부분
        //장르 스피너
        mSpinner = (Spinner) findViewById(R.id.band_spinner_genre);
        mSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                (String[]) getResources().getStringArray(R.array.sp_genre));
        //dropdown 모양 설정 부분.
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //스피너에 아답터를 연결
        mSpinner.setAdapter(mSpinnerAdapter);

        //지역 스피너
        mSpinner = (Spinner) findViewById(R.id.band_spinner_city);
        mSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                (String[])getResources().getStringArray(R.array.city));
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        //구 스피너
        // 지역의 스피너 값에 따라 구의 내용이 바뀌어야 함,,
        mSpinner = (Spinner) findViewById(R.id.band_spinner_gu);
        mSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                (String[])getResources().getStringArray(R.array.dae_jeon_gu));
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        //지원파트
        mSpinner = (Spinner) findViewById(R.id.band_spinner_part);
        mSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                (String[])getResources().getStringArray(R.array.candidate));
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        //에디트 텍스트 처음 시작되는 키보드 숨김
        InputMethodManager immhide = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

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
}
