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
import com.us.hotr.ui.fragment.found.GroupListFragment;
import com.us.hotr.ui.fragment.info.FriendListFragment;
import com.us.hotr.ui.fragment.party.PartyListFragment;
import com.us.hotr.ui.fragment.search.HintSearchFragment;
import com.us.hotr.util.Tools;

import org.greenrobot.eventbus.Subscribe;

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
    private TextView tvNext, tvNumber;
    private ConstraintLayout clTitle;

    private int type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        type = getIntent().getIntExtra(Constants.PARAM_TYPE, -1);
        initStaticView();
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

    private void initStaticView(){
        viewPager = (DeactivatedViewPager) findViewById(R.id.pager);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        clTitle = (ConstraintLayout) findViewById(R.id.cl_title);

        mSearchView.setSearchViewListener(this);

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

        setSearchText(getIntent().getStringExtra(Constants.PARAM_SEARCH_STRING));
    }

    private void updateResult(){
        tvNumber.setText("");
        configListFragment();
        fragmentList.set(0, myListFragment);
        adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private void configListFragment(){
        switch (type){
            case Constants.TYPE_CASE:
                myListFragment = CaseListFragment.newInstance(mSearchView.getEtInput(), true, false, -1, -1, -1);
                break;
            case Constants.TYPE_SEARCH_POST:
                myListFragment = PostListFragment.newInstance(mSearchView.getEtInput(), true,  Constants.TYPE_SEARCH_POST,-1 ,-1);
                break;
            case Constants.TYPE_PARTY:
                myListFragment = PartyListFragment.newInstance(mSearchView.getEtInput(), false);
                break;
            case Constants.TYPE_DOCTOR:
            case Constants.TYPE_HOSPITAL:
            case Constants.TYPE_SPA:
            case Constants.TYPE_MASSEUR:
                myListFragment = ListWithFilterFragment.newInstance(mSearchView.getEtInput(), type, -1, true);
                break;
            case Constants.TYPE_PRODUCT:
            case Constants.TYPE_MASSAGE:
                myListFragment = ProductListWithFilterFragment.newInstance(mSearchView.getEtInput(), type, true, -1l);
                break;
            case Constants.TYPE_SEARCH_PEOPLE:
                myListFragment = FriendListFragment.newInstance(mSearchView.getEtInput(), type);
                break;
            case Constants.TYPE_GROUP:
                myListFragment = GroupListFragment.newInstance(-1, mSearchView.getEtInput());
        }
    }

    public void setSearchText(String text){
        if(mSearchView!=null)
            mSearchView.setEtInput(text);
    }

    @Subscribe
    public void getMessage(Events.GetSearchCount getSearchCount) {
        String s = "";
        switch (type){
            case Constants.TYPE_CASE:
                s = getString(R.string.case_title);
                break;
            case Constants.TYPE_SEARCH_POST:
                s = getString(R.string.post_title);
                break;
            case Constants.TYPE_PARTY:
                s = getString(R.string.party_title);
                break;
            case Constants.TYPE_DOCTOR:
                s = getString(R.string.doctor);
                break;
            case Constants.TYPE_HOSPITAL:
                s = getString(R.string.hospital1);
                break;
            case Constants.TYPE_SPA:
                s = getString(R.string.spa);
                break;
            case Constants.TYPE_MASSEUR:
                s = getString(R.string.masseur2);
                break;
            case Constants.TYPE_PRODUCT:
                s = getString(R.string.product1);
                break;
            case Constants.TYPE_MASSAGE:
                s = getString(R.string.massage1);
                break;
            case Constants.TYPE_SEARCH_PEOPLE:
                s = getString(R.string.user);
                break;
            case Constants.TYPE_GROUP:
                s= getString(R.string.group_title);
                break;
        }
        tvNumber.setText(String.format(getString(R.string.search_total), s, getSearchCount.getSearchCount()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_TYPE_REQUEST){
            if(resultCode == Activity.RESULT_OK && data != null) {
                type = data.getExtras().getInt(Constants.PARAM_TYPE);
                updateResult();
            }
        }
    }

    @Override
    public void onRefreshAutoComplete(String text) {
//        if(text.length() > 0) {
//            viewPager.setCurrentItem(1, false);
//            clTitle.setVisibility(View.GONE);
//            Events.SearchKeywordHint event = new Events.SearchKeywordHint(text);
//            GlobalBus.getBus().post(event);
//        }
//        else {
//            viewPager.setCurrentItem(0, false);
//            clTitle.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onSearch(String text) {
        if(text.isEmpty())
            Tools.Toast(this, getString(R.string.search_keyword));
        else {
            viewPager.setCurrentItem(0, false);
            clTitle.setVisibility(View.VISIBLE);
            DataBaseHelper.getInstance(getApplicationContext()).insertSearchHistory(text);
            updateResult();
        }
    }

    @Override
    public void onCancel() {
        finish();
    }

    @Override
    public void onSearchEmpty() {

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
