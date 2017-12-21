package com.us.hotr.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.util.Tools;

import java.util.List;

/**
 * Created by liaobo on 2017/12/5.
 */

public class SelectTypeFragment extends Fragment{
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private LinearLayout llContainer;
    private int type;
    private boolean isProduct = false;

    public static final String PARAM_IS_PRODUCT = "PARAM_IS_PRODUCT";

    public static SelectTypeFragment newInstance(int type, boolean isProduct) {
        SelectTypeFragment selectTypeFragment = new SelectTypeFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_TYPE, type);
        b.putBoolean(PARAM_IS_PRODUCT, isProduct);
        selectTypeFragment.setArguments(b);
        return selectTypeFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt(Constants.PARAM_TYPE);
        isProduct = getArguments().getBoolean(PARAM_IS_PRODUCT);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        llContainer.setBackgroundResource(R.color.dim_bg);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(isProduct)
            myAdapter = new MyAdapter(Tools.getProductTypes(type, getContext()));
        else
            myAdapter = new MyAdapter(Tools.getTypes(type, getContext()));
        recyclerView.setAdapter(myAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Type> typeList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvSubject;

            public MyViewHolder(View view) {
                super(view);
                tvSubject = (TextView) view.findViewById(R.id.tv_subject);
            }
        }

        public MyAdapter(List<Type> typeList) {
            this.typeList = typeList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_massage_subject, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Type type = typeList.get(position);
            holder.tvSubject.setText(type.getTypeName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Events.TypeSelected typeSelected = new Events.TypeSelected(type.getTypeId(), type.getTypeName());
                    GlobalBus.getBus().post(typeSelected);

                }
            });
        }

        @Override
        public int getItemCount() {
            return typeList.size();
        }
    }
}
