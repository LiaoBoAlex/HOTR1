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
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.Arrays;

/**
 * Created by liaobo on 2017/12/27.
 */

public class MasseurView extends FrameLayout {
    private TextView tvName, tvAddress, tvAppointment;
    private ImageView ivAvatar, ivLike;
    private ConstraintLayout clContainer;
    private boolean isCollected;

    public MasseurView(Context context) {
        super(context);
        init();
    }

    public MasseurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_masseur, this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAddress = (TextView) findViewById(R.id.tv_place);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        ivLike = (ImageView) findViewById(R.id.iv_like);
        clContainer = (ConstraintLayout) findViewById(R.id.cl_container);
    }

    public void setData(final Masseur masseur, int position, boolean isWhiteFrame){
        isCollected = masseur.getIs_collected()==1?true:false;
        if(isCollected)
            ivLike.setImageResource(R.mipmap.ic_masseur_liked);
        else
            ivLike.setImageResource(R.mipmap.ic_masseur_like);
        if(!isWhiteFrame){
            if(position%2==0) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) clContainer.getLayoutParams();
                lp.setMargins(12, 6, 6, 6);
                clContainer.setLayoutParams(lp);
            }
            else {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) clContainer.getLayoutParams();
                lp.setMargins(6, 6, 12, 6);
                clContainer.setLayoutParams(lp);
            }
        }else {
            if (position % 2 == 0) {
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ivAvatar.getLayoutParams();
                lp.setMargins(12, 0, 6, 0);
                ivAvatar.setLayoutParams(lp);
            } else {
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ivAvatar.getLayoutParams();
                lp.setMargins(6, 0, 12, 0);
                ivAvatar.setLayoutParams(lp);
            }
        }
        Glide.with(getContext()).load(masseur.getMassagist_main_img()).placeholder(R.drawable.holder_masseur).error(R.drawable.holder_masseur).into(ivAvatar);
        tvAddress.setText(masseur.getLandmark_name());
        tvAppointment.setText(String.format(getContext().getString(R.string.masseur_appointment), masseur.getOrder_num()));
        tvName.setText(masseur.getMassagist_name());
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MasseurActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, masseur.getId());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
        ivLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteItem(masseur);
            }
        });
    }

    public void enableEdit(boolean isEdit){
    }

    private void favoriteItem(final Masseur masseur){
        if(isCollected) {
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    isCollected = false;
                    Tools.Toast(getContext(), getContext().getString(R.string.remove_fav_item_success));
                    ivLike.setImageResource(R.mipmap.ic_masseur_like);
                }
            };
            ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(),  Arrays.asList(masseur.getId()), 5);
        }else{
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    isCollected = true;
                    Tools.Toast(getContext(), getContext().getString(R.string.fav_item_success));
                    ivLike.setImageResource(R.mipmap.ic_masseur_liked);
                }
            };
            ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), masseur.getId(), 5);
        }
    }
}
