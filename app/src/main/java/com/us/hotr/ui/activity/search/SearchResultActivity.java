package com.us.hotr.ui.activity.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.DeactivatedViewPager;
import com.us.hotr.customview.SearchView;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.greendao.DataBaseHelper;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.ListWithFilterFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListWithFilterFragment;
import com.us.hotr.ui.fragment.info.FriendListFragment;
import com.us.hotr.ui.fragment.party.PartyListFragment;
import com.us.hotr.ui.fragment.search.HintSearchFragment;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/9/21.
 */

public class SearchResultActivity extends AppCompatActivity implements SearchView.SearchViewListener {

    static final int SELECT_TYPE_REQUEST = 1;

    private ArrayList<Fragment> fragmentList;
    private PagerAdapter adapter;
    private DeactivatedViewPager viewPager;
    private SearchView mSearchView;
    private Fragment myListFragment;
    private TextView tvNext;
    private ConstraintLayout clTitle;

    private int type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        type = getIntent().getIntExtra(Constants.PARAM_TYPE, -1);
        initStaticView();
    }

    private void initStaticView(){
        viewPager = (DeactivatedViewPager) findViewById(R.id.pager);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        tvNext = (TextView) findViewById(R.id.tv_next);
        clTitle = (ConstraintLayout) findViewById(R.id.cl_title);

        mSearchView.setSearchViewListener(this);

        setSearchText(getIntent().getStringExtra(Constants.PARAM_SEARCH_STRING));

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchResultActivity.this, SearchTypeActivity.class);
                i.putExtra(Constants.PARAM_SEARCH_STRING, mSearchView.getEtInput());
                startActivityForResult(i, SELECT_TYPE_REQUEST);
            }
        });

        configListFragment();
        fragmentList = new ArrayList<Fragment>() {{
            add(myListFragment);
            add(HintSearchFragment.newInstance());
        }};

        adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setSwipeLocked(true);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0, false);
    }

    private void updateResult(){
        configListFragment();
        fragmentList.set(0, myListFragment);
        adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private void configListFragment(){
        switch (type){
            case Constants.TYPE_CASE:
                myListFragment = CaseListFragment.newInstance(true);
                break;
            case Constants.TYPE_POST:
                myListFragment = PostListFragment.newInstance(true);
                break;
            case Constants.TYPE_PARTY:
                myListFragment = PartyListFragment.newInstance();
                break;
            case Constants.TYPE_DOCTOR:
            case Constants.TYPE_HOSPITAL:
            case Constants.TYPE_SPA:
            case Constants.TYPE_MASSEUR:
                myListFragment = ListWithFilterFragment.newInstance(type, true);
                break;
            case Constants.TYPE_PRODUCT:
            case Constants.TYPE_MASSAGE:
                myListFragment = ProductListWithFilterFragment.newInstance(type, true, null);
                break;
            case Constants.TYPE_FRIEND:
                myListFragment = FriendListFragment.newInstance(Constants.TYPE_FRIEND);
        }
    }

    public void setSearchText(String text){
        if(mSearchView!=null)
            mSearchView.setEtInput(text);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_TYPE_REQUEST){
            if(resultCode == Activity.RESULT_OK && data != null) {
                type = data.getExtras().getInt(Constants.PARAM_TYPE);
                switch(type){
                    case 106:
                        type = Constants.TYPE_MASSAGE;
                        break;
                    case 107:
                        type = Constants.TYPE_MASSEUR;
                        break;
                    case 108:
                        type = Constants.TYPE_SPA;
                        break;
                }
                updateResult();
            }
        }
    }

    @Override
    public void onRefreshAutoComplete(String text) {
        if(text.length() > 0) {
            viewPager.setCurrentItem(1, false);
            clTitle.setVisibility(View.GONE);
            Events.SearchKeywordHint event = new Events.SearchKeywordHint(text);
            GlobalBus.getBus().post(event);
        }
        else {
            viewPager.setCurrentItem(0, false);
            clTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSearch(String text) {
        viewPager.setCurrentItem(0, false);
        clTitle.setVisibility(View.VISIBLE);
        DataBaseHelper.insertSearchHistory(text);

    }

    @Override
    public void onCancel() {
        finish();
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
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
