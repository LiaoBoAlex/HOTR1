package com.us.hotr.ui.fragment.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.info.ChatActivity;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Mloong on 2017/9/20.
 */

public class NoticeMessageFragment extends Fragment {



    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RefreshLayout refreshLayout;

    public static NoticeMessageFragment newInstance() {
        NoticeMessageFragment noticeMessageFragment = new NoticeMessageFragment();
        return noticeMessageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter();
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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            public MyViewHolder(View view) {
                super(view);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
            }
        }

        public MyAdapter() {

        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notice_message, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            new QBadgeView(getContext())
                    .bindTarget(holder.ivAvatar)
                    .setBadgeGravity(Gravity.TOP | Gravity.END)
                    .setBadgeBackgroundColor(getResources().getColor(R.color.red))
                    .setShowShadow(false)
                    .setBadgeNumber(5);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        User user = new User();
                        user.setNickname("HOTR君");
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.PARAM_DATA, user);
                        i.putExtras(b);
                        startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
