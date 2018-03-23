package com.us.hotr.ui.activity.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Theme;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.fragment.found.GroupListFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/10/26.
 */

public class AllGroupActivity extends BaseLoadingActivity {

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    private boolean isReload = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_all_group;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.all_group);
        isReload = false;
        initStaticView();
        loadData(0);
    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<List<Theme>>() {
            @Override
            public void onNext(List<Theme> result) {
                if(result!=null && result.size()>0) {
                    for (Theme t:result) {
                        titleList.add(t.getThemeName());
                        fragmentList.add(GroupListFragment.newInstance(t.getKey(), null));
                    }
                    if(result.size()<=5)
                        tabLayout.setTabMode(TabLayout.MODE_FIXED);
                    adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
                    viewPager.setAdapter(adapter);
                    viewPager.setOffscreenPageLimit(result.size());
                    tabLayout.setupWithViewPager(viewPager, true);
                }
            }
        };
        ServiceClient.getInstance().getGroupTheme(new LoadingSubscriber(mListener, this));
    }

    private void initStaticView(){
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public void onBackPressed() {
        if(isReload)
            setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Subscribe
    public void getMessage(Events.Refresh refreshEvent) {
        isReload = true;
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
