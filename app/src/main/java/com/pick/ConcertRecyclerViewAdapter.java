package com.pick;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-08-05.
 */
public class ConcertRecyclerViewAdapter extends RecyclerView.Adapter<ConcertRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<ConcertDataEntity> datas;
    private Context mContext;

    public ConcertRecyclerViewAdapter(Context context) {
        super();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        datas = new ArrayList<ConcertDataEntity>();
    }

    public void add(ArrayList<ConcertDataEntity> datas) {
        this.datas = datas;
    }

    @Override
    public ConcertRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ConcertRecyclerViewAdapter.ViewHolder holder, final int position) {
        //여기에다 사진이랑 그림을 갖고 와야하는디...
        holder.title.setText(datas.get(position).subj);
        Glide.with(mContext).load(datas.get(position).img_url).into(holder.poster);
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //클릭 이벤트
                Intent intent = new Intent(v.getContext(),ConcertDetailActivity.class);
                mContext.startActivity(intent);
                Toast.makeText(mContext, datas.get(position).subj, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView poster;
        CardView cardView;
        RecyclerView recyclerView;

        //뷰 홀더
        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.poster_title);
            poster = (ImageView) v.findViewById(R.id.poster_image_view);
            cardView = (CardView) v.findViewById(R.id.cardView);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        }
    }
}
