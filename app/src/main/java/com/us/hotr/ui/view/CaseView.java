package com.us.hotr.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Case;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaobo on 2017/12/27.
 */

public class CaseView extends FrameLayout {
    private ImageView imgBefore, imgAfter, ivDelete, ivAvatar;
    private TextView tvName, tvTime, tvFollow, tvContent, tvSubject, tvRead, tvComment, tvLike;

    private Case aCase;
    private boolean isFollow, isLiked;
    private ItemSelectedListener itemSelectedListener;


    public CaseView(Context context) {
        super(context);
        init();
    }

    public CaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_case,this);
        imgBefore = (ImageView) findViewById(R.id.img_before);
        imgAfter = (ImageView) findViewById(R.id.imge_after);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_info);
        tvFollow = (TextView) findViewById(R.id.tv_follow);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvSubject = (TextView) findViewById(R.id.tv_subject);
        tvRead = (TextView) findViewById(R.id.tv_read);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvLike = (TextView) findViewById(R.id.tv_like);

    }

    public void setData(final Case c){
        aCase = c;
        Glide.with(getContext()).load(aCase.getBeforeOperationPhoto()).dontAnimate().error(R.drawable.image_holder_1).placeholder(R.drawable.image_holder_1).into(imgBefore);
        Glide.with(getContext()).load(aCase.getAfterOperationPhoto()).dontAnimate().error(R.drawable.image_holder_1).placeholder(R.drawable.image_holder_1).into(imgAfter);
        Glide.with(getContext()).load(aCase.getHead_portrait()).dontAnimate().error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
        tvName.setText(aCase.getNick_name());
        if(aCase.getCreateTime()!=null)
            tvTime.setText(Tools.getPostTime(getContext(), aCase.getCreateTime()));
        if(aCase.getIs_attention() == 1){
            isFollow = true;
            tvFollow.setVisibility(VISIBLE);
            tvFollow.setText(R.string.fav_ed);
            tvFollow.setTextColor(getContext().getResources().getColor(R.color.text_grey2));
        }else{
            isFollow = false;
            if(HOTRSharePreference.getInstance(getContext()).getUserInfo()!= null
                    && aCase.getUserId() == HOTRSharePreference.getInstance(getContext()).getUserInfo().getUserId())
                tvFollow.setVisibility(GONE);
            else {
                tvFollow.setVisibility(VISIBLE);
                tvFollow.setText(R.string.guanzhu);
                tvFollow.setTextColor(getContext().getResources().getColor(R.color.text_black));

            }
        }
        tvFollow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser();
            }
        });
        tvContent.setText(aCase.getContrastPhotoContent());
        tvSubject.setText(aCase.getProjectName());
        tvRead.setText(aCase.getReadCnt()+"");
        tvComment.setText(aCase.getCommentCnt()+"");
        tvLike.setText(aCase.getLikeCnt()+"");
        isLiked = aCase.getIs_like()==1?true:false;
        if(isLiked) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan_ed);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
        }
        else {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
        }
        tvLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLiked)
                    likeCase();
                else
                    Tools.Toast(getContext(), getContext().getString(R.string.already_liked));
            }
        });
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readCase();
                Intent i = new Intent(getContext(), CaseActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                b.putLong(Constants.PARAM_ID, aCase.getKey());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }

    private void followUser(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                isFollow = !isFollow;
                if(isFollow){
                    Tools.Toast(getContext(), getContext().getString(R.string.fav_people_success));
                    tvFollow.setText(R.string.fav_ed);
                    tvFollow.setTextColor(getContext().getResources().getColor(R.color.text_grey2));
                }else{
                    Tools.Toast(getContext(), getContext().getString(R.string.remove_fav_people_success));
                    tvFollow.setText(R.string.guanzhu);
                    tvFollow.setTextColor(getContext().getResources().getColor(R.color.text_black));
                }
            }
        };
        if(isFollow)
            ServiceClient.getInstance().deleteFavoritePeople(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), aCase.getUserId());
        else
            ServiceClient.getInstance().favoritePeople(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), aCase.getUserId());
    }

    public void setItemSelectedListener(ItemSelectedListener listener){
        this.itemSelectedListener = listener;
    }

    private void likeCase(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                isLiked = true;
                Tools.Toast(getContext(), getContext().getString(R.string.like_success));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_zan_ed);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvLike.setCompoundDrawables(drawable, null, null, null);
                tvLike.setText((Integer.parseInt(tvLike.getText().toString())+1)+"");
            }
        };
        ServiceClient.getInstance().likeCase(new ProgressSubscriber(mListener, getContext()),
                HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), aCase.getKey());
    }

    private void readCase(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
            }
        };
        ServiceClient.getInstance().readCase(new SilentSubscriber(mListener, getContext(), null), aCase.getKey());
    }

    public void enableEdit(boolean isEdit){
        if (isEdit) {
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setImageResource(R.mipmap.ic_delete_order);
            setTag(false);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((boolean)CaseView.this.getTag()) {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order);
                        CaseView.this.setTag(false);

                    } else {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
                        CaseView.this.setTag(true);
                    }
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected((boolean)getTag());
                }
            });
            imgBefore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((boolean)CaseView.this.getTag()) {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order);
                        CaseView.this.setTag(false);

                    } else {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
                        CaseView.this.setTag(true);
                    }
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected((boolean)getTag());
                }
            });
            imgAfter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((boolean)CaseView.this.getTag()) {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order);
                        CaseView.this.setTag(false);

                    } else {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
                        CaseView.this.setTag(true);
                    }
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected((boolean)getTag());
                }
            });
        } else {
            ivDelete.setVisibility(View.GONE);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readCase();
                    Intent i = new Intent(getContext(), CaseActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                    b.putLong(Constants.PARAM_ID, aCase.getKey());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
            final List<String> photoes = new ArrayList<String>() {{
                add(aCase.getBeforeOperationPhoto());
                add(aCase.getAfterOperationPhoto());
            }};
            imgBefore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    readCase();
                    Intent i = new Intent(getContext(), ImageViewerActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, (Serializable) photoes);
                    b.putInt(Constants.PARAM_ID, 0);
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
            imgAfter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    readCase();
                    Intent i = new Intent(getContext(), ImageViewerActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, (Serializable) photoes);
                    b.putInt(Constants.PARAM_ID, 1);
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });

        }
    }
}
