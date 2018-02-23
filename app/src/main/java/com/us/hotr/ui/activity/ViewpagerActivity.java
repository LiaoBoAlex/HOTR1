package com.us.hotr.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rd.PageIndicatorView;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.ui.fragment.AdvFragment;
import com.us.hotr.ui.fragment.HomeFragment;

import java.util.ArrayList;

/**
 * Created by liaobo on 2017/12/12.
 */

public class ViewpagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private PageIndicatorView mPageIndicatorView;
    private ArrayList<Adv> advList;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);
        advList = (ArrayList<Adv>)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        for(Adv adv:advList)
            fragmentList.add(new AdvFragment().newInstance(adv));
        adapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(advList.size());
        mViewPager.setAdapter(adapter);
        if(advList.size()==1)
            mPageIndicatorView.setVisibility(View.GONE);
        else
            mPageIndicatorView.setViewPager(mViewPager);

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm,  ArrayList<Fragment> fragmentList) {
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
