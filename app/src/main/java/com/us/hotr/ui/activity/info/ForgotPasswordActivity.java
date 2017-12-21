package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.us.hotr.R;
import com.us.hotr.customview.DeactivatedViewPager;
import com.us.hotr.ui.fragment.info.ForgotPasswordFragment;
import com.us.hotr.ui.fragment.info.LoginPasswordFragment;
import com.us.hotr.ui.fragment.info.LoginPhoneFragment;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/10/16.
 */

public class ForgotPasswordActivity extends AppCompatActivity {
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private DeactivatedViewPager viewPager;
    private PagerAdapter adapter;
    private ImageView ivBack, ivTencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initStaticView();
    }

    private void initStaticView(){
        titleList = new ArrayList<String>() {{
            add(getString(R.string.get_password));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(ForgotPasswordFragment.newInstance());
        }};

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (DeactivatedViewPager) findViewById(R.id.pager);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivTencent = (ImageView) findViewById(R.id.iv_tencent);

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setSwipeLocked(true);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
