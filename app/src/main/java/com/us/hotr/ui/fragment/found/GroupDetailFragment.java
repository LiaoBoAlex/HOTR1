package com.us.hotr.ui.fragment.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.PostView;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;
import java.util.Random;

/**
 * Created by Mloong on 2017/11/1.
 */

public class GroupDetailFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;

    private Group mGroup;

    private int totalSize = 0;
    private int currentPage = 1;

    public static GroupDetailFragment newInstance(Group mGroup) {
        GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, mGroup);
        groupDetailFragment.setArguments(b);
        return groupDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroup = (Group)getArguments().getSerializable(Constants.PARAM_DATA);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        if(loadType == Constants.LOAD_MORE){
            mListener = new SubscriberListener<BaseListResponse<List<Post>>>() {
                @Override
                public void onNext(BaseListResponse<List<Post>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getPostByGroup(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    mGroup.getId(), 1, Constants.MAX_PAGE_ITEM, currentPage,
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }else{
            currentPage = 1;
            mListener = new SubscriberListener<BaseListResponse<List<Post>>>() {
                @Override
                public void onNext(BaseListResponse<List<Post>> result) {
                    updateList(loadType, result);
                }
            };
            if(loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getPostByGroup(new LoadingSubscriber(mListener, this),
                        mGroup.getId(), 1, Constants.MAX_PAGE_ITEM, currentPage,
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getPostByGroup(new ProgressSubscriber(mListener, getContext()),
                        mGroup.getId(), 1, Constants.MAX_PAGE_ITEM, currentPage,
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getPostByGroup(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        mGroup.getId(), 1, Constants.MAX_PAGE_ITEM, currentPage,
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Post>> result){
        totalSize = result.getTotal();
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
            myBaseAdapter.setFooterView();
        }
        else
            enableLoadMore(true);
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int VIEW_TYPE_HEADER =100;
        public static final int VIEW_TYPE_POST = 101;
        private List<Post> postList;
        //        int postColumn;
//        int postPhontoCount;
//        int count = 5;
        boolean haveHeader = true;

        public MyAdapter(List<Post> postList){
            this.postList = postList;
        }

        public void setItem(List<Post> postList){
            this.postList = postList;
        }

        public void addItems(List<Post> postList){
            this.postList.addAll(postList);
            notifyDataSetChanged();
        }

        public class PostHolder extends RecyclerView.ViewHolder {
            PostView postView;
            public PostHolder(View view) {
                super(view);
                postView = (PostView) view;
            }
        }

        public class HeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivClose;
            TextView tvContent, tvSeeMore;
            public HeaderHolder(View itemView) {
                super(itemView);
                ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvSeeMore = (TextView) itemView.findViewById(R.id.tv_see_more);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_POST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                    return new PostHolder(view);
                case VIEW_TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_group_head, parent, false);
                    return new HeaderHolder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_HEADER:
                    final HeaderHolder headerHolder = (HeaderHolder) holder;
                    headerHolder.ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            haveHeader = false;
                            notifyItemRemoved(0);
                        }
                    });
                    if(mGroup.getNotice().length()>50) {
                        final String displayData = mGroup.getNotice().substring(0, 50) + "...";
                        headerHolder.tvContent.setText(displayData);
                        headerHolder.tvSeeMore.setTag(false);
                        headerHolder.tvSeeMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!(boolean) view.getTag()) {
                                    headerHolder.tvContent.setText(mGroup.getNotice());
                                    view.setTag(true);
                                    headerHolder.tvSeeMore.setText(R.string.see_part);
                                } else {
                                    headerHolder.tvContent.setText(displayData);
                                    view.setTag(false);
                                    headerHolder.tvSeeMore.setText(R.string.see_all);
                                }

                            }
                        });
                    }else{
                        headerHolder.tvSeeMore.setVisibility(View.GONE);
                        headerHolder.tvContent.setText(mGroup.getNotice());
                    }
                    break;
                case VIEW_TYPE_POST:
                    PostHolder postHolder = (PostHolder) holder;
                    if(haveHeader)
                        postHolder.postView.setData(postList.get(position-1));
                    else
                        postHolder.postView.setData(postList.get(position));
                    postHolder.postView.enableEdit(false);
            }

        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0 && haveHeader){
                return VIEW_TYPE_HEADER;
            }else{
                return VIEW_TYPE_POST;
            }
        }

        @Override
        public int getItemCount() {
            if(postList == null) {
                if (haveHeader)
                    return  1;
                else
                    return 0;
            }else{
                if (haveHeader)
                    return  postList.size() + 1;
                else
                    return postList.size();
            }
        }
    }
}
