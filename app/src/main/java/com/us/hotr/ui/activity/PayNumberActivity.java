package com.us.hotr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;

/**
 * Created by Mloong on 2017/9/12.
 */

public class PayNumberActivity extends BaseActivity{

    ImageView ivDeduct, ivAdd;
    TextView tvNumber, tvPay;

    int number = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.confirm_order_title);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_purchase_number;
    }

    private void initStaticView(){
        ivDeduct = (ImageView) findViewById(R.id.iv_deduct);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        tvNumber.setText(number+"");
        ivDeduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number > 1) {
                    number--;
                    tvNumber.setText(Integer.toString(number));
                }
                if(number == 1){
                    number--;
                    tvNumber.setText(Integer.toString(number));
                    ivDeduct.setImageResource(R.mipmap.ic_deduct_gray);
                }
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number++;
                tvNumber.setText(Integer.toString(number));
                if(number == 1){
                    ivDeduct.setImageResource(R.mipmap.ic_deduct);
                }
            }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PayNumberActivity.this, PayOrderActivity.class));
            }
        });

    }
}
