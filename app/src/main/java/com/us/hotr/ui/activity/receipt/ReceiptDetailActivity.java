package com.us.hotr.ui.activity.receipt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.MassageReceipt;
import com.us.hotr.storage.bean.ProductReceipt;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MapViewActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.PermissionUtil;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;

/**
 * Created by Mloong on 2017/9/15.
 */

public class ReceiptDetailActivity extends BaseLoadingActivity {
    public static final int TYPE_PRODUCT_ORDER = 991;
    public static final int TYPE_MASSAGE_ORDER = 992;

    private TextView tvMarchent, tvId, tvTitle, tvAmount, tvCode, tvSubTitle, tvMap, tvPhone;
    private ImageView ivCode;

    private int type;
    private long id;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        id = getIntent().getExtras().getLong(Constants.PARAM_ID);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    private void initStaticView(){
        tvMarchent = (TextView) findViewById(R.id.tv_marchent);
        tvId = (TextView) findViewById(R.id.tv_id);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
        ivCode = (ImageView) findViewById(R.id.iv_code);
        tvMap = (TextView) findViewById(R.id.tv_map);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
    }

    @Override
    protected void loadData(int loadType) {
        if(type == Constants.TYPE_PRODUCT || type ==TYPE_PRODUCT_ORDER) {
            SubscriberListener mListener = new SubscriberListener<ProductReceipt>() {
                @Override
                public void onNext(final ProductReceipt result) {
                    tvMarchent.setText(result.getHospital_name());
                    tvId.setText(getString(R.string.pay_id)+result.getOrder_code());
                    tvTitle.setText(result.getProduct_name_usp());
                    String money1;
                    if(result.getPayment_type() == Constants.PRE_PAYMENT)
                        money1 = getString(R.string.money) + new DecimalFormat("0.00").format(result.getPrepayment());
                    else
                        money1 = getString(R.string.money) + new DecimalFormat("0.00").format(result.getFixed_price());
                    String money2 = getString(R.string.receipt_title);
                    SpannableString msp = new SpannableString(money1+money2);
                    msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, money1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvAmount.setText(msp);
                    tvCode.setText(getString(R.string.receipt_code)+result.getEncryption_QR_code());
                    if(result.getPayment_type() == Constants.PRE_PAYMENT){
                        money1 = getString(R.string.money) + new DecimalFormat("0.00").format(result.getPay_at_shop());
                        money2 = getString(R.string.pay_at_shop1);
                        msp = new SpannableString(money2+money1);
                        msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), money2.length(), money2.length() + money1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvSubTitle.setText(msp);
                    }else
                        tvSubTitle.setText(getString(R.string.no_need_to_pay_at_shop));
                    ivCode.setImageBitmap(Tools.createQRCodeBitmap(result.getEncryption_QR_code(), 100, 100));
                    tvMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ReceiptDetailActivity.this, MapViewActivity.class);
                            Bundle b= new Bundle();
                            b.putParcelable(Constants.PARAM_DATA, new LatLng(result.getLat(),result.getLon()));
                            b.putString(Constants.PARAM_TITLE, result.getHospital_name());
                            b.putString(Constants.PARAM_HOSPITAL_ID, result.getAddress());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                    String number = result.getContact_mobile();
                    String s = String.format(getString(R.string.hint_make_appointment), number);
                    msp = new SpannableString(s);
                    msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), s.length()-number.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvPhone.setText(msp);
                    tvPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            phoneNumber = result.getContact_mobile();
                            if (PermissionUtil.hasCallPermission(ReceiptDetailActivity.this)) {
                                callPhoneNumber();
                            } else {
                                PermissionUtil.requestCallPermission(ReceiptDetailActivity.this);
                            }
                        }
                    });

                }
            };
            if(type == Constants.TYPE_PRODUCT)
                ServiceClient.getInstance().getProductReceiptDetailbyId(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id);
            if(type == TYPE_PRODUCT_ORDER)
                ServiceClient.getInstance().getProductReceiptDetailbyOrderId(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id);
        }
        if(type == Constants.TYPE_MASSAGE || type ==TYPE_MASSAGE_ORDER) {
            SubscriberListener mListener = new SubscriberListener<MassageReceipt>() {
                @Override
                public void onNext(final MassageReceipt result) {
                    tvMarchent.setText(result.getMassage_name());
                    tvId.setText(getString(R.string.pay_id)+result.getOrder_code());
                    tvTitle.setText(result.getProduct_name_usp());
                    String money1 = getString(R.string.money) + new DecimalFormat("0.00").format(result.getReal_payment());
                    String money2 = getString(R.string.receipt_title);
                    SpannableString msp = new SpannableString(money1+money2);
                    msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, money1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvAmount.setText(msp);
                    tvCode.setText(getString(R.string.receipt_code)+result.getEncryption_QR_code());
                    tvSubTitle.setText(String.format(getString(R.string.masseur3), result.getMassagist_name()));
                    ivCode.setImageBitmap(Tools.createQRCodeBitmap(result.getEncryption_QR_code(), 100, 100));
                    tvMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ReceiptDetailActivity.this, MapViewActivity.class);
                            Bundle b= new Bundle();
                            b.putParcelable(Constants.PARAM_DATA, new LatLng(result.getLat(),result.getLon()));
                            b.putString(Constants.PARAM_TITLE, result.getMassage_name());
                            b.putString(Constants.PARAM_HOSPITAL_ID, result.getAddress());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                    String number = result.getContact_mobile();
                    String s = String.format(getString(R.string.hint_make_appointment), number);
                    msp = new SpannableString(s);
                    msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), s.length()-number.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvPhone.setText(msp);
                    tvPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            phoneNumber = result.getContact_mobile();
                            if (PermissionUtil.hasCallPermission(ReceiptDetailActivity.this)) {
                                callPhoneNumber();
                            } else {
                                PermissionUtil.requestCallPermission(ReceiptDetailActivity.this);
                            }
                        }
                    });
                }
            };
            if(type == Constants.TYPE_MASSAGE)
                ServiceClient.getInstance().getMassageReceiptDetailbyId(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id);
            if(type == TYPE_MASSAGE_ORDER)
                ServiceClient.getInstance().getMassageReceiptDetailbyOrderId(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), id);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_receipt_detail;
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
        alertDialogBuilder.setMessage(phoneNumber);
        alertDialogBuilder.setPositiveButton(getString(R.string.call),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
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
