package com.us.hotr.ui.activity.beauty;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.SnapBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ImageBanner;
import com.us.hotr.customview.ObserveScrollView;
import com.us.hotr.customview.PagerAdapter;
import com.us.hotr.receiver.Share;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.PayNumberActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.dialog.PriceQueryDialogFragment;
import com.us.hotr.ui.dialog.PromiseQueryDialogFragment;
import com.us.hotr.ui.dialog.ShareDialogFragment;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.ProductHospitalFragment;
import com.us.hotr.ui.fragment.beauty.ProductIntroFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetProductDetailResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Mloong on 2017/9/11.
 */

public class ProductActivity extends BaseLoadingActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ObserveScrollView scrollView;
    private ImageView ivQuestion, ivBackHome, ivOnePrice, ivPromoPrice, ivHospitalAvatar, ivHospitalCetificate, ivFav;
    private ImageBanner mBanner;
    private TextView tvPurchase, tvTitle, tvPriceAfter, tvPriceBefore, tvAppointment, tvApplyTime,
            tvHospitalName, tvHospitalType, tvHospitalAddress, tvHospitalEnqurey, tvDoctorName, tvDoctorSpecial, tvPaymentType, tvPaymentAmount, tvPayOther;
    private ConstraintLayout clPromise, clHospital, clDoctor;
    private List<LinearLayout> llPromiseList = new ArrayList<>();
    private List<TextView> tvPromiseList = new ArrayList<>();
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private long productId;
    private GetProductDetailResponse mProduct;
    private boolean isCollected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Properties prop = new Properties();
        prop.setProperty("id", productId+"");
        StatService.trackCustomKVEvent(this, Constants.MTA_ID_PRODUCT_DETAIL_SCREEN, prop);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_product;
    }

    private void initStaticView(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        scrollView = (ObserveScrollView) findViewById(R.id.scrollView);
        ivQuestion = (ImageView) findViewById(R.id.iv_question);
        ivBackHome = (ImageView) findViewById(R.id.iv_homepage);
        tvPurchase = (TextView) findViewById(R.id.tv_purchase);

        mBanner = (ImageBanner) findViewById(R.id.banner);
        ivOnePrice = (ImageView) findViewById(R.id.iv_one_price);
        ivPromoPrice = (ImageView) findViewById(R.id.iv_promo_price);
        ivHospitalAvatar = (ImageView) findViewById(R.id.iv_hospital_avatar);
        ivFav = (ImageView) findViewById(R.id.iv_fav);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPriceAfter = (TextView) findViewById(R.id.tv_amount);
        tvPriceBefore = (TextView) findViewById(R.id.tv_price_before);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise1));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise2));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise3));
        tvPromiseList.add((TextView) findViewById(R.id.tv_promise4));
        ivHospitalCetificate = (ImageView) findViewById(R.id.iv_certified);
        tvHospitalName = (TextView) findViewById(R.id.tv_hospital1);
        tvHospitalType = (TextView) findViewById(R.id.tv_hospital2);
        tvHospitalAddress = (TextView) findViewById(R.id.tv_hospital3);
        tvHospitalEnqurey = (TextView) findViewById(R.id.tv_query);
        tvDoctorName = (TextView) findViewById(R.id.tv_doctor_name);
        tvDoctorSpecial = (TextView) findViewById(R.id.tv_special);
        tvPaymentType = (TextView) findViewById(R.id.tv_payment_type);
        tvPaymentAmount = (TextView) findViewById(R.id.tv_deposit);
        tvPayOther = (TextView) findViewById(R.id.tv_pay_other);
        tvApplyTime = (TextView) findViewById(R.id.tv_apply_time);
        clPromise = (ConstraintLayout) findViewById(R.id.cl_promise);
        clHospital = (ConstraintLayout) findViewById(R.id.cl_hospital);
        clDoctor = (ConstraintLayout) findViewById(R.id.cl_doctor);
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise1));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise2));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise3));
        llPromiseList.add((LinearLayout) findViewById(R.id.ll_promise4));

        for(LinearLayout l:llPromiseList)
            l.setVisibility(View.GONE);

        enableLoadMore(false);

        ivQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PriceQueryDialogFragment().show(getSupportFragmentManager(), "dialog");
            }
        });

        ivBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mBanner.setRatio(1);
        mBanner.setPlacehoderResource(R.drawable.placeholder_post_2);

        findViewById(R.id.tb_title).setAlpha(0);
        findViewById(R.id.v_divider).setAlpha(0);
        scrollView.setScrollListener(new ObserveScrollView.ScrollListener() {
            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt) {
                float y = (float) t;
                if(y > 300){
                    ivBack.setImageResource(R.mipmap.ic_back);
                    ivShare.setImageResource(R.mipmap.ic_share);
                    findViewById(R.id.tb_title).setAlpha(1);
                    findViewById(R.id.v_divider).setAlpha(1);
                }else{
                    findViewById(R.id.tb_title).setAlpha(y / 300);
                    findViewById(R.id.v_divider).setAlpha(y / 300);
                    ivBack.setImageResource(R.mipmap.ic_back_dark);
                    ivShare.setImageResource(R.mipmap.ic_share_dark);
                }
            }
        });

        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<GetProductDetailResponse>() {
            @Override
            public void onNext(final GetProductDetailResponse result) {
                final GetProductDetailResponse.Product product =result.getProduct();
                mProduct = result;
                final List<String> urls = Tools.mapToList(Tools.gsonStringToMap(product.getProductImg()));
                if(urls!=null && urls.size()>0) {
                    mBanner.setSource(urls);
                    mBanner.startScroll();
                    if(urls.size()==1)
                        mBanner.pauseScroll();
                }
//                mBanner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
//                    @Override
//                    public void onBannerItemClicked(int position) {
//                        Intent i = new Intent(ProductActivity.this, ImageViewerActivity.class);
//                        Bundle b = new Bundle();
//                        b.putSerializable(Constants.PARAM_DATA, (Serializable)urls);
//                        b.putInt(Constants.PARAM_ID, position);
//                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
//                        i.putExtras(b);
//                        startActivity(i);
//                    }
//                });
                tvTitle.setText(getString(R.string.bracket_left)+product.getProductName()+getString(R.string.bracket_right)+product.getProductUsp());
                tvApplyTime.setText(getString(R.string.use_time) + product.getUsableTime());
                if(product.getPaymentType() == Constants.FULL_PAYMENT) {
                    ivOnePrice.setVisibility(View.VISIBLE);
                    tvPaymentType.setText(getString(R.string.full_amount));
                    tvPayOther.setText(getString(R.string.no_need_to_pay_at_shop));
                    if(product.getProductType() == Constants.PROMOTION_PRODUCT)
                        tvPaymentAmount.setText(new DecimalFormat("0.00").format(product.getActivityOnlinePrice()));
                    else
                        tvPaymentAmount.setText(new DecimalFormat("0.00").format(product.getOnlinePrice()));
                }
                else {
                    ivOnePrice.setVisibility(View.GONE);
                    tvPaymentType.setText(getString(R.string.deposit));
                    if(product.getProductType() == Constants.PROMOTION_PRODUCT) {
                        tvPaymentAmount.setText(new DecimalFormat("0.00").format(product.getActivityPrepayment()));
                        tvPayOther.setText(String.format(getString(R.string.pay_at_shop), new DecimalFormat("0.00").format(product.getActivityOnlinePrice()-product.getActivityPrepayment())));
                    }
                    else {
                        tvPaymentAmount.setText(new DecimalFormat("0.00").format(product.getPrepayment()));
                        tvPayOther.setText(String.format(getString(R.string.pay_at_shop), new DecimalFormat(".00").format(product.getOnlinePrice()-product.getPrepayment())));
                    }
                }
                if(product.getProductType() == Constants.PROMOTION_PRODUCT){
                    ivPromoPrice.setVisibility(View.VISIBLE);
                    tvPriceAfter.setText(product.getActivityOnlinePrice()+"");
                    if(product.getActivityCount()<=0) {
                        tvPurchase.setText(R.string.sold_out);
                        tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        tvPurchase.setOnClickListener(null);
                    }else{
                        tvPurchase.setText(R.string.buy_now);
                        tvPurchase.setBackgroundResource(R.color.text_black);
                        tvPurchase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                purchase();
                            }
                        });
                    }
                }else{
                    ivPromoPrice.setVisibility(View.GONE);
                    tvPriceAfter.setText(new DecimalFormat("0.00").format(product.getOnlinePrice()));
                    tvPurchase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            purchase();
                        }
                    });
                }
                if(product.getOnsaleState() == 0){
                    tvPurchase.setText(R.string.not_on_sale);
                    tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                    tvPurchase.setOnClickListener(null);
                }
                TextPaint tp = tvPriceAfter.getPaint();
                tp.setFakeBoldText(true);
                tvPriceBefore.setText(String.format(getString(R.string.price), new DecimalFormat("0.00").format(product.getShopPrice())));
                tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                tvAppointment.setText(String.format(getString(R.string.massage_appointment), result.getProductOrderNum()));

                if(result.getPromiseList()!=null && result.getPromiseList().size()>0){
                    clPromise.setVisibility(View.VISIBLE);
                    clPromise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new PromiseQueryDialogFragment().newInstance(result.getPromiseList()).show(getSupportFragmentManager(), "dialog");
                        }
                    });
                    for(int i=0;i<(Math.min(4,result.getPromiseList().size()));i++){
                        llPromiseList.get(i).setVisibility(View.VISIBLE);
                        tvPromiseList.get(i).setText(result.getPromiseList().get(i).getPromise_title());
                    }
                }else
                    clPromise.setVisibility(View.GONE);

                if(result.getHospital()!=null){
                    clHospital.setVisibility(View.VISIBLE);
                    Glide.with(ProductActivity.this).load(result.getHospital().getHospLogo()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivHospitalAvatar);
                    tvHospitalName.setText(result.getHospital().getHospName());
                    tvHospitalType.setText(result.getHospital().getHospType());
                    tvHospitalAddress.setText(result.getHospital().getCityName()+result.getHospital().getAreaName()+result.getHospital().getHospAddress());
                    clHospital.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent i = new Intent(ProductActivity.this, HospitalActivity.class);
                            Bundle b = new Bundle();
                            b.putLong(Constants.PARAM_ID, result.getHospital().getKey());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                }else
                    clHospital.setVisibility(View.GONE);

                if(result.getDoctor()!=null && result.getDoctor().getDoctor_name()!=null){
                    clDoctor.setVisibility(View.VISIBLE);
                    tvDoctorName.setText(result.getDoctor().getDoctor_name());
                    tvDoctorSpecial.setText(getString(R.string.specialize2) + result.getDoctor().getTypeName());
                    clDoctor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ProductActivity.this, DoctorActivity.class);
                            Bundle b = new Bundle();
                            b.putLong(Constants.PARAM_ID, result.getDoctor().getDoctor_id());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                }else
                    clDoctor.setVisibility(View.GONE);
                isCollected = result.getIs_collected()==1?true:false;
                if(isCollected)
                    ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                else
                    ivFav.setImageResource(R.mipmap.ic_fav_text);
                ivFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isCollected) {
                            SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
                                @Override
                                public void onNext(String result) {
                                    isCollected = false;
                                    Tools.Toast(ProductActivity.this, getString(R.string.remove_fav_item_success));
                                    ivFav.setImageResource(R.mipmap.ic_fav_text);
                                }
                                @Override
                                public void reload() {
                                    loadData(Constants.LOAD_DIALOG);
                                }
                            };
                            ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, ProductActivity.this),
                                    HOTRSharePreference.getInstance(ProductActivity.this.getApplicationContext()).getUserID(), Arrays.asList(product.getKey()), 3);
                        }else{
                            SubscriberListener mListener = new SubscriberListener<String>() {
                                @Override
                                public void onNext(String result) {
                                    isCollected = true;
                                    Tools.Toast(ProductActivity.this, getString(R.string.fav_item_success));
                                    ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                                }
                            };
                            Properties prop = new Properties();
                            prop.setProperty("id", product.getKey()+"");
                            StatService.trackCustomKVEvent(ProductActivity.this, Constants.MTA_ID_FAV_PRODUCT, prop);
                            ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, ProductActivity.this),
                                    HOTRSharePreference.getInstance(ProductActivity.this.getApplicationContext()).getUserID(), product.getKey(), 3);
                        }
                    }
                });

                ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Share share = new Share();
                        share.setDescription(getString(R.string.share_product));
                        share.setImageUrl(Tools.getMainPhoto(product.getProductImg()));
                        share.setTitle(getString(R.string.bracket_left)+product.getProductName()+getString(R.string.bracket_right)+product.getProductUsp());
                        share.setUrl(Constants.SHARE_URL + "#/commodityYM?id="+product.getKey());
                        share.setSinaContent(getString(R.string.share_sina_product));
                        share.setType(Share.TYPE_NORMAL);
                        ShareDialogFragment.newInstance(share).show(getSupportFragmentManager(), "dialog");
                    }
                });

                titleList = new ArrayList<String>() {{
                    add(getString(R.string.product_intro));
                    add(getString(R.string.related_case));
                    add(getString(R.string.hospital_highlight));
                }};

                fragmentList = new ArrayList<Fragment>() {{
                    add(ProductIntroFragment.newInstance(result));
                    add(CaseListFragment.newInstance(null, false, false, 2, productId, -1));
                    add(ProductHospitalFragment.newInstance(result));
                }};

                adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
                viewPager.setAdapter(adapter);
            }
        };
        if(loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getProductDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    productId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        if(loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getProductDetail(new LoadingSubscriber(mListener, this),
                    productId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    public void purchase(){
        if(Tools.isUserLogin(getApplicationContext())){
            purchaseCheckCount();
        }else{
            LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                @Override
                public void onLoginSuccess() {
                    purchaseCheckCount();
                }

                @Override
                public void onLoginCancel() {

                }
            });
            startActivityForResult(new Intent(ProductActivity.this, LoginActivity.class), 0);
        }

    }

    public void purchaseCheckCount(){
        if(mProduct.getProduct().getProductType() == Constants.PROMOTION_PRODUCT){
            SubscriberListener mListener = new SubscriberListener<Integer>() {
                @Override
                public void onNext(Integer result) {
                    if(result == 0){
                        Tools.Toast(ProductActivity.this, getString(R.string.product_sold_out));
                        tvPurchase.setText(R.string.sold_out);
                        tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        tvPurchase.setOnClickListener(null);
                    }else {
                        Intent i = new Intent(ProductActivity.this, PayNumberActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
                        b.putSerializable(Constants.PARAM_DATA, mProduct);
                        i.putExtras(b);
                        startActivity(i);
                    }
                }
            };
            ServiceClient.getInstance().checkOrderCount(new ProgressSubscriber(mListener, this),
                    mProduct.getProduct().getKey(), 0);
        }else{
            Intent i = new Intent(ProductActivity.this, PayNumberActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
            b.putSerializable(Constants.PARAM_DATA, mProduct);
            i.putExtras(b);
            startActivity(i);
        }
    }
}
