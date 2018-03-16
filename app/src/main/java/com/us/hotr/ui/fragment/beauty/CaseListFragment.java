package com.us.hotr.ui.fragment.beauty;

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
import com.us.hotr.storage.bean.Case;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.CaseView;
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
 * Created by Mloong on 2017/9/6.
 */

public class CaseListFragment extends BaseLoadingFragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;

    private boolean enableRefresh;
    private int totalSize = 0;
    private int currentPage = 1;
    private boolean isLoaded = false;
    private long userId = -1;
    private boolean isFav = false;
    private int catagory = 0;
    private long id;
    private Long subjectId;

    public static CaseListFragment newInstance(String keyword, boolean enableRefresh, boolean isFav, int catagory, long id, long subjectId) {
        CaseListFragment caseListFragment = new CaseListFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_ENABLE_REFRESH, enableRefresh);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        b.putBoolean(Constants.PARAM_IS_FAV, isFav);
        b.putInt(Constants.PARAM_CATEGORY, catagory);
        b.putLong(Constants.PARAM_ID, id);
        b.putLong(Constants.PARAM_SUBJECT_ID, subjectId);
        caseListFragment.setArguments(b);
        return caseListFragment;
    }

    public static CaseListFragment newInstance(long userId) {
        CaseListFragment caseListFragment = new CaseListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_DOCTOR_ID, userId);
        caseListFragment.setArguments(b);
        return caseListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = getArguments().getLong(Constants.PARAM_DOCTOR_ID);
        isFav = getArguments().getBoolean(Constants.PARAM_IS_FAV);
        enableRefresh = getArguments().getBoolean(Constants.PARAM_ENABLE_REFRESH);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        catagory = getArguments().getInt(Constants.PARAM_CATEGORY);
        subjectId = getArguments().getLong(Constants.PARAM_SUBJECT_ID, -1);
        id = getArguments().getLong(Constants.PARAM_ID);
        if(userId>0)
            enableRefresh = false;
        if(subjectId < 0)
            subjectId = null;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        enablePullDownRefresh(enableRefresh);

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
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Case>>>() {
            @Override
            public void onNext(BaseListResponse<List<Case>> result) {
                updateList(loadType, result);
            }
        };
        if(userId>0){
            if (loadType == Constants.LOAD_MORE) {
                ServiceClient.getInstance().getCaseListbyUser(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), userId, Constants.MAX_PAGE_ITEM, currentPage);
            } else {
                currentPage = 1;
                ServiceClient.getInstance().getCaseListbyUser(new SilentSubscriber(mListener, getActivity(), null),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), userId, Constants.MAX_PAGE_ITEM, currentPage);
            }
        }else if(isFav){
            if (loadType == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getCollectionCase(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            else if (loadType == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getCollectionCase(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }else if (catagory > 0) {
            if (loadType == Constants.LOAD_MORE) {
                ServiceClient.getInstance().getCaseByType(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), catagory, id, null, currentPage, Constants.MAX_PAGE_ITEM);
            } else {
                currentPage = 1;
                if (loadType == Constants.LOAD_PAGE) {
//                    if (getActivity() instanceof BaseLoadingActivity)
//                        ServiceClient.getInstance().getCaseByType(new LoadingSubscriber(mListener, (BaseLoadingActivity) getActivity()),
//                                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), catagory, id, subjectId, currentPage, Constants.MAX_PAGE_ITEM);
//                    else
                    ServiceClient.getInstance().getCaseByType(new LoadingSubscriber(mListener, this),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), catagory, id, subjectId, currentPage, Constants.MAX_PAGE_ITEM);
                }
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getCaseByType(new ProgressSubscriber(mListener, getContext()),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), catagory, id, subjectId, currentPage, Constants.MAX_PAGE_ITEM);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getCaseByType(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), catagory, id, subjectId, currentPage, Constants.MAX_PAGE_ITEM);
            }
        }else {
            if (loadType == Constants.LOAD_MORE) {
                ServiceClient.getInstance().getAllCase(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM, keyword);
            } else {
                currentPage = 1;
                if (loadType == Constants.LOAD_PAGE)
                    ServiceClient.getInstance().getAllCase(new LoadingSubscriber(mListener, this),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM, keyword);
                else if (loadType == Constants.LOAD_DIALOG)
                    ServiceClient.getInstance().getAllCase(new ProgressSubscriber(mListener, getContext()),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM, keyword);
                else if (loadType == Constants.LOAD_PULL_REFRESH)
                    ServiceClient.getInstance().getAllCase(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), currentPage, Constants.MAX_PAGE_ITEM, keyword);
            }
        }
    }

    private void updateList(int loadType, BaseListResponse<List<Case>> result){
        isLoaded = true;
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
            enablePullDownRefresh(enableRefresh);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_EDIT) {
            mAdapter.setEnableEdit(true);
            enablePullDownRefresh(false);
        }
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DELETE) {
            enablePullDownRefresh(enableRefresh);
            mAdapter.setEnableEdit(false);
            final int length = mAdapter.checkList.size();
            List<Long> removeIds = new ArrayList<>();
            for (int i = length - 1; i >= 0; i--)
                if(mAdapter.checkList.get(i))
                    removeIds.add(mAdapter.caseList.get(i).getKey());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.caseList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.caseList.size());
                            }
                        }
                        if (mAdapter.checkList.size() == 0) {
                            Events.GetSearchCount event = new Events.GetSearchCount(0);
                            GlobalBus.getBus().post(event);
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 7);
            }
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<Case> caseList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            CaseView caseView;
            public MyViewHolder(View view) {
                super(view);
                caseView = (CaseView) view;
            }
        }

        public MyAdapter(List<Case> caseList) {
            this.caseList = caseList;
            for(int i=0;i<caseList.size();i++)
                checkList.add(false);
        }

        public void setItems(List<Case> caseList){
            this.caseList = caseList;
            checkList = new ArrayList<>();
            for(int i=0;i<caseList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void addItems(List<Case> caseList){
            this.caseList.addAll(caseList);
            for(int i=0;i<caseList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Case c = caseList.get(position);
            holder.caseView.setData(c);
            holder.caseView.enableEdit(isEdit);
            holder.caseView.setItemSelectedListener(new ItemSelectedListener() {
                @Override
                public void onItemSelected(boolean isSelected) {
                    checkList.set(position, isSelected);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(caseList == null)
                return 0;
            return caseList.size();
        }
    }
}
