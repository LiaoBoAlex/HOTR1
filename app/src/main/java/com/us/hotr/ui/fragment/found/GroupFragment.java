package com.us.hotr.ui.fragment.found;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
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
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.ui.activity.found.AllGroupActivity;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.GroupView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetGroupMainPageResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.util.List;

/**
 * Created by Mloong on 2017/10/26.
 */

public class GroupFragment extends BaseLoadingFragment {

    private RecyclerView rvMyGroup, rvRecommendedGroup;
    private MyGroupAdapter myGroupAdapter;
    private RecommendedAdapter RecommendedAdapter;
    private ConstraintLayout clAllGroup, clRecommendedGroup, clNoGroup;

    private boolean isLoaded = false;
    private boolean isLogin;

    public static GroupFragment newInstance() {
        GroupFragment groupFragment = new GroupFragment();
        return groupFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLogin!=Tools.isUserLogin(getActivity().getApplicationContext())){
            isLogin = !isLogin;
            loadData(Constants.LOAD_PULL_REFRESH);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLogin = Tools.isUserLogin(getActivity().getApplicationContext());
        rvMyGroup = (RecyclerView) view.findViewById(R.id.rv_my_group);
        rvRecommendedGroup = (RecyclerView) view.findViewById(R.id.rv_recommended_group);
        clAllGroup = (ConstraintLayout) view.findViewById(R.id.cl_all_group);
        clRecommendedGroup = (ConstraintLayout) view.findViewById(R.id.cl_recommended_group);
        clNoGroup = (ConstraintLayout) view.findViewById(R.id.cl_no_group);

        ViewCompat.setNestedScrollingEnabled(rvMyGroup, false);
        ViewCompat.setNestedScrollingEnabled(rvRecommendedGroup, false);
        rvMyGroup.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecommendedGroup.setLayoutManager(new LinearLayoutManager(getActivity()));

        clAllGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), AllGroupActivity.class), 0);
            }
        });
        clNoGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), AllGroupActivity.class), 0);
            }
        });

//        clRecommendedGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), AllGroupActivity.class));
//            }
//        });

        clRecommendedGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        enableLoadMore(false);

        if(getUserVisibleHint() && !isLoaded){
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<GetGroupMainPageResponse>() {
            @Override
            public void onNext(GetGroupMainPageResponse result) {
                isLoaded = true;
                if(result.getMyCoshow()!= null && result.getMyCoshow().size()>0){
                    if(myGroupAdapter == null) {
                        myGroupAdapter = new MyGroupAdapter(result.getMyCoshow());
                        rvMyGroup.setAdapter(myGroupAdapter);
                    }else
                        myGroupAdapter.setItems(result.getMyCoshow());
                    clNoGroup.setVisibility(View.GONE);
                    rvMyGroup.setVisibility(View.VISIBLE);
                }else{
                    rvMyGroup.setVisibility(View.GONE);
                    clNoGroup.setVisibility(View.VISIBLE);
                }
                if(result.getRecommendCoshowList()!=null && result.getRecommendCoshowList().size()>0){
                    clRecommendedGroup.setVisibility(View.VISIBLE);
                    rvRecommendedGroup.setVisibility(View.VISIBLE);
                    if(RecommendedAdapter == null) {
                        RecommendedAdapter = new RecommendedAdapter(result.getRecommendCoshowList());
                        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(RecommendedAdapter);
                        myBaseAdapter.setFooterView();
                        rvRecommendedGroup.setAdapter(myBaseAdapter);
                    }else
                        RecommendedAdapter.setItems(result.getRecommendCoshowList());
                }else {
                    clRecommendedGroup.setVisibility(View.GONE);
                    rvRecommendedGroup.setVisibility(View.GONE);
                }

            }
        };
        if(loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getGroupMainPage(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        else if (loadType == Constants.LOAD_DIALOG)
            ServiceClient.getInstance().getGroupMainPage(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getGroupMainPage(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());

    }

    private void favoriteGroup(final long id){
        SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
            @Override
            public void onNext(String result) {
                Tools.Toast(getActivity(), getString(R.string.fav_people_success));
                loadData(Constants.LOAD_DIALOG);

            }

            @Override
            public void reload() {
                loadData(Constants.LOAD_DIALOG);
            }
        };
        ServiceClient.getInstance().favoriteGroup(new ProgressSubscriber(mListener, getContext()),
                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), id, 1);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            loadData(Constants.LOAD_DIALOG);
    }

    public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyViewHolder> {
        private List<Group> groupList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            GroupView groupView;
            public MyViewHolder(View view) {
                super(view);
                groupView = (GroupView) view;
            }
        }

        public MyGroupAdapter(List<Group> groupList){
            this.groupList = groupList;
        }

        public void setItems(List<Group> groupList){
            this.groupList = groupList;
            notifyDataSetChanged();
        }

        @Override
        public MyGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group1, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Group group = groupList.get(position);
            holder.groupView.setData(group);
            holder.groupView.enableEdit(false);
        }

        @Override
        public int getItemCount() {
            if(groupList!=null)
                return groupList.size();
            else
                return 0;
        }
    }

    public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.MyViewHolder> {
        private List<Group> groupList;

        public RecommendedAdapter(List<Group> groupList){
            this.groupList = groupList;
        }

        public void setItems(List<Group> groupList){
            this.groupList = groupList;
            notifyDataSetChanged();
        }

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
            final Group group = groupList.get(position);
            if(group.getIs_attention()== 1)
                holder.ivFav.setImageResource(R.mipmap.ic_fav_ed);
            else
                holder.ivFav.setImageResource(R.mipmap.ic_add_fav);
            Glide.with(GroupFragment.this).load(group.getBig_img()).placeholder(R.drawable.holder_group).error(R.drawable.holder_group).into(holder.ivAvatar);
//            holder.tvNumber.setText(String.format(getString(R.string.group_attention), group.getAttention_cnt()));
            holder.ivFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteGroup(group.getId());
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), GroupDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, group);
                    i.putExtras(b);
                    startActivityForResult(i, 0);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(groupList!=null)
                return groupList.size();
            else
                return 0;
        }
    }
}
