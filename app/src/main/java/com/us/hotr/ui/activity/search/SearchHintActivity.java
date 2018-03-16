package com.us.hotr.ui.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.DeactivatedViewPager;
import com.us.hotr.customview.SearchView;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.fragment.search.HintSearchFragment;
import com.us.hotr.ui.fragment.search.PopularSearchFragment;
import com.us.hotr.ui.fragment.search.TypeSearchFragment;
import com.us.hotr.util.Tools;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/8/29.
 */

public class SearchHintActivity extends AppCompatActivity implements SearchView.SearchViewListener{

    private ArrayList<Fragment> fragmentList;
    private PagerAdapter adapter;
    private DeactivatedViewPager viewPager;
    private SearchView mSearchview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initStaticView();
    }

    private void initStaticView(){
        viewPager = (DeactivatedViewPager) findViewById(R.id.pager);
        mSearchview = (SearchView) findViewById(R.id.search_view);

        mSearchview.setSearchViewListener(this);

        fragmentList = new ArrayList<Fragment>() {{
            add(PopularSearchFragment.newInstance());
            add(HintSearchFragment.newInstance());
            add(TypeSearchFragment.newInstance(""));
        }};

        adapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setSwipeLocked(true);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(viewPager!=null)
            viewPager.setCurrentItem(0, false);
        if(mSearchview!=null)
            mSearchview.setEtInputEx("");
    }

    public void setSearchText(String text){
            if(mSearchview!=null)
                mSearchview.setEtInput(text);
    }

    @Subscribe
    public void getMessage(Events.SearchTypeChosen searchTypeChoosed) {
        Intent i = new Intent(this, SearchResultActivity.class);
        i.putExtra(Constants.PARAM_SEARCH_STRING, mSearchview.getEtInput());
        i.putExtra(Constants.PARAM_TYPE, searchTypeChoosed.getSearchTypeChosen());
        startActivity(i);
    }

    @Override
    public void onRefreshAutoComplete(String text) {
//        if(text.length() > 0) {
//            viewPager.setCurrentItem(1, false);
//            Events.SearchKeywordHint event = new Events.SearchKeywordHint(text);
//            GlobalBus.getBus().post(event);
//        }
//        else
//            viewPager.setCurrentItem(0, false);
    }

    @Override
    public void onSearch(String text) {
        if(text.isEmpty())
            Tools.Toast(this, getString(R.string.search_keyword));
        else {
            viewPager.setCurrentItem(2, false);
            Events.SearchKeywordSearch event = new Events.SearchKeywordSearch(text);
            GlobalBus.getBus().post(event);
        }

    }

    @Override
    public void onCancel() {
        finish();
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
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
}
