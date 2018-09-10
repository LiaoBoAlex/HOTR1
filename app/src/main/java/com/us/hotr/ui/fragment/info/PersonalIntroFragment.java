package com.us.hotr.ui.fragment.info;

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
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.MasseurExtraData;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.ui.view.GroupView;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetGroupListbyUserResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

/**
 * Created by Mloong on 2017/9/22.
 */

public class PersonalIntroFragment  extends BaseLoadingFragment{
    private RecyclerView lrecyclerView;
    private MyAdapter mAdapter;

    private User mUser;
    private MasseurExtraData masseurExtraData;

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
        mUser = (User)getArguments().getSerializable(Constants.PARAM_DATA);
        masseurExtraData = HOTRSharePreference.getInstance(getActivity()).getMasseurInfo();
        lrecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        lrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lrecyclerView.setItemAnimator(new DefaultItemAnimator());

        enablePullDownRefresh(false);
        enableLoadMore(false);
        loadData(Constants.LOAD_PAGE);

    }

    public void updateUserInfo(User user){
        mUser = user;
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener;
        mListener = new SubscriberListener<GetGroupListbyUserResponse>() {
            @Override
            public void onNext(GetGroupListbyUserResponse result) {
                mAdapter = new MyAdapter(result.getMyCoshow());
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                lrecyclerView.setAdapter(myBaseAdapter);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getGroupListByUser(new LoadingSubscriber(mListener, (BaseLoadingActivity)getActivity()),
                    mUser.getUserId());
        else if (loadType == Constants.LOAD_DIALOG)
            ServiceClient.getInstance().getGroupListByUser(new ProgressSubscriber(mListener, getContext()),
                    mUser.getUserId());
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getGroupListByUser(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                    mUser.getUserId());
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int VIEW_TYPE_HEADER =100;
        public static final int VIEW_TYPE_GROUP = 101;
        public static final int VIEW_TYPE_GROUP_HEADER = 102;
        public static final int VIEW_TYPE_NO_GROUP = 103;
        public static final int VIEW_TYPE_MASSEUR_HEADER =104;

        private List<Group> groupList;

        public MyAdapter(List<Group> groupList){
            this.groupList = groupList;
        }

        public class HeaderHolder extends RecyclerView.ViewHolder {
            TextView tvGender, tvAge, tvArea, tvIntro;
            public HeaderHolder(View view) {
                super(view);
                tvGender = (TextView) view.findViewById(R.id.tv_gender);
                tvAge = (TextView) view.findViewById(R.id.tv_worktime);
                tvArea = (TextView) view.findViewById(R.id.tv_subjects);
                tvIntro = (TextView) view.findViewById(R.id.tv_intro);
            }
        }

        public class MasseurHeaderHolder extends RecyclerView.ViewHolder {
            TextView tvGender, tvWorkTime, tvHeight, tvSubjects, tvIntro;
            public MasseurHeaderHolder(View view) {
                super(view);
                tvGender = (TextView) view.findViewById(R.id.tv_gender);
                tvWorkTime = (TextView) view.findViewById(R.id.tv_worktime);
                tvHeight = (TextView) view.findViewById(R.id.tv_height);
                tvIntro = (TextView) view.findViewById(R.id.tv_intro);
                tvSubjects = (TextView) view.findViewById(R.id.tv_subjects);
            }
        }

        public class GroupHolder extends RecyclerView.ViewHolder {
            GroupView groupView;
            public GroupHolder(View view) {
                super(view);
                groupView = (GroupView) view;
            }
        }

        public class NoGroupHolder extends RecyclerView.ViewHolder {
            public NoGroupHolder(View view) {
                super(view);
            }
        }

        public class GroupHeaderHolder extends RecyclerView.ViewHolder {
            public GroupHeaderHolder(View view) {
                super(view);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_personal_intro_header, parent, false);
                    return new HeaderHolder(view);
                case VIEW_TYPE_MASSEUR_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_masseur_intro_header, parent, false);
                    return new MasseurHeaderHolder(view);
                case VIEW_TYPE_GROUP:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group1, parent, false);
                    return new GroupHolder(view);
                case VIEW_TYPE_GROUP_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_header, parent, false);
                    return new GroupHeaderHolder(view);
                case VIEW_TYPE_NO_GROUP:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_group, parent, false);
                    return new NoGroupHolder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_HEADER:
                    HeaderHolder headerHolder = (HeaderHolder) holder;
                    if(mUser.getGender()!=null)
                        headerHolder.tvGender.setText(getResources().getStringArray(R.array.gender)[mUser.getGender()]);
                    if(mUser.getAge()!=null)
                        headerHolder.tvAge.setText(String.format(getString(R.string.age_number), mUser.getAge()));
                    if(mUser.getProvince_name()!=null && mUser.getCity_name()!=null)
                        headerHolder.tvArea.setText(mUser.getProvince_name() + " " + mUser.getCity_name());
                    if(mUser.getSignature()!=null)
                        headerHolder.tvIntro.setText(mUser.getSignature());
                    break;
                case VIEW_TYPE_MASSEUR_HEADER:
                    MasseurHeaderHolder masseurHeaderHolder = (MasseurHeaderHolder) holder;
                    if(masseurExtraData!=null) {
                        masseurHeaderHolder.tvGender.setText(getString(R.string.male));
                        if (masseurExtraData.getJobStartTime() != null)
                            masseurHeaderHolder.tvWorkTime.setText(masseurExtraData.getJobStartTime());
                        masseurHeaderHolder.tvHeight.setText(masseurExtraData.getMassagistHeight() + "cm");
                        if (masseurExtraData.getSubjectName() != null)
                            masseurHeaderHolder.tvSubjects.setText(masseurExtraData.getSubjectName());
                        if (masseurExtraData.getMassagistInfo() != null)
                            masseurHeaderHolder.tvIntro.setText(masseurExtraData.getMassagistInfo());
                    }
                    break;
                case VIEW_TYPE_GROUP:
                    final Group group = groupList.get(position-2);
                    GroupHolder groupHolder = (GroupHolder)holder;
                    groupHolder.groupView.setData(group);
                    groupHolder.groupView.enableEdit(false);
                    break;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                if(mUser.getUser_typ() == Constants.USER_TYPE_MASSEUR)
                    return VIEW_TYPE_MASSEUR_HEADER;
                else
                    return VIEW_TYPE_HEADER;
            }
            else if(groupList!=null && groupList.size()>0) {
                if (position == 1) {
                    return VIEW_TYPE_GROUP_HEADER;
                }
                else
                    return VIEW_TYPE_GROUP;
            }else
                return VIEW_TYPE_NO_GROUP;


        }

        @Override
        public int getItemCount() {
            if(groupList == null)
                return 2;
            else
                return 2 + groupList.size();
        }
    }
}
