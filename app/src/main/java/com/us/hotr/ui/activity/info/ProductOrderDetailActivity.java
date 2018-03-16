package com.us.hotr.ui.activity.info;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.ProductOrder;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MapViewActivity;
import com.us.hotr.ui.activity.PayOrderActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.activity.receipt.ReceiptDetailActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.PermissionUtil;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/19.
 */

public class ProductOrderDetailActivity extends BaseLoadingActivity {
    private long orderId;
    private TextView tvTitle, tvSubTitle, tvPrice, tvAddress, tvPlace, tvVoucher, tvAmount, tvPayNow, tvPayNowMoney, tvPayLater,
            tvOrderId, tvPayId, tvPayMethod, tvTime, tvPhone, tvBuyAgain, tvBuyNow;
    private ConstraintLayout clAddress, clPlace, clPromise;
    private List<LinearLayout> llPromiseList = new ArrayList<>();
    private List<TextView> tvPromiseList = new ArrayList<>();
    private ImageView ivAvatar, ivMessage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.order_detail);
        orderId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<ProductOrder>() {
            @Override
            public void onNext(ProductOrder result) {
                updateData(result);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getProductOrderDetail(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), orderId);
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getProductOrderDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), orderId);
    }

    private void updateData(final ProductOrder result){
        Glide.with(this).load(result.getProduct_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
        tvTitle.setText(result.getProduct_name_usp());
        tvSubTitle.setVisibility(View.GONE);
        if(result.getPromiseList()!=null && result.getPromiseList().size()>0){
            clPromise.setVisibility(View.VISIBLE);
            clPromise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            for(int i=0;i<(Math.min(4,result.getPromiseList().size()));i++){
                llPromiseList.get(i).setVisibility(View.VISIBLE);
                tvPromiseList.get(i).setText(result.getPromiseList().get(i).getPromise_title());
            }
        }else
            clPromise.setVisibility(View.GONE);
        tvPrice.setText(getString(R.string.money) + new DecimalFormat("0.00").format(result.getOnline_price()));
        tvAddress.setText(result.getCity_name()+result.getArea_name()+result.getHospital_address());
        tvPlace.setText(result.getHospital_name());
        tvVoucher.setText(String.format(getString(R.string.deduct_amount), new DecimalFormat("0.00").format(result.getCoupon_price())));
        tvAmount.setText(String.format(getString(R.string.pay_number1), result.getOrder_product_sum()));
        tvPayNowMoney.setText(getString(R.string.money) + new DecimalFormat("0.00").format(result.getSum_price()));
        if(result.getPay_at_shop()<=0) {
            tvPayNow.setText(R.string.total_price1);
            tvPayLater.setVisibility(View.GONE);
        }else{
            tvPayLater.setVisibility(View.VISIBLE);
            tvPayNow.setText(R.string.total_amount);
            tvPayLater.setText(String.format(getString(R.string.pay_at_shop), new DecimalFormat("0.00").format(result.getPay_at_shop())));
        }
        tvOrderId.setText(String.format(getString(R.string.order_id), result.getOrder_code()));
        if(result.getOrder_payment_state() == 1){
            tvPayId.setVisibility(View.VISIBLE);
            tvPayMethod.setVisibility(View.VISIBLE);
            if(result.getOrder_account_type() == 0){
                tvPayId.setText(String.format(getString(R.string.wechat_pay_id), result.getNumerical_order()));
                tvPayMethod.setText(R.string.pay_by_wechat);
            }
            if(result.getOrder_account_type() == 1){
                tvPayId.setText(String.format(getString(R.string.alipay_pay_id), result.getNumerical_order()));
                tvPayMethod.setText(R.string.pay_by_alipay);
            }
            tvTime.setText(String.format(getString(R.string.order_pay_time), result.getOrder_payment_time()));
        }else{
            tvPayId.setVisibility(View.GONE);
            tvPayMethod.setVisibility(View.GONE);
            tvTime.setText(String.format(getString(R.string.order_create_time), result.getOrder_create_time()));
        }
        tvPhone.setText(Constants.SUPPORT_LINE);
        if(result.getOrder_payment_state() == 1){
            tvBuyAgain.setVisibility(View.VISIBLE);
            tvBuyNow.setText(R.string.go_to_coupon);
            tvBuyAgain.setText(R.string.buy_again);
            tvBuyAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProductOrderDetailActivity.this, ProductActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, result.getProduct_id());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
            tvBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProductOrderDetailActivity.this, ReceiptDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, result.getId());
                    b.putInt(Constants.PARAM_TYPE, ReceiptDetailActivity.TYPE_PRODUCT_ORDER);
                    i.putExtras(b);
                    startActivity(i);

                }
            });
        }else{
            tvBuyAgain.setVisibility(View.GONE);
            tvBuyNow.setText(R.string.pay);
            tvBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProductOrderDetailActivity.this, PayOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, result);
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
                    b.putBoolean(PayOrderActivity.PARAM_FROM_ORDED_LIST, true);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }

        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        clAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductOrderDetailActivity.this, MapViewActivity.class);
                Bundle b= new Bundle();
                b.putParcelable(Constants.PARAM_DATA, new LatLng(result.getLat(),result.getLon()));
                b.putString(Constants.PARAM_TITLE, result.getHospital_name());
                b.putString(Constants.PARAM_HOSPITAL_ID, result.getHospital_address());
                i.putExtras(b);
                startActivity(i);
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtil.hasCallPermission(ProductOrderDetailActivity.this)) {
                    callPhoneNumber();
                } else {
                    PermissionUtil.requestCallPermission(ProductOrderDetailActivity.this);
                }
            }
        });


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order_detail;
    }

    private void initStaticView(){
        ivAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvPlace = (TextView) findViewById(R.id.tv_place);
        ivMessage = (ImageView) findViewById(R.id.iv_message);
        tvVoucher = (TextView) findViewById(R.id.tv_voucher);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvPayNow = (TextView) findViewById(R.id.tv_pay_now);
        tvPayNowMoney = (TextView) findViewById(R.id.tv_pay_now_amount);
        tvPayLater = (TextView) findViewById(R.id.tv_pay_later);
        tvOrderId = (TextView) findViewById(R.id.tv_order_id);
        tvPayId = (TextView) findViewById(R.id.tv_pay_id);
        tvPayMethod = (TextView) findViewById(R.id.tv_pay_method);
        tvTime = (TextView) findViewById(R.id.tv_pay_time);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvBuyAgain = (TextView) findViewById(R.id.tv_buy_again);
        tvBuyNow = (TextView) findViewById(R.id.tv_buy_now);
        clAddress = (ConstraintLayout) findViewById(R.id.cl_address);
        clPlace = (ConstraintLayout) findViewById(R.id.cl_place);
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise1));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise2));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise3));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise4));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise1));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise2));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise3));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise4));
        clPromise = (ConstraintLayout) findViewById(R.id.cl_promise);
        enableLoadMore(false);
        for(LinearLayout l:llPromiseList)
            l.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PermissionUtil.PERMISSIONS_REQUEST_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void callPhoneNumber()
    {
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(Constants.SUPPORT_LINE);
        alertDialogBuilder.setPositiveButton(getString(R.string.call),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Constants.SUPPORT_LINE));
                        startActivity(callIntent);
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create().show();
    }
}
