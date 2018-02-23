package com.us.hotr.ui.fragment.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.GroupView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

/**
 * Created by Mloong on 2017/10/26.
 */

public class GroupListFragment extends BaseLoadingFragment {


    private RecyclerView mRecyclerView;
    private MyGroupAdapter mAdapter;

    private long themeId;
    private boolean isLogin;

    public static GroupListFragment newInstance(long id) {
        GroupListFragment groupListFragment = new GroupListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_ID, id);
        groupListFragment.setArguments(b);
        return groupListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLogin!=Tools.isUserLogin(getActivity().getApplicationContext())){
            isLogin = !isLogin;
            loadData(Constants.LOAD_DIALOG);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        themeId = getArguments().getLong(Constants.PARAM_ID);
        isLogin = Tools.isUserLogin(getActivity().getApplicationContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        enableLoadMore(false);

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener;
        mListener = new SubscriberListener<List<Group>>() {
            @Override
            public void onNext(List<Group> result) {
                mAdapter = new MyGroupAdapter(result);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                if(result == null ||result.size()==0)
                    myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_empty, mRecyclerView, false));
                mRecyclerView.setAdapter(myBaseAdapter);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getGroup(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), themeId, null);
        else if (loadType == Constants.LOAD_DIALOG)
            ServiceClient.getInstance().getGroup(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), themeId, null);
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getGroup(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), themeId, null);
    }

    public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyViewHolder> {
        private List<Group> groupList;
        public MyGroupAdapter(List<Group> groupList){
            this.groupList = groupList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            GroupView groupView;
            public MyViewHolder(View view) {
                super(view);
                groupView = (GroupView) view;
            }
        }

        @Override
        public MyGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group1, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Group group = groupList.get(position);
            holder.groupView.setData(group);
        }

        @Override
        public int getItemCount() {
            if(groupList == null)
                return 0;
            else
                return groupList.size();
        }
    }
}
