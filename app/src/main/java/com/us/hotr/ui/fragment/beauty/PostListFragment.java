package com.us.hotr.ui.fragment.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.PostView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/21.
 */

public class PostListFragment extends BaseLoadingFragment{

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private boolean enableRefresh;

    private int totalSize = 0;
    private int currentPage = 1;
    private boolean isLoaded = false;
    private long userId = -1;
    private int type;

    public static PostListFragment newInstance(String keyword, boolean enableRefresh, int type, long groupId, int typeId) {
        PostListFragment postListFragment = new PostListFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_ENABLE_REFRESH, enableRefresh);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        b.putInt(Constants.PARAM_IS_FAV, type);
        b.putLong(Constants.PARAM_ID, groupId);
        b.putInt(Constants.PARAM_TYPE, typeId);
        postListFragment.setArguments(b);
        return postListFragment;
    }

    public static PostListFragment newInstance(long userId) {
        PostListFragment postListFragment = new PostListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_DOCTOR_ID, userId);
        postListFragment.setArguments(b);
        return postListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = getArguments().getLong(Constants.PARAM_DOCTOR_ID);
        subjectId = getArguments().getLong(Constants.PARAM_ID);
        enableRefresh = getArguments().getBoolean(Constants.PARAM_ENABLE_REFRESH);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        subjectId = getArguments().getLong(Constants.PARAM_ID);
        typeId = getArguments().getInt(Constants.PARAM_TYPE);
        type = getArguments().getInt(Constants.PARAM_IS_FAV);
        if(userId>0)
            enableRefresh = false;
        if(subjectId<0)
            subjectId = null;
        if(typeId<0)
            typeId = null;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        enablePullDownRefresh(enableRefresh);

        if(getUserVisibleHint() && !isLoaded){
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Post>>>() {
            @Override
            public void onNext(BaseListResponse<List<Post>> result) {
                updateList(loadType, result);
            }
        };
        if(userId>0){
            if (loadType == Constants.LOAD_MORE) {
                ServiceClient.getInstance().getPostListbyUser(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), userId, Constants.MAX_PAGE_ITEM, currentPage);
            } else {
                currentPage = 1;
                ServiceClient.getInstance().getPostListbyUser(new SilentSubscriber(mListener, getActivity(), null),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), userId, Constants.MAX_PAGE_ITEM, currentPage);
            }
        }else if(type == Constants.TYPE_FAVORITE_POST){
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getCollectionPost(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getCollectionPost(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }else if(type == Constants.TYPE_NEARBY_POST) {
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getNearbyPost(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                        String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude()),
                        String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude()));
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getNearbyPost(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                        String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLatitude()),
                        String.valueOf(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getLongitude()));
        }else{
            if (loadType == Constants.LOAD_MORE) {
                ServiceClient.getInstance().getPostByGroup(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        subjectId, typeId, Constants.MAX_PAGE_ITEM, currentPage,
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            } else {
                currentPage = 1;
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getPostByGroup(new LoadingSubscriber(mListener, this),
                            subjectId, typeId, Constants.MAX_PAGE_ITEM, currentPage,
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getPostByGroup(new ProgressSubscriber(mListener, getContext()),
                            subjectId, typeId, Constants.MAX_PAGE_ITEM, currentPage,
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getPostByGroup(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            subjectId, typeId, Constants.MAX_PAGE_ITEM, currentPage,
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            }
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Post>> result){
        isLoaded = true;
        totalSize = result.getTotal();
        Events.GetSearchCount event = new Events.GetSearchCount(totalSize);
        GlobalBus.getBus().post(event);
        if(loadType == Constants.LOAD_MORE){
            mAdapter.addItems(result.getRows());
        }else{
            if(mAdapter == null)
                mAdapter = new MyAdapter(result.getRows());
            else
                mAdapter.setItem(result.getRows());
            myBaseAdapter = new MyBaseAdapter(mAdapter);
            mRecyclerView.setAdapter(myBaseAdapter);
        }
        currentPage ++;
        if((mAdapter.getItemCount() >= totalSize && mAdapter.getItemCount() > 0)
                ||totalSize == 0) {
            enableLoadMore(false);
            if(totalSize>0)
                myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false));
            else
                myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_empty, mRecyclerView, false));
        }
        else
            enableLoadMore(true);
    }

    @Subscribe
    public void getMessage(Events.EnableEdit enableEdit) {
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DONE) {
            mAdapter.setEnableEdit(false);
            enablePullDownRefresh(enableRefresh);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_EDIT) {
            mAdapter.setEnableEdit(true);
            enablePullDownRefresh(false);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DELETE) {
            enablePullDownRefresh(enableRefresh);
            mAdapter.setEnableEdit(false);
            final int length = mAdapter.checkList.size();
            List<Long> removeIds = new ArrayList<>();
            for (int i = length - 1; i >= 0; i--)
                if(mAdapter.checkList.get(i))
                    removeIds.add(mAdapter.postList.get(i).getId());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.postList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.postList.size());
                            }
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 8);
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<Post> postList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            PostView postView;
            public MyViewHolder(View view) {
                super(view);
                postView = (PostView) view;
            }
        }

        public MyAdapter(List<Post> postList) {
            this.postList = postList;
            for(int i=0;i<postList.size();i++)
                checkList.add(false);
        }

        public void setItem(List<Post> postList){
            this.postList = postList;
            checkList = new ArrayList<>();
            for(int i=0;i<postList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void addItems(List<Post> postList){
            this.postList.addAll(postList);
            for(int i=0;i<postList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
            holder.postView.setData(postList.get(position));
            holder.postView.enableEdit(isEdit);
            holder.postView.setItemSelectedListener(new ItemSelectedListener() {
                @Override
                public void onItemSelected(boolean isSelected) {
                    checkList.set(position, isSelected);
                }
            });

        }

        @Override
        public int getItemCount() {
            if(postList == null)
                return 0;
            return postList.size();
        }
    }
}
