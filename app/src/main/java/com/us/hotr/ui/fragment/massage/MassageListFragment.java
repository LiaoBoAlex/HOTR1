package com.us.hotr.ui.fragment.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.MassageView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
    private boolean isFav = false;

    private Long spalId = null;

    public static MassageListFragment newInstance(String keyword, boolean isFav, long subjectId, long cityId, long spalId) {
        MassageListFragment massageListFragment = new MassageListFragment();
        Bundle b = new Bundle();
        b.putLong(PARAM_SUBJECT, subjectId);
        b.putLong(PARAM_CITY, cityId);
        b.putLong(PARAM_SPA, spalId);
        b.putBoolean(Constants.PARAM_IS_FAV, isFav);
        b.putString(Constants.PARAM_KEYWORD, keyword);
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
        subjectId = getArguments().getLong(PARAM_SUBJECT);
        isFav = getArguments().getBoolean(Constants.PARAM_IS_FAV);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        if(subjectId<0)
            subjectId = null;
        cityCode = getArguments().getLong(PARAM_CITY);
        if(cityCode<=0 || (keyword != null && !keyword.isEmpty()))
            cityCode = null;
        spalId = getArguments().getLong(PARAM_SPA);
        if(spalId<=0)
            spalId = null;
        if(spalId!=null){
            cityCode = null;
            if(subjectId == null || subjectId < 0)
                subjectId = 0l;
        }


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        if(isFav){
            mListener = new SubscriberListener<BaseListResponse<List<Massage>>>() {
                @Override
                public void onNext(BaseListResponse<List<Massage>> result) {
                    updateList(loadType, result);
                }
            };
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getCollectionMassage(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getCollectionMassage(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }else {
            if (loadType == Constants.LOAD_MORE) {
                mListener = new SubscriberListener<BaseListResponse<List<Massage>>>() {
                    @Override
                    public void onNext(BaseListResponse<List<Massage>> result) {
                        updateList(loadType, result);
                    }
                };
                ServiceClient.getInstance().getMassageList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        keyword, typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
            } else {
                currentPage = 1;
                mListener = new SubscriberListener<BaseListResponse<List<Massage>>>() {
                    @Override
                    public void onNext(BaseListResponse<List<Massage>> result) {
                        updateList(loadType, result);
                    }
                };
                if (loadType == Constants.LOAD_PAGE) {
                    if (spalId != null)
                        ServiceClient.getInstance().getMassageListBySpa(new LoadingSubscriber(mListener, this),
                                spalId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                    else
                        ServiceClient.getInstance().getMassageList(new LoadingSubscriber(mListener, this),
                                keyword, typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
                } else if (loadType == Constants.LOAD_PULL_REFRESH)
                    if (spalId != null)
                        ServiceClient.getInstance().getMassageListBySpa(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                                spalId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                    else
                        ServiceClient.getInstance().getMassageList(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                                keyword, typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
                else if (loadType == Constants.LOAD_DIALOG)
                    if (spalId != null)
                        ServiceClient.getInstance().getMassageListBySpa(new ProgressSubscriber(mListener, getContext()),
                                spalId, subjectId, Constants.MAX_PAGE_ITEM, currentPage);
                    else
                        ServiceClient.getInstance().getMassageList(new ProgressSubscriber(mListener, getContext()),
                                keyword, typeId, subjectId, cityCode, null, null, Constants.MAX_PAGE_ITEM, currentPage);
            }
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Massage>> result){
        totalSize = result.getTotal();
        Events.GetSearchCount event = new Events.GetSearchCount(totalSize);
        GlobalBus.getBus().post(event);
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
                    removeIds.add(mAdapter.massageList.get(i).getKey());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.massageList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.massageList.size());
                            }
                        }
                        if (mAdapter.checkList.size() == 0) {
                            Events.GetSearchCount event = new Events.GetSearchCount(0);
                            GlobalBus.getBus().post(event);
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 6);
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Massage> massageList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            MassageView massageView;
            public MyViewHolder(View view) {
                super(view);
                massageView = (MassageView) view;
            }
        }

        public void setEnableEdit(boolean enable) {
            isEdit = enable;
            notifyDataSetChanged();
        }

        public MyAdapter(List<Massage> massageList) {
            this.massageList = massageList;
            for(int i=0;i<massageList.size();i++)
                checkList.add(false);
        }

        public void addItems(List<Massage> massageList){
            this.massageList.addAll(massageList);
            for(int i=0;i<massageList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setItems(List<Massage> massageList) {
            this.massageList = massageList;
            for(int i=0;i<massageList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_massage, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
            final Massage massage = massageList.get(position);
            holder.massageView.setData(massage, -1);
            holder.massageView.enableEdit(isEdit);
            holder.massageView.setItemSelectedListener(new ItemSelectedListener() {
                @Override
                public void onItemSelected(boolean isSelected) {
                    checkList.set(position, isSelected);
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
