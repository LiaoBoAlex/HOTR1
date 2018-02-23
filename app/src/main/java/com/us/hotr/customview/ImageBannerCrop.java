package com.us.hotr.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.us.hotr.R;
import com.us.hotr.util.Tools;


/**
 * Created by Mloong on 2017/8/31.
 */

public class ImageBannerCrop extends BaseIndicatorBanner<String, ImageBanner> {

    private BannerClickListener mListener;
    private double ratio;
    private int placehoderResource = 0;

    public ImageBannerCrop(Context context) {
        this(context, null, 0);
    }

    public ImageBannerCrop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBannerCrop(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPlacehoderResource(int resource){
        placehoderResource = resource;
    }

    public void setRatio(double ratio){
        this.ratio = ratio;
    }

    public void setBannerItemClickListener(BannerClickListener l){
        mListener = l;
    }

    @Override
    public View onCreateItemView(final int position) {
        View inflate = View.inflate(mContext, R.layout.item_banner_image_crop, null);
        final ImageView iv = (ImageView) inflate.findViewById(R.id.iv);

        int itemWidth = Tools.getScreenWidth(mContext.getApplicationContext());
        int itemHeight = (int) (itemWidth * ratio);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));

        String imgUrl = mDatas.get(position);

        if (imgUrl != null && !imgUrl.isEmpty()) {
            if(placehoderResource == 0)
                placehoderResource = R.drawable.placeholder_banner;
            Glide.with(mContext).load(imgUrl).placeholder(placehoderResource).error(placehoderResource).fitCenter().into(iv);
        }
        if(mListener!=null){
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onBannerItemClicked(position);
                }
            });
        }

        return inflate;
    }

    public interface BannerClickListener {
        void onBannerItemClicked(int position);
    }
}
