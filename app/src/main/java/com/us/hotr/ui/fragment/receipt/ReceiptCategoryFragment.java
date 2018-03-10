package com.us.hotr.ui.fragment.receipt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/8/28.
 */

public class ReceiptCategoryFragment extends BaseFragment{

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private List<TextView> tvTitleList = new ArrayList<>();
    private int type;
    private boolean isLoaded = false;

    public static ReceiptCategoryFragment newInstance(int type) {
        ReceiptCategoryFragment receiptCategoryFragment = new ReceiptCategoryFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_TYPE, type);
        receiptCategoryFragment.setArguments(b);
        return receiptCategoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt(Constants.PARAM_TYPE);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        if(getUserVisibleHint() && !isLoaded) {
            loadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData();
        }
    }

    public void loadData(){
        if (HOTRSharePreference.getInstance(getActivity().getApplicationContext()).isUserLogin()) {
            isLoaded = true;
            titleList = new ArrayList<String>() {{
                add(getString(R.string.unused_title));
                add(getString(R.string.used_title));
                add(getString(R.string.expired_title));
                add(getString(R.string.refunding_title));
                add(getString(R.string.refunded_title));
            }};
            if (type == Constants.TYPE_PRODUCT)
                fragmentList = new ArrayList<Fragment>() {{
                    add(ProductReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_UNUSED));
                    add(ProductReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_USED));
                    add(ProductReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_EXPIRED));
                    add(ProductReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_REFUNDING));
                    add(ProductReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_REFUNDED));
                }};

            if (type == Constants.TYPE_MASSAGE)
                fragmentList = new ArrayList<Fragment>() {{
                    add(MassageReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_UNUSED));
                    add(MassageReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_USED));
                    add(MassageReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_EXPIRED));
                    add(MassageReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_REFUNDING));
                    add(MassageReceiptListFragment.newInstance(Constants.RECEIPT_STATUS_REFUNDED));
                }};

            adapter = new PagerAdapter(getChildFragmentManager(), titleList, fragmentList);
            viewPager.setOffscreenPageLimit(5);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager, true);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(adapter.getTabView(i));
                }
            }
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tvTitleList.get(tab.getPosition()).setTextColor(getResources().getColor(R.color.red));
                    tvTitleList.get(tab.getPosition()).setTextSize(14);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tvTitleList.get(tab.getPosition()).setTextColor(getResources().getColor(R.color.text_grey2));
                    tvTitleList.get(tab.getPosition()).setTextSize(12);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
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

        public View getTabView(int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_tab, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            tvTitle.setText(getPageTitle(position));
            if(position == 0){
                tvTitle.setTextColor(getResources().getColor(R.color.red));
                tvTitle.setTextSize(14);
            }else
                tvTitle.setTextSize(12);
            tvTitleList.add(tvTitle);
            return view;
        }
    }



}
