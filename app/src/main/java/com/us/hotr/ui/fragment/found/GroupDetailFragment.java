package com.us.hotr.ui.fragment.found;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemDecorationAlbumColumns;
import com.us.hotr.customview.ScrollThroughRecyclerView;
import com.us.hotr.storage.bean.Compare;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.beauty.CompareActivity;
import com.us.hotr.util.Tools;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

/**
 * Created by Mloong on 2017/11/1.
 */

public class GroupDetailFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RefreshLayout refreshLayout;


    public static GroupDetailFragment newInstance() {
        GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
        return groupDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

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

    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int VIEW_TYPE_HEADER =100;
        public static final int VIEW_TYPE_POST = 101;

        int postColumn;
        int postPhontoCount;
        int count = 5;
        boolean haveHeader = true;

        public class PostHolder extends RecyclerView.ViewHolder {
            ScrollThroughRecyclerView recyclerView;
            ImageView ivDelete;
            TextView tvTitle;
            public PostHolder(View view){
                super(view);
                recyclerView = (ScrollThroughRecyclerView) view.findViewById(R.id.recyclerview);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public class HeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivClose;
            TextView tvContent, tvSeeMore;
            public HeaderHolder(View itemView) {
                super(itemView);
                ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvSeeMore = (TextView) itemView.findViewById(R.id.tv_see_more);
            }
        }

        public MyAdapter() {
            Random rand = new Random();
            postPhontoCount =  rand.nextInt(9) + 1;
            if(postPhontoCount==1)
                postColumn = 1;
            else if(postPhontoCount == 2 || postPhontoCount == 4)
                postColumn = 2;
            else
                postColumn = 3;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_group_head, parent, false);
                    return new HeaderHolder(view);
                case VIEW_TYPE_POST:
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_post, parent, false);
                    PostHolder holder = new PostHolder(itemView);
                    holder.recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(12, postColumn));
                    return holder;
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_HEADER:
                    final HeaderHolder headerHolder = (HeaderHolder) holder;
                    headerHolder.ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            haveHeader = false;
                            notifyItemRemoved(0);
                        }
                    });
                    final String mData = "这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。";
                    final String displayData = mData.substring(0, 50) + "...";
                    headerHolder.tvContent.setText(displayData);
                    headerHolder.tvSeeMore.setTag(false);
                    headerHolder.tvSeeMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!(boolean) view.getTag()) {
                                headerHolder.tvContent.setText(mData);
                                view.setTag(true);
                                headerHolder.tvSeeMore.setText(R.string.see_part);
                            } else {
                                headerHolder.tvContent.setText(displayData);
                                view.setTag(false);
                                headerHolder.tvSeeMore.setText(R.string.see_all);
                            }

                        }
                    });
                    break;
                case VIEW_TYPE_POST:
                    PostHolder postHolder = (PostHolder) holder;
                    postHolder.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), postColumn));
                    PicGridAdapter adapter = new PicGridAdapter(postPhontoCount, false);
                    postHolder.recyclerView.setAdapter(adapter);

                    postHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getActivity(), CompareActivity.class);
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_POST);
                            startActivity(i);
                        }
                    });

                    postHolder.ivDelete.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0 && haveHeader){
                return VIEW_TYPE_HEADER;
            }else{
                return VIEW_TYPE_POST;
            }
        }

        @Override
        public int getItemCount() {
            if(haveHeader)
                return count + 1;
            else
                return  count;
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
                imageWidth = Tools.getScreenWidth(getContext());
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
