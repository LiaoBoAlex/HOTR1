package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.ui.activity.AvailableVoucherActivity;
import com.us.hotr.ui.activity.BaseActivity;

import org.w3c.dom.Text;

/**
 * Created by Mloong on 2017/10/23.
 */

public class PartyOrderActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView tvDeliver1, tvDeliver2, tvDeliver3, tvVoucher;
    private ConstraintLayout ccAddress, ccBuyer, clvoucher;
    @Override
    protected int getLayout() {
        return R.layout.activity_party_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.confirm_order_title);
        initStaticView();
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvDeliver1 = (TextView) findViewById(R.id.tv_deliver_1);
        tvDeliver2 = (TextView) findViewById(R.id.tv_deliver_2);
        tvDeliver3 = (TextView) findViewById(R.id.tv_deliver_3);
        ccAddress = (ConstraintLayout) findViewById(R.id.cc_address);
        ccBuyer = (ConstraintLayout) findViewById(R.id.cc_buyer_info);
        clvoucher = (ConstraintLayout) findViewById(R.id.cl_voucher);
        tvVoucher = (TextView) findViewById(R.id.tv_voucher);

        tvDeliver1.setOnClickListener(this);
        tvDeliver2.setOnClickListener(this);
        tvDeliver3.setOnClickListener(this);
        ccAddress.setOnClickListener(this);
        ccBuyer.setOnClickListener(this);
        clvoucher.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_deliver_1:
                tvDeliver1.setTextColor(getResources().getColor(R.color.white));
                tvDeliver1.setBackground(getResources().getDrawable(R.drawable.bg_button_green));
                tvDeliver2.setTextColor(getResources().getColor(R.color.text_black));
                tvDeliver2.setBackground(getResources().getDrawable(R.drawable.bg_button_dark));
                tvDeliver3.setTextColor(getResources().getColor(R.color.text_black));
                tvDeliver3.setBackground(getResources().getDrawable(R.drawable.bg_button_dark));
                break;
            case R.id.tv_deliver_2:
                tvDeliver2.setTextColor(getResources().getColor(R.color.white));
                tvDeliver2.setBackground(getResources().getDrawable(R.drawable.bg_button_green));
                tvDeliver1.setTextColor(getResources().getColor(R.color.text_black));
                tvDeliver1.setBackground(getResources().getDrawable(R.drawable.bg_button_dark));
                tvDeliver3.setTextColor(getResources().getColor(R.color.text_black));
                tvDeliver3.setBackground(getResources().getDrawable(R.drawable.bg_button_dark));
                break;
            case R.id.tv_deliver_3:
                tvDeliver3.setTextColor(getResources().getColor(R.color.white));
                tvDeliver3.setBackground(getResources().getDrawable(R.drawable.bg_button_green));
                tvDeliver2.setTextColor(getResources().getColor(R.color.text_black));
                tvDeliver2.setBackground(getResources().getDrawable(R.drawable.bg_button_dark));
                tvDeliver1.setTextColor(getResources().getColor(R.color.text_black));
                tvDeliver1.setBackground(getResources().getDrawable(R.drawable.bg_button_dark));
                break;
            case R.id.cc_address:
                startActivity(new Intent(PartyOrderActivity.this, DeliverAddressListActivity.class));
                break;
            case R.id.cc_buyer_info:
                startActivity(new Intent(PartyOrderActivity.this, BuyerInfoActivity.class));
                break;
            case R.id.cl_voucher:
                startActivity(new Intent(PartyOrderActivity.this, AvailableVoucherActivity.class));
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvAmount;
            public MyViewHolder(View view){
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            }
        }

        public MyAdapter() {

        }


        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_party_order, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
