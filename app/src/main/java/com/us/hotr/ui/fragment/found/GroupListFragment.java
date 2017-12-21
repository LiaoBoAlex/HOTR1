package com.us.hotr.ui.fragment.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.us.hotr.R;
import com.us.hotr.ui.activity.found.GroupDetailActivity;

/**
 * Created by Mloong on 2017/10/26.
 */

public class GroupListFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private MyGroupAdapter mAdapter;
    private RefreshLayout refreshLayout;


    public static GroupListFragment newInstance() {
        GroupListFragment groupListFragment = new GroupListFragment();
        return groupListFragment;
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyGroupAdapter();
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


    public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyViewHolder> {
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDesc, tvNumber1, tvNumber2;
            ImageView ivAvatar, ivAdd;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDesc = (TextView) view.findViewById(R.id.tv_desc);
                tvNumber1 = (TextView) view.findViewById(R.id.tv_number1);
                tvNumber2 = (TextView) view.findViewById(R.id.tv_number2);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                ivAdd = (ImageView) view.findViewById(R.id.iv_add);
            }
        }

        @Override
        public MyGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group1, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tvNumber2.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), GroupDetailActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
