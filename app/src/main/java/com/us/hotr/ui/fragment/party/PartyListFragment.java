package com.us.hotr.ui.fragment.party;

import android.content.Intent;
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
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Party;
import com.us.hotr.ui.activity.party.PartyActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/10/25.
 */

public class PartyListFragment extends BaseLoadingFragment {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private List<Party> partyList;

    private List<Integer> partyStatusList = null;

    private int totalSize = 0;
    private int currentPage = 1;
    private boolean isFav = false;

    public static PartyListFragment newInstance(String keyword, boolean isFav) {
        PartyListFragment partyListFragment = new PartyListFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_IS_FAV, isFav);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        partyListFragment.setArguments(b);
        return partyListFragment;
    }

    public static PartyListFragment newInstance(List<Party> partyList) {
        PartyListFragment partyListFragment = new PartyListFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, (Serializable) partyList);
        partyListFragment.setArguments(b);
        return partyListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Subscribe
    public void filterData(List<Integer> statusList) {
        if(statusList!=null && statusList.size()>0)
            partyStatusList = statusList;
        else
            partyStatusList = null;
        loadData(Constants.LOAD_DIALOG);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFav = getArguments().getBoolean(Constants.PARAM_IS_FAV, false);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);

        if(getArguments()!=null && getArguments().getSerializable(Constants.PARAM_DATA)!=null)
            partyList = (List<Party>)getArguments().getSerializable(Constants.PARAM_DATA);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if(partyList==null)
            loadData(Constants.LOAD_PAGE);
        else {
            enablePullDownRefresh(false);
            updateList(Constants.LOAD_PAGE, partyList.size(), partyList);
        }

    }

    @Override
    protected void loadData(final int loadType) {
//        if(getArguments()!=null && getArguments().getSerializable(Constants.PARAM_DATA)!=null){
//            partyList = (List<Party>)getArguments().getSerializable(Constants.PARAM_DATA);
//            enablePullDownRefresh(false);
//            enableLoadMore(false);
//            mAdapter = new MyAdapter(partyList);
//            mRecyclerView.setAdapter(mAdapter);
//        }else {
        SubscriberListener mListener;
        if(isFav){
            mListener = new SubscriberListener<BaseListResponse<List<Party>>>() {
                @Override
                public void onNext(BaseListResponse<List<Party>> result) {
                    updateList(loadType, result.getTotal(), result.getRows());
                }
            };
            if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getCollectionParty(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getCollectionParty(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }else {
            mListener = new SubscriberListener<BaseListResponse<List<Party>>>() {
                @Override
                public void onNext(BaseListResponse<List<Party>> result) {
                    updateList(loadType, result.getTotal(), result.getRows());
                }
            };
            if (loadType == Constants.LOAD_MORE) {
                ServiceClient.getInstance().getPartyList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        keyword, 0, partyStatusList, Constants.MAX_PAGE_ITEM, currentPage);
            } else {
                currentPage = 1;
                if (loadType == Constants.LOAD_PAGE) {
                    ServiceClient.getInstance().getPartyList(new LoadingSubscriber(mListener, this),
                            keyword, 0, partyStatusList, Constants.MAX_PAGE_ITEM, currentPage);
                } else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getPartyList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            keyword, 0, partyStatusList, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getPartyList(new ProgressSubscriber(mListener, getContext()),
                            keyword, 0, partyStatusList, Constants.MAX_PAGE_ITEM, currentPage);
            }
        }
    }

    private void updateList(int loadType, int totalSize, List<Party> partyList){
        this.totalSize = totalSize;
        Events.GetSearchCount event = new Events.GetSearchCount(totalSize);
        GlobalBus.getBus().post(event);
//        this.partyList = partyList;
        if(loadType == Constants.LOAD_MORE){
            mAdapter.addItems(partyList);
        }else{
            if(mAdapter == null)
                mAdapter = new MyAdapter(partyList);
            else
                mAdapter.setItems(partyList);
            myBaseAdapter = new MyBaseAdapter(mAdapter);
            mRecyclerView.setAdapter(myBaseAdapter);
        }
        currentPage ++;
        if((mAdapter.getItemCount() >= totalSize && mAdapter.getItemCount() > 0)
                ||totalSize == 0) {
            enableLoadMore(false);
            if(totalSize>0)
                myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_general, mRecyclerView, false));
            else
                myBaseAdapter.setFooterView(LayoutInflater.from(getContext()).inflate(R.layout.footer_empty, mRecyclerView, false));
        }
        else
            enableLoadMore(true);
    }

    @Subscribe
    public void getMessage(Events.EnableEdit enableEdit) {
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DONE) {
            mAdapter.setEnableEdit(false);
            enablePullDownRefresh(true);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_EDIT) {
            mAdapter.setEnableEdit(true);
            enablePullDownRefresh(false);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DELETE) {
            enablePullDownRefresh(true);
            mAdapter.setEnableEdit(false);
            final int length = mAdapter.checkList.size();
            List<Long> removeIds = new ArrayList<>();
            for (int i = length - 1; i >= 0; i--)
                if(mAdapter.checkList.get(i))
                    removeIds.add(mAdapter.partyList.get(i).getId());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.partyList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.partyList.size());
                            }
                        }
                        if (mAdapter.checkList.size() == 0) {
                            Events.GetSearchCount event = new Events.GetSearchCount(0);
                            GlobalBus.getBus().post(event);
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 0);
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<Party> partyList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar, ivDelete;
            TextView tvNumber, tvTitle, tvDate, tvStatus;
            public MyViewHolder(View view){
                super(view);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvStatus = (TextView) view.findViewById(R.id.tv_status);
                tvNumber = (TextView) view.findViewById(R.id.tv_number);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            }
        }

        public MyAdapter(List<Party> partyList) {
            this.partyList = partyList;
            for(int i=0;i<partyList.size();i++)
                checkList.add(false);
        }

        public void addItems(List<Party> partyList){
            this.partyList.addAll(partyList);
            for(int i=0;i<partyList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setItems(List<Party> partyList){
            this.partyList = partyList;
            for(int i=0;i<partyList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_party, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Party party = partyList.get(position);
            Glide.with(PartyListFragment.this).load(party.getParty_list_img()).dontAnimate().error(R.drawable.holder_party).placeholder(R.drawable.holder_party).into(holder.ivAvatar);
            holder.tvTitle.setText(party.getTravel_name());
            if(party.getTravel_end_time()!=null)
                holder.tvDate.setText(Tools.getPartyTime(getActivity().getApplicationContext(), party.getTravel_start_time(), party.getTravel_end_time()));
            else
                holder.tvDate.setText(Tools.getPartyTime(getActivity().getApplicationContext(), party.getTravel_start_time()));
            switch (party.getSale_ticket_status()) {
                case 0:
                    holder.tvStatus.setText(getString(R.string.status_on_sale));
                    holder.tvNumber.setVisibility(View.VISIBLE);
                    holder.tvNumber.setText(String.format(getString(R.string.party_sale_number), party.getOrder_num()));
                    break;
                case 1:
                    holder.tvStatus.setText(getString(R.string.status_on_going));
                    holder.tvNumber.setVisibility(View.VISIBLE);
                    holder.tvNumber.setText(String.format(getString(R.string.party_sale_number), party.getOrder_num()));
                    break;
                case 2:
                    holder.tvStatus.setText(Tools.getSaleTime(getActivity(), party.getSale_ticket_time()));
                    holder.tvNumber.setVisibility(View.VISIBLE);
                    holder.tvNumber.setText(String.format(getString(R.string.party_interested_number), party.getAccess_count()));
                    break;
                case 3:
                    holder.tvStatus.setText(getString(R.string.status_promo));
                    holder.tvNumber.setVisibility(View.VISIBLE);
                    holder.tvNumber.setText(String.format(getString(R.string.party_interested_number), party.getAccess_count()));
                    break;
                case 4:
                    holder.tvStatus.setText(getString(R.string.status_pre_view));
                    holder.tvNumber.setVisibility(View.VISIBLE);
                    holder.tvNumber.setText(String.format(getString(R.string.party_interested_number), party.getAccess_count()));
                    break;
                case 5:
                    holder.tvStatus.setText(getString(R.string.status_end));
                    holder.tvNumber.setVisibility(View.GONE);
                    break;
            }
            if (isEdit) {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setImageResource(R.mipmap.ic_delete_order);
                holder.itemView.setTag(false);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((boolean) view.getTag()) {
                            holder.ivDelete.setImageResource(R.mipmap.ic_delete_order);
                            view.setTag(false);
                        } else {
                            holder.ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
                            view.setTag(true);
                        }
                        checkList.set(position, (boolean)view.getTag());
                    }
                });
            } else {
                holder.ivDelete.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(), PartyActivity.class);
                        Bundle b = new Bundle();
                        b.putLong(Constants.PARAM_ID, party.getId());
                        i.putExtras(b);
                        startActivity(i);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(partyList == null)
                return 0;
            else
                return partyList.size();
        }
    }
}
