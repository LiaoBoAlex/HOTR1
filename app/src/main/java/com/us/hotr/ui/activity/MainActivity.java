package com.us.hotr.ui.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.DeactivatedViewPager;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.activity.info.SettingActivity;
import com.us.hotr.ui.activity.post.UploadCompareActivity1;
import com.us.hotr.ui.activity.post.UploadPostActivity1;
import com.us.hotr.ui.fragment.HomeFragment;
import com.us.hotr.ui.fragment.found.FoundFragment;
import com.us.hotr.ui.fragment.info.InfoFragment;
import com.us.hotr.ui.fragment.receipt.ReceiptFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import q.rorbin.badgeview.QBadgeView;

public class
MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView tabHome, tabFound, tabAll, tabVoucher, tabInfo, ivPost, ivCompare, ivAll1;
    private ArrayList<Fragment> fragmentList;
    private PagerAdapter adapter;
    private DeactivatedViewPager viewPager;
    private RelativeLayout mFrameLayout;

    private InfoFragment infoFragment;
    private ReceiptFragment receiptFragment;
    private QBadgeView badgeView;

    private int currentPage = 0;
    private int orderCount = 0, noticeCount = 0;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JMessageClient.registerEventReceiver(this);
        GlobalBus.getBus().register(this);
        if(getIntent()!=null && getIntent().getExtras()!=null)
            currentPage = getIntent().getExtras().getInt(Constants.PARAM_DATA, 0);
        initStaticView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNoticCount(true);
    }

    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        GlobalBus.getBus().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MessageEvent event) {
        updateNoticCount(false);

    }

    public void onEvent(OfflineMessageEvent event) {
        updateNoticCount(false);

    }

    @Subscribe
    public void getMessage(Events.GotoMainPageNumber gotoMainPageNumber) {
        currentPage = gotoMainPageNumber.getPage();
        setupButton(currentPage);
    }

    private void updateNoticCount(boolean updateorderCount){
        noticeCount = 0;
        List<Conversation> conversationList = JMessageClient.getConversationList();
        if(conversationList!=null && conversationList.size()>0){
            for(Conversation conversation:conversationList)
                noticeCount = noticeCount + conversation.getUnReadMsgCnt();
        }
        if(updateorderCount){
            SubscriberListener mListener = new SubscriberListener<Integer>() {
                @Override
                public void onNext(Integer result) {
                    if(result == null)
                        orderCount = 0;
                    else
                        orderCount = result;
                    GlobalBus.getBus().post(new Events.GetNoticeCount(noticeCount, orderCount));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tabInfo != null && orderCount + noticeCount > 0) {
                                badgeView.setBadgeNumber(orderCount + noticeCount);
                            } else if (badgeView != null) {
                                badgeView.hide(false);
                            }
                        }
                    });
                }
            };
                ServiceClient.getInstance().getUnpiadOrderCount(mListener,
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        }else {
            GlobalBus.getBus().post(new Events.GetNoticeCount(noticeCount, orderCount));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tabInfo != null && orderCount + noticeCount > 0) {
                        badgeView.setBadgeNumber(orderCount + noticeCount);
                    } else if (badgeView != null) {
                        badgeView.hide(false);
                    }
                }
            });
        }
    }

    private void updateOrderCount(){

    }

    private void initStaticView(){
        tabHome = (ImageView)findViewById(R.id.tab_home);
        tabFound = (ImageView)findViewById(R.id.tab_found);
        tabAll = (ImageView)findViewById(R.id.tab_all);
        tabVoucher = (ImageView)findViewById(R.id.tab_voucher);
        tabInfo = (ImageView)findViewById(R.id.tab_info);
        viewPager = (DeactivatedViewPager) findViewById(R.id.pager);
        ivPost = (ImageView) findViewById(R.id.iv_post);
        ivCompare = (ImageView) findViewById(R.id.iv_compare);
        ivAll1 = (ImageView) findViewById(R.id.tab_all1);
        mFrameLayout = (RelativeLayout) findViewById(R.id.fl_dim);

        badgeView = new QBadgeView(this);
        badgeView.bindTarget(tabInfo)
                .setBadgeGravity(Gravity.TOP | Gravity.END)
                .setBadgeBackgroundColor(getResources().getColor(R.color.red))
                .setShowShadow(false);

        tabHome.setOnClickListener(this);
        tabFound.setOnClickListener(this);
        tabAll.setOnClickListener(this);
        tabVoucher.setOnClickListener(this);
        tabInfo.setOnClickListener(this);
        ivAll1.setOnClickListener(this);
        mFrameLayout.setOnClickListener(this);

        infoFragment = InfoFragment.newInstance();
        receiptFragment = ReceiptFragment.newInstance();
        fragmentList = new ArrayList<Fragment>() {{
            add(HomeFragment.newInstance());
            add(FoundFragment.newInstance());
            add(receiptFragment);
            add(infoFragment);
        }};

        adapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setSwipeLocked(true);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);

        setupButton(currentPage);

        ivPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UploadPostActivity1.class));
                buttonRollIn();
            }
        });

        ivCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UploadCompareActivity1.class));
                buttonRollIn();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tab_home:
                currentPage = 0;
                setupButton(0);
                break;
            case R.id.tab_found:
                currentPage = 1;
                setupButton(1);
                break;
            case R.id.tab_all:
                if(HOTRSharePreference.getInstance(getApplicationContext()).isUserLogin())
                    buttonRollOut();
                else{
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.tab_voucher:
                if(HOTRSharePreference.getInstance(getApplicationContext()).isUserLogin()) {
                    currentPage = 2;
                    setupButton(2);
                }
                else{
                    LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            currentPage = 2;
                            setupButton(currentPage);
                            receiptFragment.loadData();
                        }
                    });
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.tab_info:
            case -1:
                if(!HOTRSharePreference.getInstance(getApplicationContext()).isUserLogin()) {
                    LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            currentPage = 3;
                            setupButton(currentPage);
                            infoFragment.loadData(Constants.LOAD_PAGE);
                        }
                    });
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    currentPage = 3;
                    setupButton(3);
                }
                break;
            case R.id.fl_dim:
            case R.id.tab_all1:
                buttonRollIn();
                break;
            default:
                break;
        }
    }

    private void setupButton(int page){
        switch (page){
            case 0:
                tabHome.setImageResource(R.mipmap.icon_home_click);
                tabFound.setImageResource(R.mipmap.icon_found);
                tabVoucher.setImageResource(R.mipmap.icon_voucher);
                tabInfo.setImageResource(R.mipmap.icon_info);
                viewPager.setCurrentItem(0, false);
                break;
            case 1:
                tabHome.setImageResource(R.mipmap.icon_home);
                tabFound.setImageResource(R.mipmap.icon_found_click);
                tabVoucher.setImageResource(R.mipmap.icon_voucher);
                tabInfo.setImageResource(R.mipmap.icon_info);
                viewPager.setCurrentItem(1, false);
                break;
            case 2:
                tabHome.setImageResource(R.mipmap.icon_home);
                tabFound.setImageResource(R.mipmap.icon_found);
                tabVoucher.setImageResource(R.mipmap.icon_voucher_click);
                tabInfo.setImageResource(R.mipmap.icon_info);
                viewPager.setCurrentItem(2, false);
                break;
            case 3:
                tabHome.setImageResource(R.mipmap.icon_home);
                tabFound.setImageResource(R.mipmap.icon_found);
                tabVoucher.setImageResource(R.mipmap.icon_voucher);
                tabInfo.setImageResource(R.mipmap.icon_info_click);
                viewPager.setCurrentItem(3, false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Tools.Toast(this, getString(R.string.press_again_to_exit));
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SettingActivity.CODE_LOGOUT){
            currentPage = 0;
            setupButton(currentPage);
        }
        if(resultCode == SettingActivity.CODE_BACK){
            infoFragment.updateUserInfo();
        }
    }

    private void buttonRollOut(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, 200);
        animator.setDuration(200).start();
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                if((Float)animation.getAnimatedValue() == 0){
                    mFrameLayout.setVisibility(View.VISIBLE);
                    mFrameLayout.setClickable(true);
                    tabAll.setVisibility(View.INVISIBLE);
                }
                ivPost.setTranslationY((Float) animation.getAnimatedValue()*-1.2F);
                ivPost.setTranslationX((Float) animation.getAnimatedValue()*-0.9F);
                ivPost.setScaleX((Float) animation.getAnimatedValue()/200*1.5F);
                ivPost.setScaleY((Float) animation.getAnimatedValue()/200*1.5F);
                ivCompare.setTranslationY((Float) animation.getAnimatedValue()*-1.2F);
                ivCompare.setTranslationX((Float) animation.getAnimatedValue()*0.9F);
                ivCompare.setScaleX((Float) animation.getAnimatedValue()/200*1.5F);
                ivCompare.setScaleY((Float) animation.getAnimatedValue()/200*1.5F);
                ivAll1.setRotation((Float) animation.getAnimatedValue()/200*45);
            }
        });
    }

    private void buttonRollIn(){
        ValueAnimator animator = ValueAnimator.ofFloat(200, 0);
        animator.setDuration(200).start();
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                if((Float)animation.getAnimatedValue() == 0){
                    mFrameLayout.setVisibility(View.GONE);
                    mFrameLayout.setClickable(false);
                    tabAll.setVisibility(View.VISIBLE);
                }
                ivPost.setTranslationY((Float) animation.getAnimatedValue()*-1.2F);
                ivPost.setTranslationX((Float) animation.getAnimatedValue()*-0.9F);
                ivPost.setScaleX((Float) animation.getAnimatedValue()/200*1.5F);
                ivPost.setScaleY((Float) animation.getAnimatedValue()/200*1.5F);
                ivCompare.setTranslationY((Float) animation.getAnimatedValue()*-1.2F);
                ivCompare.setTranslationX((Float) animation.getAnimatedValue()*0.9F);
                ivCompare.setScaleX((Float) animation.getAnimatedValue()/200*1.5F);
                ivCompare.setScaleY((Float) animation.getAnimatedValue()/200*1.5F);
                ivAll1.setRotation((Float) animation.getAnimatedValue()/200*45);
            }
        });

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
            super(fm);
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
    }
}
