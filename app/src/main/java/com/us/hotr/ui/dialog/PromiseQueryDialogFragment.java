package com.us.hotr.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.webservice.response.GetProductDetailResponse;

import java.io.Serializable;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

/**
 * Created by liaobo on 2018/2/22.
 */

public class PromiseQueryDialogFragment extends BottomSheetDialogFragment {
    List<GetProductDetailResponse.Promise> promiseList;
    TextView tvDone;
    RecyclerView recyclerView;

    private BottomSheetBehavior mBehavior;
    public static PromiseQueryDialogFragment newInstance(List<GetProductDetailResponse.Promise> promiseList) {
        PromiseQueryDialogFragment promiseQueryDialogFragment = new PromiseQueryDialogFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, (Serializable)promiseList);
        promiseQueryDialogFragment.setArguments(b);
        return promiseQueryDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        promiseList = (List<GetProductDetailResponse.Promise>)getArguments().get(Constants.PARAM_DATA);
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_promise_query, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        tvDone = (TextView) view.findViewById(R.id.tv_done);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        return dialog;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mBehavior.setState(STATE_EXPANDED);
    }

    public void doclick(View v)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvContent;
            public MyViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
            }
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promise, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tvTitle.setText(promiseList.get(position).getPromise_title());
            holder.tvContent.setText(promiseList.get(position).getPromise_content());
        }

        @Override
        public int getItemCount() {
            if (promiseList==null)
                return 0;
            else
                return promiseList.size();
        }
    }
}
