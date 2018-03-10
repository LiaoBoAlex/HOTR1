package com.us.hotr.ui.fragment.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MainPageAdapter;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetHomePageResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by macb00k on 2017/8/25.
 */

public class BeautyFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MainPageAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean isLoaded = false;

    public static BeautyFragment newInstance() {
        BeautyFragment beautyFragment = new BeautyFragment();
        return beautyFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beauty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        enableLoadMore(false);
        if(getUserVisibleHint() && !isLoaded){
            loadData(Constants.LOAD_PAGE);
        }
    }
    @Override
    protected void loadData(int type){
        SubscriberListener mListener = new SubscriberListener<GetHomePageResponse>() {
            @Override
            public void onNext(GetHomePageResponse result) {
                isLoaded = true;
                if(result == null || result.getListHomePageModule() == null){
                    showErrorPage();
                    return;
                }
                mAdapter = new MainPageAdapter(getActivity(), result);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                mRecyclerView.setAdapter(myBaseAdapter);
            }
        };
        long provinceCode = 0;
        Long cityCode = null;
        if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedProductCityID()>0) {

            cityCode = HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedProductCityID();
            provinceCode = cityCode / 1000 * 1000;
        }else if(Tools.getCityCodeFromBaidu(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityID()).getProductCityCode()>=0) {
            cityCode = Tools.getCityCodeFromBaidu(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityID()).getProductCityCode();
            provinceCode = cityCode / 1000 * 1000;
        }else if(Tools.getProductProvinceCode(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentProvinceName())>0)
            provinceCode = Tools.getProductProvinceCode(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentProvinceName());
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getHomePage(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), provinceCode, cityCode, 0);
        else if (type == Constants.LOAD_DIALOG)
            ServiceClient.getInstance().getHomePage(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), provinceCode, cityCode, 0);
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getHomePage(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), provinceCode, cityCode, 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Subscribe
    public void getMessage(Events.Refresh refreshEvent) {
        if(getUserVisibleHint())
            loadData(Constants.LOAD_DIALOG);
        else
            isLoaded = false;
    }

    @Subscribe
    public void getMessage(Events.CitySelected citySelectedEvent) {
    }

    @Subscribe
    public void getMessage(Events.TypeSelected typeSelected) {
    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().register(this);
    }
}
