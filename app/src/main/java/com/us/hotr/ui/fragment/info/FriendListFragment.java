package com.us.hotr.ui.fragment.info;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.PayOrderActivity;
import com.us.hotr.ui.activity.info.FriendActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/21.
 */

public class FriendListFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private ConstraintLayout clEmpty;
    private TextView tvEmpty;
    private int type;
    private int totalSize = 0;
    private int currentPage = 1;

    public static FriendListFragment newInstance(String keyword, int type) {
        FriendListFragment friendListFragment = new FriendListFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_TYPE, type);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        friendListFragment.setArguments(b);
        return friendListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt(Constants.PARAM_TYPE);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        clEmpty = (ConstraintLayout) view.findViewById(R.id.cl_empty);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        if(loadType == Constants.LOAD_MORE){
            mListener = new SubscriberListener<BaseListResponse<List<User>>>() {
                @Override
                public void onNext(BaseListResponse<List<User>> result) {
                    updateList(loadType, result);
                }
            };
            if(type == Constants.TYPE_FANS)
                ServiceClient.getInstance().getFanPeople(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
            if(type == Constants.TYPE_FAVORITE)
                ServiceClient.getInstance().getFavoritePeople(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
            if(type == Constants.TYPE_SEARCH_PEOPLE)
                ServiceClient.getInstance().getUserList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        keyword, Constants.MAX_PAGE_ITEM, currentPage);
        }else{
            currentPage = 1;
            mListener = new SubscriberListener<BaseListResponse<List<User>>>() {
                @Override
                public void onNext(BaseListResponse<List<User>> result) {
                    updateList(loadType, result);
                }
            };
            if(type == Constants.TYPE_FAVORITE) {
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getFavoritePeople(new LoadingSubscriber(mListener, this),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getFavoritePeople(new ProgressSubscriber(mListener, getContext()),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getFavoritePeople(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
            }
            if(type == Constants.TYPE_FANS) {
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getFanPeople(new LoadingSubscriber(mListener, this),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getFanPeople(new ProgressSubscriber(mListener, getContext()),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getFanPeople(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), Constants.MAX_PAGE_ITEM, currentPage);
            }
            if(type == Constants.TYPE_SEARCH_PEOPLE) {
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getUserList(new LoadingSubscriber(mListener, this),
                            keyword, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getUserList(new ProgressSubscriber(mListener, getContext()),
                            keyword, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getUserList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            keyword, Constants.MAX_PAGE_ITEM, currentPage);
            }
            if(type == Constants.TYPE_NEARBY_PEOPLE) {
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getNearbyPeople(new LoadingSubscriber(mListener, this),
                            String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude()),
                            String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude()));
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getNearbyPeople(new ProgressSubscriber(mListener, getContext()),
                            String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude()),
                            String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude()));
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getNearbyPeople(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude()),
                            String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude()));
            }
        }
    }

    private void updateList(int loadType, BaseListResponse<List<User>> result){
        totalSize = result.getTotal();
        Events.GetSearchCount event = new Events.GetSearchCount(totalSize);
        GlobalBus.getBus().post(event);
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
            if(totalSize>0) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false);
                if(type == Constants.TYPE_FAVORITE)
                    ((TextView) view.findViewById(R.id.tv_footer)).setText(R.string.end_of_fav);
                if(type == Constants.TYPE_FANS)
                    ((TextView) view.findViewById(R.id.tv_footer)).setText(R.string.end_of_fan);
                myBaseAdapter.setFooterView(view);
            }
            else {
                mRecyclerView.setVisibility(View.GONE);
                clEmpty.setVisibility(View.VISIBLE);
                if(type == Constants.TYPE_FAVORITE)
                    tvEmpty.setText(R.string.end_of_fav);
                if(type == Constants.TYPE_FANS)
                    tvEmpty.setText(R.string.end_of_fan);
            }
        }
        else
            enableLoadMore(true);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<User> userList;
        private List<Boolean> followList = new ArrayList<>();

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvTitle1, tvTitle2, tvDistance, tvFollow;
            ImageView ivAvatar, ivCertified1, ivCertified2;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
                tvTitle2 = (TextView) view.findViewById(R.id.tv_title2);
                tvDistance = (TextView) view.findViewById(R.id.tv_distance);
                ivAvatar = (ImageView) view.findViewById(R.id.img_avator);
                ivCertified1 = (ImageView) view.findViewById(R.id.iv_certified1);
                ivCertified2 = (ImageView) view.findViewById(R.id.iv_certified2);
                tvFollow = (TextView) view.findViewById(R.id.tv_follow_user);
            }
        }

        public MyAdapter(List<User> userList) {
            this.userList = userList;
            for(int i=0;i<userList.size();i++)
                followList.add(userList.get(i).getIs_attention()==1?true:false);
        }

        public void setItems(List<User> userList){
            this.userList = userList;
            for(int i=0;i<userList.size();i++)
                followList.add(userList.get(i).getIs_attention()==1?true:false);
            notifyDataSetChanged();
        }

        public void addItems(List<User> userList){
            for(User h:userList) {
                this.userList.add(h);
                followList.add(h.getIs_attention()==1?true:false);
            }
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
            final User user = userList.get(position);
            holder.tvName.setText(user.getNickname());
            if(user.getGender()!=null && user.getAge()!=null)
                holder.tvTitle1.setText(getResources().getStringArray(R.array.gender)[user.getGender()] + " | " + String.format(getContext().getString(R.string.age_number), user.getAge()) + " | " + user.getProvince_name());
            holder.tvTitle2.setText(user.getSignature());
            Glide.with(FriendListFragment.this).load(user.getHead_portrait()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.ivAvatar);
            if(type == Constants.TYPE_FANS || type == Constants.TYPE_FAVORITE || type == Constants.TYPE_SEARCH_PEOPLE){
                holder.ivCertified2.setVisibility(View.GONE);
                holder.tvDistance.setVisibility(View.GONE);
                holder.ivCertified1.setVisibility(View.VISIBLE);
                if(user.getUser_typ() == 6)
                    holder.ivCertified1.setVisibility(View.VISIBLE);
                else
                    holder.ivCertified1.setVisibility(View.GONE);
                if(type == Constants.TYPE_FAVORITE || type == Constants.TYPE_FANS) {
                    holder.tvFollow.setVisibility(View.VISIBLE);
                    if (followList.get(position)) {
                        holder.tvFollow.setText(R.string.fav_ed);
                        holder.tvFollow.setTextColor(getContext().getResources().getColor(R.color.text_grey2));
                    } else {
                        holder.tvFollow.setText(R.string.guanzhu);
                        holder.tvFollow.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    }
                    holder.tvFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final SubscriberListener mListener = new SubscriberListener<String>() {
                                @Override
                                public void onNext(String result) {
                                    followList.set(position, !followList.get(position));
                                    if(followList.get(position))
                                        Tools.Toast(getActivity(), getString(R.string.fav_people_success));
                                    else
                                        Tools.Toast(getActivity(), getString(R.string.remove_fav_people_success));
                                    notifyItemChanged(position);
                                }
                            };
                            if (followList.get(position)) {
                                TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(getActivity());
                                alertDialogBuilder.setMessage(getString(R.string.delete_fav_people));
                                alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                ServiceClient.getInstance().deleteFavoritePeople(new ProgressSubscriber(mListener, getContext()),
                                                        HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), user.getUserId());
                                            }
                                        });
                                alertDialogBuilder.setNegativeButton(getString(R.string.no),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialogBuilder.create().show();
                            }
                            else
                                ServiceClient.getInstance().favoritePeople(new ProgressSubscriber(mListener, getContext()),
                                        HOTRSharePreference.getInstance(getContext().getApplicationContext()).getUserID(), user.getUserId());
                        }
                    });
                }else
                    holder.tvFollow.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), FriendActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, user.getUserId());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(userList == null)
                return 0;
            return userList.size();
        }
    }
}
