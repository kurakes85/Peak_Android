package com.pick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ccei on 2016-08-16.
 */
public class RequestFragmentMe extends Fragment{

    public RequestFragmentMe(){

    }

    public static RequestFragmentMe newInstance(){
        RequestFragmentMe fragment = new RequestFragmentMe();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.request_fragment_me,container,false);
    }
}
