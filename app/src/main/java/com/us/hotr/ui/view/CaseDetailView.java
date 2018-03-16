package com.us.hotr.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.beauty.DoctorActivity;
import com.us.hotr.ui.activity.beauty.HospitalActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.activity.beauty.SubjectActivity;
import com.us.hotr.ui.activity.info.FriendActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetCaseDetailResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaobo on 2018/2/12.
 */

public class CaseDetailView extends FrameLayout {
    private ImageView ivAvatar, ivProductAvatar, ivBefore, ivAfter;
    private TextView tvName, tvInfo, tvFollow, tvHospital, tvDoctor, tvProductName, tvProductHospital, tvProductDoctor, tvAppointment, tvTitle,
            tvPriceBefore, tvPriceAfter, tvTime, tvContent, tvRead, tvLike, tvComment, tvSeeMore;
    private ConstraintLayout clHospital, clDoctor, clProduct, clPromise, clUser;
    private List<LinearLayout> llPromiseList = new ArrayList<>();
    private List<TextView> tvPromiseList = new ArrayList<>();

    private GetCaseDetailResponse response;
    private boolean isLiked, isFav;
    public CaseDetailView(Context context) {
        super(context);
        init();
    }

    public CaseDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_case_detail, this);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvFollow = (TextView) findViewById(R.id.tv_follow);
        tvHospital = (TextView) findViewById(R.id.tv_hospital);
        tvDoctor = (TextView) findViewById(R.id.tv_doctor);
        clHospital = (ConstraintLayout) findViewById(R.id.cl_hospital);
        clDoctor = (ConstraintLayout) findViewById(R.id.cl_doctor);
        ivProductAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvProductName = (TextView) findViewById(R.id.tv_title);
        tvProductHospital = (TextView) findViewById(R.id.tv_address);
        tvProductDoctor = (TextView) findViewById(R.id.tv_sub_title);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        tvPriceBefore = (TextView) findViewById(R.id.tv_price_before);
        tvPriceAfter = (TextView) findViewById(R.id.tv_amount);
        clProduct = (ConstraintLayout) findViewById(R.id.cl_product);
        clPromise = (ConstraintLayout) findViewById(R.id.cl_promise);
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise1));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise2));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise3));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise4));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise1));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise2));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise3));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise4));
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvRead = (TextView) findViewById(R.id.tv_read);
        tvLike = (TextView) findViewById(R.id.tv_like);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvSeeMore = (TextView) findViewById(R.id.tv_see_more);
        ivBefore = (ImageView) findViewById(R.id.iv_before);
        ivAfter = (ImageView) findViewById(R.id.iv_after);
        clUser = (ConstraintLayout) findViewById(R.id.cl_user);
        tvTitle = (TextView) findViewById(R.id.tv_case_title);
    }

    public void setData(final GetCaseDetailResponse response) {
        this.response = response;
        Glide.with(getContext()).load(response.getUser().getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
        Glide.with(getContext()).load(response.getProduct().getProduct_main_img()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivProductAvatar);
        Glide.with(getContext()).load(response.getYmContrastPhoto().getBeforeOperationPhoto()).error(R.drawable.image_holder_1).placeholder(R.drawable.image_holder_1).into(ivBefore);
        Glide.with(getContext()).load(response.getYmContrastPhoto().getAfterOperationPhoto()).error(R.drawable.image_holder_1).placeholder(R.drawable.image_holder_1).into(ivAfter);
        tvTitle.setText(response.getYmContrastPhoto().getContrastPhotoTitle());
        tvName.setText(response.getUser().getNickname());
        if (response.getUser().getGender() != null && response.getUser().getAge() != null)
            tvInfo.setText(getResources().getStringArray(R.array.gender)[response.getUser().getGender()] + " | " + String.format(getContext().getString(R.string.age_number), response.getUser().getAge()) + " | " + response.getUser().getProvince_name());
        isFav = response.getIs_attention()==1?true:false;
        if (isFav) {
            tvFollow.setText(R.string.fav_ed);
            tvFollow.setTextColor(getContext().getResources().getColor(R.color.text_grey2));
        } else {
            if (HOTRSharePreference.getInstance(getContext()).getUserInfo() != null
                    && response.getUser().getUserId() == HOTRSharePreference.getInstance(getContext()).getUserInfo().getUserId())
                tvFollow.setVisibility(GONE);
            else {
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
        clUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FriendActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, response.getUser().getUserId());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
        tvHospital.setText(response.getYmContrastPhoto().getHospitalName());
        clHospital.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), HospitalActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_ID, response.getYmContrastPhoto().getHospitalId());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
        tvDoctor.setText(response.getYmContrastPhoto().getDoctorName());
        clDoctor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DoctorActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_ID, response.getYmContrastPhoto().getDoctorId());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
        tvProductName.setText(getContext().getString(R.string.bracket_left)+response.getProduct().getProduct_name()+getContext().getString(R.string.bracket_right)+response.getProduct().getProduct_usp());
        tvProductDoctor.setText(response.getProduct().getDoctor_name());
        tvProductHospital.setText(response.getProduct().getHospital_name());
        tvAppointment.setText(String.format(getContext().getString(R.string.num_of_appointment1), response.getProduct().getOrder_num()));
        tvPriceAfter.setText(new DecimalFormat("0.00").format(response.getProduct().getOnline_price()));
        tvPriceBefore.setText(getContext().getString(R.string.money)+new DecimalFormat("0.00").format(response.getProduct().getShop_price()));
        tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        clProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProductActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_ID, response.getProduct().getProductId());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
        for (LinearLayout l : llPromiseList)
            l.setVisibility(View.GONE);
        if (response.getProduct().getPromiseList() != null && response.getProduct().getPromiseList().size() > 0) {
            clPromise.setVisibility(View.VISIBLE);
            for (int i = 0; i < (Math.min(4, response.getProduct().getPromiseList().size())); i++) {
                llPromiseList.get(i).setVisibility(View.VISIBLE);
                tvPromiseList.get(i).setText(response.getProduct().getPromiseList().get(i).getPromise_title());
            }
        } else
            clPromise.setVisibility(View.GONE);
        tvTime.setText(Tools.getPostTime(getContext(), response.getYmContrastPhoto().getCreateTime()));
        tvContent.setText(response.getYmContrastPhoto().getContrastPhotoContent());
        tvRead.setText(response.getYmContrastPhoto().getReadCnt()+"");
        tvComment.setText(response.getYmContrastPhoto().getCommentCnt()+"");
        tvLike.setText(response.getYmContrastPhoto().getLikeCnt()+"");
        isLiked = response.getIs_like_contrast_photo()==1?true:false;
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
                    likePost();
                else
                    Tools.Toast(getContext(), getContext().getString(R.string.already_liked));
            }
        });
        final List<String> photoes = new ArrayList<String>() {{
            add(response.getYmContrastPhoto().getBeforeOperationPhoto());
            add(response.getYmContrastPhoto().getAfterOperationPhoto());
        }};
        ivBefore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ImageViewerActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_DATA, (Serializable) photoes);
                b.putInt(Constants.PARAM_ID, 0);
                b.putSerializable(Constants.PARAM_PRODUCT_ID, response.getProduct());
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
        ivAfter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ImageViewerActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_DATA, (Serializable) photoes);
                b.putInt(Constants.PARAM_ID, 1);
                b.putSerializable(Constants.PARAM_PRODUCT_ID, response.getProduct());
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
        tvSeeMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SubjectActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, response.getYmContrastPhoto().getProjectId());
                b.putBoolean(SubjectActivity.PARAM_CASE, true);
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }

    private void likePost(){
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
                HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), response.getYmContrastPhoto().getKey());
    }

    private void followUser(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                isFav = !isFav;
                if(isFav){
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
        if(isFav)
            ServiceClient.getInstance().deleteFavoritePeople(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), response.getUser().getUserId());
        else
            ServiceClient.getInstance().favoritePeople(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), response.getUser().getUserId());
    }
}
