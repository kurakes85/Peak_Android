package com.pick;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by ccei on 2016-08-12.
 */
public class RequestResumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_resume_application);

        Button accept = (Button) findViewById(R.id.accept_button);
        Button refuse = (Button) findViewById(R.id.refuse_button);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RequestResumeActivity.this, "신청을 수락하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RequestResumeActivity.this, "신청을 거절 하였습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        //툴바 뒤로가기 버튼
        Toolbar toolbar = (Toolbar)findViewById(R.id.request_resume_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle("신청서");
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

}
