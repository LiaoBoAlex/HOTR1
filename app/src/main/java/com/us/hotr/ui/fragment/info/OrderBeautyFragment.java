package com.us.hotr.ui.fragment.info;

import android.content.DialogInterface;
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
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.R;
import com.us.hotr.ui.activity.info.OrderDetailActivity;
import com.us.hotr.ui.dialog.CancelOrderDialogFragment;
import com.us.hotr.ui.dialog.TwoButtonDialog;


/**
 * Created by Mloong on 2017/9/19.
 */

public class OrderBeautyFragment extends Fragment {
    private final int TYPE_ALL =100;
    private final int TYPE_PAID =101;
    private final int TYPE_NOT_PAID =102;

    private TextView tvAll, tvPaid, tvNotPaid;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RefreshLayout refreshLayout;

    public static OrderBeautyFragment newInstance() {
        OrderBeautyFragment orderBeautyFragment = new OrderBeautyFragment();
        return orderBeautyFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_beauty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAll = (TextView) view.findViewById(R.id.tv_all);
        tvPaid = (TextView) view.findViewById(R.id.tv_paid);
        tvNotPaid = (TextView) view.findViewById(R.id.tv_not_paid);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setupList(TYPE_ALL);

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupList(TYPE_ALL);
            }
        });
        tvPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupList(TYPE_PAID);
            }
        });
        tvNotPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupList(TYPE_NOT_PAID);
            }
        });

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

    private void setupList(int type){
        switch (type){
            case TYPE_ALL:
                tvAll.setTextColor(getResources().getColor(R.color.red));
                tvPaid.setTextColor(getResources().getColor(R.color.text_black));
                tvNotPaid.setTextColor(getResources().getColor(R.color.text_black));
                break;
            case TYPE_PAID:
                tvAll.setTextColor(getResources().getColor(R.color.text_black));
                tvPaid.setTextColor(getResources().getColor(R.color.red));
                tvNotPaid.setTextColor(getResources().getColor(R.color.text_black));
                break;
            case TYPE_NOT_PAID:
                tvAll.setTextColor(getResources().getColor(R.color.text_black));
                tvPaid.setTextColor(getResources().getColor(R.color.text_black));
                tvNotPaid.setTextColor(getResources().getColor(R.color.red));
                break;
        }
        mAdapter = new MyAdapter(type);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setVisibility(View.GONE);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private int type;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvPay, tvCancel, tvBuy;
            public MyViewHolder(View view){
                super(view);
                tvPay = (TextView) view.findViewById(R.id.tv_pay);
                tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
                tvBuy = (TextView) view.findViewById(R.id.tv_buy);
            }
        }

        public MyAdapter(int type) {
            this.type = type;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            switch (type){
                case TYPE_ALL:
                    if(position<2){
                        setupPaid(holder);
                    }else{
                        setupUnpaid(holder);
                    }
                    break;
                case TYPE_NOT_PAID:
                    setupUnpaid(holder);
                    break;
                case TYPE_PAID:
                    setupPaid(holder);
                    break;
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), OrderDetailActivity.class));
                }
            });
        }

        private void setupPaid(MyViewHolder holder){
            holder.tvPay.setText(R.string.order_pay_success);
            holder.tvCancel.setText(R.string.delete_order);
            holder.tvBuy.setText(R.string.buy_again);
            holder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(getContext());
                    alertDialogBuilder.setMessage(getString(R.string.do_you_want_to_delete_order));
                    alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialogBuilder.setNegativeButton(getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialogBuilder.create().show();
                }
            });
        }

        private void setupUnpaid(MyViewHolder holder){
            holder.tvPay.setText(R.string.order_wait_pay);
            holder.tvCancel.setText(R.string.cancel_order);
            holder.tvBuy.setText(R.string.pay_now);
            holder.tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CancelOrderDialogFragment().show(getFragmentManager(), "dialog");
                }
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
