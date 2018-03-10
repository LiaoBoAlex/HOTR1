package com.us.hotr.ui.fragment.beauty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.DoctorView;
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

public class DoctorListFragment extends BaseLoadingFragment {
    private static final String PARAM_CITY = "PARAM_CITY";
    private static final String PARAM_HOSPITAL = "PARAM_HOSPITAL";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;
    private Long hospitalId = null;
    private int type;

    public static DoctorListFragment newInstance(String keyword, int type, long cityId, long hospitalId) {
        DoctorListFragment doctorListFragment = new DoctorListFragment();
        Bundle b = new Bundle();
        b.putLong(PARAM_CITY, cityId);
        b.putInt(Constants.PARAM_TYPE, type);
        b.putLong(PARAM_HOSPITAL, hospitalId);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        doctorListFragment.setArguments(b);
        return doctorListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityCode = getArguments().getLong(PARAM_CITY);
        type = getArguments().getInt(Constants.PARAM_TYPE);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        if(cityCode<0)
            cityCode = null;
        hospitalId = getArguments().getLong(PARAM_HOSPITAL);
        if(hospitalId<=0)
            hospitalId = null;
        if(hospitalId!=null){
            typeId = 5l;
            subjectId = null;
            cityCode = null;
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        if(type == Constants.TYPE_FAVORITE){
            mListener = new SubscriberListener<BaseListResponse<List<Doctor>>>() {
                @Override
                public void onNext(BaseListResponse<List<Doctor>> result) {
                    updateList(loadType, result);
                }
            };
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getCollectionDoctor(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getCollectionDoctor(new SilentSubscriber(mListener, getContext(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }else {
            if (loadType == Constants.LOAD_MORE) {
                mListener = new SubscriberListener<BaseListResponse<List<Doctor>>>() {
                    @Override
                    public void onNext(BaseListResponse<List<Doctor>> result) {
                        updateList(loadType, result);
                    }
                };
                ServiceClient.getInstance().getDoctorList(new SilentSubscriber(mListener, getContext(), refreshLayout),
                        keyword, typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
            } else {
                currentPage = 1;
                mListener = new SubscriberListener<BaseListResponse<List<Doctor>>>() {
                    @Override
                    public void onNext(BaseListResponse<List<Doctor>> result) {
                        updateList(loadType, result);
                    }
                };
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getDoctorList(new LoadingSubscriber(mListener, this),
                            keyword, typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getDoctorList(new ProgressSubscriber(mListener, getContext()),
                            keyword, typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getDoctorList(new SilentSubscriber(mListener, getContext(), refreshLayout),
                            keyword, typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
            }
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Doctor>> result){
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
                    removeIds.add(mAdapter.doctorList.get(i).getDoctor_id());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.doctorList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.doctorList.size());
                            }
                        }
                        if (mAdapter.checkList.size() == 0) {
                            Events.GetSearchCount event = new Events.GetSearchCount(0);
                            GlobalBus.getBus().post(event);
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 2);
            }
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<Doctor> doctorList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            DoctorView doctorView;
            public MyViewHolder(View view) {
                super(view);
                doctorView = (DoctorView) view;
            }
        }

        public MyAdapter(List<Doctor> doctorList) {
            this.doctorList = doctorList;
            for(int i=0;i<doctorList.size();i++)
                checkList.add(false);
        }

        public void setItems(List<Doctor> doctorList){
            this.doctorList = doctorList;
            for(int i=0;i<doctorList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void addItems(List<Doctor> doctorList){
            this.doctorList.addAll(doctorList);
            for(int i=0;i<doctorList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Doctor doctor = doctorList.get(position);
            holder.doctorView.setData(doctor);
            if(type ==Constants.TYPE_MY_DOCTOR) {
                holder.doctorView.enableSelect(true);
                holder.doctorView.setItemSelectedListener(new ItemSelectedListener() {
                    @Override
                    public void onItemSelected(boolean isSelected) {
                        Intent i = new Intent();
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.PARAM_DATA, doctor);
                        i.putExtras(b);
                        getActivity().setResult(Activity.RESULT_OK, i);
                        getActivity().finish();
                    }
                });
            }else {
                holder.doctorView.enableEdit(isEdit);
                holder.doctorView.setItemSelectedListener(new ItemSelectedListener() {
                    @Override
                    public void onItemSelected(boolean isSelected) {
                        checkList.set(position, isSelected);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (doctorList==null)
                return 0;
            else
                return doctorList.size();
        }
    }
}
