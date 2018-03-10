package com.us.hotr.ui.fragment.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.greendao.DataBaseHelper;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

/**
 * Created by Mloong on 2017/8/29.
 */

public class TypeSearchFragment extends BaseLoadingFragment {
    private TextView tvTotal;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    private String keyword;

    public static TypeSearchFragment newInstance(String searchText) {
        TypeSearchFragment typeSearchFragment = new TypeSearchFragment();
        Bundle b = new Bundle();
        b.putString(Constants.PARAM_SEARCH_STRING, searchText);
        typeSearchFragment.setArguments(b);
        return typeSearchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_type_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvTotal = (TextView) view.findViewById(R.id.tv_amount);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());



        if(getArguments()!=null) {
            keyword = getArguments().getString(Constants.PARAM_SEARCH_STRING);
            DataBaseHelper.getInstance(getActivity().getApplicationContext()).insertSearchHistory(keyword);
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<HashMap<String, Integer>>() {
            @Override
            public void onNext(HashMap<String, Integer> result) {
                int total = 0;
                if(result!=null && result.size()>0) {
                    if(mAdapter == null) {
                        mAdapter = new MyAdapter(result);
                        mRecycleView.setAdapter(mAdapter);
                    }else
                        mAdapter.setData(result);
                }
                tvTotal.setText(String.format(getString(R.string.total), total));
            }
        };
        ServiceClient.getInstance().getSearchCount(new LoadingSubscriber(mListener, this),
                keyword);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        HashMap<String, Integer> map;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvTotal;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTotal = (TextView) view.findViewById(R.id.tv_amount);
            }
        }

        public MyAdapter(HashMap<String, Integer> map) {
            this.map = map;
        }

        public void setData(HashMap<String, Integer> map){
            this.map = map;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_type, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            switch (position + 101){
                case Constants.TYPE_CASE:
                    holder.tvTitle.setText(R.string.case_title);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("7")));
                    break;
                case Constants.TYPE_SEARCH_POST:
                    holder.tvTitle.setText(R.string.post_title);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("8")));
                    break;
                case Constants.TYPE_PARTY:
                    holder.tvTitle.setText(R.string.party_title);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("10")));
                    break;
                case Constants.TYPE_DOCTOR:
                    holder.tvTitle.setText(R.string.doctor);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("2")));
                    break;
                case Constants.TYPE_HOSPITAL:
                    holder.tvTitle.setText(R.string.hospital1);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("1")));
                    break;
                case Constants.TYPE_SPA:
                    holder.tvTitle.setText(R.string.spa1);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("4")));
                    break;
                case Constants.TYPE_MASSEUR:
                    holder.tvTitle.setText(R.string.masseur2);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("5")));
                    break;
                case Constants.TYPE_PRODUCT:
                    holder.tvTitle.setText(R.string.product1);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("3")));
                    break;
                case Constants.TYPE_MASSAGE:
                    holder.tvTitle.setText(R.string.massage1);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("6")));
                    break;
                case Constants.TYPE_SEARCH_PEOPLE:
                    holder.tvTitle.setText(R.string.user);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("11")));
                    break;
                case Constants.TYPE_GROUP:
                    holder.tvTitle.setText(R.string.group_title);
                    holder.tvTotal.setText(String.format(getString(R.string.total2), map.get("9")));
                    break;

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Events.SearchTypeChosen event = new Events.SearchTypeChosen(position + 101);
                    GlobalBus.getBus().post(event);
                }
            });

        }

        @Override
        public int getItemCount() {
            if(map == null)
                return 0;
            else
                return map.size();
        }
    }

    @Subscribe
    public void getMessage(Events.SearchKeywordSearch searchKeywordSearch) {
        DataBaseHelper.getInstance(getActivity().getApplicationContext()).insertSearchHistory(searchKeywordSearch.getSearchKeywordSearch());
        keyword = searchKeywordSearch.getSearchKeywordSearch();
        loadData(Constants.LOAD_PAGE);
    }
}
