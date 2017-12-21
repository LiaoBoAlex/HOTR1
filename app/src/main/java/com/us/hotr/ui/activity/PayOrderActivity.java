package com.us.hotr.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.ui.dialog.TwoButtonDialog;

/**
 * Created by Mloong on 2017/9/13.
 */

public class PayOrderActivity extends BaseActivity {

    private ConstraintLayout clZfb, clWx;
    private ImageView ivZfb, ivWx;
    private TextView tvPay, tvTitle;

    boolean isZfb = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.pay_order_title);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_order;
    }

    private void initStaticView(){
        clZfb = (ConstraintLayout) findViewById(R.id.cl_zfb);
        clWx = (ConstraintLayout) findViewById(R.id.cl_wx);
        ivZfb = (ImageView) findViewById(R.id.iv_zfb);
        ivWx = (ImageView) findViewById(R.id.iv_wx);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        tvTitle = (TextView) findViewById(R.id.tb_title);

        clZfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isZfb = true;
                ivZfb.setImageResource(R.mipmap.ic_pay_clicked);
                ivWx.setImageResource(R.mipmap.ic_pay_click);
            }
        });

        clWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isZfb = true;
                ivZfb.setImageResource(R.mipmap.ic_pay_click);
                ivWx.setImageResource(R.mipmap.ic_pay_clicked);
            }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PayOrderActivity.this, PaySuccessActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.do_you_want_to_leave));
        alertDialogBuilder.setPositiveButton(getString(R.string.leave),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PayOrderActivity.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.stay),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create().show();
    }
}
