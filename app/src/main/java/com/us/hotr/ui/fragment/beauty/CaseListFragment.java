package com.us.hotr.ui.fragment.beauty;

import android.content.Intent;
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
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Compare;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.beauty.CompareActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/6.
 */

public class CaseListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<Compare> compareList;
    private RefreshLayout refreshLayout;
    private boolean enableRefresh;

    public static CaseListFragment newInstance(boolean enableRefresh) {
        CaseListFragment caseListFragment = new CaseListFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_ENABLE_REFRESH, enableRefresh);
        caseListFragment.setArguments(b);
        return caseListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enableRefresh = getArguments().getBoolean(Constants.PARAM_ENABLE_REFRESH);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        compareList = new ArrayList<>();
        for(int i=0;i<5;i++){
            Compare c = new Compare();
            compareList.add(c);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(compareList);
        mRecyclerView.setAdapter(mAdapter);

        if(enableRefresh){
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    refreshlayout.finishRefresh(2000);
                }
            });
        }else
            refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
                for(int i=0;i<2;i++){
                    Compare c = new Compare();
                    compareList.add(c);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getMessage(Events.EnableEdit enableEdit) {
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DONE)
            mAdapter.setEnableEdit(false);
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_EDIT)
            mAdapter.setEnableEdit(true);
        mAdapter.notifyDataSetChanged();
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Compare> compareList;
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBefore, imgAfter, ivDelete;
            public MyViewHolder(View view){
                super(view);
                imgBefore = (ImageView) view.findViewById(R.id.img_before);
                imgAfter = (ImageView) view.findViewById(R.id.imge_after);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            }
        }

        public MyAdapter(List<Compare> compareList) {
            this.compareList = compareList;
        }

        public void setItems(List<Compare> compareList){
            this.compareList = compareList;
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_compare, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Compare compare = compareList.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CompareActivity.class);
                    i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                    startActivity(i);
                }
            });
            holder.imgBefore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), ImageViewerActivity.class));
                }
            });
            holder.imgAfter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), ImageViewerActivity.class));
                }
            });

            if(isEdit) {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setImageResource(R.mipmap.ic_delete_order);
                holder.ivDelete.setTag(false);
            }else
                holder.ivDelete.setVisibility(View.GONE);

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if((boolean)view.getTag()){
                        ((ImageView)view).setImageResource(R.mipmap.ic_delete_order);
                        view.setTag(false);
                    }else{
                        ((ImageView)view).setImageResource(R.mipmap.ic_delete_order_clicked);
                        view.setTag(true);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(compareList == null)
                return 0;
            return compareList.size();
        }
    }
}
