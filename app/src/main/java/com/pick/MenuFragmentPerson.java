package com.pick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ccei on 2016-08-13.
 */
public class MenuFragmentPerson extends Fragment {

    public MenuFragmentPerson() {
        // Required empty public constructor
    }

    TextView first, second;
    ImageView choose;
    Button gender;
    Button saveBtn;
    EditText team;

    boolean select = false;

    public static MenuFragmentPerson newInstance() {
        MenuFragmentPerson mFragment = new MenuFragmentPerson();
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_fragment_input,container,false);


        // 상우씨의 투혼이 들어있는 부분.
        first = (TextView) view.findViewById(R.id.btn_first);
        second = (TextView) view.findViewById(R.id.btn_second);
        gender = (Button) view.findViewById(R.id.btn_gender);
        team = (EditText) view.findViewById(R.id.edit_team);
        choose = (ImageView) view.findViewById(R.id.img_choose);
        saveBtn = (Button) view.findViewById(R.id.menu_save_btn);


        choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!select) { // 밴드 버튼 눌렀을 때
                    choose.setImageResource(R.drawable.toggle_band);
                    first.setText("밴드명");
                    second.setText("팀구성");
                    gender.setVisibility(View.VISIBLE);
                    team.setVisibility(View.GONE);
                    select = true;
                } else { // 사람 버튼 눌렀을 때
                    choose.setImageResource(R.drawable.toggle_user);
                    first.setText("이름");
                    second.setText("성별");
                    gender.setVisibility(View.GONE);
                    team.setVisibility(View.VISIBLE);
                    select = false;
                }
            }
        });

        //setOnClick 저장 리스너,

        return view;


    }
}

