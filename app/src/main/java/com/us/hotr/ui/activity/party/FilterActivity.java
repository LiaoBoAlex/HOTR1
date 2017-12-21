package com.us.hotr.ui.activity.party;

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

import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/10/17.
 */

public class FilterActivity extends BaseActivity {

    public static final String PARAM_FILTER = "PARAM_FILTER";

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView tvConfirm;

    private int totalSelected = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.filter);
        initStaticView();
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(PARAM_FILTER,totalSelected);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_party_filter;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Boolean> isSelected;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivClick;
            TextView tvTitle;
            public MyViewHolder(View view){
                super(view);
                ivClick = (ImageView) view.findViewById(R.id.iv_select);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public MyAdapter() {
            isSelected = new ArrayList<>();
            for(int i=0;i<6;i++)
                isSelected.add(false);
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_filter, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isSelected.get(position)){
                        holder.ivClick.setImageResource(R.mipmap.ic_filter_click);
                        isSelected.set(position, false);
                        totalSelected = totalSelected - 1;
                    }else{
                        holder.ivClick.setImageResource(R.mipmap.ic_filter_clicked);
                        isSelected.set(position, true);
                        totalSelected = totalSelected + 1;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return isSelected.size();
        }
    }
}
