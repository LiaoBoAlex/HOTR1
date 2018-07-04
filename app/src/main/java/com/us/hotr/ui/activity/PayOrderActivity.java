package com.us.hotr.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.MassageOrder;
import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.ProductOrder;
import com.us.hotr.storage.bean.WechatBill;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.AliPayResult;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.GetAppVersionRequest;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

/**
 * Created by Mloong on 2017/9/13.
 */

public class PayOrderActivity extends BaseActivity {
    public static final String PARAM_FROM_ORDED_LIST = "PARAM_FROM_ORDED_LIST";
    private ConstraintLayout clZfb, clWx;
    private ImageView ivZfb, ivWx;
    private TextView tvPay, tvNumber, tvAmount;

    private boolean isZfb = true;
    private int type;
    private long orderId;
    private ProductOrder productOrder;
    private MassageOrder massageOrder;
    private PartyOrder partyOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.pay_order_title);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        switch (type) {
            case Constants.TYPE_PRODUCT:
                productOrder = (ProductOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
                break;
            case Constants.TYPE_MASSAGE:
                massageOrder = (MassageOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
                break;
            case Constants.TYPE_PARTY:
                partyOrder = (PartyOrder) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
                break;
        }
        initStaticView();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onDestroy() {
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void getMessage(Events.PaymentSuccess paymentSuccess) {
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
            }
        };
        ServiceClient.getInstance().isFirstOrder(new SilentSubscriber(mListener, this, null),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), new GetAppVersionRequest());
        Intent i = new Intent(this, PaySuccessActivity.class);
        Bundle b = new Bundle();
        b.putBoolean(PARAM_FROM_ORDED_LIST, getIntent().getExtras().getBoolean(PARAM_FROM_ORDED_LIST, false));
        switch (type) {
            case Constants.TYPE_PRODUCT:
                b.putSerializable(Constants.PARAM_DATA, productOrder);
                break;
            case Constants.TYPE_MASSAGE:
                b.putSerializable(Constants.PARAM_DATA, massageOrder);
                break;
            case Constants.TYPE_PARTY:
                b.putSerializable(Constants.PARAM_DATA, partyOrder);
                break;
        }
        b.putInt(Constants.PARAM_TYPE, type);
        i.putExtras(b);
        startActivity(i);
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
                tvNumber.setText("x" + productOrder.getOrder_product_sum());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(productOrder.getSum_price()));
                orderId = productOrder.getId();
                break;
            case Constants.TYPE_MASSAGE:
                tvNumber.setText("x" + massageOrder.getOrder_product_sum());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(massageOrder.getSum_price()));
                orderId = massageOrder.getId();
                break;
            case Constants.TYPE_PARTY:
                tvNumber.setText("x" + partyOrder.getTotal_number());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(partyOrder.getRel_order_price()));
                orderId = partyOrder.getId();
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
            SubscriberListener mListener = new SubscriberListener<AliPayResult>() {
                @Override
                public void onNext(AliPayResult result) {
                    if ("9000".equals(result.getResultStatus())) {
                        Tools.Toast(PayOrderActivity.this, getString(R.string.alipay_pay_success));
                        GlobalBus.getBus().post(new Events.PaymentSuccess());
                    } else {
                        Tools.Toast(PayOrderActivity.this, getString(R.string.alipay_pay_fail));
                    }
                }
            };
            ServiceClient.getInstance().createAlipayBill(new ProgressSubscriber(mListener, PayOrderActivity.this),
                    HOTRSharePreference.getInstance(PayOrderActivity.this.getApplicationContext()).getUserID(), orderId, type, this);
        }else {
            SubscriberListener mListener = new SubscriberListener<WechatBill>() {
                @Override
                public void onNext(WechatBill result) {
                    Tools.wechatPay(PayOrderActivity.this, result);
                }
            };
            ServiceClient.getInstance().createWechatBill(new ProgressSubscriber(mListener, PayOrderActivity.this),
                    HOTRSharePreference.getInstance(PayOrderActivity.this.getApplicationContext()).getUserID(), orderId, type, this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
