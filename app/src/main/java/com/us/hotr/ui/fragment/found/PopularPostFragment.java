package com.us.hotr.ui.fragment.found;

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
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Module;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.response.GetHomePageResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Mloong on 2017/10/26.
 */

public class PopularPostFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MainPageAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private MyBaseAdapter myBaseAdapter;

    private boolean isLoaded = false;
    private int totalSize = 0;
    private int currentPage = 1;

    public static PopularPostFragment newInstance() {
        PopularPostFragment popularPostFragment = new PopularPostFragment();
        return popularPostFragment;
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
    protected void loadData(int loadType) {
        SubscriberListener mListener;
        if (loadType == Constants.LOAD_MORE) {
            mListener = new SubscriberListener<BaseListResponse<List<Post>>>() {
                @Override
                public void onNext(BaseListResponse<List<Post>> result) {
                    updateList(result);
                }
            };
            ServiceClient.getInstance().getAllPost(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM, keyword);
        } else {
            currentPage = 1;
            mListener = new SubscriberListener<GetHomePageResponse>() {
                @Override
                public void onNext(GetHomePageResponse result) {
                    updateList(result);
                }
            };
            Long userId = 0l;
            if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserInfo()!=null)
                userId = HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserInfo().getUserId();
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getPostHomePage(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), userId);
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getPostHomePage(new ProgressSubscriber(mListener, getContext()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), userId);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getPostHomePage(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), userId);
        }
    }

    private void updateList(GetHomePageResponse result){
        isLoaded = true;
        totalSize = result.getTotal();
        Module module;
        if(result.getMyGrouList()!=null && result.getMyGrouList().size()>0) {
            module = new Module();
            module.setModuleTypeId(MainPageAdapter.TYPE_GROUP);
            result.getListHomePageModule().add(module);
        }
        if(result.getRecommendHotTopicList()!=null && result.getRecommendHotTopicList().size()>0) {
            module = new Module();
            module.setModuleTypeId(MainPageAdapter.TYPE_POST);
            result.getListHomePageModule().add(module);
        }
        mAdapter = new MainPageAdapter(getActivity(), result, MainPageAdapter.PAGE_GROUP);
        myBaseAdapter = new MyBaseAdapter(mAdapter);
        mRecyclerView.setAdapter(myBaseAdapter);
        currentPage ++;
        if((mAdapter.getPostCount() >= totalSize && mAdapter.getPostCount() > 0)
                ||totalSize == 0) {
            enableLoadMore(false);
            myBaseAdapter.setFooterView();
        }
        else
            enableLoadMore(true);
    }

    private void updateList(BaseListResponse<List<Post>> result){
        totalSize = result.getTotal();
        mAdapter.addPost(result.getRows());
        currentPage ++;
        if((mAdapter.getPostCount() >= totalSize && mAdapter.getPostCount() > 0)
                ||totalSize == 0) {
            enableLoadMore(false);
            myBaseAdapter.setFooterView();
        }
        else
            enableLoadMore(true);
    }

    @Subscribe
    public void getMessage(Events.TypeSelected typeSelected) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData(Constants.LOAD_PAGE);
        }
    }
}
