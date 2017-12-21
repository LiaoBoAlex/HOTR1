package com.us.hotr.ui.fragment.receipt;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.fragment.BaseFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Mloong on 2017/8/28.
 */

public class ReceiptFragment extends BaseFragment{

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    public static ReceiptFragment newInstance() {
        ReceiptFragment voucherFragment = new ReceiptFragment();
        return voucherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMyTitle(R.string.receipt_title);

        titleList = new ArrayList<String>() {{
            add(getString(R.string.unused_title));
            add(getString(R.string.used_title));
            add(getString(R.string.expired_title));
            add(getString(R.string.refunding_title));
            add(getString(R.string.refunded_title));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(ReceiptTypeFragment.newInstance(Constants.RECEIPT_STATUS_UNUSED));
            add(ReceiptTypeFragment.newInstance(Constants.RECEIPT_STATUS_USED));
            add(ReceiptTypeFragment.newInstance(Constants.RECEIPT_STATUS_EXPITED));
            add(ReceiptTypeFragment.newInstance(Constants.RECEIPT_STATUS_REFUNDING));
            add(ReceiptTypeFragment.newInstance(Constants.RECEIPT_STATUS_REFUNDED));
        }};

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        adapter = new PagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);

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
