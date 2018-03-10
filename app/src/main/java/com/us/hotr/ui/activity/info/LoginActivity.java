package com.us.hotr.ui.activity.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
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
    private ImageView ivBack, ivTencent, ivBackground;
    private static LoginListener loginListener;

    public static void setLoginListener(LoginListener l){
        loginListener = l;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GlobalBus.getBus().register(this);
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
        ivBackground = (ImageView) findViewById(R.id.iv_background);

        ivBackground.setImageBitmap(readBitMap(this, R.drawable.bg_login));
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

    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    public void loginSuccess(){
        if(loginListener!=null)
            loginListener.onLoginSuccess();
        loginListener = null;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

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

    @Subscribe
    public void getMessage(final Events.WechatLogin wechatLogin){
        JMessageClient.register("user" + wechatLogin.getGetLoginResponse().getUser().getUserId(), "123456", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0 || i ==898001){
                    JMessageClient.login("user" + wechatLogin.getGetLoginResponse().getUser().getUserId(), "123456", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if(i == 0){
                                UserInfo userInfo = JMessageClient.getMyInfo();
                                userInfo.setNickname(wechatLogin.getGetLoginResponse().getUser().getNickname());
                                JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        HOTRSharePreference.getInstance(getApplicationContext()).storeUserID(wechatLogin.getGetLoginResponse().getJsessionid());
        HOTRSharePreference.getInstance(getApplicationContext()).storeUserInfo(wechatLogin.getGetLoginResponse().getUser());
        loginSuccess();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }
}
