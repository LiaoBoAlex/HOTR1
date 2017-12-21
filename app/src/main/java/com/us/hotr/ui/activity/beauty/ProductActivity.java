package com.us.hotr.ui.activity.beauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SnapBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ImageBanner;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.PayNumberActivity;
import com.us.hotr.ui.dialog.PriceQueryDialogFragment;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.ProductHospitalFragment;
import com.us.hotr.ui.fragment.beauty.ProductIntroFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetProductDetailResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mloong on 2017/9/11.
 */

public class ProductActivity extends BaseLoadingActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private AppBarLayout appBarLayout;
    private ImageView ivQuestion, ivBackHome, ivOnePrice, ivPromoPrice, ivHospitalAvatar, ivHospitalCetificate;
    private ImageBanner mBanner;
    private TextView tvPurchase, tvDivider, tvTitle, tvPriceAfter, tvPriceBefore, tvAppointment,
                        tvHospitalName, tvHospitalType, tvHospitalAddress, tvHospitalEnqurey, tvDoctorName, tvDoctorSpecial, tvPaymentType, tvPaymentAmount, tvPayOther;
    private ConstraintLayout clPromise, clHospital, clDoctor;
    private List<LinearLayout> llPromiseList = new ArrayList<>();
    private List<TextView> tvPromiseList = new ArrayList<>();
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private SnapBehavior mBehavior;

    private int productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getIntent().getExtras().getInt(Constants.PARAM_ID);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_product;
    }

    private void initStaticView(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        ivQuestion = (ImageView) findViewById(R.id.iv_question);
        ivBackHome = (ImageView) findViewById(R.id.iv_homepage);
        tvPurchase = (TextView) findViewById(R.id.tv_purchase);
        tvDivider = (TextView) findViewById(R.id.tv_divider);

        mBanner = (ImageBanner) findViewById(R.id.banner);
        ivOnePrice = (ImageView) findViewById(R.id.iv_one_price);
        ivPromoPrice = (ImageView) findViewById(R.id.iv_promo_price);
        ivHospitalAvatar = (ImageView) findViewById(R.id.iv_hospital_avatar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPriceAfter = (TextView) findViewById(R.id.tv_pay_amount);
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
        tvPaymentAmount = (TextView) findViewById(R.id.tv_pay_amount);
        tvPayOther = (TextView) findViewById(R.id.tv_pay_other);
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

        mBanner.setRatio(0.96);

        mBehavior = new SnapBehavior(this, tvDivider);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setBehavior(mBehavior);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float dis = Math.abs(verticalOffset);
                mBehavior.setAppBarLayoutOffset((int)dis);
                if(dis > 300){
                    ivBack.setImageResource(R.mipmap.ic_back);
                    ivShare.setImageResource(R.mipmap.ic_share);
                    findViewById(R.id.tb_title).setAlpha(1);
                    findViewById(R.id.v_divider).setAlpha(1);
                }else{
                    findViewById(R.id.tb_title).setAlpha(dis / 300);
                    findViewById(R.id.v_divider).setAlpha(dis / 300);
                    ivBack.setImageResource(R.mipmap.ic_back_dark);
                    ivShare.setImageResource(R.mipmap.ic_share_dark);
                }
            }
        });

        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager, true);

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<GetProductDetailResponse>() {
            @Override
            public void onNext(final GetProductDetailResponse result) {
                GetProductDetailResponse.Product product =result.getProduct();
                final List<String> urls = Arrays.asList(Tools.validatePhotoString(product.getProductImg()).split("\\s*,\\s*"));
                mBanner.setSource(urls);
                mBanner.startScroll();
                mBanner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
                    @Override
                    public void onBannerItemClicked(int position) {
                        Intent i = new Intent(ProductActivity.this, ImageViewerActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.PARAM_DATA, (Serializable)urls);
                        b.putInt(Constants.PARAM_ID, position);
                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                        i.putExtras(b);
                        startActivity(i);
                    }
                });
                tvTitle.setText(product.getProductName());
                if(product.getPaymentType() == Constants.FULL_PAYMENT) {
                    ivOnePrice.setVisibility(View.VISIBLE);
                    tvPaymentType.setText(getString(R.string.full_amount));
                    tvPayOther.setText(getString(R.string.no_need_to_pay_at_shop));
                    if(product.getProductType() == Constants.PROMOTION_PRODUCT)
                        tvPaymentAmount.setText(product.getActivityOnlinePrice()+"");
                    else
                        tvPaymentAmount.setText(product.getOnlinePrice()+"");
                }
                else {
                    ivOnePrice.setVisibility(View.GONE);
                    tvPaymentType.setText(getString(R.string.deposit));
                    if(product.getProductType() == Constants.PROMOTION_PRODUCT) {
                        tvPaymentAmount.setText(product.getActivityPrepayment() + "");
                        tvPayOther.setText(String.format(getString(R.string.pay_at_shop), product.getActivityOnlinePrice()-product.getActivityPrepayment()));
                    }
                    else {
                        tvPaymentAmount.setText(product.getOnlinePrice() + "");
                        tvPayOther.setText(String.format(getString(R.string.pay_at_shop), product.getOnlinePrice()-product.getPrepayment()));
                    }
                }
                if(product.getProductType() == Constants.PROMOTION_PRODUCT){
                    ivPromoPrice.setVisibility(View.VISIBLE);
                    tvPriceAfter.setText(product.getActivityOnlinePrice());
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
                                startActivity(new Intent(ProductActivity.this, PayNumberActivity.class));
                            }
                        });
                    }

                }else{
                    ivPromoPrice.setVisibility(View.GONE);
                    tvPriceAfter.setText(product.getOnlinePrice()+"");
                }
                tvPriceBefore.setText(String.format(getString(R.string.price), product.getShopPrice()));
                tvAppointment.setText(String.format(getString(R.string.massage_appointment), result.getProductOrderNum()));

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

                if(result.getHospital()!=null){
                    clHospital.setVisibility(View.VISIBLE);
                    Glide.with(ProductActivity.this).load(result.getHospital().getHospLogo()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivHospitalAvatar);
                    tvHospitalName.setText(result.getHospital().getHospName());
                    tvHospitalType.setText(result.getHospital().getHospType());
                    tvHospitalAddress.setText(result.getHospital().getProvinceName()+result.getHospital().getCityName()+result.getHospital().getAreaName()+result.getHospital().getHospAddress());
                    clHospital.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ProductActivity.this, HospitalActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_ID, result.getHospital().getKey());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                }else
                    clHospital.setVisibility(View.GONE);

                if(result.getDoctor()!=null){
                    clDoctor.setVisibility(View.VISIBLE);
                    tvDoctorName.setText(result.getDoctor().getDocName());
                    tvDoctorSpecial.setText(result.getDoctor().getTypeName());
                    clDoctor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ProductActivity.this, DoctorActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_ID, result.getDoctor().getId());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                }else
                    clDoctor.setVisibility(View.GONE);

                titleList = new ArrayList<String>() {{
                    add(getString(R.string.product_intro));
                    add(getString(R.string.related_case));
                    add(getString(R.string.hospital_highlight));
                }};

                fragmentList = new ArrayList<Fragment>() {{
                    add(ProductIntroFragment.newInstance(result));
                    add(CaseListFragment.newInstance(false));
                    add(ProductHospitalFragment.newInstance(result));
                }};

                adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
                viewPager.setAdapter(adapter);
            }
        };
        if(loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getProductDetail(new SilentSubscriber(mListener, this, refreshLayout), productId);
        if(loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getProductDetail(new LoadingSubscriber(mListener, this), productId);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
