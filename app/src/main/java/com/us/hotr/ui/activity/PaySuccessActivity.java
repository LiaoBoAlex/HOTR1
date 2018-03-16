package com.us.hotr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.MassageOrder;
import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.ProductOrder;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.activity.info.OrderListActivity;
import com.us.hotr.ui.activity.massage.MassageActivity;
import com.us.hotr.ui.activity.party.PartyActivity;

import java.text.DecimalFormat;

/**
 * Created by Mloong on 2017/9/13.
 */

public class PaySuccessActivity extends BaseActivity {
    private TextView tvId, tvMarchent, tvTitle, tvNumber, tvAmount, tvVoucher, tvContinue, tvMarchentTitle, tvHint;
    private int type;
    private ProductOrder productOrder;
    private MassageOrder massageOrder;
    private PartyOrder partyOrder;
    private boolean isFromOrderList = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.pay_order_title);
        isFromOrderList = getIntent().getExtras().getBoolean(PayOrderActivity.PARAM_FROM_ORDED_LIST, false);
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
        initView();
    }

    private void initView(){
        tvId = (TextView) findViewById(R.id.tv_id);
        tvMarchent = (TextView) findViewById(R.id.tv_marchent);
        tvTitle = (TextView) findViewById(R.id.tv_product);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvVoucher = (TextView) findViewById(R.id.tv_voucher);
        tvContinue = (TextView) findViewById(R.id.tv_continue);
        tvMarchentTitle = (TextView) findViewById(R.id.tv_marchent_title);
        tvHint = (TextView) findViewById(R.id.tv_hint);

        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        switch (type) {
            case Constants.TYPE_PRODUCT:
                tvId.setText(productOrder.getOrder_code());
                tvMarchent.setText(productOrder.getHospital_name());
                tvTitle.setText(productOrder.getProduct_name_usp());
                tvNumber.setText("x"+productOrder.getOrder_product_sum());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(productOrder.getSum_price()));
                break;
            case Constants.TYPE_MASSAGE:
                tvId.setText(massageOrder.getOrder_code());
                tvMarchent.setText(massageOrder.getMassage_name());
                tvTitle.setText(massageOrder.getProduct_name_usp());
                tvNumber.setText("x"+massageOrder.getOrder_product_sum());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(massageOrder.getSum_price()));
                break;
            case Constants.TYPE_PARTY:
                tvId.setText(partyOrder.getOrder_code());
                tvMarchent.setVisibility(View.GONE);
                tvMarchentTitle.setVisibility(View.GONE);
                tvVoucher.setVisibility(View.GONE);
                tvHint.setText(R.string.pay_success2);
                tvTitle.setText(partyOrder.getTravel_name());
                tvNumber.setText("x"+partyOrder.getTotal_number());
                tvAmount.setText(getString(R.string.money)+new DecimalFormat("0.00").format(partyOrder.getRel_order_price()));
                break;
        }
        tvVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaySuccessActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_DATA, 2);
                intent.putExtras(b);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueShopping();
            }
        });
    }

    private void continueShopping(){
        if(isFromOrderList){
            Intent intent = new Intent(PaySuccessActivity.this, OrderListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {
            Intent intent;
            Bundle b = new Bundle();
            switch (type) {
                case Constants.TYPE_PRODUCT:
                    intent = new Intent(PaySuccessActivity.this, ProductActivity.class);
                    b.putLong(Constants.PARAM_ID, productOrder.getProduct_id());
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case Constants.TYPE_MASSAGE:
                    intent = new Intent(PaySuccessActivity.this, MassageActivity.class);
                    b.putLong(Constants.PARAM_ID, massageOrder.getProduct_id());
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case Constants.TYPE_PARTY:
                    intent = new Intent(PaySuccessActivity.this, PartyActivity.class);
                    b.putLong(Constants.PARAM_ID, partyOrder.getTravel_id());
                    intent.putExtras(b);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        continueShopping();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_success;
    }
}

