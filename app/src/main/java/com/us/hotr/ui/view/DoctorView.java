package com.us.hotr.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.ItemSelectedListener;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.ui.activity.beauty.DoctorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaobo on 2017/12/27.
 */

public class DoctorView extends FrameLayout {
    private TextView tvName, tvTitle1, tvTitle2, tvAppointment, tvCase;
    private ImageView ivAvatar, ivDelete;
    private FlowLayout flSubject;
    private View vDivider;

    private ItemSelectedListener itemSelectedListener;
    private Doctor doctor;

    public DoctorView(Context context) {
        super(context);
        init();
    }

    public DoctorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_doctor, this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTitle1 = (TextView) findViewById(R.id.tv_title1);
        tvTitle2 = (TextView) findViewById(R.id.tv_title2);
        tvAppointment = (TextView) findViewById(R.id.tv_appointment);
        tvCase = (TextView) findViewById(R.id.tv_case);
        ivAvatar = (ImageView) findViewById(R.id.img_avator);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        flSubject = (FlowLayout) findViewById(R.id.fl_subject);
        vDivider = findViewById(R.id.v_divider);

        flSubject.setHorizontalSpacing(6);
        flSubject.setTextPaddingH(14);
    }

    public void setData(final Doctor doctor){
        this.doctor = doctor;
        tvName.setText(doctor.getDoctor_name());
        tvTitle1.setText(doctor.getDoctor_job());
        tvTitle2.setText(doctor.getHospital_name());
        tvAppointment.setText(String.format(getContext().getString(R.string.num_of_appointment), doctor.getOrder_num()));
        tvCase.setText(String.format(getContext().getString(R.string.num_of_case), 345) );
        Glide.with(getContext()).load(doctor.getDoctor_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
        List<String> subjects = new ArrayList<>();
        if(doctor.getGood_at_project_list()!=null && doctor.getGood_at_project_list().size()>0)
            for(Doctor.Subject s:doctor.getGood_at_project_list())
                subjects.add(s.getType_name());
        flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content, int position) {

            }
        });

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DoctorActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_ID, doctor.getDoctor_id());
                i.putExtras(b);
                getContext().startActivity(i);
            }
        });
    }

    public void enableEdit(boolean isEdit){
        if (isEdit) {
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setImageResource(R.mipmap.ic_delete_order);
            setTag(false);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((boolean) view.getTag()) {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order);
                        view.setTag(false);
                    } else {
                        ivDelete.setImageResource(R.mipmap.ic_delete_order_clicked);
                        view.setTag(true);
                    }
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected((boolean)view.getTag());
                }
            });
        } else {
            ivDelete.setVisibility(View.GONE);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), DoctorActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_ID, doctor.getDoctor_id());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
        }
    }

    public void enableSelect(boolean isSelect){
        if(isSelect){
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemSelectedListener!=null)
                        itemSelectedListener.onItemSelected(true);
                }
            });
        }else{
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), DoctorActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_ID, doctor.getDoctor_id());
                    i.putExtras(b);
                    getContext().startActivity(i);
                }
            });
        }
    }

    public void showDivider(boolean show){
        if(show)
            vDivider.setVisibility(View.VISIBLE);
        else
            vDivider.setVisibility(View.GONE);
    }

    public void setItemSelectedListener(ItemSelectedListener listener){
        this.itemSelectedListener = listener;
    }

}
