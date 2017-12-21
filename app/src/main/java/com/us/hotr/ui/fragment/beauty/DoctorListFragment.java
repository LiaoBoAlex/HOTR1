package com.us.hotr.ui.fragment.beauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.ui.activity.beauty.DoctorActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Integer hospitalId = null;

    public static DoctorListFragment newInstance(int cityId, int hospitalId) {
        DoctorListFragment doctorListFragment = new DoctorListFragment();
        Bundle b = new Bundle();
        b.putInt(PARAM_CITY, cityId);
        b.putInt(PARAM_HOSPITAL, hospitalId);
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
        cityCode = getArguments().getInt(PARAM_CITY);
        if(cityCode<0)
            cityCode = null;
        hospitalId = getArguments().getInt(PARAM_HOSPITAL);
        if(hospitalId<0)
            hospitalId = null;
        if(hospitalId!=null){
            typeId = 5;
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
        if(loadType == Constants.LOAD_MORE){
            mListener = new SubscriberListener<BaseListResponse<List<Doctor>>>() {
                @Override
                public void onNext(BaseListResponse<List<Doctor>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getDoctorList(new SilentSubscriber(mListener, getContext(), refreshLayout),
                    typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
        }else{
            currentPage = 1;
            mListener = new SubscriberListener<BaseListResponse<List<Doctor>>>() {
                @Override
                public void onNext(BaseListResponse<List<Doctor>> result) {
                    updateList(loadType, result);
                }
            };
            if(loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getDoctorList(new LoadingSubscriber(mListener, this),
                        typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getDoctorList(new ProgressSubscriber(mListener, getContext()),
                        typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getDoctorList(new SilentSubscriber(mListener, getContext(), refreshLayout),
                        typeId, hospitalId, subjectId, cityCode, Constants.MAX_PAGE_ITEM, currentPage);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Doctor>> result){
        totalSize = result.getTotal();
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
            View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false);
            myBaseAdapter.setFooterView(footer);
        }
        else
            enableLoadMore(true);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Doctor> doctorList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvTitle1, tvTitle2, tvAppointment, tvCase;
            ImageView ivAvatar;
            FlowLayout flSubject;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
                tvTitle2 = (TextView) view.findViewById(R.id.tv_title2);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                tvCase = (TextView) view.findViewById(R.id.tv_case);
                ivAvatar = (ImageView) view.findViewById(R.id.img_avator);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);

                flSubject.setHorizontalSpacing(6);
                flSubject.setTextPaddingH(14);
            }
        }

        public MyAdapter(List<Doctor> doctorList) {
            this.doctorList = doctorList;
        }

        public void setItems(List<Doctor> doctorList){
            this.doctorList = doctorList;
            notifyDataSetChanged();
        }

        public void addItems(List<Doctor> doctorList){
            for(Doctor d:doctorList)
                this.doctorList.add(d);
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
            final Doctor doctor = doctorList.get(position);
            holder.tvName.setText(doctor.getDoctor_name());
            holder.tvTitle1.setText(doctor.getDoctor_job());
            holder.tvTitle2.setText(doctor.getHospital_name());
            holder.tvAppointment.setText(String.format(getString(R.string.num_of_appointment), doctor.getOrder_num()));
            holder.tvCase.setText(String.format(getString(R.string.num_of_case), 345) );
            Glide.with(getContext()).load(doctor.getDoctor_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.ivAvatar);
            List<String> subjects = new ArrayList<>();
            if(doctor.getGood_at_project_list()!=null && doctor.getGood_at_project_list().size()>0)
                for(Doctor.Subject s:doctor.getGood_at_project_list())
                    subjects.add(s.getType_name());
            holder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(String content, int position) {

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), DoctorActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_ID, doctor.getDoctor_id());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
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
