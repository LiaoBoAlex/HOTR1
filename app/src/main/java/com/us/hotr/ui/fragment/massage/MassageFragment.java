package com.us.hotr.ui.fragment.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Data;
import com.us.hotr.R;
import com.us.hotr.customview.MainPageAdapter;
import com.us.hotr.customview.MyBaseAdapter;

/**
 * Created by macb00k on 2017/8/25.
 */

public class MassageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MainPageAdapter mAdapter;
    private RefreshLayout refreshLayout;
    private LinearLayoutManager mLayoutManager;

    public static MassageFragment newInstance() {
        MassageFragment massageFragment = new MassageFragment();
        return massageFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beauty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MainPageAdapter(getActivity(), Data.getMassageItems());
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
        mRecyclerView.setAdapter(myBaseAdapter);
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false);
        myBaseAdapter.setFooterView(footer);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setEnableLoadmore(false);
    }
}
