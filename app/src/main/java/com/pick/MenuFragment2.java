package com.pick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ccei on 2016-08-13.
 */
public class MenuFragment2 extends Fragment{

    public MenuFragment2() {
        // Required empty public constructor
    }

    public static MenuFragment2 newInstance() {
        MenuFragment2 mFragment = new MenuFragment2();
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu_fragment_list, container, false);
    }
}
