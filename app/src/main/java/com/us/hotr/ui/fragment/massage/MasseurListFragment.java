package com.us.hotr.ui.fragment.massage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

/**
 * Created by Mloong on 2017/9/29.
 */

public class MasseurListFragment extends BaseLoadingFragment {
    private static final String PARAM_CITY = "PARAM_CITY";
    private static final String PARAM_SPA = "PARAM_SPA";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private int currentPage = 1;
    private Integer SpaId = null;

    public static MasseurListFragment newInstance(int cityId, int SpaId) {
        MasseurListFragment masseurListFragment = new MasseurListFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_DATA, cityId);
        b.putInt(PARAM_SPA, SpaId);
        masseurListFragment.setArguments(b);
        return masseurListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityCode = getArguments().getInt(Constants.PARAM_DATA);
        if(cityCode<0)
            cityCode = null;
        SpaId = getArguments().getInt(PARAM_SPA);
        if(SpaId<0)
            SpaId = null;
        if(SpaId!=null){
            typeId = 0;
            subjectId = null;
            cityCode = null;
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        if(loadType == Constants.LOAD_MORE){
            mListener = new SubscriberListener<BaseListResponse<List<Masseur>>>() {
                @Override
                public void onNext(BaseListResponse<List<Masseur>> result) {
                    updateList(loadType, result);
                }
            };
            ServiceClient.getInstance().getMasseurList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    SpaId, cityCode, null, null, null, typeId, Constants.MAX_PAGE_ITEM, currentPage);
        }else{
            currentPage = 1;
            mListener = new SubscriberListener<BaseListResponse<List<Masseur>>>() {
                @Override
                public void onNext(BaseListResponse<List<Masseur>> result) {
                    updateList(loadType, result);
                }
            };
            if(loadType == Constants.LOAD_PAGE)
                if(SpaId!=null)
                    ServiceClient.getInstance().getMasseurListBySpa(new LoadingSubscriber(mListener, this),
                            SpaId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMasseurList(new LoadingSubscriber(mListener, this),
                            SpaId, cityCode, subjectId, null, null, typeId, Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_DIALOG)
                if(SpaId!=null)
                    ServiceClient.getInstance().getMasseurListBySpa(new LoadingSubscriber(mListener, this),
                            SpaId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMasseurList(new LoadingSubscriber(mListener, this),
                            SpaId, cityCode, subjectId, null, null, typeId, Constants.MAX_PAGE_ITEM, currentPage);
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                if(SpaId!=null)
                    ServiceClient.getInstance().getMasseurListBySpa(new LoadingSubscriber(mListener, this),
                            SpaId, Constants.MAX_PAGE_ITEM, currentPage);
                else
                    ServiceClient.getInstance().getMasseurList(new LoadingSubscriber(mListener, this),
                            SpaId, cityCode, subjectId, null, null, typeId, Constants.MAX_PAGE_ITEM, currentPage);
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Masseur>> result){
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
            View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false);
            myBaseAdapter.setFooterView(footer);
        }
        else
            enableLoadMore(true);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Masseur> masseurList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvAddress, tvAppointment;
            ImageView ivAvatar, ivLike;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvAddress = (TextView) view.findViewById(R.id.tv_address);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                ivLike = (ImageView) view.findViewById(R.id.iv_like);
            }
        }

        public MyAdapter(List<Masseur> masseurList) {
            this.masseurList = masseurList;
        }

        public void setItems(List<Masseur> masseurList){
            this.masseurList = masseurList;
            notifyDataSetChanged();
        }

        public void addItems(List<Masseur> masseurList){
            for(Masseur m:masseurList)
                this.masseurList.add(m);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_masseur, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Masseur masseur = masseurList.get(position);
            if(position%2==0) {
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.ivAvatar.getLayoutParams();
                lp.setMargins(12, 0, 6, 0);
                holder.ivAvatar.setLayoutParams(lp);
            }
            else {
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.ivAvatar.getLayoutParams();
                lp.setMargins(6, 0, 12, 0);
                holder.ivAvatar.setLayoutParams(lp);
            }
            Glide.with(getContext()).load(masseur.getMassagist_main_img()).placeholder(R.drawable.holder_masseur).error(R.drawable.holder_masseur).into(holder.ivAvatar);
            holder.tvAddress.setText(masseur.getAddress());
            holder.tvAppointment.setText(String.format(getString(R.string.masseur_appointment), masseur.getOrder_num()));
            holder.tvName.setText(masseur.getMassagist_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), MasseurActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.PARAM_ID, masseur.getId());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(masseurList == null)
                return 0;
            return masseurList.size();
        }
    }
}
