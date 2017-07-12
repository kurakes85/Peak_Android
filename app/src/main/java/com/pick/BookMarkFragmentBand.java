package com.pick;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ccei on 2016-08-16.
 */
public class BookMarkFragmentBand extends Fragment {

    public BookMarkFragmentBand() {
        // Required empty public constructor
    }

    public static BookMarkFragmentBand newInstance() {
        BookMarkFragmentBand fragment = new BookMarkFragmentBand();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bookmark_band_fragment, container, false);
    }

}
