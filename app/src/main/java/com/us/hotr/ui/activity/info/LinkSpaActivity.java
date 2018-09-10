package com.us.hotr.ui.activity.info;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.MasseurExtraData;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.search.SearchSpaActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.ui.view.MassageView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;
import java.util.List;

public class LinkSpaActivity extends BaseActivity {
    private TextView tvCancel, tvSpaName, tvMassageNumber, tvConfirmConnect;
    private RecyclerView rvMassage;
    private LinearLayout llSpa;

    private MyAdapter mAdapter;
    private long spaId;

    @Override
    protected int getLayout() {
        return R.layout.activity_link_spa;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.connect_org);
        initStaticView();
        spaId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        loadData();

    }

    private void initStaticView(){
        tvCancel = (TextView) findViewById(R.id.tv_save);
        tvSpaName = (TextView) findViewById(R.id.tv_spa_name);
        tvMassageNumber = (TextView) findViewById(R.id.tv_massage_number);
        tvConfirmConnect = (TextView) findViewById(R.id.tv_confirm);
        rvMassage = (RecyclerView) findViewById(R.id.rv_massage);
        llSpa = (LinearLayout) findViewById(R.id.ll_spa);

        tvCancel.setText(R.string.cancel_connect);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(LinkSpaActivity.this);
                alertDialogBuilder.setMessage(getString(R.string.cancel_connect));
                alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                cancelConnect();
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
        });
        tvSpaName.setText(getIntent().getExtras().getString(Constants.PARAM_TITLE));
        tvConfirmConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSelectedMassage().size()== 0){
                    Tools.Toast(LinkSpaActivity.this, getString(R.string.choose_massage1));
                }else {
                    TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(LinkSpaActivity.this);
                    alertDialogBuilder.setMessage(getString(R.string.confirm_connnect));
                    alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    confirmConnect();
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
            }
        });
        rvMassage.setLayoutManager(new LinearLayoutManager(this));
        rvMassage.setItemAnimator(new DefaultItemAnimator());

        llSpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LinkSpaActivity.this, SearchSpaActivity.class);
                Bundle b = new Bundle();
                b.putBoolean(Constants.PARAM_DATA, true);
                i.putExtras(b);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            tvSpaName.setText(data.getExtras().getString(Constants.PARAM_TITLE));
            spaId = data.getExtras().getLong(Constants.PARAM_ID);
            loadData();
        }
    }

    private void loadData(){
        tvMassageNumber.setText(String.format(getString(R.string.choose_massage_number), 0));
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Massage>>>() {
            @Override
            public void onNext(BaseListResponse<List<Massage>> result) {
                if(mAdapter == null)
                    mAdapter = new MyAdapter(result.getRows());
                else
                    mAdapter.setItems(result.getRows());
                rvMassage.setAdapter(mAdapter);
                if(getIntent().getExtras().getString(Constants.PARAM_DATA)!=null
                        && spaId == getIntent().getExtras().getLong(Constants.PARAM_ID))
                    mAdapter.setSelectedMassage(getIntent().getExtras().getString(Constants.PARAM_DATA));
            }
        };
        ServiceClient.getInstance().getMassageListBySpa(new ProgressSubscriber(mListener, this),
                spaId, 0l, 100, 1);
    }

    private void cancelConnect(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                setResult(SearchSpaActivity.CHANGE_SPA);
                finish();
            }
        };
        ServiceClient.getInstance().disconnectSpa(new ProgressSubscriber(mListener, LinkSpaActivity.this),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    private void confirmConnect(){
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    setResult(SearchSpaActivity.CHANGE_SPA);
                    finish();
                }
            };
            ServiceClient.getInstance().connectSpa(new ProgressSubscriber(mListener, LinkSpaActivity.this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(),
                    spaId,
                    mAdapter.getSelectedMassage());
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Massage> massageList;
        public List<Boolean> checkList = new ArrayList<>();
        private boolean isEdit = false;

        public List<Long> getSelectedMassage() {
            List<Long> result = new ArrayList<>();
            for (int i = 0; i < massageList.size(); i++) {
                if (checkList.get(i))
                    result.add(massageList.get(i).getKey());
            }
            return result;
        }

        public void setSelectedMassage(String ids){
            String[] idList = ids.split(",");
            for(int i = 0; i < massageList.size(); i++){
                for(int j=0;j<idList.length;j++){
                    checkList.set(i, false);
                    if(massageList.get(i).getKey()==Long.parseLong(idList[j])){
                        checkList.set(i, true);
                        break;
                    }
                }
            }
            notifyDataSetChanged();
            updateMassageNumber();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            MassageView massageView;
            public MyViewHolder(View view) {
                super(view);
                massageView = (MassageView) view;
            }
        }

        public void setEnableEdit(boolean enable) {
            isEdit = enable;
            notifyDataSetChanged();
        }

        public MyAdapter(List<Massage> massageList) {
            this.massageList = massageList;
            for(int i=0;i<massageList.size();i++)
                checkList.add(false);
            updateMassageNumber();
        }

        public void addItems(List<Massage> massageList){
            this.massageList.addAll(massageList);
            for(int i=0;i<massageList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
            updateMassageNumber();
        }

        public void setItems(List<Massage> massageList) {
            this.massageList = massageList;
            checkList = new ArrayList<>();
            for(int i=0;i<massageList.size();i++)
                checkList.add(false);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_massage, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            final Massage massage = massageList.get(position);
            holder.massageView.setData(massage, -1);
            holder.massageView.enableEdit(isEdit);
            if(checkList.get(position))
                holder.massageView.setGoButtonResource(R.mipmap.ic_massage_clicked);
            else
                holder.massageView.setGoButtonResource(R.mipmap.ic_massage_click);
            holder.massageView.setGoButtonLisenter(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkList.get(position) == true){
                        holder.massageView.setGoButtonResource(R.mipmap.ic_massage_click);
                        checkList.set(position, false);
                    }else{
                        holder.massageView.setGoButtonResource(R.mipmap.ic_massage_clicked);
                        checkList.set(position, true);
                    }

                    updateMassageNumber();
                }
            });
        }

        private void updateMassageNumber(){
            int count = 0;
            for(boolean b:checkList){
                if(b)
                    count++;
            }
            tvMassageNumber.setText(String.format(getString(R.string.choose_massage_number), count));
        }

        @Override
        public int getItemCount() {
            if (massageList == null)
                return 0;
            return massageList.size();
        }
    }
}
