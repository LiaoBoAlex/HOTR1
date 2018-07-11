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
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.MasseurView;
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

public class MasseurListFragment extends BaseLoadingFragment {
    private static final String PARAM_CITY = "PARAM_CITY";
    private static final String PARAM_SPA = "PARAM_SPA";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;
    private Long SpaId = null;

    public static MasseurListFragment newInstance(String keyword, long cityId, long SpaId) {
        MasseurListFragment masseurListFragment = new MasseurListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_DATA, cityId);
        b.putLong(PARAM_SPA, SpaId);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        masseurListFragment.setArguments(b);
        return masseurListFragment;
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
        if(cityCode<0)
            cityCode = null;
        SpaId = getArguments().getLong(PARAM_SPA);
        if(SpaId<0)
            SpaId = null;
        if(SpaId!=null){
            typeId = 0l;
            subjectId = null;
            cityCode = null;
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        if (loadType == Constants.LOAD_MORE) {
            mListener = new SubscriberListener<BaseListResponse<List<Masseur>>>() {
                @Override
                public void onNext(BaseListResponse<List<Masseur>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getMasseurList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                    keyword, SpaId, cityCode, null,
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(), typeId, Constants.MAX_PAGE_ITEM, currentPage);
        } else {
            currentPage = 1;
            mListener = new SubscriberListener<BaseListResponse<List<Masseur>>>() {
                @Override
                public void onNext(BaseListResponse<List<Masseur>> result) {
                    updateList(loadType, result);
                }
            };
            if (loadType == Constants.LOAD_PAGE)
                if (SpaId != null)
                    ServiceClient.getInstance().getMasseurListBySpa(new LoadingSubscriber(mListener, this),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            SpaId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMasseurList(new LoadingSubscriber(mListener, this),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            keyword, SpaId, cityCode, subjectId,
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(), typeId, Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_DIALOG)
                if (SpaId != null)
                    ServiceClient.getInstance().getMasseurListBySpa(new ProgressSubscriber(mListener, getContext()),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            SpaId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMasseurList(new ProgressSubscriber(mListener, getContext()),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            keyword, SpaId, cityCode, subjectId,
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(), typeId, Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                if (SpaId != null)
                    ServiceClient.getInstance().getMasseurListBySpa(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            SpaId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMasseurList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            keyword, SpaId, cityCode, subjectId,
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude(),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude(), typeId, Constants.MAX_PAGE_ITEM, currentPage);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Masseur>> result){
        totalSize = result.getTotal();
        Events.GetSearchCount event = new Events.GetSearchCount(totalSize);
        GlobalBus.getBus().post(event);
        if(loadType == Constants.LOAD_MORE){
            mAdapter.addItems(result.getRows());
        }else{
            if(mAdapter!=null)
                mAdapter.setItems(result.getRows());
            else
                mAdapter = new MyAdapter(result.getRows());
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

        private List<Masseur> masseurList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            MasseurView massageView;
            public MyViewHolder(View view) {
                super(view);
                massageView = (MasseurView) view;
            }
        }

        public MyAdapter(List<Masseur> masseurList) {
            this.masseurList = masseurList;
        }

        public void setItems(List<Masseur> masseurList){
            this.masseurList = masseurList;
            notifyDataSetChanged();
        }

        public void addItems(List<Masseur> masseurList){
            for(Masseur m:masseurList)
                this.masseurList.add(m);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Masseur masseur = masseurList.get(position);
            holder.massageView.setData(masseur, position, false);
        }

        @Override
        public int getItemCount() {
            if(masseurList == null)
                return 0;
            return masseurList.size();
        }
    }
}
