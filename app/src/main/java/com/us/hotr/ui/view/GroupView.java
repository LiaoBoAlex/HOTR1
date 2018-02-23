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
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

/**
 * Created by liaobo on 2018/1/25.
 */

public class GroupView extends FrameLayout {
    private TextView tvTitle, tvDesc, tvNumber1, tvNumber2, tvRecommended;
    private ImageView ivAvatar, ivAdd;

    public GroupView(Context context) {
        super(context);
        init();
    }

    public GroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_group, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvNumber1 = (TextView) findViewById(R.id.tv_number1);
        tvNumber2 = (TextView) findViewById(R.id.tv_number2);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        tvRecommended = (TextView) findViewById(R.id.tv_recommanded);
    }

    public void setData(final Group group){
        tvNumber2.setVisibility(View.GONE);
        tvTitle.setText(group.getCoshow_name());
        tvDesc.setText(group.getCoshow_info());
        if(group.getIs_attention() == 1){
            ivAdd.setImageResource(R.mipmap.ic_group_added);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteGroup(group, ivAdd);
                }
            });
        }else{
            ivAdd.setImageResource(R.mipmap.ic_group_add);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteGroup(group, ivAdd);
                }
            });
        }
        Glide.with(getContext()).load(group.getSmall_img()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
        if(group.getIs_recommend() == 1)
            tvRecommended.setVisibility(View.VISIBLE);
        else
            tvRecommended.setVisibility(View.GONE);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GroupDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_DATA, group);
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }

    public void enableEdit(boolean value){
        if(value)
            ivAdd.setVisibility(VISIBLE);
        else
            ivAdd.setVisibility(GONE);
    }

    private void favoriteGroup(final Group group, final ImageView ivAdd){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                Events.Refresh event = new Events.Refresh();
                GlobalBus.getBus().post(event);
                if(group.getIs_attention() == 1)
                    group.setIs_attention(0);
                else
                    group.setIs_attention(1);
                if(group.getIs_attention() == 1){
                    Tools.Toast(getContext(), getContext().getString(R.string.fav_people_success));
                    ivAdd.setImageResource(R.mipmap.ic_group_added);
                }else{
                    Tools.Toast(getContext(), getContext().getString(R.string.remove_fav_people_success));
                    ivAdd.setImageResource(R.mipmap.ic_group_add);
                }
            }
        };
        ServiceClient.getInstance().favoriteGroup(new ProgressSubscriber(mListener, getContext()),
                HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), group.getId(), group.getIs_attention()==1?0:1);
    }
}