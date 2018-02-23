package com.us.hotr.ui.fragment.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.DeactivatedViewPager;

import java.util.ArrayList;


/**
 * Created by Mloong on 2017/9/19.
 */

public class OrderCategoryFragment extends Fragment {
    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private TabLayout tabLayout;
    private DeactivatedViewPager viewPager;
    private PagerAdapter adapter;
    private int type;

    public static OrderCategoryFragment newInstance(int type) {
        OrderCategoryFragment orderCategoryFragment = new OrderCategoryFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_TYPE, type);
        orderCategoryFragment.setArguments(b);
        return orderCategoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt(Constants.PARAM_TYPE);

        titleList = new ArrayList<String>() {{
            add(getString(R.string.all));
            add(getString(R.string.not_paid));
            add(getString(R.string.paid));
        }};

        switch (type) {
            case Constants.TYPE_PRODUCT:
                fragmentList = new ArrayList<Fragment>() {{
                    add(ProductOrderListFragment.newInstance(ProductOrderListFragment.TYPE_ALL));
                    add(ProductOrderListFragment.newInstance(ProductOrderListFragment.TYPE_NOT_PAID));
                    add(ProductOrderListFragment.newInstance(ProductOrderListFragment.TYPE_PAID));
                }};
                break;
            case Constants.TYPE_MASSAGE:
                fragmentList = new ArrayList<Fragment>() {{
                    add(MassageOrderListFragment.newInstance(ProductOrderListFragment.TYPE_ALL));
                    add(MassageOrderListFragment.newInstance(ProductOrderListFragment.TYPE_NOT_PAID));
                    add(MassageOrderListFragment.newInstance(ProductOrderListFragment.TYPE_PAID));
                }};
                break;
            case Constants.TYPE_PARTY:
                fragmentList = new ArrayList<Fragment>() {{
                    add(PartyOrderListFragment.newInstance(ProductOrderListFragment.TYPE_ALL));
                    add(PartyOrderListFragment.newInstance(ProductOrderListFragment.TYPE_NOT_PAID));
                    add(PartyOrderListFragment.newInstance(ProductOrderListFragment.TYPE_PAID));
                }};
                break;
        }

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (DeactivatedViewPager) view.findViewById(R.id.pager);

        adapter = new PagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setSwipeLocked(true);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
}
