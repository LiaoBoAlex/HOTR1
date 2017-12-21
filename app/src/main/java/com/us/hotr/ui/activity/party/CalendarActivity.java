package com.us.hotr.ui.activity.party;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.customview.StartSnapHelper;
import com.us.hotr.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Mloong on 2017/10/17.
 */

public class CalendarActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private String[] months;
    private int currentMonth, currentYear, currentDay;

    @Override
    protected int getLayout() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.calendar);
        initStaticView();
    }

    private void initStaticView(){
        months = getResources().getStringArray(R.array.month);
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        currentMonth = cal.get(Calendar.MONTH);
        currentYear = cal.get(Calendar.YEAR);
        currentDay = cal.get(Calendar.DATE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        StartSnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {



        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvMonth;
            private RecyclerView mMonthRecyclerView;
            public MyViewHolder(View view){
                super(view);
                mMonthRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                tvMonth = (TextView) view.findViewById(R.id.tv_month);
            }
        }

        public MyAdapter() {
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_month, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            int month = (currentMonth+position)%12;
            int year = currentYear + (currentMonth+position)/12;
            if(year == currentYear)
                holder.tvMonth.setText(months[month]);
            else
                holder.tvMonth.setText(String.format(getString(R.string.date), year, month+1));
            holder.mMonthRecyclerView.setLayoutManager(new GridLayoutManager(CalendarActivity.this, 7, GridLayoutManager.VERTICAL, false));
            holder.mMonthRecyclerView.setItemAnimator(new DefaultItemAnimator());
            MonthAdapter mMonthAdapter = new MonthAdapter(month, year);
            holder.mMonthRecyclerView.setAdapter(mMonthAdapter);
        }

        @Override
        public int getItemCount() {
            return 12;
        }
    }

    public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyMonthViewHolder> {

        private int month, count, firstDay;

        public class MyMonthViewHolder extends RecyclerView.ViewHolder {
            private TextView tvDate;
            private ImageView ivDot;
            public MyMonthViewHolder(View view){
                super(view);
                ivDot = (ImageView) view.findViewById(R.id.iv_dot);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
            }
        }

        public MonthAdapter(int month, int year) {
            this.month = month;
            Calendar mycal = new GregorianCalendar(year, month, 1);
            int days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            firstDay = mycal.get(Calendar.DAY_OF_WEEK) -1;
            count = days + firstDay;
        }

        @Override
        public MyMonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_date, parent, false);

            return new MyMonthViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyMonthViewHolder holder, final int position) {

            if(position<firstDay){
                holder.ivDot.setVisibility(View.INVISIBLE);
                holder.tvDate.setVisibility(View.INVISIBLE);
            }else{
                int day = position - firstDay + 1;
                holder.tvDate.setText(day + "");
                if(month == currentMonth){
                    if(day<currentDay) {
                        holder.tvDate.setTextColor(getResources().getColor(R.color.text_grey));
                        holder.ivDot.setVisibility(View.INVISIBLE);
                    }
                    else if(day == currentDay){
                        holder.tvDate.setTextColor(getResources().getColor(R.color.text_black));
                        holder.ivDot.setVisibility(View.VISIBLE);
                    }else{
                        holder.tvDate.setTextColor(getResources().getColor(R.color.text_black));
                        holder.ivDot.setVisibility(View.INVISIBLE);
                    }
                }
                if(day == 8+month){
                    holder.tvDate.setBackgroundResource(R.drawable.green_dot);
                    holder.tvDate.setTextColor(getResources().getColor(R.color.white));
                    holder.ivDot.setImageResource(R.drawable.white_dot);
                }
            }
        }

        @Override
        public int getItemCount() {
            return count;

        }
    }
}
