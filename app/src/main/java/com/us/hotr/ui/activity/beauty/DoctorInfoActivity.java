package com.us.hotr.ui.activity.beauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemDecorationAlbumColumns;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.ImageViewerActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/8.
 */

public class DoctorInfoActivity extends BaseActivity {

    private TextView tvName, tvTitle, tvSubject, tvIntro;
    private ConstraintLayout clSubject;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private Doctor mDoctor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.doctor_info);
        mDoctor = (Doctor)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_doctor_info;
    }

    private void initStaticView(){
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubject = (TextView) findViewById(R.id.tv_subject);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        clSubject = (ConstraintLayout) findViewById(R.id.cl_subject);

        tvName.setText(mDoctor.getDoctor_name());
        tvTitle.setText(mDoctor.getDoctor_job());
        if(mDoctor.getGood_at_project_list()!=null && mDoctor.getGood_at_project_list().size()>0) {
            String subject = "";
            for (Doctor.Subject s : mDoctor.getGood_at_project_list())
                subject = subject + " " + s.getType_name();
            tvSubject.setText(subject);
        }else
            clSubject.setVisibility(View.GONE);
        tvIntro.setText(mDoctor.getDoctor_info());


        recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(12, 3));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setFocusable(false);
        myAdapter = new MyAdapter(mDoctor.getDoctor_img());
        recyclerView.setAdapter(myAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mPhotoes = new ArrayList<>();
        public MyAdapter(List<String> mPhotoes) {
            this.mPhotoes = mPhotoes;
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_album, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(DoctorInfoActivity.this).load(mPhotoes.get(position)).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.mImageView);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(DoctorInfoActivity.this, ImageViewerActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, (Serializable)mPhotoes);
                    b.putInt(Constants.PARAM_ID, position);
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_DOCTOR);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mPhotoes == null)
                return 0;
            else
                return mPhotoes.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            }
        }
    }
}
