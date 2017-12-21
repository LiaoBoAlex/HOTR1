package com.us.hotr.ui.fragment.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Data;
import com.us.hotr.R;
import com.us.hotr.customview.MainPageAdapter;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.ui.activity.found.AllGroupActivity;
import com.us.hotr.ui.fragment.beauty.BeautyFragment;

/**
 * Created by Mloong on 2017/10/26.
 */

public class GroupFragment extends Fragment {

    private RecyclerView rvMyGroup, rvRecommendedGroup;
    private MyGroupAdapter myGroupAdapter;
    private RecommendedAdapter RecommendedAdapter;
    private RefreshLayout refreshLayout;
    private ConstraintLayout clAllGroup, clRecommendedGroup, clNoGroup;
    private TextView tvAllGroup;


    public static GroupFragment newInstance() {
        GroupFragment groupFragment = new GroupFragment();
        return groupFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMyGroup = (RecyclerView) view.findViewById(R.id.rv_my_group);
        rvRecommendedGroup = (RecyclerView) view.findViewById(R.id.rv_recommended_group);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        clAllGroup = (ConstraintLayout) view.findViewById(R.id.cl_all_group);
        clRecommendedGroup = (ConstraintLayout) view.findViewById(R.id.cl_recommended_group);
        clNoGroup = (ConstraintLayout) view.findViewById(R.id.cl_no_group);
        tvAllGroup = (TextView) view.findViewById(R.id.tv_all_group);

        rvMyGroup.setLayoutManager(new LinearLayoutManager(getActivity()));
        myGroupAdapter = new MyGroupAdapter();
        rvMyGroup.setAdapter(myGroupAdapter);

        rvRecommendedGroup.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecommendedAdapter = new RecommendedAdapter();
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(RecommendedAdapter);
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, rvRecommendedGroup, false);
        myBaseAdapter.setFooterView(footer);
        rvRecommendedGroup.setAdapter(myBaseAdapter);

        clAllGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllGroupActivity.class));
            }
        });

        clRecommendedGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvAllGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setEnableLoadmore(false);
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
            holder.ivAdd.setVisibility(View.GONE);
            holder.tvNumber1.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.MyViewHolder> {
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumber;
            ImageView ivAvatar, ivFav;

            public MyViewHolder(View view) {
                super(view);
                tvNumber = (TextView) view.findViewById(R.id.tv_number);
                ivFav = (ImageView) view.findViewById(R.id.iv_fav);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            }
        }

        @Override
        public RecommendedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
