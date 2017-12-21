package com.us.hotr.customview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.us.hotr.R;
import com.us.hotr.util.Tools;

/**
 * Created by liaobo on 2017/12/7.
 */

public class PicGridAdapter extends RecyclerView.Adapter<PicGridAdapter.ViewHolder> {

    private int picCount;
    private Context mContext;

    public PicGridAdapter(int picCount, Context mContext) {
        this.picCount = picCount;
        this.mContext = mContext;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int h;
        LinearLayout.LayoutParams layoutParams;
        switch (picCount) {
            case 1:
                h = (int) (Tools.getScreenWidth(mContext) * 0.46);
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                holder.mImageView.setLayoutParams(layoutParams);
                holder.mImageView.setImageResource(R.drawable.placeholder_post1);
                break;
            case 2:
            case 4:
                h = (int) (Tools.getScreenWidth(mContext) * 0.46);
                layoutParams = new LinearLayout.LayoutParams(h, h);
                holder.mImageView.setLayoutParams(layoutParams);
                holder.mImageView.setImageResource(R.drawable.placeholder_post_2);
                break;
            default:
                h = (int) (Tools.getScreenWidth(mContext) * 0.301);
                layoutParams = new LinearLayout.LayoutParams(h, h);
                holder.mImageView.setLayoutParams(layoutParams);
                holder.mImageView.setImageResource(R.drawable.placeholder_post_2);
        }
    }

    @Override
    public int getItemCount() {
        return picCount;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}