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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/10/17.
 */

public class FilterActivity extends BaseLoadingActivity {

    public static final String PARAM_FILTER = "PARAM_FILTER";

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView tvConfirm;

    private int totalSelected = 0;
    private List<Integer> selectedStatus = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.filter);
        selectedStatus = (List<Integer>)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        initStaticView();
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putInt(PARAM_FILTER, totalSelected);
                b.putSerializable(Constants.PARAM_DATA, (Serializable)selectedStatus);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        loadData(0);

    }
    @Override
    public void loadData(int type){
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Integer>>>() {
            @Override
            public void onNext(BaseListResponse<List<Integer>> result) {
                mAdapter = new MyAdapter(result.getRows());
                mRecyclerView.setAdapter(mAdapter);
            }
        };
        ServiceClient.getInstance().getPartyStatusList(new LoadingSubscriber(mListener, this));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_party_filter;
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Boolean> isSelected = new ArrayList<>();
        private List<Integer> partyStatus = new ArrayList<>();

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivClick;
            TextView tvTitle;
            public MyViewHolder(View view){
                super(view);
                ivClick = (ImageView) view.findViewById(R.id.iv_select);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public MyAdapter(List<Integer> partyStatus) {
            if(partyStatus!=null){
                for(Integer i:partyStatus){
                    this.partyStatus.add(i);
                    isSelected.add(false);
                }
            }
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_filter, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            String[] status = getResources().getStringArray(R.array.party_status);
            holder.tvTitle.setText(status[partyStatus.get(position)]);
            for(Integer i:selectedStatus){
                if(i==partyStatus.get(position)){
                    holder.ivClick.setImageResource(R.mipmap.ic_filter_clicked);
                    totalSelected = totalSelected + 1;
                    isSelected.set(position, true);
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isSelected.get(position)){
                        holder.ivClick.setImageResource(R.mipmap.ic_filter_click);
                        isSelected.set(position, false);
                        totalSelected = totalSelected - 1;
                        selectedStatus.remove(partyStatus.get(position));
                    }else{
                        holder.ivClick.setImageResource(R.mipmap.ic_filter_clicked);
                        isSelected.set(position, true);
                        totalSelected = totalSelected + 1;
                        selectedStatus.add(partyStatus.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(partyStatus!=null)
                return partyStatus.size();
            else
                return 0;
        }
    }
}
