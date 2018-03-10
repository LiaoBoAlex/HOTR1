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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.MassageOrder;
import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.ProductOrder;
import com.us.hotr.storage.bean.WechatBill;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;

/**
 * Created by Mloong on 2017/9/13.
 */

public class PayOrderActivity extends BaseActivity {

    private ConstraintLayout clZfb, clWx;
    private ImageView ivZfb, ivWx;
    private TextView tvPay, tvNumber, tvAmount;

    boolean isZfb = true;
    int type;
    long orderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.pay_order_title);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
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
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvAmount = (TextView) findViewById(R.id.tv_amount);

        switch (type) {
            case Constants.TYPE_PRODUCT:
                tvNumber.setText("x" + ((ProductOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getOrder_product_sum());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(((ProductOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getSum_price()));
                orderId = ((ProductOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getId();
                break;
            case Constants.TYPE_MASSAGE:
                tvNumber.setText("x" + ((MassageOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getOrder_product_sum());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(((MassageOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getSum_price()));
                orderId = ((MassageOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getId();
                break;
            case Constants.TYPE_PARTY:
                tvNumber.setText("x" + ((PartyOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getTotal_number());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(((PartyOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getRel_order_price()));
                orderId = ((PartyOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA)).getId();
                break;
        }

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
                isZfb = false;
                ivZfb.setImageResource(R.mipmap.ic_pay_click);
                ivWx.setImageResource(R.mipmap.ic_pay_clicked);
            }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
    }

    private void pay(){
        if(isZfb){

        }else {
            SubscriberListener mListener = new SubscriberListener<WechatBill>() {
                @Override
                public void onNext(WechatBill result) {
                    Tools.wechatPay(PayOrderActivity.this, result);
                }
            };
            ServiceClient.getInstance().createWechatBill(new ProgressSubscriber(mListener, PayOrderActivity.this),
                    HOTRSharePreference.getInstance(PayOrderActivity.this.getApplicationContext()).getUserID(), orderId, this);
        }

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
