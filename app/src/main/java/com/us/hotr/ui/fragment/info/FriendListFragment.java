package com.us.hotr.ui.fragment.info;

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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.ui.activity.info.FriendActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/21.
 */

public class FriendListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RefreshLayout refreshLayout;
    private List<Doctor> doctorList;
    private int type;

    public static FriendListFragment newInstance(int type) {
        FriendListFragment friendListFragment = new FriendListFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_TYPE, type);
        friendListFragment.setArguments(b);
        return friendListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = getArguments().getInt(Constants.PARAM_TYPE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        doctorList = new ArrayList<>();
        Doctor d = new Doctor();
        for(int i=0;i<10;i++)
            doctorList.add(d);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(doctorList);
        mRecyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Doctor> doctorList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvTitle1, tvTitle2, tvDistance;
            ImageView ivAvatar, ivCertified1, ivCertified2;
            View vDivider;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
                tvTitle2 = (TextView) view.findViewById(R.id.tv_title2);
                tvDistance = (TextView) view.findViewById(R.id.tv_distance);
                ivAvatar = (ImageView) view.findViewById(R.id.img_avator);
                ivCertified1 = (ImageView) view.findViewById(R.id.iv_certified1);
                ivCertified2 = (ImageView) view.findViewById(R.id.iv_certified2);
                vDivider = view.findViewById(R.id.v_divider);

            }
        }

        public MyAdapter(List<Doctor> doctorList) {
            this.doctorList = doctorList;
        }

        public void setItems(List<Doctor> doctorList){
            this.doctorList = doctorList;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friend, parent, false);

            return new MyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
            final Doctor doctor = doctorList.get(position);
            holder.tvName.setText("陈超" + position);
            holder.tvTitle1.setText("中心主任");
            holder.tvTitle2.setText("北京叶子整形医院");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), FriendActivity.class));
                }
            });
            if(position == (doctorList.size()-1))
                holder.vDivider.setVisibility(View.GONE);
            if(type == Constants.TYPE_FRIEND){
                holder.ivCertified1.setVisibility(View.GONE);
                holder.tvDistance.setVisibility(View.GONE);
            }else if(type == Constants.TYPE_NEARBY_PEOPLE){
                holder.ivCertified2.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if(doctorList == null)
                return 0;
            return doctorList.size();
        }
    }
}
