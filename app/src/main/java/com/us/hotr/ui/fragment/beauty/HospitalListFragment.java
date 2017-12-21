package com.us.hotr.ui.fragment.beauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.ui.activity.beauty.HospitalActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
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

    public static HospitalListFragment newInstance(int cityId) {
        HospitalListFragment hospitalListFragment = new HospitalListFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_DATA, cityId);
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
        cityCode = getArguments().getInt(Constants.PARAM_DATA);
        if(cityCode<0)
            cityCode = null;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadData(Constants.LOAD_PAGE);

    }

    @Override
    protected void loadData(final int loadType){
        SubscriberListener mListener;
        if(loadType == Constants.LOAD_MORE){
            mListener = new SubscriberListener<BaseListResponse<List<Hospital>>>() {
                @Override
                public void onNext(BaseListResponse<List<Hospital>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getHospitalList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    cityCode, typeId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
        }else{
            currentPage = 1;
            mListener = new SubscriberListener<BaseListResponse<List<Hospital>>>() {
                @Override
                public void onNext(BaseListResponse<List<Hospital>> result) {
                    updateList(loadType, result);
                }
            };
            if(loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getHospitalList(new LoadingSubscriber(mListener, this),
                        cityCode, typeId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getHospitalList(new ProgressSubscriber(mListener, getContext()),
                        cityCode, typeId, subjectId,Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getHospitalList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        cityCode, typeId, subjectId,Constants.MAX_PAGE_ITEM, currentPage);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Hospital>> result){
        totalSize = result.getTotal();
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
            View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false);
            myBaseAdapter.setFooterView(footer);
        }
        else
            enableLoadMore(true);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Hospital> hospitalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvTitle1, tvTitle2, tvAppointment, tvCase;
            ImageView ivAvatar;
            FlowLayout flSubject;
            View vDivider;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
                tvTitle2 = (TextView) view.findViewById(R.id.tv_title2);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                tvCase = (TextView) view.findViewById(R.id.tv_case);
                ivAvatar = (ImageView) view.findViewById(R.id.img_avator);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);
                vDivider = view.findViewById(R.id.v_divider);

                flSubject.setHorizontalSpacing(6);
                flSubject.setTextPaddingH(14);
            }
        }

        public MyAdapter(List<Hospital> hospitalList) {
            this.hospitalList = hospitalList;
        }

        public void addItems(List<Hospital> hospitalList){
            for(Hospital h:hospitalList)
                this.hospitalList.add(h);
            notifyDataSetChanged();
        }

        public void setItems(List<Hospital> hospitalList) {
            this.hospitalList = hospitalList;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_doctor, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Hospital hospital = hospitalList.get(position);
            holder.tvName.setText(hospital.getHospital_name());
            holder.tvTitle1.setText(hospital.getHospital_type());
            holder.tvTitle2.setText(hospital.getHospital_address());
            holder.tvAppointment.setText(String.format(getString(R.string.num_of_appointment), hospital.getOrder_num()));
            holder.tvCase.setText(String.format(getString(R.string.num_of_case), 345) );
            Glide.with(getActivity()).load(hospital.getHospital_logo()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.ivAvatar);
            List<String> subjects = new ArrayList<>();
            if(hospital.getGood_at_project_list()!=null && hospital.getGood_at_project_list().size()>0) {
                for (Hospital.Subject s : hospital.getGood_at_project_list())
                    subjects.add(s.getType_name());
                holder.flSubject.setFlowLayout(subjects, null);
            }else
                holder.flSubject.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), HospitalActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_ID, hospital.getHospital_id());
                    i.putExtras(b);
                    startActivity(i);
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
