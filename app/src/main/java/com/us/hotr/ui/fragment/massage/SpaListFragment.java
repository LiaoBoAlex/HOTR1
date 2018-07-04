package com.us.hotr.ui.fragment.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.SpaView;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

/**
 * Created by Mloong on 2017/9/29.
 */

public class SpaListFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;

    public static SpaListFragment newInstance(String keyword, long cityId) {
        SpaListFragment spaListFragment = new SpaListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_DATA, cityId);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        spaListFragment.setArguments(b);
        return spaListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityCode = getArguments().getLong(Constants.PARAM_DATA);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        if(cityCode<=0)
            cityCode = null;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        if (loadType == Constants.LOAD_MORE) {
            mListener = new SubscriberListener<BaseListResponse<List<Spa>>>() {
                @Override
                public void onNext(BaseListResponse<List<Spa>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getSpaList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    keyword, cityCode, typeId, subjectId,
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(),  Constants.MAX_PAGE_ITEM, currentPage);
        } else {
            currentPage = 1;
            mListener = new SubscriberListener<BaseListResponse<List<Spa>>>() {
                @Override
                public void onNext(BaseListResponse<List<Spa>> result) {
                    updateList(loadType, result);
                }
            };
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getSpaList(new LoadingSubscriber(mListener, this),
                        keyword, cityCode, typeId, subjectId,
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(),  Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getSpaList(new ProgressSubscriber(mListener, getContext()),
                        keyword, cityCode, typeId, subjectId,
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(),  Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getSpaList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        keyword, cityCode, typeId, subjectId,
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(),  Constants.MAX_PAGE_ITEM, currentPage);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Spa>> result){
        totalSize = result.getTotal();
        Events.GetSearchCount event = new Events.GetSearchCount(totalSize);
        GlobalBus.getBus().post(event);
        if(loadType == Constants.LOAD_MORE){
            mAdapter.addItems(result.getRows());
        }else{
            if(mAdapter == null)
                mAdapter = new MyAdapter(result.getRows());
            else
                mAdapter.setItems(result.getRows());
            myBaseAdapter = new MyBaseAdapter(mAdapter);
            mRecyclerView.setAdapter(myBaseAdapter);
        }
        currentPage ++;
        if((mAdapter.getItemCount() >= totalSize && mAdapter.getItemCount() > 0)
                ||totalSize == 0) {
            enableLoadMore(false);
            if(totalSize>0)
                myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false));
            else
                myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_empty, mRecyclerView, false));
        }
        else
            enableLoadMore(true);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Spa> spaList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            SpaView spaView;
            public MyViewHolder(View view) {
                super(view);
                spaView = (SpaView) view;
            }
        }

        public MyAdapter(List<Spa> spaList) {
            this.spaList = spaList;
        }

        public void setItems(List<Spa> SpaList){
            this.spaList = SpaList;
            notifyDataSetChanged();
        }

        public void addItems(List<Spa> SpaList){
            for(Spa s:SpaList)
                this.spaList.add(s);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Spa spa = spaList.get(position);
            holder.spaView.setData(spa, position, false);
        }

        @Override
        public int getItemCount() {
            if(spaList == null)
                return 0;
            return spaList.size();
        }
    }
}
