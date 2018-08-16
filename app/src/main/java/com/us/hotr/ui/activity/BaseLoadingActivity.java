package com.us.hotr.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.LoadingView;

/**
 * Created by liaobo on 2017/12/5.
 */

public abstract class BaseLoadingActivity extends BaseActivity {
    private LoadingView mLoadingView;
    protected RefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);

        if(refreshLayout != null){
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    loadData(Constants.LOAD_PULL_REFRESH);
                }
            });
            refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    loadData(Constants.LOAD_MORE);
                }
            });
        }

    }

    protected void enablePullDownRefresh(boolean value){
        if(refreshLayout!=null)
            refreshLayout.setEnableRefresh(value);
    }

    protected  void enableLoadMore(boolean value){
        if(refreshLayout!=null)
            refreshLayout.setEnableLoadmore(value);
    }

    public void showLoadingPage(){
        if(mLoadingView != null)
            mLoadingView.notifyDataChanged(LoadingView.LOADING);
    }

    public void showErrorPage(){
        if(mLoadingView != null) {
            mLoadingView.notifyDataChanged(LoadingView.ERROR);
            mLoadingView.setOnRetryListener(new LoadingView.OnRetryListener() {
                @Override
                public void onRetry() {
                    loadData(Constants.LOAD_PAGE);
                }
            });
        }
    }

    public void showContentPage(){
        if(mLoadingView != null)
            mLoadingView.notifyDataChanged(LoadingView.DONE);
    }

    protected abstract void loadData(int type);
}
