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
import com.us.hotr.ui.activity.BaseActivity;

/**
 * Created by Mloong on 2017/10/13.
 */

public class HelpAndFeedbackActivity extends BaseActivity {

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
    }

    private void initStaticView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvFeedback = (TextView) findViewById(R.id.tv_feedback);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpAndFeedbackActivity.this, FeedbackActivity.class));
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private String[] myList = {"我要投诉", "优惠券使用说明", "购物注意事项", "最后一个"};
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public MyAdapter() {

        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_help, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tvTitle.setText(myList[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HelpAndFeedbackActivity.this, FAQActivity.class);
                    i.putExtra(Constants.PARAM_TITLE, myList[position]);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return myList.length;
        }
    }
}
