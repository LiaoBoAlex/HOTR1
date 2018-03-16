package com.us.hotr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.MassageOrder;
import com.us.hotr.storage.bean.ProductOrder;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.Voucher;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.AvailableVoucherRequest;
import com.us.hotr.webservice.request.CreateMassageOrderRequest;
import com.us.hotr.webservice.request.CreateProductOrderRequest;
import com.us.hotr.webservice.response.GetProductDetailResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;

/**
 * Created by Mloong on 2017/9/12.
 */

public class PayNumberActivity extends BaseActivity{

    private ImageView ivDeduct, ivAdd, ivAvatar;
    private TextView tvNumber, tvPay, tvTitle, tvSubTitle, tvPayTitle, tvPayAmount, tvPayTotal, tvPayOther, tvMin, tvTotal, tvVoucher;
    private LinearLayout llPayOther;

    private int number = 1;
    private GetProductDetailResponse product;
    private Massage massage;
    private Long masseurId;
    private Spa spa;
    private int type;
    private double price, priceOther, totalPayNow = 0, totalPayAtShop = 0, voucherPrice = 0;
    private Voucher selectedvoucher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.confirm_order_title);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        if(type == Constants.TYPE_PRODUCT)
            product = (GetProductDetailResponse)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        if(type == Constants.TYPE_MASSAGE) {
            massage = (Massage) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
            masseurId = getIntent().getExtras().getLong(Constants.PARAM_ID);
            spa = (Spa)getIntent().getExtras().getSerializable(Constants.PARAM_SPA_ID);
        }
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
        ivAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
        tvPayTitle = (TextView) findViewById(R.id.tv_price_title);
        tvPayAmount = (TextView) findViewById(R.id.tv_amount);
        tvPayTotal = (TextView) findViewById(R.id.tv_pay_total);
        tvPayOther = (TextView) findViewById(R.id.tv_pay_other);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        tvMin = (TextView) findViewById(R.id.tv_min);
        llPayOther = (LinearLayout) findViewById(R.id.ll_other_pay);
        tvVoucher = (TextView) findViewById(R.id.tv_voucher);

        if(type == Constants.TYPE_MASSAGE){
            if(massage.getProductImg()!=null)
                Glide.with(this).load(Tools.getMainPhoto(Tools.gsonStringToMap(massage.getProductImg()))).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
            if(massage.getProduct_main_img()!=null)
                Glide.with(this).load(massage.getProduct_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
            tvTitle.setText(getString(R.string.bracket_left)+massage.getProductName()+getString(R.string.bracket_right)+massage.getProductUsp());
            tvSubTitle.setText(spa.getMassageName());
            tvPayTitle.setVisibility(View.GONE);
            llPayOther.setVisibility(View.GONE);
            if(massage.getProductType()==Constants.PROMOTION_PRODUCT) {
                tvPayAmount.setText(new DecimalFormat("0.00").format(massage.getActivityPrice()) + "/" + massage.getServiceTime());
                price = massage.getActivityPrice();
            }
            else {
                tvPayAmount.setText(new DecimalFormat("0.00").format(massage.getOnlinePrice()) + "/" + massage.getServiceTime());
                price = massage.getOnlinePrice();
            }
        }
        if(type == Constants.TYPE_PRODUCT){
            Glide.with(this).load(Tools.getMainPhoto(Tools.gsonStringToMap(product.getProduct().getProductImg()))).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
            tvTitle.setText(getString(R.string.bracket_left)+product.getProduct().getProductName()+getString(R.string.bracket_right)+product.getProduct().getProductUsp());
            tvSubTitle.setText(product.getHospital().getHospName());
            tvMin.setVisibility(View.GONE);
            if(product.getProduct().getPaymentType() == Constants.FULL_PAYMENT) {
                if(product.getProduct().getProductType() == Constants.PROMOTION_PRODUCT) {
                    price = product.getProduct().getActivityOnlinePrice();
                    tvPayAmount.setText(new DecimalFormat("0.00").format(product.getProduct().getActivityOnlinePrice()));
                }
                else {
                    price = product.getProduct().getOnlinePrice();
                    tvPayAmount.setText(new DecimalFormat("0.00").format(product.getProduct().getOnlinePrice()));
                }
                tvPayTitle.setText(R.string.pay1);
                llPayOther.setVisibility(View.GONE);
            }else {
                if(product.getProduct().getProductType() == Constants.PROMOTION_PRODUCT) {
                    price = product.getProduct().getActivityPrepayment();
                    priceOther = product.getProduct().getActivityOnlinePrice() - price;
                    tvPayAmount.setText(new DecimalFormat("0.00").format(product.getProduct().getActivityPrepayment()));
                }
                else {
                    price = product.getProduct().getPrepayment();
                    priceOther = product.getProduct().getOnlinePrice() - price;
                    tvPayAmount.setText(new DecimalFormat("0.00").format(product.getProduct().getPrepayment()));
                }
                tvPayTitle.setText(R.string.deposit);
            }
        }

        updateContent();

        tvNumber.setText(number+"");
        ivDeduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number > 2) {
                    number--;
                    tvNumber.setText(Integer.toString(number));
                }
                if(number == 2){
                    number--;
                    tvNumber.setText(Integer.toString(number));
                    ivDeduct.setImageResource(R.mipmap.ic_deduct_gray);
                }
                updateContent();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number++;
                tvNumber.setText(Integer.toString(number));
                if(number == 2){
                    ivDeduct.setImageResource(R.mipmap.ic_deduct);
                }
                updateContent();
            }
        });

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchaseCheckPersonalCount();
            }
        });

        tvVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayNumberActivity.this, AvailableVoucherActivity.class);
                Bundle b = new Bundle();
                AvailableVoucherRequest request = new AvailableVoucherRequest();
                if(type == Constants.TYPE_MASSAGE){
                    request.setType(2);
                    request.setProduct_price(number*price);
                }
                if(type == Constants.TYPE_PRODUCT){
                    request.setType(1);
                    if(product.getProduct().getPaymentType() == Constants.FULL_PAYMENT)
                        request.setProduct_price(number*price);
                    else
                        request.setProduct_price(number*(price+priceOther));
                }
                b.putSerializable(Constants.PARAM_DATA, request);
                b.putSerializable(Constants.PARAM_TYPE, selectedvoucher);
                i.putExtras(b);
                startActivityForResult(i, 0);
            }
        });

    }

    private void createOrder(){

        if(type == Constants.TYPE_PRODUCT) {
            CreateProductOrderRequest request = new CreateProductOrderRequest();
            request.setActual_payable_price(totalPayNow);
            if(selectedvoucher!=null) {
                request.setCoupon_id(selectedvoucher.getId());
                request.setCoupon_user_id(selectedvoucher.getCouponUserId());
            }
            request.setHospital_id(product.getHospital().getKey());
            request.setProduct_id(product.getProduct().getKey());
            request.setOrder_product_sum(number);
            request.setPay_at_shop(totalPayAtShop);
            SubscriberListener mListener = new SubscriberListener<ProductOrder>() {
                @Override
                public void onNext(ProductOrder result) {
                    result.setProduct_name_usp(getString(R.string.bracket_left)+product.getProduct().getProductName()+getString(R.string.bracket_right)+product.getProduct().getProductUsp());
                    result.setHospital_name(product.getHospital().getHospName());
                    result.setProduct_id(product.getProduct().getKey());
                    Intent i = new Intent(PayNumberActivity.this, PayOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, result);
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
                    i.putExtras(b);
                    startActivity(i);
                }
            };
            ServiceClient.getInstance().createOrderProduct(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), request);
        }
        if(type == Constants.TYPE_MASSAGE) {
            CreateMassageOrderRequest request = new CreateMassageOrderRequest();
            request.setActual_payable_price(totalPayNow);
            if(selectedvoucher!=null) {
                request.setCoupon_id(selectedvoucher.getId());
                request.setCoupon_user_id(selectedvoucher.getCouponUserId());
            }
            request.setMassage_id(spa.getKey());
            request.setMassagist_id(masseurId);
            request.setProduct_id(massage.getKey());
            request.setOrder_product_sum(number);
            SubscriberListener mListener = new SubscriberListener<MassageOrder>() {
                @Override
                public void onNext(MassageOrder result) {
                    result.setProduct_name_usp(getString(R.string.bracket_left)+massage.getProductName()+getString(R.string.bracket_right)+massage.getProductUsp());
                    result.setMassage_name(spa.getMassageName());
                    result.setProduct_id(massage.getKey());
                    Intent i = new Intent(PayNumberActivity.this, PayOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, result);
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                    i.putExtras(b);
                    startActivity(i);
            }
            };
            ServiceClient.getInstance().createOrderMassage(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), request);
        }

    }

    private void purchaseCheckPersonalCount(){
        if(type == Constants.TYPE_PRODUCT) {
            if (product.getProduct().getProductType() == Constants.PROMOTION_PRODUCT) {
                SubscriberListener mListener = new SubscriberListener<Integer>() {
                    @Override
                    public void onNext(Integer result) {
                        if (result < number)
                            Tools.Toast(PayNumberActivity.this, String.format(getString(R.string.buy_limit), result));
                        else {
                            purchaseCheckCount();
                        }
                    }
                };
                ServiceClient.getInstance().checkPersonalCountProduct(new ProgressSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), product.getProduct().getKey());
            } else {
                createOrder();
            }
        }
        if(type == Constants.TYPE_MASSAGE){
            if (massage.getProductType() == Constants.PROMOTION_PRODUCT) {
                SubscriberListener mListener = new SubscriberListener<Integer>() {
                    @Override
                    public void onNext(Integer result) {
                        if (result < number)
                            Tools.Toast(PayNumberActivity.this, String.format(getString(R.string.buy_limit), result));
                        else {
                            purchaseCheckCount();
                        }
                    }
                };
                ServiceClient.getInstance().checkPersonalCountMassage(new ProgressSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), massage.getKey());
            } else {
                createOrder();
            }
        }
    }

    private void purchaseCheckCount(){
        if(type == Constants.TYPE_PRODUCT) {
            if (product.getProduct().getProductType() == Constants.PROMOTION_PRODUCT) {
                SubscriberListener mListener = new SubscriberListener<Integer>() {
                    @Override
                    public void onNext(Integer result) {
                        if (result == 0) {
                            Tools.Toast(PayNumberActivity.this, getString(R.string.product_sold_out));
                            tvPay.setText(R.string.sold_out);
                            tvPay.setBackgroundResource(R.color.bg_button_grey);
                            tvPay.setOnClickListener(null);
                        } else if (result < number)
                            Tools.Toast(PayNumberActivity.this, String.format(getString(R.string.product_left), result));
                        else {
                            createOrder();
                        }
                    }
                };
                ServiceClient.getInstance().checkOrderCount(new ProgressSubscriber(mListener, this),
                        product.getProduct().getKey(), 0);
            } else {
                createOrder();
            }
        }
        if(type == Constants.TYPE_MASSAGE){
            if (massage.getProductType() == Constants.PROMOTION_PRODUCT) {
                SubscriberListener mListener = new SubscriberListener<Integer>() {
                    @Override
                    public void onNext(Integer result) {
                        if (result == 0) {
                            Tools.Toast(PayNumberActivity.this, getString(R.string.product_sold_out));
                            tvPay.setText(R.string.sold_out);
                            tvPay.setBackgroundResource(R.color.bg_button_grey);
                            tvPay.setOnClickListener(null);
                        } else if (result < number)
                            Tools.Toast(PayNumberActivity.this, String.format(getString(R.string.product_left), result));
                        else {
                            createOrder();
                        }
                    }
                };
                ServiceClient.getInstance().checkOrderCount(new ProgressSubscriber(mListener, this),
                        massage.getKey(), 1);
            } else {
                createOrder();
            }
        }
    }

    private void updateContent(){
        if(type == Constants.TYPE_MASSAGE){
            tvPayTotal.setText(String.format(getString(R.string.pay_total),  String.format("%.2f ",number*price)));
            tvTotal.setText(getString(R.string.money)+String.format("%.2f ",number*price-voucherPrice));
            totalPayAtShop = 0;
            totalPayNow = number*price-voucherPrice;
        }
        if(type == Constants.TYPE_PRODUCT){
            if(product.getProduct().getPaymentType() == Constants.FULL_PAYMENT) {
                tvPayTotal.setText(String.format(getString(R.string.pay_total),  String.format("%.2f ",number*price)));
                tvTotal.setText(getString(R.string.money)+String.format("%.2f ",number*price-voucherPrice));
                totalPayAtShop = 0;
                totalPayNow = number*price-voucherPrice;
            }else {
                tvPayTotal.setText(String.format(getString(R.string.pay_deposit),  String.format("%.2f ",number*price)));
                tvPayOther.setText(String.format(getString(R.string.pay_other),  String.format("%.2f ",number*priceOther)));
                tvTotal.setText(getString(R.string.money)+String.format("%.2f ",number*price-voucherPrice));
                totalPayAtShop = number*priceOther;
                totalPayNow = number*price-voucherPrice;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data.getExtras()!=null){
                Voucher voucher = (Voucher)data.getExtras().getSerializable(Constants.PARAM_DATA);
                tvVoucher.setText(voucher.getCoupon_name());
                selectedvoucher = voucher;
                voucherPrice = voucher.getCoupon_money();
            }else {
                tvVoucher.setText(R.string.choose_voucher);
                selectedvoucher = null;
                voucherPrice = 0;
            }
            updateContent();
        }
    }
}
