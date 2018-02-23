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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.MassageReceipt;
import com.us.hotr.storage.bean.ProductReceipt;
import com.us.hotr.ui.activity.receipt.ReceiptDetailActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.response.GetReceiptListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mloong on 2017/9/14.
 */

public class ReceiptTypeFragment extends BaseLoadingFragment {
    private static final int TYPE_PRODUCT = 1;
    private static final int TYPE_MASSAGE = 2;
    private static final String PARAM_TYPE = "PARAM_TYPE";

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<GetReceiptListResponse>() {
            @Override
            public void onNext(GetReceiptListResponse result) {
//                updateList(loadType, result);
                mAdapter = new MyAdapter(sortData(result));
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                mRecyclerView.setAdapter(myBaseAdapter);
                enableLoadMore(false);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getReceiptList(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), type);
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getReceiptList(new SilentSubscriber(mListener, getContext(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), type);
    }

    private List<Item> sortData(GetReceiptListResponse response){
        List<Item> result = new ArrayList<>();

        for(ProductReceipt pr:response.getYmList()){
            if(result.size() == 0)
                result.add(new Item(TYPE_PRODUCT, pr));
            else{
                for(Item item:result){
                    String time1 = "", time2 = "";
                    if(item.getId() == TYPE_PRODUCT) {
                        if(type == Constants.RECEIPT_STATUS_UNUSED || type == Constants.RECEIPT_STATUS_EXPIRED) {
                            time1 = ((ProductReceipt) item.getContent()).getRepeal_time();
                            time2 = pr.getRepeal_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDED){
                            time1 = ((ProductReceipt) item.getContent()).getRefund_time();
                            time2 = pr.getRefund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDING){
                            time1 = ((ProductReceipt) item.getContent()).getAction_refund_time();
                            time2 = pr.getAction_refund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_USED){
                            time1 = ((ProductReceipt) item.getContent()).getVerification_time();
                            time2 = pr.getVerification_time();
                        }
                    }

                    if(item.getId() == TYPE_MASSAGE) {
                        if(type == Constants.RECEIPT_STATUS_UNUSED || type == Constants.RECEIPT_STATUS_EXPIRED) {
                            time1 = ((MassageReceipt) item.getContent()).getRepeal_time();
                            time2 = pr.getRepeal_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDED){
                            time1 = ((MassageReceipt) item.getContent()).getRefund_time();
                            time2 = pr.getRefund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDING){
                            time1 = ((MassageReceipt) item.getContent()).getAction_refund_time();
                            time2 = pr.getAction_refund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_USED){
                            time1 = ((MassageReceipt) item.getContent()).getVerification_time();
                            time2 = pr.getVerification_time();
                        }
                    }
                    if(compareTime(time1, time2)>=0) {
                        result.add(new Item(TYPE_PRODUCT, pr));
                        break;
                    }
                }
            }
        }
        for(MassageReceipt mr:response.getAmList()){
            if(result.size() == 0)
                result.add(new Item(TYPE_MASSAGE, mr));
            else{
                for(Item item:result){
                    String time1 = "", time2 = "";
                    if(item.getId() == TYPE_PRODUCT) {
                        if(type == Constants.RECEIPT_STATUS_UNUSED || type == Constants.RECEIPT_STATUS_EXPIRED) {
                            time1 = ((ProductReceipt) item.getContent()).getRepeal_time();
                            time2 = mr.getRepeal_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDED){
                            time1 = ((ProductReceipt) item.getContent()).getRefund_time();
                            time2 = mr.getRefund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDING){
                            time1 = ((ProductReceipt) item.getContent()).getAction_refund_time();
                            time2 = mr.getAction_refund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_USED){
                            time1 = ((ProductReceipt) item.getContent()).getVerification_time();
                            time2 = mr.getVerification_time();
                        }
                    }

                    if(item.getId() == TYPE_MASSAGE) {
                        if(type == Constants.RECEIPT_STATUS_UNUSED || type == Constants.RECEIPT_STATUS_EXPIRED) {
                            time1 = ((MassageReceipt) item.getContent()).getRepeal_time();
                            time2 = mr.getRepeal_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDED){
                            time1 = ((MassageReceipt) item.getContent()).getRefund_time();
                            time2 = mr.getRefund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_REFUNDING){
                            time1 = ((MassageReceipt) item.getContent()).getAction_refund_time();
                            time2 = mr.getAction_refund_time();
                        }
                        if(type == Constants.RECEIPT_STATUS_USED){
                            time1 = ((MassageReceipt) item.getContent()).getVerification_time();
                            time2 = mr.getVerification_time();
                        }
                    }
                    if(compareTime(time1, time2)>=0) {
                        result.add(new Item(TYPE_MASSAGE, mr));
                        break;
                    }
                }
            }
        }
        return result;
    }

    private int compareTime(String time1, String time2){

        try {
            Calendar cal1=Calendar.getInstance();
            cal1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time1));
            Calendar cal2=Calendar.getInstance();
            cal2.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time2));
            if(cal1.getTimeInMillis() == cal2.getTimeInMillis())
                return 0;
            else if(cal1.getTimeInMillis()>cal2.getTimeInMillis())
                return 1;
            else
                return  -1;
        } catch (ParseException e) {
            return 0;
        }
    }

public class Item{
    private int id;
    private Object content;

    public Item(int id){
        this.id = id;
    }

    public Item(int id, Object content){
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Item> itemList;

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

    public MyAdapter(List<Item> itemList) {
        this.itemList = itemList;
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
        if(type == Constants.RECEIPT_STATUS_EXPIRED){
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
        if(itemList==null)
            return 0;
        else
            return itemList.size();
    }
}

}
