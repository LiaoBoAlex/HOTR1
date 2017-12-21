package com.us.hotr.ui.activity.info;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.DeactivatedViewPager;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.fragment.info.InfoFragment;
import com.us.hotr.ui.fragment.info.LoginPasswordFragment;
import com.us.hotr.ui.fragment.info.LoginPhoneFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetWechatAccessTokenResponse;

import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mloong on 2017/10/13.
 */

public class LoginActivity extends AppCompatActivity {
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private DeactivatedViewPager viewPager;
    private PagerAdapter adapter;
    private ImageView ivBack, ivTencent;
//    private static LoginListener loginListener;

//    public static void setLoginListener(LoginListener l){
//        loginListener = l;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initStaticView();
    }

    private void initStaticView(){
        titleList = new ArrayList<String>() {{
            add(getString(R.string.phone_login));
            add(getString(R.string.password_login));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(LoginPhoneFragment.newInstance());
            add(LoginPasswordFragment.newInstance());
        }};

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (DeactivatedViewPager) findViewById(R.id.pager);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivTencent = (ImageView) findViewById(R.id.iv_tencent);

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setSwipeLocked(true);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivTencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.loginWechat(LoginActivity.this);
            }
        });
    }

    public void loginSuccess(){
//        if(loginListener!=null)
//            loginListener.onLoginSuccess();
//        loginListener = null;
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

    public interface LoginListener{
        void onLoginSuccess();
    }
}
