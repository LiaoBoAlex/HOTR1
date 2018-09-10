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
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.util.Tools;

/**
 * Created by liaobo on 2018/1/11.
 */

public class MasseurBigView extends FrameLayout {
    private TextView tvName, tvHeight, tvAppointment, tvExperience;
    private ImageView ivAvatar, ivClick, ivDelete;

    private ItemSelectedListener itemSelectedListener;
    private Masseur masseur;

    public MasseurBigView(Context context) {
        super(context);
        init();
    }

    public MasseurBigView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_masseur_big, this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvHeight = (TextView) findViewById(R.id.tv_height);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        ivClick = (ImageView) findViewById(R.id.iv_click);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        tvExperience = (TextView) findViewById(R.id.tv_experience);
    }

//    public void setData(final Masseur masseur, boolean isSelected, final ItemSelectedListener listener){
//        setData(masseur);
//        ivClick.setVisibility(VISIBLE);
//        if(isSelected)
//            ivClick.setImageResource(R.mipmap.ic_massage_clicked);
//        else
//            ivClick.setImageResource(R.mipmap.ic_massage_click);
//        ivClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.itemSelected();
//            }
//        });
//    }

    public void setData(final Masseur masseur, final long massageId){
        this.masseur = masseur;
        tvAppointment.setText(String.format(getContext().getString(R.string.masseur_appointment), masseur.getOrder_num()));
        tvHeight.setText(String.format(getContext().getString(R.string.height), masseur.getMassagist_height()));
        tvName.setText(masseur.getMassagist_name());
        tvExperience.setText(String.format(getContext().getString(R.string.experience), masseur.getJob_time()));
        ivClick.setVisibility(GONE);
        if(masseur.getMassagistPhotos()!=null && !masseur.getMassagistPhotos().isEmpty())
            Glide.with(getContext()).load(Tools.getMainPhoto(masseur.getMassagistPhotos())).dontAnimate().error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
        if(masseur.getMassagist_main_img()!=null && !masseur.getMassagist_main_img().isEmpty())
            Glide.with(getContext()).load(masseur.getMassagist_main_img()).dontAnimate().error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getContext(), MasseurActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, masseur.getId());
                b.putLong(Constants.PARAM_MASSAGE_ID, massageId);
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
                public void onClick(View view) {
                    Intent i =new Intent(getContext(), MasseurActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, masseur.getId());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
        }
    }

    public void setItemSelectedListener(ItemSelectedListener listener){
        this.itemSelectedListener = listener;
    }

    public void setGoButtonResource(int res){
        ivClick.setVisibility(VISIBLE);
        ivClick.setImageResource(res);
    }
    public void setGoButtonLisenter(OnClickListener lisenter){
        ivClick.setOnClickListener(lisenter);
    }
}
