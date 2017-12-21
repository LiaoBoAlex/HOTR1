package com.us.hotr.ui.activity.massage;

import android.content.Intent;
import android.graphics.Paint;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ImageBanner;
import com.us.hotr.webservice.response.GetMassageDetailResponse;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.PayNumberActivity;
import com.us.hotr.ui.fragment.massage.MassageIntroFragment;
import com.us.hotr.ui.fragment.massage.MassageSpaFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mloong on 2017/9/30.
 */

public class MassageActivity extends BaseLoadingActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private AppBarLayout appBarLayout;
    private ImageView ivBack, ivShare, ivBackHome, ivSpaAvatar, ivPromo;
    private ImageBanner mBanner;
    private TextView tvPurchase, tvDivider, tvTitle, tvPriceAfter, tvPriceBefore, tvAppointment, tvAddress, tvMasseurTitle,
                        tvSpaName, tvSpaTime, tvSpaAddress, tvSpaQuery;
    private ConstraintLayout clSpa;
    private RecyclerView rvMasseur;
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private SnapBehavior mBehavior;

    private int mMassageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMassageId = getIntent().getExtras().getInt(Constants.PARAM_ID);
        initStaticView();
        setMyTitle(R.string.product_detail);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_massage;
    }

    private void initStaticView(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        ivBack = (ImageView) findViewById(R.id.img_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivBackHome = (ImageView) findViewById(R.id.iv_homepage);
        tvPurchase = (TextView) findViewById(R.id.tv_purchase);
        tvDivider = (TextView) findViewById(R.id.tv_divider);
        rvMasseur = (RecyclerView) findViewById(R.id.recyclerview);
        mBanner = (ImageBanner) findViewById(R.id.banner);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPriceAfter = (TextView) findViewById(R.id.tv_pay_amount);
        tvPriceBefore = (TextView) findViewById(R.id.tv_price_before);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvMasseurTitle = (TextView) findViewById(R.id.tv_masseur_title);
        ivSpaAvatar = (ImageView) findViewById(R.id.iv_hospital_avatar);
        tvSpaName = (TextView) findViewById(R.id.tv_hospital1);
        tvSpaTime = (TextView) findViewById(R.id.tv_hospital2);
        tvSpaAddress = (TextView) findViewById(R.id.tv_hospital3);
        tvSpaQuery = (TextView) findViewById(R.id.tv_query);
        ivPromo = (ImageView) findViewById(R.id.iv_promo);
        clSpa = (ConstraintLayout) findViewById(R.id.cl_spa);

        tvPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MassageActivity.this, PayNumberActivity.class));
            }
        });

        ivBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MassageActivity.this, MainActivity.class);
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

        rvMasseur.setLayoutManager(new LinearLayoutManager(this));
        rvMasseur.setItemAnimator(new DefaultItemAnimator());

        enableLoadMore(false);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager, true);

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<GetMassageDetailResponse>() {
            @Override
            public void onNext(final GetMassageDetailResponse result) {
                if(result == null || result.getProduct()==null) {
                    showErrorPage();
                    return;
                }

                final List<String> photoes = Arrays.asList(Tools.validatePhotoString(result.getProduct().getProductImg()).split("\\s*,\\s*"));
                mBanner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
                    @Override
                    public void onBannerItemClicked(int position) {
                        Intent i = new Intent(MassageActivity.this, ImageViewerActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.PARAM_DATA, (Serializable)photoes);
                        b.putInt(Constants.PARAM_ID, position);
                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                        i.putExtras(b);
                        startActivity(i);
                    }
                });
                mBanner.setSource(photoes);
                mBanner.startScroll();
                tvTitle.setText(getString(R.string.bracket_left)+result.getProduct().getProductName()+getString(R.string.bracket_right)+result.getProduct().getProductUsp());
                if(result.getProduct().getProductType()==Constants.PROMOTION_PRODUCT){
                    tvPriceAfter.setText(result.getProduct().getActivityPrice() + "/" + result.getProduct().getServiceTime());
                    ivPromo.setVisibility(View.VISIBLE);
                }else {
                    tvPriceAfter.setText(result.getProduct().getShopPrice() + "/" + result.getProduct().getServiceTime());
                    ivPromo.setVisibility(View.GONE);
                }
                TextPaint tp = tvPriceAfter.getPaint();
                tp.setFakeBoldText(true);
                tvPriceBefore.setText(String.format(getString(R.string.price), result.getProduct().getOnlinePrice()));
                tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                tvAppointment.setText(String.format(getString(R.string.massage_appointment), result.getProductOrderNum()));
                tvAddress.setText(result.getMassage().getLandmark_name());
                if(result.getMassage()==null)
                    clSpa.setVisibility(View.GONE);
                else {
                    clSpa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MassageActivity.this, SpaActivity.class);
                            Bundle b= new Bundle();
                            b.putInt(Constants.PARAM_ID, result.getMassage().getId());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    tvSpaName.setText(result.getMassage().getMassageName());
                    Glide.with(MassageActivity.this).load(result.getMassage().getMassageLogo()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivSpaAvatar);
                    tvSpaAddress.setText(result.getMassage().getProvinceName() +
                            result.getMassage().getCityName() +
                            result.getMassage().getAreaName() +
                            result.getMassage().getMassageAddress());
                    tvSpaTime.setText(String.format(getString(R.string.operation_time1),
                            Tools.convertTime(result.getMassage().getOpenTimeStart()) + "-" + Tools.convertTime(result.getMassage().getOpenTimeOver())));
                }
                if(result.getProposeMassagist()!=null && result.getProposeMassagist().size()>0) {
                    MyAdapter mAdapter = new MyAdapter(result.getProposeMassagist());
                    rvMasseur.setAdapter(mAdapter);
                }else
                    tvMasseurTitle.setVisibility(View.GONE);

                titleList = new ArrayList<String>() {{
                    add(getString(R.string.product_intro));
                    add(getString(R.string.massage_highlight));
                }};

                fragmentList = new ArrayList<Fragment>() {{
                    add(MassageIntroFragment.newInstance(result));
                    add(MassageSpaFragment.newInstance(result));
                }};
                adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
                viewPager.setAdapter(adapter);
            }
        };
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getMassageDetail(new LoadingSubscriber(mListener, this),
                    mMassageId);
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getMassageDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    mMassageId);
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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<GetMassageDetailResponse.Masseur> masseurList;
        private int selectedPosition = 0;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvHeight, tvAppointment, tvExperience;
            ImageView ivAvatar, ivClick;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvHeight = (TextView) view.findViewById(R.id.tv_height);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                ivClick = (ImageView) view.findViewById(R.id.iv_click);
                tvExperience = (TextView) view.findViewById(R.id.tv_experience);
            }
        }

        public MyAdapter(List<GetMassageDetailResponse.Masseur> masseurList) {
            this.masseurList = masseurList;
        }

        public int getSelectedId(){
            return  masseurList.get(selectedPosition).getId();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_masseur1, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final GetMassageDetailResponse.Masseur masseur = masseurList.get(position);
            holder.tvAppointment.setText(String.format(getString(R.string.masseur_appointment), masseur.getOrder_num()));
            holder.tvHeight.setText(String.format(getString(R.string.height), masseur.getMassagist_height()));
            holder.tvName.setText(masseur.getMassagist_name());
            holder.tvExperience.setText(String.format(getString(R.string.experience), masseur.getJob_time()));
            if(position == selectedPosition)
                holder.ivClick.setImageResource(R.mipmap.ic_massage_clicked);
            else
                holder.ivClick.setImageResource(R.mipmap.ic_massage_click);
            Glide.with(MassageActivity.this).load(masseur.getMassagist_main_img()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(holder.ivAvatar);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(MassageActivity.this, MasseurActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.PARAM_ID, masseur.getId());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
            holder.ivClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            if(masseurList==null)
                return 0;
            else
                return masseurList.size();
        }
    }
}
