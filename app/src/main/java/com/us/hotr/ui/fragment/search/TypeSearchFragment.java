package com.us.hotr.ui.fragment.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.Data;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.SearchTypeResult;
import com.us.hotr.storage.greendao.DataBaseHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Mloong on 2017/8/29.
 */

public class TypeSearchFragment extends Fragment {
    private TextView tvTotal;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;


    public static TypeSearchFragment newInstance() {
        TypeSearchFragment typeSearchFragment = new TypeSearchFragment();
        return typeSearchFragment;
    }

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
        tvTotal = (TextView) view.findViewById(R.id.tv_price);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        if(getArguments()!=null) {
            DataBaseHelper.getInstance(getActivity().getApplicationContext()).insertSearchHistory(getArguments().getString(Constants.PARAM_SEARCH_STRING));
            mAdapter = new MyAdapter(Data.getSearchTypeResult());
            mRecycleView.setAdapter(mAdapter);
            tvTotal.setText(String.format(getString(R.string.total), 1254));
        }

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<SearchTypeResult> resultList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvTotal;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTotal = (TextView) view.findViewById(R.id.tv_price);
            }
        }

        public MyAdapter(List<SearchTypeResult> resultList) {
            this.resultList = resultList;
        }

        public void setResultList(List<SearchTypeResult> resultList){
            this.resultList = resultList;
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
            final SearchTypeResult result = resultList.get(position);
            holder.tvTitle.setText(result.getTitle());
            holder.tvTotal.setText(String.format(getString(R.string.total2), result.getCount()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Events.SearchTypeChosen event = new Events.SearchTypeChosen(101 + position);
                    GlobalBus.getBus().post(event);
                }
            });

        }

        @Override
        public int getItemCount() {
            if(resultList == null)
                return 0;
            return resultList.size();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getMessage(Events.SearchKeywordSearch searchKeywordSearch) {
        DataBaseHelper.getInstance(getActivity().getApplicationContext()).insertSearchHistory(searchKeywordSearch.getSearchKeywordSearch());
        mAdapter = new MyAdapter(Data.getSearchTypeResult());
        mRecycleView.setAdapter(mAdapter);
        tvTotal.setText(String.format(getString(R.string.total), 1254));
    }
}
