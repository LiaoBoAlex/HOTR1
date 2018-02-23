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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Address;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.util.List;

/**
 * Created by Mloong on 2017/10/23.
 */

public class DeliverAddressListActivity extends BaseLoadingActivity {
    public static final int TYPE_SHOW_LIST = 1;
    public static final int TYPE_SELECT_ADDRESS = 2;
    private RecyclerView recyclerView;
    private ConstraintLayout clEmptyView;
    private MyAdapter mAdapter;
    private TextView tvAdd;

    private int type;

    @Override
    protected int getLayout() {
        return R.layout.activity_deliver_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        if(type == TYPE_SHOW_LIST)
            setMyTitle(R.string.manage_deliver_address);
        if(type == TYPE_SELECT_ADDRESS)
            setMyTitle(R.string.select_deliver_address);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<List<Address>>() {
            @Override
            public void onNext(List<Address> listBaseListResponse) {
                if(listBaseListResponse!=null && listBaseListResponse.size()>0){
                    clEmptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    mAdapter = new MyAdapter(listBaseListResponse);
                    recyclerView.setAdapter(mAdapter);
                }else {
                    clEmptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        };
        if (type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getDeliveryAddressList(new LoadingSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        if (type == Constants.LOAD_DIALOG)
            ServiceClient.getInstance().getDeliveryAddressList(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getDeliveryAddressList(new SilentSubscriber(mListener, this, refreshLayout),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    private void initStaticView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvAdd = (TextView) findViewById(R.id.tv_add_address);
        clEmptyView = (ConstraintLayout) findViewById(R.id.container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeliverAddressListActivity.this, EditDeliverAddressActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TITLE, R.string.add_address);
                i.putExtras(b);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            loadData(Constants.LOAD_DIALOG);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<Address> addressList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDefault;
            TextView tvName, tvPhone, tvAddress, tvDefault, tvEdit,tvDelete;
            public MyViewHolder(View view){
                super(view);
                ivDefault = (ImageView) view.findViewById(R.id.iv_default);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvPhone = (TextView) view.findViewById(R.id.tv_phone);
                tvAddress = (TextView) view.findViewById(R.id.tv_address);
                tvDefault = (TextView) view.findViewById(R.id.tv_default);
                tvEdit = (TextView) view.findViewById(R.id.tv_edit);
                tvDelete = (TextView) view.findViewById(R.id.tv_delete);
            }
        }

        public MyAdapter(List<Address> addressList) {
            this.addressList = addressList;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_address, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Address address = addressList.get(position);
            holder.tvName.setText(address.getPersonName());
            holder.tvAddress.setText(address.getProvinceName()
                    +address.getCityName()
                    +address.getStreetName()
                    +address.getDetailAddr());
            holder.tvPhone.setText(address.getTelephone());
            if(address.getDefaultAddress() == 1){
                holder.tvDefault.setText(R.string.default_address);
                holder.tvDefault.setTextColor(getResources().getColor(R.color.cyan));
                holder.ivDefault.setImageResource(R.mipmap.ic_address_clicked);
            }else{
                holder.tvDefault.setText(R.string.set_default);
                holder.tvDefault.setTextColor(getResources().getColor(R.color.text_black));
                holder.ivDefault.setImageResource(R.mipmap.ic_address_click);
            }
            holder.ivDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubscriberListener mListener = new SubscriberListener<String>() {
                        @Override
                        public void onNext(String result) {
                            for(int i=0;i<addressList.size();i++){
                                if(i==position)
                                    addressList.get(i).setDefaultAddress(1);
                                else
                                    addressList.get(i).setDefaultAddress(0);
                            }
                            notifyDataSetChanged();
                            HOTRSharePreference.getInstance(getApplicationContext()).storeDefaultAddress(address);
                        }
                    };
                    ServiceClient.getInstance().setDefaultDeliveryAddress(new ProgressSubscriber(mListener, DeliverAddressListActivity.this),
                            HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), address.getId());
                }
            });
            holder.tvDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubscriberListener mListener = new SubscriberListener<String>() {
                        @Override
                        public void onNext(String result) {
                            for(int i=0;i<addressList.size();i++){
                                if(i==position)
                                    addressList.get(i).setDefaultAddress(1);
                                else
                                    addressList.get(i).setDefaultAddress(0);
                            }
                            notifyDataSetChanged();
                            HOTRSharePreference.getInstance(getApplicationContext()).storeDefaultAddress(address);
                        }
                    };
                    ServiceClient.getInstance().setDefaultDeliveryAddress(new ProgressSubscriber(mListener, DeliverAddressListActivity.this),
                            HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), address.getId());
                }
            });
            holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DeliverAddressListActivity.this, EditDeliverAddressActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, address);
                    b.putInt(Constants.PARAM_TITLE, R.string.edit_address);
                    i.putExtras(b);
                    startActivityForResult(i, 0);
                }
            });
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubscriberListener mListener = new SubscriberListener<String>() {
                        @Override
                        public void onNext(String result) {
                            addressList.remove(position);
                            notifyItemRemoved(position);
                            if(address.getDefaultAddress() == 1)
                                HOTRSharePreference.getInstance(getApplicationContext()).storeDefaultAddress(null);
                        }
                    };
                    ServiceClient.getInstance().deleteDeliveryAddress(new ProgressSubscriber(mListener, DeliverAddressListActivity.this),
                            HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), address.getId());
                }
            });
            if(type == TYPE_SELECT_ADDRESS){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.PARAM_DATA, address);
                        i.putExtras(b);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if(addressList == null)
                return 0;
            else
                return addressList.size();
        }
    }
}
