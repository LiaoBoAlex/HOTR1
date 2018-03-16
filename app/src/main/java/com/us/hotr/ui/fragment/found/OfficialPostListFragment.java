package com.us.hotr.ui.fragment.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.us.hotr.storage.bean.Post;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

/**
 * Created by Mloong on 2017/11/1.
 */

public class OfficialPostListFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;

    private int totalSize = 0;
    private int currentPage = 1;

    public static OfficialPostListFragment newInstance() {
        OfficialPostListFragment officialPostListFragment = new OfficialPostListFragment();
        return officialPostListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mRecyclerView.setPadding(24, 24, 24, 24);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Post>>>() {
            @Override
            public void onNext(BaseListResponse<List<Post>> result) {
                updateList(loadType, result);
            }
        };
        if (loadType == Constants.LOAD_MORE) {
            ServiceClient.getInstance().getDiscoveryPost(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM);
        } else {
            currentPage = 1;
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getDiscoveryPost(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM);
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getDiscoveryPost(new ProgressSubscriber(mListener, getContext()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getDiscoveryPost(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM);
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
                mAdapter.setItems(result.getRows());
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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Post> postList;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvContent, tvRead, tvComment;
            ImageView ivAvatar;
            View vDivider;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
                tvRead = (TextView) view.findViewById(R.id.tv_read);
                tvComment = (TextView) view.findViewById(R.id.tv_comment);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                vDivider = view.findViewById(R.id.v_divider);
            }
        }

        public MyAdapter(List<Post> postList) {
            this.postList = postList;
        }

        public void setItems(List<Post> postList)  {
            this.postList = postList;
            notifyDataSetChanged();
        }

        public void addItems(List<Post> postList){
            postList.addAll(postList);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_offical_post, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Post post = postList.get(position);
            Glide.with(OfficialPostListFragment.this).load(post.getShow_img()).error(R.drawable.placeholder_post1).placeholder(R.drawable.placeholder_post1).into(holder.ivAvatar);
            holder.tvTitle.setText(post.getTitle());
            holder.tvRead.setText(post.getRead_cnt()+"");
            holder.tvComment.setText(post.getComment_cnt()+"");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CaseActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                    b.putLong(Constants.PARAM_ID, post.getId());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(postList == null)
                return 0;
            else
                return postList.size();
        }
    }
}
