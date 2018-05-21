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
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.MasseurBigView;
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
 * Created by Mloong on 2017/9/29.
 */

public class MasseurBigListFragment extends BaseLoadingFragment {
    private static final String PARAM_CITY = "PARAM_CITY";
    private static final String PARAM_SPA = "PARAM_SPA";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private MyBaseAdapter myBaseAdapter;
    private int totalSize = 0;
    private Long SpaId = null;

    public static MasseurBigListFragment newInstance(long cityId, long SpaId) {
        MasseurBigListFragment masseurBigListFragment = new MasseurBigListFragment();
        Bundle b = new Bundle();
        b.putLong(Constants.PARAM_DATA, cityId);
        b.putLong(PARAM_SPA, SpaId);
        masseurBigListFragment.setArguments(b);
        return masseurBigListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityCode = getArguments().getLong(Constants.PARAM_DATA);
        if(cityCode<0 || (keyword != null && !keyword.isEmpty()))
            cityCode = null;
        SpaId = getArguments().getLong(PARAM_SPA);
        if(SpaId<0)
            SpaId = null;
        if(SpaId!=null){
            typeId = 0l;
            subjectId = null;
            cityCode = null;
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        enableLoadMore(false);
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(final int loadType) {
        SubscriberListener mListener;
        mListener = new SubscriberListener<BaseListResponse<List<Masseur>>>() {
            @Override
            public void onNext(BaseListResponse<List<Masseur>> result) {
                updateList(loadType, result);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getCollectionMasseur(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getCollectionMasseur(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
    }

    private void updateList(int loadType, BaseListResponse<List<Masseur>> result){
        totalSize = result.getTotal();
        Events.GetSearchCount event = new Events.GetSearchCount(totalSize);
        GlobalBus.getBus().post(event);
        if(mAdapter!=null)
            mAdapter.setItems(result.getRows());
        else
            mAdapter = new MyAdapter(result.getRows());
        myBaseAdapter = new MyBaseAdapter(mAdapter);
        mRecyclerView.setAdapter(myBaseAdapter);
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
                    removeIds.add(mAdapter.masseurList.get(i).getId());
            if(removeIds.size()>0) {
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(getActivity(), getString(R.string.remove_fav_item_success));
                        for (int i = length - 1; i >= 0; i--) {
                            if (mAdapter.checkList.get(i)) {
                                mAdapter.masseurList.remove(i);
                                mAdapter.checkList.remove(i);
                                mAdapter.notifyItemRemoved(i);
                                mAdapter.notifyItemRangeChanged(0, mAdapter.masseurList.size());
                            }
                        }
                        if (mAdapter.checkList.size() == 0) {
                            Events.GetSearchCount event = new Events.GetSearchCount(0);
                            GlobalBus.getBus().post(event);
                        }
                    }
                };
                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID(), removeIds, 2);
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Masseur> masseurList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            MasseurBigView masseurBigView;
            public MyViewHolder(View view) {
                super(view);
                masseurBigView = (MasseurBigView) view;
            }
        }

        public MyAdapter(List<Masseur> masseurList) {
            this.masseurList = masseurList;
            for(int i=0;i<masseurList.size();i++)
                checkList.add(false);
        }

        public void setItems(List<Masseur> masseurList){
            this.masseurList = masseurList;
            for(int i=0;i<masseurList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void addItems(List<Masseur> masseurList){
            this.masseurList.addAll(masseurList);
            for(int i=0;i<masseurList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur_big, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Masseur masseur = masseurList.get(position);
            holder.masseurBigView.setData(masseur, -1);
            holder.masseurBigView.enableEdit(isEdit);
            holder.masseurBigView.setItemSelectedListener(new ItemSelectedListener() {
                @Override
                public void onItemSelected(boolean isSelected) {
                    checkList.set(position, isSelected);
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
