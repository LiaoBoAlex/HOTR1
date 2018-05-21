package com.us.hotr.ui.fragment.massage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.customview.BlankRecyclerView;
import com.us.hotr.customview.CloseFragmentListener;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

/**
 * Created by Mloong on 2017/9/30.
 */

public class SelectMassageSubjectFragment extends BaseLoadingFragment {
    private BlankRecyclerView recyclerView;
    private MyAdapter myAdapter;
    private LinearLayout llContainer;
    private CloseFragmentListener listener;

    public static SelectMassageSubjectFragment newInstance() {
        SelectMassageSubjectFragment selectMassageSubjectFragment = new SelectMassageSubjectFragment();
        return selectMassageSubjectFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CloseFragmentListener) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (BlankRecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setBackground(null);
        llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        llContainer.setBackgroundResource(R.color.dim_bg);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadData(0);
        recyclerView.setBlankListener(new BlankRecyclerView.BlankListener() {
            @Override
            public void onBlankClick() {
                listener.onFragmentClose();
            }
        });

    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Subject>>>() {
            @Override
            public void onNext(BaseListResponse<List<Subject>> result) {
                if(isAdded()) {
                    if (result != null && result.getRows() != null && result.getRows().size() > 0) {
                        Subject subject = new Subject();
                        subject.setTypeName(getString(R.string.filter_subject));
                        subject.setKey(-1);
                        result.getRows().add(0, subject);
                        myAdapter = new MyAdapter(result.getRows());
                        recyclerView.setAdapter(myAdapter);
                    }
                }
            }
        };
        ServiceClient.getInstance().getMassageTypeList(new LoadingSubscriber(mListener, this));
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Subject> subjectList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvSubject;

            public MyViewHolder(View view) {
                super(view);
                tvSubject = (TextView) view.findViewById(R.id.tv_subject);
            }
        }

        public MyAdapter(List<Subject> subjectList) {
            this.subjectList = subjectList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_massage_subject, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Subject subject = subjectList.get(position);
            holder.tvSubject.setText(subject.getTypeName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Events.SubjectSelected subjectSelected = new Events.SubjectSelected(subject.getTypeName(), 0, subject.getKey());
                        GlobalBus.getBus().post(subjectSelected);

                }
            });
        }

        @Override
        public int getItemCount() {
            if(subjectList==null)
                return 0;
            else
                return subjectList.size();
        }
    }
}
