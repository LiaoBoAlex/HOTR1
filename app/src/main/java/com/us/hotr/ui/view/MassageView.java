package com.us.hotr.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.ui.activity.massage.MassageActivity;

import java.text.DecimalFormat;

/**
 * Created by liaobo on 2017/12/27.
 */

public class MassageView extends FrameLayout{
    private TextView tvTitle, tvDoctor, tvHospital, tvAppointment, tvPriceBefore, tvPriceAfter, tvMin;
    private ImageView ivAvatar, ivGo, ivDelete, ivPromoPrice, ivOnePrice;
    private View vDivider;

    private ItemSelectedListener itemSelectedListener;
    private Massage massage;

    public MassageView(Context context) {
        super(context);
        init();
    }

    public MassageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_product, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDoctor = (TextView) findViewById(R.id.tv_sub_title);
        tvHospital = (TextView) findViewById(R.id.tv_address);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        tvPriceBefore = (TextView) findViewById(R.id.tv_price_before);
        tvPriceAfter = (TextView) findViewById(R.id.tv_amount);
        ivAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        ivGo = (ImageView) findViewById(R.id.iv_go);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        tvMin = (TextView) findViewById(R.id.tv_min);
        ivPromoPrice = (ImageView) findViewById(R.id.iv_promo_price);
        ivOnePrice = (ImageView) findViewById(R.id.iv_one_price);
        vDivider = findViewById(R.id.v_divider);
    }

    public void setData(final Massage massage){
        this.massage = massage;
        tvTitle.setText(getContext().getString(R.string.bracket_left)+massage.getProductName()+getContext().getString(R.string.bracket_right)+massage.getProductUsp());
        tvDoctor.setText(massage.getMassage_name());
        tvHospital.setText("");
        tvAppointment.setText(String.format(getContext().getString(R.string.num_of_appointment1), massage.getOrder_num()));
        tvPriceBefore.setText(String.format(getContext().getString(R.string.price), new DecimalFormat("0.00").format(massage.getShopPrice())));
        if(massage.getProductType() == Constants.PROMOTION_PRODUCT) {
            tvPriceAfter.setText(new DecimalFormat("0.00").format(massage.getActivityPrice()) + "/" + massage.getServiceTime());
            ivPromoPrice.setVisibility(View.VISIBLE);
        }
        else {
            tvPriceAfter.setText(new DecimalFormat("0.00").format(massage.getOnlinePrice()) + "/" + massage.getServiceTime());
            ivPromoPrice.setVisibility(View.GONE);
        }
        tvPriceBefore.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvMin.setVisibility(View.VISIBLE);
        ivOnePrice.setVisibility(View.GONE);
        Glide.with(getContext()).load(massage.getProduct_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MassageActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, massage.getKey());
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
                    Intent i = new Intent(getContext(), MassageActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, massage.getKey());
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
        ivGo.setImageResource(res);
    }
    public void setGoButtonLisenter(OnClickListener lisenter){
        ivGo.setOnClickListener(lisenter);
    }

    public void showDivider(boolean show){
        if(show)
            vDivider.setVisibility(View.VISIBLE);
        else
            vDivider.setVisibility(View.GONE);
    }
}
