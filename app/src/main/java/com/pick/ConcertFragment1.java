package com.pick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ccei on 2016-08-12.
 */
public class ConcertFragment1 extends Fragment {

    public ConcertFragment1(){

    }

    public static ConcertFragment1 newInstance(){
        ConcertFragment1 fragment = new ConcertFragment1();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fragment 관련된 레이아웃을 넣는다
        return inflater.inflate(R.layout.concert_detail_1,container,false);
    }


}
