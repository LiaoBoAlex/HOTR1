package com.us.hotr.ui.fragment.massage;

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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.ui.activity.beauty.CompareActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/10/9.
 */

public class InterviewListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RefreshLayout refreshLayout;
    private List<Hospital> hospitalList;


    public static InterviewListFragment newInstance() {
        InterviewListFragment interviewListFragment = new InterviewListFragment();
        return interviewListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        hospitalList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Hospital h = new Hospital();
            hospitalList.add(h);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(hospitalList);
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

        private List<Hospital> hospitalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            TextView tvName, tvNumber;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            }
        }

        public MyAdapter(List<Hospital> hospitalList) {
            this.hospitalList = hospitalList;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_interview, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Hospital hospital = hospitalList.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CompareActivity.class);
                    i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_INTERVIEW);
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
