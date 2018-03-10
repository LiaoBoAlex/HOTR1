package com.us.hotr.ui.activity.info;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.Ticket;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.PayOrderActivity;
import com.us.hotr.ui.activity.party.PartyActivity;
import com.us.hotr.ui.activity.receipt.ReceiptDetailActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.PermissionUtil;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetPartyOrderDetailResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Mloong on 2017/10/24.
 */

public class PartyOrderDetailActivity extends BaseLoadingActivity {
    private long orderId;
    private TextView tvTitle, tvSubTitle, tvPrice, tvVoucher, tvAmount, tvPayNow, tvPayNowMoney, tvPayLater,
            tvOrderId, tvPayId, tvPayMethod, tvTime, tvPhone, tvBuyAgain, tvBuyNow, tvGetterName, tvGetterEmail, tvGetterPhone,
            tvGetterAddress, tvBuyerName, tvBuyerAddress, tvNotice;
    private ImageView ivAvatar;
    private RecyclerView recyclerview;
    private LinearLayout llNotice;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.order_detail);
        orderId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int loadType) {
        SubscriberListener mListener = new SubscriberListener<GetPartyOrderDetailResponse>() {
            @Override
            public void onNext(GetPartyOrderDetailResponse result) {
                updateData(result);
            }
        };
        if (loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getPartyOrderDetail(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), orderId);
        else if (loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getPartyOrderDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), orderId);
    }

    private void updateData(final GetPartyOrderDetailResponse result){
        Glide.with(this).load(result.getOrder().getTravel_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(ivAvatar);
        tvTitle.setText(result.getOrder().getTravel_name());
        if(result.getOrder().getTravel_end_time()!=null)
            tvSubTitle.setText(Tools.getPartyTime2(this, result.getOrder().getTravel_start_time(), result.getOrder().getTravel_end_time()));
        else
            tvSubTitle.setText(Tools.getPartyTime2(this, result.getOrder().getTravel_start_time()));
        tvPrice.setText(getString(R.string.money) + new DecimalFormat("0.00").format(result.getOrder().getRel_order_price()));
        tvVoucher.setText(String.format(getString(R.string.deduct_amount), new DecimalFormat("0.00").format(0)));
        tvAmount.setText(String.format(getString(R.string.pay_number1), result.getOrder().getTotal_number()));
        tvPayNowMoney.setText(getString(R.string.money) + new DecimalFormat("0.00").format(result.getOrder().getRel_order_price()));
        tvPayNow.setText(R.string.total_price1);
        tvPayLater.setVisibility(View.GONE);
        tvGetterName.setText(getString(R.string.buyer1)+result.getOrder().getAddressee_name());
        tvGetterPhone.setText(getString(R.string.phone_number1)+result.getOrder().getAddressee_phone());
        tvGetterAddress.setText(getString(R.string.delivery_address) + getString(R.string.colon) + result.getOrder().getAddress());
        tvGetterEmail.setText(getString(R.string.email) + getString(R.string.colon) + result.getOrder().getAddressee_email());
        tvBuyerName.setText(result.getOrder().getPurchaser_name());
        tvBuyerAddress.setText(String.format(getString(R.string.buyer_info1), result.getOrder().getPurchaser_credentials_numb(), result.getOrder().getPurchaser_phone()));
        if(result.getOrder().getTake_ticket_notice()!= null && !result.getOrder().getTake_ticket_notice().isEmpty())
            tvNotice.setText(result.getOrder().getTake_ticket_notice());
        else
            llNotice.setVisibility(View.GONE);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter(result.getDetail());
        recyclerview.setAdapter(mAdapter);
        tvOrderId.setText(String.format(getString(R.string.order_id), result.getOrder().getOrder_code()));
        if(result.getOrder().getOrder_state() == 1){
            tvPayId.setVisibility(View.VISIBLE);
            tvPayMethod.setVisibility(View.VISIBLE);
            if(result.getPayment().getPayment_type() == 0){
                tvPayId.setText(String.format(getString(R.string.wechat_pay_id), result.getPayment().getPayment_sn()));
                tvPayMethod.setText(R.string.pay_by_wechat);
            }
            if(result.getPayment().getPayment_type() == 1){
                tvPayId.setText(String.format(getString(R.string.alipay_pay_id), result.getPayment().getPayment_sn()));
                tvPayMethod.setText(R.string.pay_by_alipay);
            }
            tvTime.setText(String.format(getString(R.string.order_pay_time), result.getPayment().getPay_time()));
        }else{
            tvPayId.setVisibility(View.GONE);
            tvPayMethod.setVisibility(View.GONE);
            tvTime.setText(String.format(getString(R.string.order_create_time), result.getOrder().getCreate_time()));
        }
        tvPhone.setText(Constants.SUPPORT_LINE);
        if(result.getOrder().getOrder_state() == 1){
            tvBuyAgain.setVisibility(View.VISIBLE);
            tvBuyNow.setVisibility(View.GONE);
            tvBuyAgain.setVisibility(View.GONE);
//            tvBuyAgain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(PartyOrderDetailActivity.this, PartyActivity.class);
//                    Bundle b = new Bundle();
//                    b.putLong(Constants.PARAM_ID, result.getOrder().getTravel_id());
//                    i.putExtras(b);
//                    startActivity(i);
//                }
//            });
//            tvBuyNow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(PartyOrderDetailActivity.this, ReceiptDetailActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable(Constants.PARAM_DATA, result.getOrder().getId());
//                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
//                    i.putExtras(b);
//                    startActivity(i);
//                }
//            });
        }else{
            tvBuyAgain.setVisibility(View.GONE);
            tvBuyNow.setText(R.string.pay);
            tvBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(PartyOrderDetailActivity.this, PayOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, result.getOrder());
                    b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PARTY);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtil.hasCallPermission(PartyOrderDetailActivity.this)) {
                    callPhoneNumber();
                } else {
                    PermissionUtil.requestCallPermission(PartyOrderDetailActivity.this);
                }
            }
        });


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_party_order_detail;
    }

    private void initStaticView(){
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSubTitle = (TextView) findViewById(R.id.tv_date);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvVoucher = (TextView) findViewById(R.id.tv_voucher);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvPayNow = (TextView) findViewById(R.id.tv_pay_now);
        tvPayNowMoney = (TextView) findViewById(R.id.tv_pay_now_amount);
        tvPayLater = (TextView) findViewById(R.id.tv_pay_later);
        tvOrderId = (TextView) findViewById(R.id.tv_order_id);
        tvPayId = (TextView) findViewById(R.id.tv_pay_id);
        tvPayMethod = (TextView) findViewById(R.id.tv_pay_method);
        tvTime = (TextView) findViewById(R.id.tv_pay_time);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvBuyAgain = (TextView) findViewById(R.id.tv_buy_again);
        tvBuyNow = (TextView) findViewById(R.id.tv_buy_now);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        tvGetterName = (TextView) findViewById(R.id.tv_getter_name);
        tvGetterEmail = (TextView) findViewById(R.id.tv_getter_email);
        tvGetterPhone = (TextView) findViewById(R.id.tv_getter_phone);
        tvGetterAddress = (TextView) findViewById(R.id.tv_getter_address);
        tvBuyerName = (TextView) findViewById(R.id.tv_buyer_name);
        tvBuyerAddress = (TextView) findViewById(R.id.tv_buyer_address);
        llNotice = (LinearLayout) findViewById(R.id.ll_notice);
        tvNotice = (TextView) findViewById(R.id.tv_notice);


        enableLoadMore(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PermissionUtil.PERMISSIONS_REQUEST_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void callPhoneNumber()
    {
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(Constants.SUPPORT_LINE);
        alertDialogBuilder.setPositiveButton(getString(R.string.call),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Constants.SUPPORT_LINE));
                        startActivity(callIntent);
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create().show();
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
                tvAmount = (TextView) view.findViewById(R.id.tv_number);
                tvTotal = (TextView) view.findViewById(R.id.tv_amount);
            }
        }

        public MyAdapter() {

        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_party_order, parent, false);
            return new MyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
            final Ticket ticket = ticketList.get(position);
            holder.tvTitle.setText(ticket.getTicketName());
            holder.tvAmount.setText("x"+ticket.getCount());
            holder.tvTotal.setText(getString(R.string.money)+new DecimalFormat("0.00").format(ticket.getSum_price()));
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
