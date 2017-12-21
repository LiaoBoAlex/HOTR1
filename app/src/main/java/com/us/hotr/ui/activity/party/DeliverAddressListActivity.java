package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

/**
 * Created by Mloong on 2017/10/23.
 */

public class DeliverAddressListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private TextView tvAdd;

    @Override
    protected int getLayout() {
        return R.layout.activity_deliver_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.manage_deliver_address);
        initStaticView();
    }

    private void initStaticView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvAdd = (TextView) findViewById(R.id.tv_add_address);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeliverAddressListActivity.this, EditDeliverAddressActivity.class));
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imgBefore, imgAfter, ivDelete;
            public MyViewHolder(View view){
                super(view);
                imgBefore = (ImageView) view.findViewById(R.id.img_before);
                imgAfter = (ImageView) view.findViewById(R.id.imge_after);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            }
        }

        public MyAdapter() {

        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_address, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
