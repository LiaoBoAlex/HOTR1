package com.us.hotr.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.ui.activity.massage.SpaActivity;
import com.us.hotr.util.Tools;

/**
 * Created by liaobo on 2018/1/11.
 */

public class SpaBigView extends FrameLayout {
    private TextView tvSpaName, tvSpaTime, tvSpaAddress, tvSpaQuery;
    private ImageView ivSpaAvatar, ivDelete;

    private ItemSelectedListener itemSelectedListener;
    private Spa spa;

    public SpaBigView(Context context) {
        super(context);
        init();
    }

    public SpaBigView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_spa_big, this);
        ivSpaAvatar = (ImageView) findViewById(R.id.iv_hospital_avatar);
        tvSpaName = (TextView) findViewById(R.id.tv_hospital1);
        tvSpaTime = (TextView) findViewById(R.id.tv_hospital2);
        tvSpaAddress = (TextView) findViewById(R.id.tv_hospital3);
        tvSpaQuery = (TextView) findViewById(R.id.tv_query);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
    }

    public void setData(final Spa spa){
        this.spa = spa;
        tvSpaName.setText(spa.getMassageName());
        Glide.with(getContext()).load(spa.getMassageLogo()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivSpaAvatar);
        tvSpaAddress.setText(spa.getCityName() +
                spa.getAreaName() +
                spa.getMassageAddress());
        tvSpaTime.setText(String.format(getContext().getString(R.string.operation_time1),
                Tools.convertTime(spa.getOpenTimeStart()) + "-" + Tools.convertTime(spa.getOpenTimeOver())));
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SpaActivity.class);
                Bundle b= new Bundle();
                b.putLong(Constants.PARAM_ID, spa.getKey());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }

    public void enableEdit(boolean isEdit){
        if (isEdit) {
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setImageResource(R.mipmap.ic_delete_order);
            setTag(false);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((boolean) view.getTag()) {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order);
                        view.setTag(false);
                    } else {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
                        view.setTag(true);
                    }
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected((boolean)view.getTag());
                }
            });
        } else {
            ivDelete.setVisibility(View.GONE);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), SpaActivity.class);
                    Bundle b= new Bundle();
                    b.putLong(Constants.PARAM_ID, spa.getKey());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
        }
    }

    public void ShowQuery(boolean value){
        if(value)
            tvSpaQuery.setVisibility(VISIBLE);
        else
            tvSpaQuery.setVisibility(GONE);
    }

    public void setItemSelectedListener(ItemSelectedListener listener){
        this.itemSelectedListener = listener;
    }
}