package com.us.hotr.ui.fragment.info;

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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;

/**
 * Created by Mloong on 2017/9/22.
 */

public class PersonalIntroFragment  extends BaseLoadingFragment{
    private RecyclerView lrecyclerView;
    private RefreshLayout refreshLayout;
    private MyAdapter mAdapter;

    private User mUser;

    public static PersonalIntroFragment newInstance(User user) {
        PersonalIntroFragment personalIntroFragment = new PersonalIntroFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, user);
        personalIntroFragment.setArguments(b);
        return personalIntroFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = (User) getArguments().getSerializable(Constants.PARAM_DATA);

        lrecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        lrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lrecyclerView.setItemAnimator(new DefaultItemAnimator());


        enablePullDownRefresh(false);

        loadData(0);

    }

    @Override
    protected void loadData(int type) {
        mAdapter = new MyAdapter();
        lrecyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int VIEW_TYPE_HEADER =100;
        public static final int VIEW_TYPE_GROUP = 101;

        public class HeaderHolder extends RecyclerView.ViewHolder {
            TextView tvGender, tvAge, tvArea, tvIntro;
            public HeaderHolder(View view) {
                super(view);
                tvGender = (TextView) view.findViewById(R.id.tv_gender);
                tvAge = (TextView) view.findViewById(R.id.tv_age);
                tvArea = (TextView) view.findViewById(R.id.tv_area);
                tvIntro = (TextView) view.findViewById(R.id.tv_intro);
            }
        }
        public class GroupHolder extends RecyclerView.ViewHolder {
            public GroupHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_personal_intro_header, parent, false);
                    return new MyAdapter.HeaderHolder(view);
                case VIEW_TYPE_GROUP:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_personal_intro_header, parent, false);
                    return new MyAdapter.HeaderHolder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_HEADER:
                    HeaderHolder headerHolder = (HeaderHolder) holder;
                    headerHolder.tvGender.setText(getResources().getStringArray(R.array.gender)[mUser.getGender()]);
                    headerHolder.tvAge.setText(String.format(getString(R.string.age_number), mUser.getAge()));
                    headerHolder.tvArea.setText(mUser.getProvince_name() + mUser.getCity_name());
                    headerHolder.tvIntro.setText(mUser.getSignature());
                    break;
                case VIEW_TYPE_GROUP:
                    break;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return VIEW_TYPE_HEADER;
            }else{
                return VIEW_TYPE_GROUP;
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
