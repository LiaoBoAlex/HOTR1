package com.us.hotr.ui.fragment.massage;

import android.content.Intent;
import android.graphics.Paint;
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

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.ui.activity.massage.MassageActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Mloong on 2017/9/30.
 */

public class MassageListFragment extends BaseLoadingFragment {
    private static final String PARAM_CITY = "PARAM_CITY";
    private static final String PARAM_SPA = "PARAM_SPA";
    private static final String PARAM_SUBJECT = "PARAM_SUBJECT";

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;

    private Integer spalId = null;

    public static MassageListFragment newInstance(int subjectId, int cityId, int spalId) {
        MassageListFragment massageListFragment = new MassageListFragment();
        Bundle b = new Bundle();
        b.putInt(PARAM_SUBJECT, subjectId);
        b.putInt(PARAM_CITY, cityId);
        b.putInt(PARAM_SPA, spalId);
        massageListFragment.setArguments(b);
        return massageListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subjectId = getArguments().getInt(PARAM_SUBJECT);
        if(subjectId<0)
            subjectId = null;
        cityCode = getArguments().getInt(PARAM_CITY);
        if(cityCode<0)
            cityCode = null;
        spalId = getArguments().getInt(PARAM_SPA);
        if(spalId<0)
            spalId = null;
        if(spalId!=null){
            typeId = 5;
            cityCode = null;
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        if(loadType == Constants.LOAD_MORE){
            SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Massage>>>() {
                @Override
                public void onNext(BaseListResponse<List<Massage>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getMassageList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
        }else{
            currentPage = 1;
            SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Massage>>>() {
                @Override
                public void onNext(BaseListResponse<List<Massage>> result) {
                    updateList(loadType, result);
                }
            };
            if(loadType == Constants.LOAD_PAGE){
                if(spalId != null)
                    ServiceClient.getInstance().getMassageListBySpa(new LoadingSubscriber(mListener,this),
                            spalId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMassageList(new LoadingSubscriber(mListener,this),
                            typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
            }

            else if(loadType == Constants.LOAD_PULL_REFRESH)
                if(spalId != null)
                    ServiceClient.getInstance().getMassageListBySpa(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            spalId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMassageList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
            else if(loadType == Constants.LOAD_DIALOG)
                if(spalId != null)
                    ServiceClient.getInstance().getMassageListBySpa(new ProgressSubscriber(mListener,getContext()),
                            spalId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMassageList(new ProgressSubscriber(mListener,getContext()),
                            typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Massage>> result){
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
        if((mAdapter.getItemCount() >= totalSize && mAdapter.getItemCount() > 0)
                ||totalSize == 0) {
            enableLoadMore(false);
            View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_general, mRecyclerView, false);
            myBaseAdapter.setFooterView(footer);
        }
        else
            enableLoadMore(true);
    }

    @Subscribe
    public void getMessage(Events.EnableEdit enableEdit) {
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DONE)
            mAdapter.setEnableEdit(false);
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_EDIT)
            mAdapter.setEnableEdit(true);
        mAdapter.notifyDataSetChanged();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Massage> massageList;
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDoctor, tvHospital, tvAppointment, tvPriceBefore, tvPriceAfter, tvMin;
            ImageView ivAvatar, ivGo, ivDelete, ivPromoPrice, ivOnePrice;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDoctor = (TextView) view.findViewById(R.id.tv_product_doctor);
                tvHospital = (TextView) view.findViewById(R.id.tv_product_fav);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                tvPriceBefore = (TextView) view.findViewById(R.id.tv_price_before);
                tvPriceAfter = (TextView) view.findViewById(R.id.tv_pay_amount);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_product_avatar);
                ivGo = (ImageView) view.findViewById(R.id.iv_go);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                tvMin = (TextView) view.findViewById(R.id.tv_min);
                ivPromoPrice = (ImageView) view.findViewById(R.id.iv_promo_price);
                ivOnePrice = (ImageView) view.findViewById(R.id.iv_one_price);
            }
        }

        public void setEnableEdit(boolean enable) {
            isEdit = enable;
        }

        public MyAdapter(List<Massage> massageList) {
            this.massageList = massageList;
        }

        public void addItems(List<Massage> massageList){
            for(Massage m:massageList)
                this.massageList.add(m);
            notifyDataSetChanged();
        }

        public void setItems(List<Massage> massageList) {
            this.massageList = massageList;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);

            return new MyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
            final Massage massage = massageList.get(position);
            holder.tvTitle.setText(getString(R.string.bracket_left)+massage.getProduct_name()+getString(R.string.bracket_right)+massage.getProduct_usp());
            holder.tvDoctor.setText(massage.getMassage_name());
            holder.tvHospital.setText("");
            holder.tvAppointment.setText(String.format(getString(R.string.num_of_appointment1), massage.getOrder_num()));
            holder.tvPriceBefore.setText(String.format(getString(R.string.price), massage.getShop_price()));
            if(massage.getProduct_type() == Constants.PROMOTION_PRODUCT) {
                holder.tvPriceAfter.setText(massage.getActivity_price() + "/" + massage.getService_time());
                holder.ivPromoPrice.setVisibility(View.VISIBLE);
            }
            else {
                holder.tvPriceAfter.setText(massage.getOnline_price() + "/" + massage.getService_time());
                holder.ivPromoPrice.setVisibility(View.GONE);
            }
            holder.tvPriceBefore.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvMin.setVisibility(View.VISIBLE);
            holder.ivOnePrice.setVisibility(View.GONE);
            Glide.with(getContext()).load(massage.getProduct_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.ivAvatar);
            if (isEdit) {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setImageResource(R.mipmap.ic_delete_order);
                holder.ivDelete.setTag(false);
            } else
                holder.ivDelete.setVisibility(View.GONE);

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((boolean) view.getTag()) {
                        ((ImageView) view).setImageResource(R.mipmap.ic_delete_order);
                        view.setTag(false);
                    } else {
                        ((ImageView) view).setImageResource(R.mipmap.ic_delete_order_clicked);
                        view.setTag(true);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), MassageActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.PARAM_ID, massage.getId());
                    i.putExtras(b);
                    getActivity().startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (massageList == null)
                return 0;
            return massageList.size();
        }
    }
}
