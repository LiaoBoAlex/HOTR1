package com.us.hotr.ui.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.search.TypeSearchFragment;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/9/21.
 */

public class SearchTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.choose_type);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    private void initStaticView(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, TypeSearchFragment.newInstance(getIntent().getStringExtra(Constants.PARAM_SEARCH_STRING))).commit();
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
    public void getMessage(Events.SearchTypeChosen searchTypeChosen) {
        setResult(RESULT_OK, getIntent().putExtra(Constants.PARAM_TYPE, searchTypeChosen.getSearchTypeChosen()));
        finish();
    }
}
