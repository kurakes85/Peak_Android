package com.pick;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 리스트 아답터 부분
 */
public class RequestAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity activity;
    private String[] data;

    public RequestAdapter(Activity a, String[] d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {

        }
        ImageView imgView = (ImageView) v.findViewById(R.id.title_img);
        TextView textView = (TextView) v.findViewById(R.id.request_list_view_text);
        textView.setText("item" + position);
        return v;
    }
}
