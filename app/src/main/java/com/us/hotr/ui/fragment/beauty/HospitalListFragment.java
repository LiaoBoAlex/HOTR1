package com.us.hotr.ui.fragment.beauty;

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
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.HospitalView;
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
 * Created by Mloong on 2017/9/20.
 */

public class HospitalListFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;
    private boolean isFav = false;

    public static HospitalListFragment newInstance(String keyword, boolean isFav, long cityId) {
        HospitalListFragment hospitalListFragment = new HospitalListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_DATA, cityId);
        b.putBoolean(Constants.PARAM_IS_FAV, isFav);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        hospitalListFragment.setArguments(b);
        return hospitalListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityCode = getArguments().getLong(Constants.PARAM_DATA);
        isFav = getArguments().getBoolean(Constants.PARAM_IS_FAV);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        if(cityCode<=0)
            cityCode = null;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadData(Constants.LOAD_PAGE);

    }

    @Override
    protected void loadData(final int loadType){
        SubscriberListener mListener;
        if(isFav){
            mListener = new SubscriberListener<BaseListResponse<List<Hospital>>>() {
                @Override
                public void onNext(BaseListResponse<List<Hospital>> result) {
                    updateList(loadType, result);
                }
            };
            if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getCollectionHospital(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getCollectionHospital(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }else {
            if (loadType == Constants.LOAD_MORE) {
                mListener = new SubscriberListener<BaseListResponse<List<Hospital>>>() {
                    @Override
                    public void onNext(BaseListResponse<List<Hospital>> result) {
                        updateList(loadType, result);
                    }
                };
                ServiceClient.getInstance().getHospitalList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        keyword, cityCode, typeId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
            } else {
                currentPage = 1;
                mListener = new SubscriberListener<BaseListResponse<List<Hospital>>>() {
                    @Override
                    public void onNext(BaseListResponse<List<Hospital>> result) {
                        updateList(loadType, result);
                    }
                };
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getHospitalList(new LoadingSubscriber(mListener, this),
                            keyword, cityCode, typeId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getHospitalList(new ProgressSubscriber(mListener, getContext()),
                            keyword, cityCode, typeId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getHospitalList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            keyword, cityCode, typeId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
            }
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Hospital>> result){
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
                    removeIds.add(mAdapter.hospitalList.get(i).getHospital_id());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.hospitalList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.hospitalList.size());
                            }
                        }
                        if (mAdapter.checkList.size() == 0) {
                            Events.GetSearchCount event = new Events.GetSearchCount(0);
                            GlobalBus.getBus().post(event);
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 1);
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public List<Hospital> hospitalList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            HospitalView hospitalView;
            public MyViewHolder(View view) {
                super(view);
                hospitalView = (HospitalView) view;
            }
        }

        public MyAdapter(List<Hospital> hospitalList) {
            this.hospitalList = hospitalList;
            for(int i=0;i<hospitalList.size();i++)
                checkList.add(false);
        }

        public void addItems(List<Hospital> hospitalList){
            this.hospitalList.addAll(hospitalList);
            for(int i=0;i<hospitalList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setItems(List<Hospital> hospitalList) {
            this.hospitalList = hospitalList;
            for(int i=0;i<hospitalList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hospital, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Hospital hospital = hospitalList.get(position);
            holder.hospitalView.setData(hospital);
            holder.hospitalView.enableEdit(isEdit);
            holder.hospitalView.setItemSelectedListener(new ItemSelectedListener() {
                @Override
                public void onItemSelected(boolean isSelected) {
                    checkList.set(position, isSelected);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(hospitalList == null)
                return 0;
            return hospitalList.size();
        }
    }
}
