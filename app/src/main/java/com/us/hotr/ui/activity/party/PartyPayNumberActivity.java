package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Ticket;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetPartyDetailResponse;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;

/**
 * Created by Mloong on 2017/10/23.
 */

public class PartyPayNumberActivity extends BaseActivity {

    private TextView tvAmount, tvConfirm;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    private GetPartyDetailResponse data;
    private double total = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_party_pay_number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.choose_ticket);
        data = (GetPartyDetailResponse) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        initStaticView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void initStaticView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    protected void loadData() {
        SubscriberListener mListener = new SubscriberListener<GetPartyDetailResponse>() {
            @Override
            public void onNext(final GetPartyDetailResponse result) {
                if(data!=null){
                    for(int i=0;i<data.getTicket().size();i++) {
                        if(data.getTicket().get(i).getCount()<=result.getTicket().get(i).getOnhandInventory())
                            result.getTicket().get(i).setCount(data.getTicket().get(i).getCount());
                        else
                            result.getTicket().get(i).setCount(result.getTicket().get(i).getOnhandInventory());
                    }
                }
                data = result;
                mAdapter.notifyDataSetChanged();
            }
        };
        ServiceClient.getInstance().getPartyDetail(new SilentSubscriber(mListener, this, null),
                data.getTravel().getId(), HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int VIEW_TYPE_TICKET = 100;
        public static final int VIEW_TYPE_HEADER = 101;

        public class MyTicketHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvSubTitle, tvPrice, tvNumber, tvTicket;
            ImageView ivDeduct, ivAdd, ivTicket;

            public MyTicketHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
                tvPrice = (TextView) view.findViewById(R.id.tv_amount);
                tvNumber = (TextView) view.findViewById(R.id.tv_number);
                tvTicket = (TextView) view.findViewById(R.id.tv_ticket);
                ivDeduct = (ImageView) view.findViewById(R.id.iv_deduct);
                ivAdd = (ImageView) view.findViewById(R.id.iv_add);
                ivTicket = (ImageView) view.findViewById(R.id.iv_ticket);
            }
        }

        public class MyHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            TextView tvTitle, tvPeople, tvDate, tvStatus, tvNotice;
            LinearLayout llNotice;

            public MyHeaderHolder(View view) {
                super(view);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvPeople = (TextView) view.findViewById(R.id.tv_people);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvStatus = (TextView) view.findViewById(R.id.tv_status);
                tvNotice = (TextView) view.findViewById(R.id.tv_notice);
                llNotice = (LinearLayout) view.findViewById(R.id.ll_notice);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_TICKET:
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_party_ticket, parent, false);
                    MyTicketHolder holder = new MyTicketHolder(itemView);
                    return holder;
                case VIEW_TYPE_HEADER:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_party_order_header, parent, false);
                    MyHeaderHolder headerHolder = new MyHeaderHolder(view);
                    return headerHolder;
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_TICKET:
                    final int p = position - 1;
                    final MyTicketHolder ticketHolder = (MyTicketHolder) holder;
                    final Ticket ticket = data.getTicket().get(p);
                    ticketHolder.tvTitle.setText(ticket.getTicketName());
                    ticketHolder.tvSubTitle.setText(ticket.getGuideWord());
                    Glide.with(PartyPayNumberActivity.this).load(ticket.getTicketImg())
                            .error(R.drawable.holder_ticket).placeholder(R.drawable.holder_ticket).into(ticketHolder.ivTicket);
                    ticketHolder.tvPrice.setText(new DecimalFormat("0.00").format(ticket.getTicketPrice()));
                    ticketHolder.tvTicket.setText(String.format(getString(R.string.ticket_left), ticket.getOnhandInventory()));

                    ticketHolder.tvNumber.setText(ticket.getCount()+"");
                    if(ticket.getCount() == 0)
                        ticketHolder.ivDeduct.setImageResource(R.mipmap.ic_deduct_gray);
                    else
                        ticketHolder.ivDeduct.setImageResource(R.mipmap.ic_deduct);
                    if(ticket.getCount() == ticket.getOnhandInventory())
                        ticketHolder.ivAdd.setImageResource(R.mipmap.ic_plus);
                    else
                        ticketHolder.ivAdd.setImageResource(R.mipmap.ic_plus);
                    tvAmount.setText(getString(R.string.money)+calculateTotal());
                    if(total == 0){
                        tvConfirm.setBackgroundColor(getResources().getColor(R.color.bg_button_grey));
                        tvConfirm.setOnClickListener(null);
                    }else{
                        tvConfirm.setBackgroundColor(getResources().getColor(R.color.bg_button));
                        tvConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(PartyPayNumberActivity.this, PartyOrderActivity.class);
                                Bundle b = new Bundle();
                                b.putSerializable(Constants.PARAM_DATA, data);
                                b.putDouble(Constants.PARAM_ID, total);
                                i.putExtras(b);
                                startActivity(i);
                            }
                        });
                    }
                    ticketHolder.ivDeduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(ticket.getCount() > 0) {
                                ticket.setCount(ticket.getCount()-1);
//                                numbers.set(p, numbers.get(p) - 1);
                                notifyItemChanged(position);
                                tvAmount.setText(calculateTotal());
                            }
                        }
                    });

                    ticketHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(ticket.getCount()<ticket.getOnhandInventory()) {
                                ticket.setCount(ticket.getCount()+1);
//                                numbers.set(p, numbers.get(p) + 1);
                                notifyItemChanged(position);
                                tvAmount.setText(calculateTotal());
                            }
                        }
                    });
                    break;
                case VIEW_TYPE_HEADER:
                    final MyHeaderHolder headerHolder = (MyHeaderHolder) holder;
                    Glide.with(PartyPayNumberActivity.this).load(data.getTravel().getPartyDetailImg()).error(R.drawable.holder_party_order)
                            .placeholder(R.drawable.holder_party_order).into(headerHolder.ivAvatar);
                    headerHolder.tvTitle.setText(String.format(getString(R.string.buy_ticket), data.getTravel().getTravel_name()));
                    if(data.getTravel().getTravel_end_time()!=null)
                        headerHolder.tvDate.setText(Tools.getPartyTime2(PartyPayNumberActivity.this, data.getTravel().getTravel_start_time(), data.getTravel().getTravel_end_time()));
                    else
                        headerHolder.tvDate.setText(Tools.getPartyTime2(PartyPayNumberActivity.this, data.getTravel().getTravel_start_time()));
                    headerHolder.tvPeople.setText(String.format(getString(R.string.party_join_number), data.getTravel().getOrder_num()));
                    if(data.getTravel().getBuyTicketNotice()!=null)
                        headerHolder.tvNotice.setText(data.getTravel().getBuyTicketNotice());
                    else
                        headerHolder.llNotice.setVisibility(View.GONE);
                    headerHolder.tvStatus.setText(R.string.on_sale);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            if(data.getTicket()!=null)
                return data.getTicket().size() + 1;
            else
                return 1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return VIEW_TYPE_HEADER;
            }else{
                return VIEW_TYPE_TICKET;
            }
        }

        private String calculateTotal(){
            double t = 0;
            for(Ticket ticket:data.getTicket())
                t = t + ticket.getCount()*ticket.getTicketPrice();
            total = t;
            return new DecimalFormat("0.00").format(t);
        }
    }
}
