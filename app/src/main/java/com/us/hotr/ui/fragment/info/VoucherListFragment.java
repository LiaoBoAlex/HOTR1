package com.us.hotr.ui.fragment.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Voucher;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.AvailableVoucherRequest;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Mloong on 2017/9/20.
 */

public class VoucherListFragment extends BaseLoadingFragment {
    public static final int TYPE_VALID =0;
    public static final int TYPE_USED =2;
    public static final int TYPE_EXPIRED =3;
    public static final int TYPE_AVAILABLE =4;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private ConstraintLayout clEmpty;

    private int totalSize = 0;
    private int currentPage = 1;
//    private boolean isLoaded = false;

    private int type;
    private AvailableVoucherRequest request;
    private boolean enableClick = false;
    private Voucher seletedVoucher = null;

    public static VoucherListFragment newInstance(int type, AvailableVoucherRequest request, Voucher voucher) {
        VoucherListFragment voucherFragment = new VoucherListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PARAM_TYPE, type);
        if(request!=null)
            bundle.putSerializable(Constants.PARAM_DATA, request);
        if(voucher!=null)
            bundle.putSerializable(Constants.PARAM_TITLE, voucher);
        voucherFragment.setArguments(bundle);
        return voucherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = getArguments().getInt(Constants.PARAM_TYPE);
        request = (AvailableVoucherRequest) getArguments().getSerializable(Constants.PARAM_DATA);
        seletedVoucher = (Voucher) getArguments().getSerializable(Constants.PARAM_TITLE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        clEmpty = (ConstraintLayout) view.findViewById(R.id.cl_empty);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        enableLoadMore(false);
        if(type == TYPE_AVAILABLE)
            enablePullDownRefresh(false);
//        if(getUserVisibleHint() && !isLoaded){
            loadData(Constants.LOAD_PAGE);
//        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isResumed() && !isLoaded) {
//            loadData(Constants.LOAD_PAGE);
//        }
//    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Voucher>>>() {
            @Override
            public void onNext(BaseListResponse<List<Voucher>> result) {
//                isLoaded = true;
                Events.GetVoucherCount event = new Events.GetVoucherCount(result.getTotal(), type);
                GlobalBus.getBus().post(event);
                if(result!=null && result.getRows()!=null && result.getRows().size()>0){
                    updateList(loadType, result);
                    clEmpty.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }else{
                    clEmpty.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        };
        if(type == TYPE_AVAILABLE) {
            currentPage = 1;
            ServiceClient.getInstance().getAvaliableVoucher(new ProgressSubscriber(mListener, getContext()),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), request);
        }else{
            if (loadType == Constants.LOAD_MORE) {
                ServiceClient.getInstance().getAllVoucher(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                        type, currentPage, Constants.MAX_PAGE_ITEM);
            } else {
                currentPage = 1;
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getAllVoucher(new LoadingSubscriber(mListener, this),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            type, currentPage, Constants.MAX_PAGE_ITEM);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getAllVoucher(new ProgressSubscriber(mListener, getContext()),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            type, currentPage, Constants.MAX_PAGE_ITEM);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getAllVoucher(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(),
                            type, currentPage, Constants.MAX_PAGE_ITEM);
            }
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Voucher>> result){
        totalSize = result.getTotal();
        if(loadType == Constants.LOAD_MORE){
            mAdapter.addItems(result.getRows());
        }else{
            if(mAdapter == null)
                mAdapter = new MyAdapter(result.getRows());
            else
                mAdapter.setItems(result.getRows());
            myBaseAdapter = new MyBaseAdapter(mAdapter);
            mRecyclerView.setAdapter(myBaseAdapter);
        }
        currentPage ++;
        if(mAdapter.getItemCount() >= totalSize) {
            enableLoadMore(false);
            myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false));
        }
        else
            enableLoadMore(true);
    }

    public void setEnableClick(boolean value){
        enableClick = value;
        seletedVoucher = null;
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Voucher> voucherList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivExpired, ivSelected;
            TextView tvTotal, tvName, tvCondition, tvTime;
            public MyViewHolder(View view){
                super(view);
                ivExpired = (ImageView) view.findViewById(R.id.iv_expired);
                ivSelected = (ImageView) view.findViewById(R.id.iv_select);
                tvTotal = (TextView) view.findViewById(R.id.tv_amount);
                tvName = (TextView) view.findViewById(R.id.tv_title);
                tvCondition = (TextView) view.findViewById(R.id.tv_condition);
                tvTime = (TextView) view.findViewById(R.id.tv_time);
            }
        }

        public MyAdapter(List<Voucher> voucherList) {
            this.voucherList = voucherList;
        }

        public void addItems(List<Voucher> voucherList){
            this.voucherList.addAll(voucherList);
            notifyDataSetChanged();
        }

        public void setItems(List<Voucher> voucherList) {
            this.voucherList = voucherList;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_voucher, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Voucher voucher = voucherList.get(position);
            holder.tvTotal.setText(getString(R.string.money) + new DecimalFormat("#").format(voucher.getCoupon_money()));
            holder.tvName.setText(voucher.getCoupon_name());
            holder.tvCondition.setText(String.format(getString(R.string.voucher_condition), new DecimalFormat("#").format(voucher.getFull_money())));
            holder.tvTime.setText(Tools.getVoucherTime(getActivity(), voucher.getUse_start_time(), voucher.getUse_end_time()));
            switch (type){
                case TYPE_VALID:
                    holder.ivExpired.setVisibility(View.GONE);
                    holder.ivSelected.setVisibility(View.GONE);
                    break;
                case TYPE_USED:
                    holder.ivExpired.setVisibility(View.VISIBLE);
                    holder.ivExpired.setImageResource(R.mipmap.ic_used);
                    holder.ivSelected.setVisibility(View.GONE);
                    break;
                case TYPE_EXPIRED:
                    holder.ivExpired.setVisibility(View.VISIBLE);
                    holder.ivExpired.setImageResource(R.mipmap.ic_expired);
                    holder.ivSelected.setVisibility(View.GONE);
                    break;
                case TYPE_AVAILABLE:
                    holder.ivExpired.setVisibility(View.GONE);
                    holder.ivSelected.setVisibility(View.VISIBLE);
                    break;
            }
            if(seletedVoucher!=null && seletedVoucher.getCouponUserId() == voucher.getCouponUserId())
                holder.ivSelected.setImageResource(R.mipmap.ic_massage_clicked);
            else
                holder.ivSelected.setImageResource(R.mipmap.ic_massage_click);
            if(enableClick){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seletedVoucher = voucher;
                        Events.VoucherSelected event = new Events.VoucherSelected(voucher);
                        GlobalBus.getBus().post(event);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(voucherList!=null)
                return voucherList.size();
            else
                return  0;
        }
    }
}
