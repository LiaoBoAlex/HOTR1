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

import com.us.hotr.Data;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.activity.search.SearchHintActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Mloong on 2017/8/29.
 */

public class HintSearchFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private HintAdapter mAdapte;
    private LinearLayoutManager mLayoutManager;

    public static HintSearchFragment newInstance() {
        HintSearchFragment hintSearchFragment = new HintSearchFragment();
        return hintSearchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hint_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public class HintAdapter extends RecyclerView.Adapter<HintAdapter.MyViewHolder> {

        private List<String> hintList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvHint;

            public MyViewHolder(View view) {
                super(view);
                tvHint = (TextView) view.findViewById(R.id.tv_hint);
            }
        }

        public HintAdapter(List<String> hintList) {
            this.hintList = hintList;
        }

        public void setHintList(List<String> hintList){
            this.hintList = hintList;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search_hint, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final String hint = hintList.get(position);
            holder.tvHint.setText(hint);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SearchHintActivity)getActivity()).setSearchText(hint);
                }
            });

        }

        @Override
        public int getItemCount() {
            if(hintList == null)
                return 0;
            return hintList.size();
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
    public void getMessage(Events.SearchKeywordHint searchKeywordHint) {
        mAdapte = new HintAdapter(Data.getSearchHint());
        mRecyclerView.setAdapter(mAdapte);

    }
}
