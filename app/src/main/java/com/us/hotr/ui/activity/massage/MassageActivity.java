package com.us.hotr.ui.activity.massage;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.SnapBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ImageBanner;
import com.us.hotr.customview.ObserveScrollView;
import com.us.hotr.customview.PagerAdapter;
import com.us.hotr.receiver.Share;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.PayNumberActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.dialog.ShareDialogFragment;
import com.us.hotr.ui.fragment.massage.MassageIntroFragment;
import com.us.hotr.ui.fragment.massage.MassageSpaFragment;
import com.us.hotr.ui.view.MasseurBigView;
import com.us.hotr.ui.view.SpaBigView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.GetAppVersionRequest;
import com.us.hotr.webservice.response.GetMassageDetailResponse;
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
 * Created by Mloong on 2017/9/30.
 */

public class MassageActivity extends BaseLoadingActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ObserveScrollView scrollView;
    private ImageView ivBack, ivShare, ivBackHome, ivPromo, ivFav;
    private ImageBanner mBanner;
    private TextView tvPurchase, tvTitle, tvPriceAfter, tvPriceBefore, tvAppointment, tvAddress, tvMasseurTitle, tvApplyTime, tvShowAll;
    private SpaBigView mSpaBigView;
    private RecyclerView rvMasseur;
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private SnapBehavior mBehavior;

    private long mMassageId;
    private GetMassageDetailResponse mMassage;
    private boolean isCollected = false, isShowAll = false;
    private long selectedMasseurId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMassageId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        selectedMasseurId = getIntent().getExtras().getLong(Constants.PARAM_MASSEUR_ID, -1);
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
        ivBack = (ImageView) findViewById(R.id.img_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivFav = (ImageView) findViewById(R.id.iv_fav);
        scrollView = (ObserveScrollView) findViewById(R.id.scrollView);
        ivBackHome = (ImageView) findViewById(R.id.iv_homepage);
        tvPurchase = (TextView) findViewById(R.id.tv_purchase);
        rvMasseur = (RecyclerView) findViewById(R.id.recyclerview);
        mBanner = (ImageBanner) findViewById(R.id.banner);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPriceAfter = (TextView) findViewById(R.id.tv_amount);
        tvPriceBefore = (TextView) findViewById(R.id.tv_price_before);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        tvApplyTime = (TextView) findViewById(R.id.tv_apply_time);
        tvAddress = (TextView) findViewById(R.id.tv_place);
        tvMasseurTitle = (TextView) findViewById(R.id.tv_masseur_title);
        ivPromo = (ImageView) findViewById(R.id.iv_promo);
        mSpaBigView = (SpaBigView) findViewById(R.id.v_spa);
        tvShowAll = (TextView) findViewById(R.id.tv_expend);

        ivBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MassageActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mBanner.setRatio(1);
        mBanner.setPlacehoderResource(R.drawable.placeholder_post_2);
        mBehavior = new SnapBehavior(this);

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

        rvMasseur.setLayoutManager(new LinearLayoutManager(this));
        rvMasseur.setItemAnimator(new DefaultItemAnimator());
        rvMasseur.setNestedScrollingEnabled(false);
//
//        enableLoadMore(false);
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
                mMassage = result;
                isCollected = result.getIs_collected()==1?true:false;
                final List<String> photoes = Tools.mapToList(Tools.gsonStringToMap(result.getProduct().getProductImg()));
//                mBanner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
//                    @Override
//                    public void onBannerItemClicked(int position) {
//                        Intent i = new Intent(MassageActivity.this, ImageViewerActivity.class);
//                        Bundle b = new Bundle();
//                        b.putSerializable(Constants.PARAM_DATA, (Serializable)photoes);
//                        b.putInt(Constants.PARAM_ID, position);
//                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
//                        i.putExtras(b);
//                        startActivity(i);
//                    }
//                });
                if(photoes!=null && photoes.size()>0) {
                    mBanner.setSource(photoes);
                    mBanner.startScroll();
                    if(photoes.size()==1)
                        mBanner.pauseScroll();
                }
//                tvTitle.setText(getString(R.string.bracket_left)+result.getProduct().getProductName()+getString(R.string.bracket_right)+result.getProduct().getProductUsp());
                tvTitle.setText(result.getProduct().getProductUsp());
                tvApplyTime.setText(getString(R.string.use_time) + result.getProduct().getUsableTime());
                if(result.getProduct().getProductType()==Constants.PROMOTION_PRODUCT){
                    tvPriceAfter.setText(new DecimalFormat("0.00").format(result.getProduct().getActivityPrice()) + "/" + result.getProduct().getServiceTime());
                    ivPromo.setVisibility(View.VISIBLE);

                    if(result.getProduct().getActivityCount()<=0) {
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
                }else {
                    tvPriceAfter.setText(new DecimalFormat("0.00").format(result.getProduct().getOnlinePrice()) + "/" + result.getProduct().getServiceTime());
                    ivPromo.setVisibility(View.GONE);
                    tvPurchase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            purchase();
                        }
                    });
                }
                TextPaint tp = tvPriceAfter.getPaint();
                tp.setFakeBoldText(true);
                tvPriceBefore.setText(String.format(getString(R.string.price), new DecimalFormat("0.00").format(result.getProduct().getShopPrice())));
                tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                tvAppointment.setText(String.format(getString(R.string.massage_appointment), result.getProductOrderNum()));
                tvAddress.setText(result.getMassage().getLandmarkName());
                if(result.getMassage()==null)
                    mSpaBigView.setVisibility(View.GONE);
                else
                    mSpaBigView.setData(result.getMassage());
                if(result.getProposeMassagist()!=null && result.getProposeMassagist().size()>0) {
                    if(selectedMasseurId < 0)
                        selectedMasseurId = result.getProposeMassagist().get(0).getId();
                    final MyAdapter mAdapter = new MyAdapter(result.getProposeMassagist());
                    rvMasseur.setAdapter(mAdapter);
                    if(result.getProposeMassagist().size()>3) {
                        tvShowAll.setVisibility(View.VISIBLE);
                        tvShowAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isShowAll = !isShowAll;
                                if (!isShowAll)
                                    tvShowAll.setText(R.string.see_all);
                                else
                                    tvShowAll.setText(R.string.see_part);
                                if (mAdapter != null)
                                    mAdapter.notifyDataSetChanged();
                            }
                        });
                    }else
                        tvShowAll.setVisibility(View.GONE);
                }else {
                    tvMasseurTitle.setVisibility(View.GONE);
                    tvShowAll.setVisibility(View.GONE);
                    tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                    tvPurchase.setOnClickListener(null);
                }
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
                                    Tools.Toast(MassageActivity.this, getString(R.string.remove_fav_item_success));
                                    ivFav.setImageResource(R.mipmap.ic_fav_text);
                                }
                                @Override
                                public void reload() {
                                    loadData(Constants.LOAD_DIALOG);
                                }
                            };
                            ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, MassageActivity.this),
                                    HOTRSharePreference.getInstance(MassageActivity.this.getApplicationContext()).getUserID(), Arrays.asList(mMassage.getProduct().getKey()), 6);
                        }else{
                            SubscriberListener mListener = new SubscriberListener<String>() {
                                @Override
                                public void onNext(String result) {
                                    isCollected = true;
                                    Tools.Toast(MassageActivity.this, getString(R.string.fav_item_success));
                                    ivFav.setImageResource(R.mipmap.ic_fav_text_ed);
                                }
                            };
                            Properties prop = new Properties();
                            prop.setProperty("id", mMassage.getProduct().getKey()+"");
                            StatService.trackCustomKVEvent(MassageActivity.this, Constants.MTA_ID_FAV_MASSAGE, prop);
                            ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, MassageActivity.this),
                                    HOTRSharePreference.getInstance(MassageActivity.this.getApplicationContext()).getUserID(), mMassage.getProduct().getKey(), 6);
                        }
                    }
                });

                ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Share share = new Share();
                        share.setDescription(getString(R.string.share_massage));
                        share.setImageUrl(Tools.getMainPhoto(Tools.gsonStringToMap(mMassage.getProduct().getProductImg())));
                        share.setTitle(getString(R.string.bracket_left)+mMassage.getProduct().getProductName()+getString(R.string.bracket_right)+mMassage.getProduct().getProductUsp());
                        share.setUrl("http://hotr.hotr-app.com/hotr-api-web/#/commodityAM?id="+mMassage.getProduct().getKey());
                        share.setSinaContent(getString(R.string.share_massage));
                        share.setType(Share.TYPE_NORMAL);
                        ShareDialogFragment.newInstance(share).show(getSupportFragmentManager(), "dialog");
                    }
                });

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
                    mMassageId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getMassageDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    mMassageId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
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
            });
            startActivityForResult(new Intent(MassageActivity.this, LoginActivity.class), 0);
        }

    }

    public void purchaseCheckCount(){
        if(mMassage.getProduct().getProductType() == Constants.PROMOTION_PRODUCT){
            SubscriberListener mListener = new SubscriberListener<Integer>() {
                @Override
                public void onNext(Integer result) {
                    if(result == 0){
                        Tools.Toast(MassageActivity.this, getString(R.string.product_sold_out));
                        tvPurchase.setText(R.string.sold_out);
                        tvPurchase.setBackgroundResource(R.color.bg_button_grey);
                        tvPurchase.setOnClickListener(null);
                    }else {
                        Intent i = new Intent(MassageActivity.this, PayNumberActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                        b.putLong(Constants.PARAM_ID, selectedMasseurId);
                        b.putSerializable(Constants.PARAM_DATA, mMassage.getProduct());
                        b.putSerializable(Constants.PARAM_SPA_ID, mMassage.getMassage());
                        i.putExtras(b);
                        startActivity(i);
                    }
                }
            };
            ServiceClient.getInstance().checkOrderCount(new ProgressSubscriber(mListener, this),
                    mMassage.getProduct().getKey(), 1);
        }else{
            Intent i = new Intent(MassageActivity.this, PayNumberActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
            b.putSerializable(Constants.PARAM_DATA, mMassage.getProduct());
            b.putLong(Constants.PARAM_ID, selectedMasseurId);
            b.putSerializable(Constants.PARAM_SPA_ID, mMassage.getMassage());
            i.putExtras(b);
            startActivity(i);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Masseur> masseurList;
        private int selectedPosition = 0;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            MasseurBigView masseurBigView;
            public MyViewHolder(View view) {
                super(view);
                masseurBigView = (MasseurBigView) view;
            }
        }

        public MyAdapter(List<Masseur> masseurList) {
            this.masseurList = masseurList;
            for(int i=0;i<masseurList.size();i++){
                if(selectedMasseurId == masseurList.get(i).getId()) {
                    selectedPosition = i;
                    break;
                }
            }
        }

        public long getSelectedId(){
            return  masseurList.get(selectedPosition).getId();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur_big, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Masseur masseur = masseurList.get(position);
            holder.masseurBigView.setData(masseur, mMassageId);
            if(position == selectedPosition) {
                holder.masseurBigView.setGoButtonResource(R.mipmap.ic_massage_clicked);
                selectedMasseurId = masseur.getId();
            }
            else
                holder.masseurBigView.setGoButtonResource(R.mipmap.ic_massage_click);
            holder.masseurBigView.setGoButtonLisenter(new View.OnClickListener() {
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
            else {
                if (isShowAll)
                    return masseurList.size();
                else
                    return Math.min(3, masseurList.size());
            }
        }
    }
}
