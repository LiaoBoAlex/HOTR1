package com.us.hotr.ui.fragment.receipt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.ProductReceipt;
import com.us.hotr.ui.activity.receipt.ReceiptDetailActivity;
import com.us.hotr.ui.dialog.RefundDialog;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Mloong on 2017/9/14.
 */

public class ProductReceiptListFragment extends BaseLoadingFragment {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private ConstraintLayout clEmpty;
    private int totalSize = 0;
    private int currentPage = 1;
    private boolean isLoaded = false;

    private int type;

    public static ProductReceiptListFragment newInstance(int type) {
        ProductReceiptListFragment productReceiptListFragment = new ProductReceiptListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PARAM_TYPE, type);
        productReceiptListFragment.setArguments(args);
        return productReceiptListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt(Constants.PARAM_TYPE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        clEmpty = (ConstraintLayout) view.findViewById(R.id.cl_empty);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if(getUserVisibleHint() && !isLoaded){
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    protected void loadData(final int loadType) {

        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<ProductReceipt>>>() {
            @Override
            public void onNext(BaseListResponse<List<ProductReceipt>> result) {
                updateList(loadType, result);
            }
        };
        if (loadType == Constants.LOAD_MORE) {
            ServiceClient.getInstance().getProductReceiptList(new SilentSubscriber(mListener, getContext(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), type, currentPage, Constants.MAX_PAGE_ITEM);
        } else {
            currentPage = 1;
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getProductReceiptList(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), type, currentPage, Constants.MAX_PAGE_ITEM);
            else if (loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getProductReceiptList(new ProgressSubscriber(mListener, getContext()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), type, currentPage, Constants.MAX_PAGE_ITEM);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getProductReceiptList(new SilentSubscriber(mListener, getContext(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), type, currentPage, Constants.MAX_PAGE_ITEM);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<ProductReceipt>> result){
        isLoaded = true;
        totalSize = result.getTotal();
        if(loadType == Constants.LOAD_MORE){
            mAdapter.addItems(result.getRows());
        }else{
            if(mAdapter!=null)
                mAdapter.setItems(result.getRows());
            else
                mAdapter = new MyAdapter(result.getRows());
            myBaseAdapter = new MyBaseAdapter(mAdapter);
            mRecyclerView.setAdapter(myBaseAdapter);
        }
        currentPage ++;
        if((mAdapter.getItemCount() >= totalSize && mAdapter.getItemCount() > 0)
                ||totalSize == 0) {
            enableLoadMore(false);
            if(totalSize>0) {
                myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false));
                mRecyclerView.setVisibility(View.VISIBLE);
                clEmpty.setVisibility(View.GONE);
            }
            else{
                mRecyclerView.setVisibility(View.GONE);
                clEmpty.setVisibility(View.VISIBLE);
            }
        }
        else
            enableLoadMore(true);
    }

    @Subscribe
    public void getMessage(Events.Refresh refresh) {
        if(type == Constants.RECEIPT_STATUS_REFUNDING)
            loadData(Constants.LOAD_PULL_REFRESH);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<ProductReceipt> productReceiptList;

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

        public MyAdapter(List<ProductReceipt> productReceiptList) {
            this.productReceiptList = productReceiptList;
        }

        public void setItems(List<ProductReceipt> productReceiptList){
            this.productReceiptList = productReceiptList;
            notifyDataSetChanged();
        }

        public void addItems(List<ProductReceipt> productReceiptList){
            this.productReceiptList.addAll(productReceiptList);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_receipt, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final ProductReceipt productReceipt = productReceiptList.get(position);
            holder.tvId.setText(productReceipt.getOrder_code());
            holder.tvMerchant.setText(productReceipt.getHospital_name());
            holder.tvTitle.setText(productReceipt.getProduct_name_usp());
            holder.tvName.setText(productReceipt.getDoctor_name());
            holder.ivType.setImageResource(R.mipmap.ic_receipt_beauty_gray);
            if(type == Constants.RECEIPT_STATUS_UNUSED) {
                holder.tvNumber.setText(getString(R.string.unused_title));
                holder.tvNumber.setTextColor(getResources().getColor(R.color.text_black));
                holder.tvMerchant.setTextColor(getResources().getColor(R.color.text_black));
                holder.tvDate.setTextColor(getResources().getColor(R.color.red));
                holder.tvDate.setText(Tools.getReceiptTime(getContext(), productReceipt.getRepeal_time()));
                holder.ivType.setImageResource(R.mipmap.ic_receipt_beauty);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), ReceiptDetailActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
                        b.putLong(Constants.PARAM_ID, productReceipt.getId());
                        i.putExtras(b);
                        startActivity(i);
                    }
                });
            }
            if(type == Constants.RECEIPT_STATUS_USED){
                holder.tvNumber.setText(getString(R.string.used_title));
                holder.tvDate.setText(getString(R.string.receipt_used_time)+productReceipt.getVerification_time());
                holder.tvOption.setVisibility(View.VISIBLE);
                holder.tvOption.setText(R.string.delete);
                holder.tvIdText.setText(getString(R.string.receipt_used_code));
                holder.tvId.setText(productReceipt.getVerification_code());
                holder.tvOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage(getString(R.string.delete_receipt));
                        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        SubscriberListener mListener = new SubscriberListener<String>() {
                                            @Override
                                            public void onNext(String result) {
                                                Tools.Toast(getActivity(), getString(R.string.delete_success));
                                                productReceiptList.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(0, productReceiptList.size());
                                                if(productReceiptList.size() == 0){
                                                    mRecyclerView.setVisibility(View.GONE);
                                                    clEmpty.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        };
                                        ServiceClient.getInstance().deleteProductReceipt(new ProgressSubscriber(mListener, getActivity()),
                                                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), productReceipt.getId());
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
                holder.tvNumber.setText(getString(R.string.expired_title));
                holder.tvDate.setText(Tools.getReceiptTime(getContext(), productReceipt.getRepeal_time()));
                holder.tvOption.setVisibility(View.GONE);
                holder.tvOption.setText(R.string.apply_refund);
                holder.tvOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RefundDialog.Builder alertDialogBuilder = new RefundDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage(getString(R.string.money)+new DecimalFormat("0.00").format(productReceipt.getReal_payment()));
                        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        SubscriberListener mListener = new SubscriberListener<String>() {
                                            @Override
                                            public void onNext(String result) {
                                                Tools.Toast(getContext(), getString(R.string.refund_applied));
                                                GlobalBus.getBus().post(new Events.Refresh());
                                                productReceiptList.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(0, productReceiptList.size());
                                                if (productReceiptList.size() == 0) {
                                                    mRecyclerView.setVisibility(View.GONE);
                                                    clEmpty.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        };
                                        ServiceClient.getInstance().refundProductReceipt(new ProgressSubscriber(mListener, getActivity()),
                                                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), productReceipt.getId());
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
            if(type == Constants.RECEIPT_STATUS_REFUNDING){
                holder.tvNumber.setText(getString(R.string.refunding_title));
                String money = getString(R.string.money) + new DecimalFormat("0.00").format(productReceipt.getReal_payment());
                String date = Tools.getReceiptRefundTime(getContext(), productReceipt.getAction_refund_time());
                SpannableString msp = new SpannableString(date+money);
                msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), date.length(), date.length()+money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvDate.setText(msp);
            }
            if(type == Constants.RECEIPT_STATUS_REFUNDED){
                holder.tvNumber.setText(getString(R.string.refunded_title));
                String money = getString(R.string.money) + new DecimalFormat("0.00").format(productReceipt.getReal_payment());
                String date = Tools.getReceiptRefundedTime(getContext(), productReceipt.getRefund_time());
                SpannableString msp = new SpannableString(date+money);
                msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), date.length(), date.length()+money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvDate.setText(msp);
            }

        }

        @Override
        public int getItemCount() {
            if(productReceiptList==null)
                return 0;
            else
                return productReceiptList.size();
        }
    }
}
