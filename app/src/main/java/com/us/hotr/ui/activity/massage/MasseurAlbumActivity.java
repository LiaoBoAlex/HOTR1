package com.us.hotr.ui.activity.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

/**
 * Created by Mloong on 2017/10/9.
 */

public class MasseurAlbumActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.masseur_album);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_masseur_album;
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });


        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


        public ImageAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_image, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (position%3){
                case 0:
                    holder.mImageView.setImageResource(R.drawable.placeholder_ad3);
                    break;
                case 1:
                case 2:
                    holder.mImageView.setImageResource(R.drawable.placeholder_post3);
            }
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MasseurAlbumActivity.this, "pic Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return 20;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.imageview);
            }
        }
    }
}
