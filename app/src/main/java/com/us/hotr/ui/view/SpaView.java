package com.us.hotr.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.ui.activity.massage.SpaActivity;

/**
 * Created by liaobo on 2017/12/27.
 */

public class SpaView extends FrameLayout {
    private TextView tvName, tvAddress, tvAppointment;
    private ImageView ivAvatar;

    public SpaView(Context context) {
        super(context);
        init();
    }

    public SpaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_spa, this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAddress = (TextView) findViewById(R.id.tv_place);
        tvAppointment = (TextView) findViewById(R.id.tv_number);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
    }

    public void setData(final Spa spa, int position){
        if(position%2==0) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ivAvatar.getLayoutParams();
            lp.setMargins(12, 0, 6, 0);
            ivAvatar.setLayoutParams(lp);
        }
        else {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ivAvatar.getLayoutParams();
            lp.setMargins(6, 0, 12, 0);
            ivAvatar.setLayoutParams(lp);
        }
        Glide.with(getContext()).load(spa.getMassageLogo()).dontAnimate().placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
        tvAddress.setText(spa.getLandmarkName());
        tvAppointment.setText(String.format(getContext().getString(R.string.masseur_appointment), spa.getOrder_num()));
        tvName.setText(spa.getMassageName());
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SpaActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, spa.getKey());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }

    public void enableEdit(boolean isEdit){
    }
}
