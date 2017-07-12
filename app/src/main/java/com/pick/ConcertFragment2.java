package com.pick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ccei on 2016-08-12.
 */
public class ConcertFragment2 extends Fragment {

    public ConcertFragment2(){

    }

    public static ConcertFragment2 newInstance(){
        ConcertFragment2 fragment = new ConcertFragment2();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.concert_detail_2,container,false);
    }
}
