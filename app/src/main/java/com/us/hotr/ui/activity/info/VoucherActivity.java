package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.info.VoucherListFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/9/20.
 */

public class VoucherActivity extends BaseActivity {

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.voucher_title);
        initStaticView();
    }

    private void initStaticView(){

        titleList = new ArrayList<String>() {{
            add(String.format(getString(R.string.not_used), 0));
            add(String.format(getString(R.string.used), 0));
            add(String.format(getString(R.string.expired), 0));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(VoucherListFragment.newInstance(VoucherListFragment.TYPE_VALID, null, null));
            add(VoucherListFragment.newInstance(VoucherListFragment.TYPE_USED, null, null));
            add(VoucherListFragment.newInstance(VoucherListFragment.TYPE_EXPIRED, null, null));
        }};

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_voucher;
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

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getMessage(Events.GetVoucherCount getVoucherCount) {
        switch(getVoucherCount.getVoucherType()){
            case VoucherListFragment.TYPE_VALID:
                titleList.set(0,String.format(getString(R.string.not_used), getVoucherCount.getVoucherCount()));
                break;
            case VoucherListFragment.TYPE_USED:
                titleList.set(0,String.format(getString(R.string.used), getVoucherCount.getVoucherCount()));
                break;
            case VoucherListFragment.TYPE_EXPIRED:
                titleList.set(0,String.format(getString(R.string.expired), getVoucherCount.getVoucherCount()));
                break;
        }
        adapter.notifyDataSetChanged();

    }
}
