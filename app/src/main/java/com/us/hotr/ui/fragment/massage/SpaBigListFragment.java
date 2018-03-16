package com.us.hotr.ui.fragment.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.SpaBigView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/29.
 */

public class SpaBigListFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;

    public static SpaBigListFragment newInstance(long cityId) {
        SpaBigListFragment spaBigListFragment = new SpaBigListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_DATA, cityId);
        spaBigListFragment.setArguments(b);
        return spaBigListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityCode = getArguments().getLong(Constants.PARAM_DATA);
        if(cityCode<=0)
            cityCode = null;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        mListener = new SubscriberListener<BaseListResponse<List<Spa>>>() {
            @Override
            public void onNext(BaseListResponse<List<Spa>> result) {
                updateList(loadType, result);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getCollectionSpa(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getCollectionSpa(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
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

    @Subscribe
    public void getMessage(Events.EnableEdit enableEdit) {
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DONE) {
            mAdapter.setEnableEdit(false);
            enablePullDownRefresh(true);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_EDIT) {
            mAdapter.setEnableEdit(true);
            enablePullDownRefresh(false);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DELETE) {
            enablePullDownRefresh(true);
            mAdapter.setEnableEdit(false);
            final int length = mAdapter.checkList.size();
            List<Long> removeIds = new ArrayList<>();
            for (int i = length - 1; i >= 0; i--)
                if(mAdapter.checkList.get(i))
                    removeIds.add(mAdapter.spaList.get(i).getKey());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.spaList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.spaList.size());
                            }
                        }
                        if (mAdapter.checkList.size() == 0) {
                            Events.GetSearchCount event = new Events.GetSearchCount(0);
                            GlobalBus.getBus().post(event);
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 4);
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Spa> spaList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            SpaBigView spaBigView;
            public MyViewHolder(View view) {
                super(view);
                spaBigView = (SpaBigView) view;
            }
        }

        public MyAdapter(List<Spa> spaList) {
            this.spaList = spaList;
            for(int i=0;i<spaList.size();i++)
                checkList.add(false);
        }

        public void setItems(List<Spa> SpaList){
            this.spaList = SpaList;
            for(int i=0;i<spaList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void addItems(List<Spa> SpaList){
            this.spaList.addAll(spaList);
            for(int i=0;i<spaList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa_big, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Spa spa = spaList.get(position);
            holder.spaBigView.setData(spa);
            holder.spaBigView.enableEdit(isEdit);
            holder.spaBigView.ShowQuery(false);
            holder.spaBigView.setItemSelectedListener(new ItemSelectedListener() {
                @Override
                public void onItemSelected(boolean isSelected) {
                    checkList.set(position, isSelected);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(spaList == null)
                return 0;
            return spaList.size();
        }
    }
}
