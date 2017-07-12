package com.pick.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pick.R;

/**
 * 스플래시 화면 버튼 클릭 화면
 */
public class SplashJoin extends AppCompatActivity {
    String pORb = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_join_activity);

        Button pBtn = (Button) findViewById(R.id.person_btn);
        Button bBtn = (Button) findViewById(R.id.band_btn);


        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pORb = "1";
                Intent intent = new Intent(SplashJoin.this,SplashJoinIndividual.class);
                intent.putExtra("pORb", pORb);
                startActivity(intent);
                finish();
            }
        });

        bBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pORb = "0";
                Intent intent = new Intent(SplashJoin.this,SplashJoinBand.class);
                intent.putExtra("pORb", pORb);
                startActivity(intent);
                finish();
            }
        });
    }
}
