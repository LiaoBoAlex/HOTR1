package com.us.hotr.ui.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.DeactivatedViewPager;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.activity.info.SettingActivity;
import com.us.hotr.ui.activity.post.UploadCompareActivity1;
import com.us.hotr.ui.activity.post.UploadPostActivity1;
import com.us.hotr.ui.fragment.HomeFragment;
import com.us.hotr.ui.fragment.found.FoundFragment;
import com.us.hotr.ui.fragment.info.InfoFragment;
import com.us.hotr.ui.fragment.receipt.ReceiptFragment;

import java.util.ArrayList;

public class
MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView tabHome, tabFound, tabAll, tabVoucher, tabInfo, ivPost, ivCompare, ivAll1;
    private ArrayList<Fragment> fragmentList;
    private PagerAdapter adapter;
    private DeactivatedViewPager viewPager;
    private RelativeLayout mFrameLayout;

    private InfoFragment infoFragment;
    private ReceiptFragment receiptFragment;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentPage = 0;

        initStaticView();
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
        ValueAnimator animator = ValueAnimator.ofFloat(0, 150);
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
                ivPost.setTranslationX((Float) animation.getAnimatedValue()*-0.7F);
                ivPost.setScaleX((Float) animation.getAnimatedValue()/150*1.5F);
                ivPost.setScaleY((Float) animation.getAnimatedValue()/150*1.5F);
                ivCompare.setTranslationY((Float) animation.getAnimatedValue()*-1.2F);
                ivCompare.setTranslationX((Float) animation.getAnimatedValue()*0.7F);
                ivCompare.setScaleX((Float) animation.getAnimatedValue()/150*1.5F);
                ivCompare.setScaleY((Float) animation.getAnimatedValue()/150*1.5F);
                ivAll1.setRotation((Float) animation.getAnimatedValue()/150*45);
            }
        });
    }

    private void buttonRollIn(){
        ValueAnimator animator = ValueAnimator.ofFloat(150, 0);
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
                ivPost.setTranslationX((Float) animation.getAnimatedValue()*-0.7F);
                ivPost.setScaleX((Float) animation.getAnimatedValue()/150*1.5F);
                ivPost.setScaleY((Float) animation.getAnimatedValue()/150*1.5F);
                ivCompare.setTranslationY((Float) animation.getAnimatedValue()*-1.2F);
                ivCompare.setTranslationX((Float) animation.getAnimatedValue()*0.7F);
                ivCompare.setScaleX((Float) animation.getAnimatedValue()/150*1.5F);
                ivCompare.setScaleY((Float) animation.getAnimatedValue()/150*1.5F);
                ivAll1.setRotation((Float) animation.getAnimatedValue()/150*45);
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
