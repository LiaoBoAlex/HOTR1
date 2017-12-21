package com.us.hotr.ui.fragment.receipt;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.receipt.ReceiptDetailActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;

/**
 * Created by Mloong on 2017/9/14.
 */

public class ReceiptTypeFragment extends Fragment {

    private static final String PARAM_TYPE = "PARAM_TYPE";

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RefreshLayout refreshLayout;

    private int type;

    public static ReceiptTypeFragment newInstance(int type) {
        ReceiptTypeFragment receiptTypeFragment = new ReceiptTypeFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_TYPE, type);
        receiptTypeFragment.setArguments(args);
        return receiptTypeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(PARAM_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
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
            ImageView ivType;
            TextView tvNumber, tvId, tvMerchant, tvTitle, tvName, tvDate, tvOption, tvIdText;
            public MyViewHolder(View view){
                super(view);
                ivType = (ImageView) view.findViewById(R.id.iv_type);
                tvNumber = (TextView) view.findViewById(R.id.tv_number);
                tvMerchant = (TextView) view.findViewById(R.id.tv_marchent);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvId = (TextView) view.findViewById(R.id.tv_id);
                tvOption = (TextView) view.findViewById(R.id.tv_option);
                tvIdText = (TextView) view.findViewById(R.id.tv_id_text);
            }
        }

        public MyAdapter() {
        }


        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_receipt, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if(type == Constants.RECEIPT_STATUS_UNUSED) {
                holder.tvNumber.setTextColor(getResources().getColor(R.color.text_black));
                holder.tvTitle.setTextColor(getResources().getColor(R.color.text_black));
                holder.tvIdText.setTextColor(getResources().getColor(R.color.text_grey2));
                holder.tvId.setTextColor(getResources().getColor(R.color.text_grey2));
                holder.tvName.setTextColor(getResources().getColor(R.color.text_grey2));
                holder.tvMerchant.setTextColor(getResources().getColor(R.color.text_grey2));
                holder.tvDate.setTextColor(getResources().getColor(R.color.red));
                holder.ivType.setImageResource(R.mipmap.ic_receipt_massage);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), ReceiptDetailActivity.class));
                    }
                });
            }
            if(type == Constants.RECEIPT_STATUS_USED){
                holder.tvOption.setVisibility(View.VISIBLE);
                holder.tvOption.setText(R.string.delete);
                holder.tvOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage(getString(R.string.delete_receipt));
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
            if(type == Constants.RECEIPT_STATUS_EXPITED){
                holder.tvOption.setVisibility(View.GONE);
//                holder.tvOption.setText(R.string.apply_refund);
//                holder.tvOption.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        RefundDialog.Builder alertDialogBuilder = new RefundDialog.Builder(getActivity());
//                        alertDialogBuilder.setMessage(Integer.toString(1500));
//                        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        Tools.Toast(getContext(), getString(R.string.refund_applied));
//                                    }
//                                });
//                        alertDialogBuilder.setNegativeButton(getString(R.string.no),
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        alertDialogBuilder.create().show();
//                    }
//                });

            }

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

}
