package com.us.hotr.ui.fragment.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.us.hotr.Constants;
import com.us.hotr.Data;
import com.us.hotr.R;
import com.us.hotr.customview.MainPageAdapter;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.ui.fragment.BaseLoadingFragment;

/**
 * Created by macb00k on 2017/8/25.
 */

public class BeautyFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MainPageAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public static BeautyFragment newInstance() {
        BeautyFragment beautyFragment = new BeautyFragment();
        return beautyFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beauty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MainPageAdapter(getActivity(), Data.getBeautyItmes());


        enableLoadMore(false);
        loadData(Constants.LOAD_PAGE);


    }
    @Override
    protected void loadData(int type){
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false);
        myBaseAdapter.setFooterView(footer);
        mRecyclerView.setAdapter(myBaseAdapter);

    }

}
