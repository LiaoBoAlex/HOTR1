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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.fragment.BaseFragment;
import com.us.hotr.ui.fragment.BaseLoadingFragment;

import java.util.ArrayList;

/**
 * Created by liaobo on 2018/2/27.
 */

public class ReceiptFragment extends BaseFragment {

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
//    private boolean isLoaded = false;

    public static ReceiptFragment newInstance() {
        ReceiptFragment receiptFragment = new ReceiptFragment();

        return receiptFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_order_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack.setVisibility(View.GONE);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
//        if(getUserVisibleHint() && !isLoaded) {
            loadData();
//        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isResumed() && !isLoaded) {
//            loadData();
//        }
//    }

    public void loadData(){
        if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).isUserLogin()) {
//            isLoaded = true;
            titleList = new ArrayList<String>() {{
                add(getString(R.string.massage_title));
                add(getString(R.string.beauty_title));
            }};
            fragmentList = new ArrayList<Fragment>() {{
                add(ReceiptCategoryFragment.newInstance(Constants.TYPE_MASSAGE));
                add(ReceiptCategoryFragment.newInstance(Constants.TYPE_PRODUCT));
            }};
            adapter = new PagerAdapter(getChildFragmentManager(), titleList, fragmentList);
            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager, true);
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
    }
}
