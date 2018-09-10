package com.us.hotr.ui.activity.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.SearchView;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.ui.activity.info.LinkSpaActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.List;

public class SearchSpaActivity extends AppCompatActivity implements SearchView.SearchViewListener {
    public static final int CHANGE_SPA = 100;
    private SearchView mSearchview;
    private RecyclerView rvList;
    private TextView tvEmpty;
    private RelativeLayout rlReject;
    private ImageView ivReject;
    private boolean isSelectSpa = false;

    private String keyword = "";
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_spa);
        if(getIntent().getExtras()!=null)
            isSelectSpa = getIntent().getExtras().getBoolean(Constants.PARAM_DATA, false);
        initStaticView();
        loadData();
    }

    private void initStaticView(){
        mSearchview = (SearchView) findViewById(R.id.search_view);
        rvList = (RecyclerView) findViewById(R.id.rv_list);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        rlReject = (RelativeLayout) findViewById(R.id.rl_reject);
        ivReject = (ImageView) findViewById(R.id.iv_reject);

        ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlReject.setVisibility(View.GONE);
            }
        });
        mSearchview.setSearchViewListener(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setItemAnimator(new DefaultItemAnimator());

        if(HOTRSharePreference.getInstance(getApplicationContext()).getMasseurInfo().getMassagistState() == 3 && !isSelectSpa){
            rlReject.setVisibility(View.VISIBLE);
        }
    }

    private void loadData(){
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Spa>>>() {
            @Override
            public void onNext(BaseListResponse<List<Spa>> result) {
                if(result.getTotal() == 0)
                    tvEmpty.setVisibility(View.VISIBLE);
                else {
                    tvEmpty.setVisibility(View.GONE);
                    if (mAdapter == null)
                        mAdapter = new MyAdapter(result.getRows());
                    else
                        mAdapter.setItems(result.getRows());
                    rvList.setAdapter(mAdapter);
                }
            }
        };
        ServiceClient.getInstance().getSpaList(new ProgressSubscriber(mListener, SearchSpaActivity.this),
                keyword.isEmpty()?null:keyword, null, null, null,
                HOTRSharePreference.getInstance(getApplicationContext()).getLatitude(),
                HOTRSharePreference.getInstance(getApplicationContext()).getLongitude(),  999, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CHANGE_SPA)
            setResult(resultCode);
        finish();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Spa> spaList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;
            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
            }
        }

        public MyAdapter(List<Spa> spaList) {
            this.spaList = spaList;
        }

        public void setItems(List<Spa> SpaList){
            this.spaList = SpaList;
            notifyDataSetChanged();
        }

        public void addItems(List<Spa> SpaList){
            for(Spa s:SpaList)
                this.spaList.add(s);
            notifyDataSetChanged();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa_name, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Spa spa = spaList.get(position);
            holder.tvName.setText(spa.getMassageName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSelectSpa){
                        Intent i = new Intent();
                        Bundle b = new Bundle();
                        b.putString(Constants.PARAM_TITLE, spa.getMassageName());
                        b.putLong(Constants.PARAM_ID, spa.getKey());
                        i.putExtras(b);
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }else {
                        Intent i = new Intent(SearchSpaActivity.this, LinkSpaActivity.class);
                        Bundle b = new Bundle();
                        b.putString(Constants.PARAM_TITLE, spa.getMassageName());
                        b.putLong(Constants.PARAM_ID, spa.getKey());
                        i.putExtras(b);
                        startActivityForResult(i, 0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(spaList == null)
                return 0;
            return spaList.size();
        }
    }

    @Override
    public void onRefreshAutoComplete(String text) {

    }

    @Override
    public void onSearch(String text) {
        if(text.isEmpty())
            Tools.Toast(this, getString(R.string.search_keyword));
        else {
            keyword = text;
            loadData();
        }
    }

    @Override
    public void onCancel() {
        finish();
    }

    @Override
    public void onSearchEmpty() {

    }
}
