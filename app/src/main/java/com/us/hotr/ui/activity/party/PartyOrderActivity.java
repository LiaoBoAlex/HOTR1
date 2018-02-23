package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Address;
import com.us.hotr.storage.bean.Contact;
import com.us.hotr.storage.bean.Party;
import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.Ticket;
import com.us.hotr.ui.activity.AvailableVoucherActivity;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.PayOrderActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.CreatePartyOrderRequest;
import com.us.hotr.webservice.response.GetPartyDetailResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/10/23.
 */

public class PartyOrderActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView tvVoucher, tvTitle, tvDate, tvTotal, tvGetterName, tvGetterAddress, tvBuyerName, tvBuyerAddress, tvConfirm, tvBuyerTitle, tvGetterTitle;
    private ConstraintLayout clGetterTitle, clGetterInfo, clBuyerTitle, clBuyerInfo, clvoucher;
    private ImageView ivAvatar;
    private GetPartyDetailResponse data;
    private double total;
    private CreatePartyOrderRequest request;
    private Contact mContact = new Contact();
    private boolean isBuyerInfoSelected = false, isGetterInfoSelected = false;
    @Override
    protected int getLayout() {
        return R.layout.activity_party_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.confirm_order_title);
        data = (GetPartyDetailResponse) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        total = getIntent().getExtras().getDouble(Constants.PARAM_ID);
        request = new CreatePartyOrderRequest();
        request.setTravel_id(data.getTravel().getId());
        request.setPrice_without_coupon(total);
        request.setActual_payable(total);
        int count = 0;
        List<Ticket> tickets = new ArrayList<>();
        for(Ticket t:data.getTicket()){
            if(t.getCount()>0){
                Ticket ticket = new Ticket();
                ticket.setId(t.getId());
                ticket.setCount(t.getCount());
                tickets.add(ticket);
                count = count + t.getCount();
            }
        }
        String s = new Gson().toJson(tickets);
        s = s.replace("\"", "'");
        request.setTicketStr(s);
        request.setTicket_count(count);
        request.setPurchaser_credentials(data.getTravel().getIdType());
        initStaticView();
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        clGetterTitle = (ConstraintLayout) findViewById(R.id.cl_getter_title);
        clGetterInfo = (ConstraintLayout) findViewById(R.id.cl_getter_info);
        clBuyerTitle = (ConstraintLayout) findViewById(R.id.cl_buyer_title);
        clBuyerInfo = (ConstraintLayout) findViewById(R.id.cl_buyer_info);
        clvoucher = (ConstraintLayout) findViewById(R.id.cl_voucher);
        tvVoucher = (TextView) findViewById(R.id.tv_voucher);
        tvGetterName = (TextView) findViewById(R.id.tv_getter_name);
        tvGetterTitle = (TextView) findViewById(R.id.tv_getter_title);
        tvBuyerTitle = (TextView) findViewById(R.id.tv_buyer_title);
        tvGetterAddress = (TextView) findViewById(R.id.tv_getter_address);
        tvBuyerName = (TextView) findViewById(R.id.tv_buyer_name);
        tvBuyerAddress = (TextView) findViewById(R.id.tv_buyer_address);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTotal = (TextView) findViewById(R.id.tv_price);

        Glide.with(PartyOrderActivity.this).load(data.getTravel().getPartyDetailImg()).error(R.drawable.holder_party_order)
                .placeholder(R.drawable.holder_party_order).into(ivAvatar);
        tvTitle.setText(String.format(getString(R.string.buy_ticket), data.getTravel().getTravel_name()));
        if(data.getTravel().getTravel_end_time()!=null)
            tvDate.setText(Tools.getPartyTime2(PartyOrderActivity.this, data.getTravel().getTravel_start_time(), data.getTravel().getTravel_end_time()));
        else
            tvDate.setText(Tools.getPartyTime2(PartyOrderActivity.this, data.getTravel().getTravel_start_time()));
        tvTotal.setText(new DecimalFormat("0.00").format(total));

        if(data.getTravel().getIdType() == Constants.NO_ID_REQUIRED)
            clBuyerTitle.setVisibility(View.GONE);
        clvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PartyOrderActivity.this, AvailableVoucherActivity.class));
            }
        });
        clBuyerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PartyOrderActivity.this, BuyerInfoActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TYPE, data.getTravel().getIdType());
                b.putSerializable(Constants.PARAM_DATA, mContact);
                i.putExtras(b);
                startActivityForResult(i, 1);
            }
        });
        clGetterTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PartyOrderActivity.this, DeliverAddressListActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TYPE, DeliverAddressListActivity.TYPE_SELECT_ADDRESS);
                i.putExtras(b);
                startActivityForResult(i, 0);
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseCheckCount();
            }
        });

        setAddress(HOTRSharePreference.getInstance(getApplicationContext()).getDefaultAddress());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Ticket> ticketList = new ArrayList<>();
        for(Ticket t:data.getTicket())
            if(t.getCount()>0)
                ticketList.add(t);
        mAdapter = new MyAdapter(ticketList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setAddress(Address address){
        if(address!=null){
            isGetterInfoSelected = true;
            tvGetterTitle.setVisibility(View.INVISIBLE);
            clGetterInfo.setVisibility(View.VISIBLE);
            tvGetterName.setText(address.getPersonName());
            tvGetterAddress.setText(address.getProvinceName()+address.getCityName()+address.getStreetName()+address.getDetailAddr());
            request.setAddress(address.getProvinceName()+address.getCityName()+address.getStreetName()+address.getDetailAddr());
            request.setAddressee_email(address.getUserEmail());
            request.setAddressee_name(address.getPersonName());
            request.setAddressee_phone(address.getTelephone());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (resultCode == RESULT_OK) {
                setAddress((Address) data.getExtras().getSerializable(Constants.PARAM_DATA));
            }
        }
        if(requestCode == 1){

            if(resultCode == 100 || resultCode == 101) {
                mContact = (Contact) data.getExtras().getSerializable(Constants.PARAM_DATA);
            }
            if (resultCode == 100) {
                Contact contact = (Contact) data.getExtras().getSerializable(Constants.PARAM_DATA);
                clBuyerInfo.setVisibility(View.VISIBLE);
                tvBuyerName.setText(contact.getPurchaser_name());
                tvBuyerAddress.setText(String.format(getString(R.string.buyer_info1), contact.getPurchaser_credentials_numb(), contact.getPurchaser_phone()));
                request.setPurchaser_credentials_numb(contact.getPurchaser_credentials_numb());
                request.setPurchaser_name(contact.getPurchaser_name());
                request.setPurchaser_phone(contact.getPurchaser_phone());
                request.setTerm_of_validity(contact.getValideDate());
                request.setGender(contact.getGender());
                isBuyerInfoSelected = true;
                tvBuyerTitle.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void purchaseCheckCount(){
        if(!isGetterInfoSelected)
            Tools.Toast(this, getString(R.string.add_getter_info));
        if(!isBuyerInfoSelected && data.getTravel().getIdType()!=0)
            Tools.Toast(this, getString(R.string.add_buyer_info));
        else {
            SubscriberListener mListener = new SubscriberListener<Boolean>() {
                @Override
                public void onNext(Boolean result) {
                    if (!result) {
                        Tools.Toast(PartyOrderActivity.this, getString(R.string.insufficient_ticket));
                        finish();
                    } else {
                        createOrder();
                    }
                }
            };
            ServiceClient.getInstance().checkPartyOrderCount(new ProgressSubscriber(mListener, this), data.getTicket());
        }
    }

    private void createOrder(){
        SubscriberListener mListener = new SubscriberListener<PartyOrder>() {
            @Override
            public void onNext(PartyOrder result) {
                startActivity(new Intent(PartyOrderActivity.this, PayOrderActivity.class));
            }
        };
        ServiceClient.getInstance().createOrderParty(new ProgressSubscriber(mListener, this),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), request);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Ticket> ticketList;

        public MyAdapter(List<Ticket> ticketList){
            this.ticketList = ticketList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvTotal, tvAmount;
            public MyViewHolder(View view){
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvAmount = (TextView) view.findViewById(R.id.tv_amount);
                tvTotal = (TextView) view.findViewById(R.id.tv_price);
            }
        }

        public MyAdapter() {

        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_party_order, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Ticket ticket = ticketList.get(position);
            holder.tvTitle.setText(ticket.getTicketName());
            holder.tvAmount.setText("x"+ticket.getCount());
            holder.tvTotal.setText(getString(R.string.money)+new DecimalFormat("0.00").format(ticket.getCount()*ticket.getTicketPrice()));
        }

        @Override
        public int getItemCount() {
            if(ticketList == null)
                return 0;
            else
                return ticketList.size();
        }
    }
}
