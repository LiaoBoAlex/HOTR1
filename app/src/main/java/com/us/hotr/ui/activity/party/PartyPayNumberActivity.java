package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
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
 * Created by Mloong on 2017/10/23.
 */

public class PartyPayNumberActivity extends BaseActivity {

    private TextView tvAmount, tvConfirm;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_party_pay_number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.choose_ticket);
        initStaticView();
    }

    private void initStaticView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PartyPayNumberActivity.this, PartyOrderActivity.class));
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Integer> numbers = new ArrayList<>();

        public static final int VIEW_TYPE_TICKET = 100;
        public static final int VIEW_TYPE_HEADER = 101;

        public class MyTicketHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvSubTitle, tvPrice, tvNumber, tvTicket;
            ImageView ivDeduct, ivAdd;

            public MyTicketHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
                tvPrice = (TextView) view.findViewById(R.id.tv_price);
                tvNumber = (TextView) view.findViewById(R.id.tv_number);
                tvTicket = (TextView) view.findViewById(R.id.tv_ticket);
                ivDeduct = (ImageView) view.findViewById(R.id.iv_deduct);
                ivAdd = (ImageView) view.findViewById(R.id.iv_add);
            }
        }

        public class MyHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            TextView tvTitle, tvPeople, tvDate, tvStatus, tvNotice;

            public MyHeaderHolder(View view) {
                super(view);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvPeople = (TextView) view.findViewById(R.id.tv_people);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvStatus = (TextView) view.findViewById(R.id.tv_status);
                tvNotice = (TextView) view.findViewById(R.id.tv_notice);
            }
        }

        public MyAdapter() {
            for(int i=0;i<6;i++)
                numbers.add(1);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_TICKET:
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_party_ticket, parent, false);
                    MyTicketHolder holder = new MyTicketHolder(itemView);
                    return holder;
                case VIEW_TYPE_HEADER:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_party_order_header, parent, false);
                    MyHeaderHolder headerHolder = new MyHeaderHolder(view);
                    return headerHolder;
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_TICKET:
                    final MyTicketHolder ticketHolder = (MyTicketHolder) holder;
                    ticketHolder.tvNumber.setText(numbers.get(position)+"");
                    ticketHolder.ivDeduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(numbers.get(position) > 1) {
                                numbers.set(position, numbers.get(position) - 1);
                                ticketHolder.tvNumber.setText(Integer.toString(numbers.get(position)));
                            }
                            if(numbers.get(position) == 1){
                                numbers.set(position, numbers.get(position) - 1);
                                ticketHolder.tvNumber.setText(Integer.toString(numbers.get(position)));
                                ticketHolder.ivDeduct.setImageResource(R.mipmap.ic_deduct_gray);
                            }
                        }
                    });

                    ticketHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            numbers.set(position, numbers.get(position) + 1);
                            ticketHolder.tvNumber.setText(Integer.toString(numbers.get(position)));
                            if(numbers.get(position) == 1){
                                ticketHolder.ivDeduct.setImageResource(R.mipmap.ic_deduct);
                            }
                        }
                    });
                    break;
                case VIEW_TYPE_HEADER:
                    final MyHeaderHolder headerHolder = (MyHeaderHolder) holder;
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return VIEW_TYPE_HEADER;
            }else{
                return VIEW_TYPE_TICKET;
            }
        }
    }
}
