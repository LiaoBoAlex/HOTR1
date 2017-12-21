package com.us.hotr.ui.fragment.beauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemDecorationAlbumColumns;
import com.us.hotr.customview.ScrollThroughRecyclerView;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Compare;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.beauty.CompareActivity;
import com.us.hotr.util.Tools;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mloong on 2017/9/21.
 */

public class PostListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<Compare> compareList;
    private RefreshLayout refreshLayout;
    private boolean enableRefresh;

    public static PostListFragment newInstance(boolean enableRefresh) {
        PostListFragment postListFragment = new PostListFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_ENABLE_REFRESH, enableRefresh);
        postListFragment.setArguments(b);
        return postListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enableRefresh = getArguments().getBoolean(Constants.PARAM_ENABLE_REFRESH);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        compareList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Compare c = new Compare();
            compareList.add(c);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(compareList);
        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });

        if(enableRefresh){
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    refreshlayout.finishRefresh(2000);
                }
            });
        }else
            refreshLayout.setEnableRefresh(false);

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getMessage(Events.EnableEdit enableEdit) {
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_DONE)
            mAdapter.setEnableEdit(false);
        if(enableEdit.getEnableEdit() == Events.EnableEdit.ACTION_EDIT)
            mAdapter.setEnableEdit(true);
        mAdapter.notifyDataSetChanged();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Compare> compareList;
        int postColumn;
        int postPhontoCount;

        private boolean isEdit = false;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ScrollThroughRecyclerView recyclerView;
            ImageView ivDelete;
            TextView tvTitle;
            public MyViewHolder(View view){
                super(view);
                recyclerView = (ScrollThroughRecyclerView) view.findViewById(R.id.recyclerview);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public MyAdapter(List<Compare> compareList) {
            this.compareList = compareList;
            Random rand = new Random();
            postPhontoCount =  rand.nextInt(9) + 1;
            if(postPhontoCount==1)
                postColumn = 1;
            else if(postPhontoCount == 2 || postPhontoCount == 4)
                postColumn = 2;
            else
                postColumn = 3;
        }

        public void setItems(List<Compare> compareList){
            this.compareList = compareList;
            notifyDataSetChanged();
        }

        public void setEnableEdit(boolean enable){
            isEdit = enable;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false);
            MyViewHolder holder = new MyViewHolder(itemView);
            holder.recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(12, postColumn));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
            final Compare compare = compareList.get(position);
            holder.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), postColumn));
            PicGridAdapter adapter = new PicGridAdapter(postPhontoCount, isEdit);
            holder.recyclerView.setAdapter(adapter);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), CompareActivity.class);
                    i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_POST);
                    startActivity(i);
                }
            });

            if(isEdit) {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setImageResource(R.mipmap.ic_delete_order);
                holder.ivDelete.setTag(false);
            }else
                holder.ivDelete.setVisibility(View.GONE);

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if((boolean)view.getTag()){
                        ((ImageView)view).setImageResource(R.mipmap.ic_delete_order);
                        view.setTag(false);
                    }else{
                        ((ImageView)view).setImageResource(R.mipmap.ic_delete_order_clicked);
                        view.setTag(true);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(compareList == null)
                return 0;
            return compareList.size();
        }
    }

    public class PicGridAdapter extends RecyclerView.Adapter<PicGridAdapter.ViewHolder> {

        private int picCount;
        private boolean isEdit;

        public PicGridAdapter(int picCount, boolean isEdit) {
            this.picCount = picCount;
            this.isEdit = isEdit;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int h;
            int imageWidth;
            if(isEdit)
                imageWidth = (int)(Tools.getScreenWidth(getContext()) - Tools.dpToPx(getActivity(), 51));
            else
                imageWidth = Tools.getScreenWidth(getActivity().getApplicationContext());
            LinearLayout.LayoutParams layoutParams;
            switch (picCount){
                case 1:
                    h = (int)(imageWidth * 0.46);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                    holder.mImageView.setLayoutParams(layoutParams);
                    holder.mImageView.setImageResource(R.drawable.placeholder_post1);
                    break;
                case 2:
                case 4:
                    h = (int)(imageWidth*0.46);
                    layoutParams = new LinearLayout.LayoutParams(h, h);
                    holder.mImageView.setLayoutParams(layoutParams);
                    holder.mImageView.setImageResource(R.drawable.placeholder_post_2);
                    break;
                default:
                    h = (int)(imageWidth*0.301);
                    layoutParams = new LinearLayout.LayoutParams(h, h);
                    holder.mImageView.setLayoutParams(layoutParams);
                    holder.mImageView.setImageResource(R.drawable.placeholder_post_2);
            }
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), ImageViewerActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return picCount;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }
}
