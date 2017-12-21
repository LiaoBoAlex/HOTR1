package com.us.hotr.ui.fragment.beauty;

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
import com.us.hotr.storage.bean.Product;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
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
 * Created by Mloong on 2017/9/20.
 */

public class ProductListFragment extends BaseLoadingFragment {
    private static final String PARAM_CITY = "PARAM_CITY";
    private static final String PARAM_HOSPITAL = "PARAM_HOSPITAL";
    private static final String PARAM_SUBJECT = "PARAM_SUBJECT";
    private static final String PARAM_DOCTOR = "PARAM_DOCTOR";

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;

    private boolean enableRefresh;
    private Integer hospitalId = null;
    private Integer doctorId = null;

    public static ProductListFragment newInstance(boolean enableRefresh, int subjectId, int cityId, int hospitalId, int doctorId) {
        ProductListFragment productListFragment = new ProductListFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_ENABLE_REFRESH, enableRefresh);
        b.putInt(PARAM_SUBJECT, subjectId);
        b.putInt(PARAM_CITY, cityId);
        b.putInt(PARAM_HOSPITAL, hospitalId);
        b.putInt(PARAM_DOCTOR, doctorId);
        productListFragment.setArguments(b);
        return productListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enableRefresh = getArguments().getBoolean(Constants.PARAM_ENABLE_REFRESH);
        subjectId = getArguments().getInt(PARAM_SUBJECT);
        if(subjectId<0)
            subjectId = null;
        cityCode = getArguments().getInt(PARAM_CITY);
        if(cityCode<0)
            cityCode = null;
        hospitalId = getArguments().getInt(PARAM_HOSPITAL);
        if(hospitalId<0)
            hospitalId = null;
        doctorId = getArguments().getInt(PARAM_DOCTOR);
        if(doctorId<0)
            doctorId = null;
        if(hospitalId!=null || doctorId!=null){
            typeId = 5;
            subjectId = null;
            cityCode = null;
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        enablePullDownRefresh(enableRefresh);
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType){
        if(loadType == Constants.LOAD_MORE){
            SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Product>>>() {
                @Override
                public void onNext(BaseListResponse<List<Product>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getProductList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    typeId, hospitalId, doctorId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
        }else{
            currentPage = 1;
            SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Product>>>() {
                @Override
                public void onNext(BaseListResponse<List<Product>> result) {
                    updateList(loadType, result);
                }
            };
            if(loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getProductList(new LoadingSubscriber(mListener,(BaseLoadingActivity) getActivity()),
                        typeId, hospitalId, doctorId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
            else if(loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getProductList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        typeId, hospitalId, doctorId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
            else if(loadType == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getProductList(new ProgressSubscriber(mListener, getContext()),
                        typeId, hospitalId, doctorId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Product>> result){
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
            View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false);
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

    @Subscribe
    public void getMessage(Events.Refresh refresh) {
        loadData(Constants.LOAD_PULL_REFRESH);

    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
        if(subjectSelected.getSubjectId()<0)
            subjectId = null;
        else
            subjectId = subjectSelected.getSubjectId();
        if(enableRefresh)
            loadData(Constants.LOAD_DIALOG);
        else
            loadData(Constants.LOAD_PAGE);

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Product> productList;
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDoctor, tvHospital, tvAppointment, tvPriceBefore, tvPriceAfter, tvSoldOut;
            ImageView ivAvatar, ivGo, ivDelete, ivOnePrice, ivPromoPrice;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDoctor = (TextView) view.findViewById(R.id.tv_product_doctor);
                tvHospital = (TextView) view.findViewById(R.id.tv_product_fav);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                tvPriceBefore = (TextView) view.findViewById(R.id.tv_price_before);
                tvPriceAfter = (TextView) view.findViewById(R.id.tv_pay_amount);
                tvSoldOut = (TextView) view.findViewById(R.id.tv_sold_out);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_product_avatar);
                ivGo = (ImageView) view.findViewById(R.id.iv_go);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                ivOnePrice = (ImageView) view.findViewById(R.id.iv_one_price);
                ivPromoPrice = (ImageView) view.findViewById(R.id.iv_promo_price);
            }
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
        }

        public MyAdapter(List<Product> productList) {
            this.productList = productList;
        }

        public void addItems(List<Product> productList){
            for(Product p:productList)
                this.productList.add(p);
            notifyDataSetChanged();
        }

        public void setItems(List<Product> productList) {
            this.productList = productList;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Product product = productList.get(position);
            holder.tvTitle.setText(getString(R.string.bracket_left)+product.getProduct_name()+getString(R.string.bracket_right)+product.getProduct_usp());
            holder.tvDoctor.setText(product.getDoctor_name());
            holder.tvHospital.setText(product.getHospital_name());
            holder.tvAppointment.setText(String.format(getString(R.string.num_of_appointment1), product.getOrder_num()));
            holder.tvPriceBefore.setText(String.format(getString(R.string.price), product.getShop_price()));
            holder.tvPriceAfter.setText(product.getOnline_price()+"");
            holder.tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
            Glide.with(getContext()).load(product.getProduct_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.ivAvatar);
            if(product.getPayment_type() == Constants.FULL_PAYMENT)
                holder.ivOnePrice.setVisibility(View.VISIBLE);
            else
                holder.ivOnePrice.setVisibility(View.GONE);
            if(product.getProduct_type() == Constants.PROMOTION_PRODUCT){
                holder.ivPromoPrice.setVisibility(View.VISIBLE);
                if(product.getAmount()>0)
                    holder.tvSoldOut.setVisibility(View.GONE);
                else
                    holder.tvSoldOut.setVisibility(View.GONE);
            }else
                holder.ivPromoPrice.setVisibility(View.GONE);
            if(isEdit) {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setImageResource(R.mipmap.ic_delete_order);
                holder.ivDelete.setTag(false);
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if((boolean)view.getTag()){
                            ((ImageView)view).setImageResource(R.mipmap.ic_delete_order);
                            view.setTag(false);
                        }else{
                            ((ImageView)view).setImageResource(R.mipmap.ic_delete_order_clicked);
                            view.setTag(true);
                        }
                    }
                });
            }else
                holder.ivDelete.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), ProductActivity.class);
                    Bundle b= new Bundle();
                    b.putInt(Constants.PARAM_ID, product.getProductId());
                    i.putExtras(b);
                    getActivity().startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(productList == null)
                return 0;
            return productList.size();
        }
    }
}
