package com.us.hotr.customview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.ImageViewerActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaobo on 2017/12/7.
 */

public class HorizontalImageAdapter extends RecyclerView.Adapter<HorizontalImageAdapter.ViewHolder> {
    private Context mContext;
    private List<String> photoes = new ArrayList<>();

    public HorizontalImageAdapter(Context mContext, List<String> photoes) {
        this.mContext = mContext;
        this.photoes = photoes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position == photoes.size()-1)
            holder.mImageView.setPadding(0,0,0,0);
        else
            holder.mImageView.setPadding(0,0,12,0);
        Glide.with(mContext).load(photoes.get(position)).placeholder(R.drawable.holder_merchant_image).error(R.drawable.holder_merchant_image).into(holder.mImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, MasseurAlbumActivity.class));
                Intent i = new Intent(mContext, ImageViewerActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_DATA, (Serializable) photoes);
                b.putInt(Constants.PARAM_ID, position);
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_DOCTOR);
                i.putExtras(b);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
