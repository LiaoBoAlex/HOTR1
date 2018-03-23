package com.us.hotr.ui.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Info;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

/**
 * Created by Mloong on 2017/10/13.
 */

public class HelpAndFeedbackActivity extends BaseLoadingActivity {

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private TextView tvFeedback;

    @Override
    protected int getLayout() {
        return R.layout.activity_help_feedback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.help_and_feedback);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<List<Info>>() {
            @Override
            public void onNext(List<Info> result) {
                if(mAdapter == null) {
                    mAdapter = new MyAdapter(result);
                    recyclerView.setAdapter(mAdapter);
                }else
                    mAdapter.setData(result);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getFAQs(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getFAQs(new SilentSubscriber(mListener, this, refreshLayout),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    private void initStaticView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvFeedback = (TextView) findViewById(R.id.tv_feedback);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpAndFeedbackActivity.this, FeedbackActivity.class));
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Info> infoList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public MyAdapter(List<Info> infoList) {
            this.infoList = infoList;
        }

        public void setData(List<Info> infoList) {
            this.infoList = infoList;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_help, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tvTitle.setText(infoList.get(position).getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HelpAndFeedbackActivity.this, FAQActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, infoList.get(position));
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(infoList == null)
                return 0;
            else
                return infoList.size();
        }
    }
}
