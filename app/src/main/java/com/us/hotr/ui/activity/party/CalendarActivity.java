package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.StartSnapHelper;
import com.us.hotr.storage.bean.Party;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.beauty.ListActivity;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        loadData();
    }

    private void loadData(){
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Party>>>() {
            @Override
            public void onNext(BaseListResponse<List<Party>> result) {
                if(mAdapter==null) {
                    mAdapter = new MyAdapter(AttachToCalendar(result.getRows()));
                    mRecyclerView.setAdapter(mAdapter);
                }else
                    mAdapter.setData(AttachToCalendar(result.getRows()));
            }
        };
        ServiceClient.getInstance().getPartyList(new ProgressSubscriber(mListener,this),
                null, 1, null, null, 1);
    }

    private List<PartyCalendar> AttachToCalendar(List<Party> partyList){
        List<PartyCalendar> partyCalendarList = new ArrayList<>();
        try {
            for(Party p:partyList) {
                if(p.getTravel_start_time()!=null && p.getTravel_end_time()!=null) {
                    Calendar calStart = Calendar.getInstance();
                    calStart.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p.getTravel_start_time()));
                    Calendar calEnd = Calendar.getInstance();
                    calEnd.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p.getTravel_end_time()));
                    calStart.set(calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH), calStart.get(Calendar.DATE), 0, 0, 0);
                    calEnd.set(calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH), calEnd.get(Calendar.DATE), 23, 59, 59);
                    partyCalendarList.add(new PartyCalendar(calStart, calEnd, p));
                }

            }
            return partyCalendarList;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
        StartSnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mAdapter = new MyAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<PartyCalendar> partyCalendarList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvMonth;
            private RecyclerView mMonthRecyclerView;
            public MyViewHolder(View view){
                super(view);
                mMonthRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                tvMonth = (TextView) view.findViewById(R.id.tv_month);
            }
        }

        public MyAdapter(List<PartyCalendar> partyCalendarList) {
            this.partyCalendarList = partyCalendarList;
        }

        public void setData(List<PartyCalendar> partyCalendarList){
            this.partyCalendarList = partyCalendarList;
            notifyDataSetChanged();
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
            MonthAdapter mMonthAdapter = new MonthAdapter(month, year, partyCalendarList);
            if(holder.mMonthRecyclerView.getAdapter()==null)
                holder.mMonthRecyclerView.setAdapter(mMonthAdapter);
            else
                ((MonthAdapter)holder.mMonthRecyclerView.getAdapter()).setData(month, year, partyCalendarList);
        }

        @Override
        public int getItemCount() {
            return 12;
        }
    }

    public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyMonthViewHolder> {

        private int month, count, firstDay, year;
        private List<PartyCalendar> partyCalendarList;

        public class MyMonthViewHolder extends RecyclerView.ViewHolder {
            private TextView tvDate;
            private ImageView ivDot;
            public MyMonthViewHolder(View view){
                super(view);
                ivDot = (ImageView) view.findViewById(R.id.iv_dot);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
            }
        }

        public MonthAdapter(int month, int year, List<PartyCalendar> partyCalendarList) {
            this.month = month;
            this.year = year;
            Calendar mycal = new GregorianCalendar(year, month, 1);
            int days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            firstDay = mycal.get(Calendar.DAY_OF_WEEK) -1;
            count = days + firstDay;
            this.partyCalendarList = partyCalendarList;
        }

        public void setData(int month, int year, List<PartyCalendar> partyCalendarList){
            this.month = month;
            this.year = year;
            Calendar mycal = new GregorianCalendar(year, month, 1);
            int days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
            firstDay = mycal.get(Calendar.DAY_OF_WEEK) -1;
            count = days + firstDay;
            this.partyCalendarList = partyCalendarList;
            notifyDataSetChanged();
        }

        @Override
        public MyMonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_date, parent, false);

            return new MyMonthViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyMonthViewHolder holder, final int position) {
            final int day = position - firstDay + 1;
            if(position<firstDay){
                holder.ivDot.setVisibility(View.INVISIBLE);
                holder.tvDate.setVisibility(View.INVISIBLE);
            }else{
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
                }else{
                    holder.tvDate.setTextColor(getResources().getColor(R.color.text_black));
                    holder.ivDot.setVisibility(View.INVISIBLE);
                }
                holder.tvDate.setBackgroundResource(R.color.white);
                holder.ivDot.setImageResource(R.drawable.green_dot);
                Calendar thisDate = new GregorianCalendar(year, month, day, 1,0,0);
                if(partyCalendarList!=null) {
                    for (PartyCalendar p : partyCalendarList) {
                        if (thisDate.compareTo(p.getStartCalendar()) == 1 && thisDate.compareTo(p.getEndCalendar()) == -1) {
                            holder.tvDate.setBackgroundResource(R.drawable.green_dot);
                            holder.tvDate.setTextColor(getResources().getColor(R.color.white));
                            holder.ivDot.setImageResource(R.drawable.white_dot);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    List<Party> partyList = new ArrayList<>();
                                    Calendar thisDate = new GregorianCalendar(year, month, day, 1,0,0);
                                    for (PartyCalendar p : partyCalendarList) {
                                        if (thisDate.compareTo(p.getStartCalendar()) == 1 && thisDate.compareTo(p.getEndCalendar()) == -1) {
                                            partyList.add(p.getParty());
                                        }
                                    }
                                    Intent i = new Intent(CalendarActivity.this, ListActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable(Constants.PARAM_DATA, (Serializable) partyList);
                                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PARTY);
                                    b.putString(Constants.PARAM_TITLE, getString(R.string.party));
                                    i.putExtras(b);
                                    startActivity(i);
                                }
                            });
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return count;

        }
    }

    public class PartyCalendar{
        private Calendar startCalendar;
        private Calendar endCalendar;
        private Party party;

        public PartyCalendar(Calendar startCalendar, Calendar endCalendar, Party party){
            this.startCalendar = startCalendar;
            this.endCalendar = endCalendar;
            this.party = party;
        }

        public Calendar getStartCalendar() {
            return startCalendar;
        }

        public void setStartCalendar(Calendar startCalendar) {
            this.startCalendar = startCalendar;
        }

        public Calendar getEndCalendar() {
            return endCalendar;
        }

        public void setEndCalendar(Calendar endCalendar) {
            this.endCalendar = endCalendar;
        }

        public Party getParty() {
            return party;
        }

        public void setParty(Party party) {
            this.party = party;
        }
    }
}
