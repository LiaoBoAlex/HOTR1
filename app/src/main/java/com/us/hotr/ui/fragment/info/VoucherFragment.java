package com.us.hotr.ui.fragment.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.R;

/**
 * Created by Mloong on 2017/9/20.
 */

public class VoucherFragment extends Fragment {
    public static final int TYPE_VALID =100;
    public static final int TYPE_USED =101;
    public static final int TYPE_EXPIRED =102;

    private static final String PARAM_TYPE = "PARAM_TYPE";

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RefreshLayout refreshLayout;

    private int type;
    private boolean enableClick = false;

    public static VoucherFragment newInstance(int type) {
        VoucherFragment voucherFragment = new VoucherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_TYPE, type);
        voucherFragment.setArguments(bundle);
        return voucherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = getArguments().getInt(PARAM_TYPE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(type);
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

    public void setEnableClick(boolean value){
        enableClick = value;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private int type;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivExpired;
            public MyViewHolder(View view){
                super(view);
                ivExpired = (ImageView) view.findViewById(R.id.iv_expired);
            }
        }

        public MyAdapter(int type) {
            this.type = type;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_voucher, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            switch (type){
                case TYPE_VALID:
                    holder.ivExpired.setVisibility(View.GONE);
                    break;
                case TYPE_USED:
                    holder.ivExpired.setVisibility(View.VISIBLE);
                    holder.ivExpired.setImageResource(R.mipmap.ic_used);
                    break;
                case TYPE_EXPIRED:
                    holder.ivExpired.setVisibility(View.VISIBLE);
                    holder.ivExpired.setImageResource(R.mipmap.ic_expired);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
