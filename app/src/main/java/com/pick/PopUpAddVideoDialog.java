package com.pick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by ccei on 2016-08-19.
 */
public class PopUpAddVideoDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,R.style.MyDIalogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_writte,container,false);
        Button gBtn = (Button) view.findViewById(R.id.gallery_popup_button);
        Button pBtn = (Button) view.findViewById(R.id.pic_popup_button);

        gBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "갤러리에서 영상 찾기", Toast.LENGTH_SHORT).show();
            }
        });

        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "영상 촬영 시작", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int width = getResources().getDimensionPixelSize(R.dimen.band_video_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.band_video_dialog_height);
        getDialog().getWindow().setLayout(width,height);



    }
}
