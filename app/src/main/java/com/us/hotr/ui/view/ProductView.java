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
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.ui.activity.beauty.ProductActivity;

import java.text.DecimalFormat;
import java.util.Properties;

/**
 * Created by liaobo on 2017/12/27.
 */

public class ProductView extends FrameLayout{
    private TextView tvTitle, tvDoctor, tvHospital, tvAppointment, tvPriceBefore, tvPriceAfter, tvSoldOut;
    private ImageView ivAvatar, ivGo, ivDelete, ivOnePrice, ivPromoPrice;
    private View vDivider;
    private Product product;
    private boolean isLog = false;

    private ItemSelectedListener itemSelectedListener;

    public ProductView(Context context) {
        super(context);
        init();
    }

    public ProductView(Context context, AttributeSet attrs) {
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
        tvSoldOut = (TextView) findViewById(R.id.tv_sold_out);
        ivAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        ivGo = (ImageView) findViewById(R.id.iv_go);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivOnePrice = (ImageView) findViewById(R.id.iv_one_price);
        ivPromoPrice = (ImageView) findViewById(R.id.iv_promo_price);
        vDivider = findViewById(R.id.v_divider);
    }

    public void setData(final Product product){
        this.product = product;
        tvTitle.setText(getContext().getString(R.string.bracket_left)+product.getProduct_name()+getContext().getString(R.string.bracket_right)+product.getProduct_usp());
        tvDoctor.setText(product.getDoctor_name());
        tvHospital.setText(product.getHospital_name());
        tvAppointment.setText(String.format(getContext().getString(R.string.num_of_appointment1), product.getOrder_num()));
        tvPriceBefore.setText(String.format(getContext().getString(R.string.price), new DecimalFormat("0.00").format(product.getShop_price())));
        tvPriceAfter.setText(new DecimalFormat("0.00").format(product.getOnline_price()));
        tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        Glide.with(getContext()).load(product.getProduct_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
        if(product.getPayment_type() == Constants.FULL_PAYMENT)
            ivOnePrice.setVisibility(View.VISIBLE);
        else
            ivOnePrice.setVisibility(View.GONE);
        if(product.getProduct_type() == Constants.PROMOTION_PRODUCT){
            ivPromoPrice.setVisibility(View.VISIBLE);
            if(product.getAmount()>0)
                tvSoldOut.setVisibility(View.GONE);
            else
                tvSoldOut.setVisibility(View.VISIBLE);
        }else {
            ivPromoPrice.setVisibility(View.GONE);
            tvSoldOut.setVisibility(View.GONE);
        }
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLog){
                    Properties prop = new Properties();
                    prop.setProperty("id", product.getProductId()+"");
                    StatService.trackCustomKVEvent(getContext(), Constants.MTA_ID_CLICK_PURPOSE_PRODUCT, prop);
                }
                Intent i = new Intent(getContext(), ProductActivity.class);
                Bundle b= new Bundle();
                b.putLong(Constants.PARAM_ID, product.getProductId());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }

    public void setLog(boolean isLog){
        this.isLog = isLog;
    }

    public void enableEdit(boolean isEdit){
        if(isEdit) {
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setImageResource(R.mipmap.ic_delete_order);
            setTag(false);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if((boolean)view.getTag()){
                        ivDelete.setImageResource(R.mipmap.ic_delete_order);
                        view.setTag(false);
                    }else{
                        ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
                        view.setTag(true);
                    }
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected((boolean)view.getTag());
                }
            });
        }else {
            ivDelete.setVisibility(View.GONE);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), ProductActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, product.getProductId());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
        }
    }

    public void enableSelect(boolean isSelect){
        if(isSelect){
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected(true);
                }
            });
        }else{
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), ProductActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, product.getProductId());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
        }
    }


    public void showDivider(boolean show){
        if(show)
            vDivider.setVisibility(View.VISIBLE);
        else
            vDivider.setVisibility(View.GONE);
    }


    public void setItemSelectedListener(ItemSelectedListener listener){
        this.itemSelectedListener = listener;
    }
}
