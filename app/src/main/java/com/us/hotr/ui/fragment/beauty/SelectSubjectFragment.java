package com.us.hotr.ui.fragment.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/4.
 */

public class SelectSubjectFragment extends BaseLoadingFragment {
    RecyclerView rvTitle, rvSubTitle;
    TitleAdapter titleAdapter;
    SubTitleAdapter subTitleAdapter;

    private int row_index = 0;

    private static final String PARAM = "PARAM";
    private boolean haveAll = true;

    public static SelectSubjectFragment newInstance(boolean haveAll) {
        SelectSubjectFragment seleteSubjectFragment = new SelectSubjectFragment();
        Bundle b = new Bundle();
        b.putBoolean(PARAM, haveAll);
        seleteSubjectFragment.setArguments(b);
        return seleteSubjectFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_subject, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null)
            haveAll = getArguments().getBoolean(PARAM, true);

        enableLoadMore(false);
        enablePullDownRefresh(false);
        rvTitle = (RecyclerView) view.findViewById(R.id.rv_title);
        rvSubTitle = (RecyclerView) view.findViewById(R.id.rv_sub_title);

        rvTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTitle.setItemAnimator(new DefaultItemAnimator());
        rvSubTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSubTitle.setItemAnimator(new DefaultItemAnimator());

        loadData(0);
    }

    @Override
    protected void loadData(int type){
        SubscriberListener mListener = new SubscriberListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> result) {
                if (result != null && result.size() > 0) {
                    if (haveAll) {
                        Subject s = new Subject();
                        s.setTypeName(getString(R.string.filter_subject));
                        result.add(0, s);
                    }
                    titleAdapter = new TitleAdapter(result);
                    rvTitle.setAdapter(titleAdapter);
                    if (!haveAll) {
                        subTitleAdapter = new SubTitleAdapter(result.get(0).getChildren());
                        rvSubTitle.setAdapter(subTitleAdapter);
                    }
                }
            }
        };
        ServiceClient.getInstance().getSubjectList(new LoadingSubscriber(mListener, this));
    }

    @Subscribe
    public void getMessage(Events.CitySelected citySelectedEvent) {
    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
    }

    @Subscribe
    public void getMessage(Events.TypeSelected typeSelected) {
    }

    public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.MyViewHolder> {

        private List<Subject> subjectList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            View vIndicator;

            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                vIndicator = view.findViewById(R.id.v_indicator);
            }
        }


        public TitleAdapter(List<Subject> subjectList) {
            this.subjectList = subjectList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_subject_left, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Subject subject = subjectList.get(position);
            holder.tvTitle.setText(subject.getTypeName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getString(R.string.filter_subject).equals(subject.getTypeName())){
                        Events.SubjectSelected event = new Events.SubjectSelected(getString(R.string.filter_subject),
                                -1);
                        GlobalBus.getBus().post(event);
                    }
                    row_index = position;
                    notifyDataSetChanged();
                    if(subTitleAdapter!=null)
                        subTitleAdapter.setItem(subject.getChildren());
                    else {
                        subTitleAdapter = new SubTitleAdapter(subject.getChildren());
                        rvSubTitle.setAdapter(subTitleAdapter);
                    }
                }
            });
            if (row_index == position) {
                holder.tvTitle.setBackgroundResource(R.color.white);
                holder.vIndicator.setVisibility(View.VISIBLE);
            } else {
                holder.tvTitle.setBackgroundResource(R.color.bg_grey);
                holder.vIndicator.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            if(subjectList == null)
                return 0;
            else
                return  subjectList.size();
        }
    }

    public class SubTitleAdapter extends RecyclerView.Adapter<SubTitleAdapter.MyViewHolder> {

        private List<Subject.SubjectL2> subjectL2List = new ArrayList<>();

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvSubTitle;
            FlowLayout flSubSubTitle;

            public MyViewHolder(View view) {
                super(view);
                tvSubTitle = (TextView) view.findViewById(R.id.tv_title);
                flSubSubTitle = (FlowLayout) view.findViewById(R.id.fl_subtitle);
            }
        }

        public SubTitleAdapter(List<Subject.SubjectL2> subjectL2List) {
            this.subjectL2List = subjectL2List;
        }

        public void setItem(List<Subject.SubjectL2> subjectL2List) {
            this.subjectL2List = subjectL2List;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_subject_catalog, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Subject.SubjectL2 subjectL2 = subjectL2List.get(position);
            holder.tvSubTitle.setText(subjectL2.getTypeName());
            List<String> nameList = new ArrayList<>();
            if(subjectL2.getProductChild()!=null && subjectL2.getProductChild().size()>0) {

                for (Subject.SubjectL2.SubjectL3 s : subjectL2.getProductChild())
                    nameList.add(s.getProjectName());
            }
            holder.flSubSubTitle.setFlowLayout(nameList, new FlowLayout.OnItemClickListener() {
                @Override
                public void onItemClick(String content, int position) {
                    Events.SubjectSelected event = new Events.SubjectSelected(subjectL2.getProductChild().get(position).getProjectName(),
                            subjectL2.getProductChild().get(position).getKey());
                    GlobalBus.getBus().post(event);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(subjectL2List == null)
                return 0;
            return subjectL2List.size();
        }
    }
}
