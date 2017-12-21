package com.us.hotr.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.LoadingView;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/11/13.
 */

public abstract class BaseLoadingFragment extends BaseFragment {

    private LoadingView mLoadingView;
    protected RefreshLayout refreshLayout;
    protected Integer cityCode = null;
    protected Integer subjectId = null;
    protected Integer typeId = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        mLoadingView = (LoadingView) view.findViewById(R.id.loading_view);

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
    public void getMessage(Events.CitySelected citySelectedEvent) {
        if(citySelectedEvent.getSelectedCityId()<0)
            cityCode = null;
        else
            cityCode = citySelectedEvent.getSelectedCityId();
        loadData(Constants.LOAD_DIALOG);

    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
        if(subjectSelected.getSubjectId()<0)
            subjectId = null;
        else
            subjectId = subjectSelected.getSubjectId();
        loadData(Constants.LOAD_DIALOG);

    }

    @Subscribe
    public void getMessage(Events.TypeSelected typeSelected) {
        typeId = typeSelected.getTypeId();
        loadData(Constants.LOAD_DIALOG);

    }

    protected abstract void loadData(final int type);
}
