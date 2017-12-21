package com.us.hotr.ui.activity.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemDecorationAlbumColumns;
import com.us.hotr.customview.ScrollThroughRecyclerView;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.beauty.CompareActivity;
import com.us.hotr.util.Tools;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.Random;

public class PartyActivity extends BaseActivity{

    String url = "http://us-shop-2.oss-cn-beijing.aliyuncs.com/images/temp/video/pmf.mp4";
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageView ivBack, ivShare, ivFav, ivBackHome;
    private TextView tvPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("");
        initStaticView();
    }

    private void initStaticView(){

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ivBack = (ImageView) findViewById(R.id.img_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivFav = (ImageView) findViewById(R.id.iv_fav);
        ivBackHome = (ImageView) findViewById(R.id.iv_homepage);
        tvPurchase = (TextView) findViewById(R.id.tv_purchase);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

        ivBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PartyActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tvPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PartyActivity.this, PartyPayNumberActivity.class));
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = getScollYDistance();
                if (offset > 300) {
                    ivBack.setImageResource(R.mipmap.ic_back);
                    ivShare.setImageResource(R.mipmap.ic_share);
                } else {
                    findViewById(R.id.toolbar).getBackground().setAlpha((int) offset * 255 / 300);
                    findViewById(R.id.tb_title).setAlpha(offset / 300);
                    findViewById(R.id.v_divider).setAlpha(offset / 300);
                    ivBack.setImageResource(R.mipmap.ic_back_dark);
                    ivShare.setImageResource(R.mipmap.ic_share_dark);
                }
            }
        });
    }

    public int getScollYDistance() {
        int position = mLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = mLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_party;
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int postColumn;
        int postPhontoCount;

        public static final int VIEW_TYPE_POST = 100;
        public static final int VIEW_TYPE_HEADER = 101;

        private boolean isEdit = false;

        public class MyPostHolder extends RecyclerView.ViewHolder {
            ScrollThroughRecyclerView recyclerView;
            ImageView ivDelete;
            TextView tvTitle;

            public MyPostHolder(View view) {
                super(view);
                recyclerView = (ScrollThroughRecyclerView) view.findViewById(R.id.recyclerview);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public class MyHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivCity;
            TextView tvTitle, tvPeople, tvPrice, tvIntro, tvSeeAll, tvShare;
            ConstraintLayout clShare;
            RecyclerView recyclerView;
            NiceVideoPlayer vvVideo;
            public TxVideoPlayerController mController;

            public MyHeaderHolder(View view) {
                super(view);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                ivCity = (ImageView) view.findViewById(R.id.iv_city);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvPeople = (TextView) view.findViewById(R.id.tv_people);
                tvPrice = (TextView) view.findViewById(R.id.tv_price);
                tvIntro = (TextView) view.findViewById(R.id.tv_intro);
                tvSeeAll = (TextView) view.findViewById(R.id.tv_see_more);
                tvShare = (TextView) view.findViewById(R.id.tv_share);
                clShare = (ConstraintLayout) view.findViewById(R.id.cl_post_title);
                vvVideo = (NiceVideoPlayer) view.findViewById(R.id.vv_video);
            }

            public void setController(TxVideoPlayerController controller) {
                mController = controller;
                vvVideo.setController(mController);
                vvVideo.setPlayerType(NiceVideoPlayer.TYPE_NATIVE);
            }
        }

        public MyAdapter() {
            Random rand = new Random();
            postPhontoCount = rand.nextInt(9) + 1;
            if (postPhontoCount == 1)
                postColumn = 1;
            else if (postPhontoCount == 2 || postPhontoCount == 4)
                postColumn = 2;
            else
                postColumn = 3;
        }

        public void setEnableEdit(boolean enable) {
            isEdit = enable;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_POST:
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_post, parent, false);
                    MyPostHolder holder = new MyPostHolder(itemView);
                    holder.recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(12, postColumn));
                    return holder;
                case VIEW_TYPE_HEADER:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_party_header, parent, false);
                    MyHeaderHolder headerHolder = new MyHeaderHolder(view);
                    TxVideoPlayerController controller = new TxVideoPlayerController(PartyActivity.this);
                    headerHolder.setController(controller);
                    return headerHolder;
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_POST:
                    MyPostHolder postHolder = (MyPostHolder) holder;
                    postHolder.recyclerView.setLayoutManager(new GridLayoutManager(PartyActivity.this, postColumn));
                    PicGridAdapter adapter = new PicGridAdapter(postPhontoCount, isEdit);
                    postHolder.recyclerView.setAdapter(adapter);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(PartyActivity.this, CompareActivity.class);
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_POST);
                            startActivity(i);
                        }
                    });

                    if (isEdit) {
                        postHolder.ivDelete.setVisibility(View.VISIBLE);
                        postHolder.ivDelete.setImageResource(R.mipmap.ic_delete_order);
                        postHolder.ivDelete.setTag(false);
                    } else
                        postHolder.ivDelete.setVisibility(View.GONE);

                    postHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if ((boolean) view.getTag()) {
                                ((ImageView) view).setImageResource(R.mipmap.ic_delete_order);
                                view.setTag(false);
                            } else {
                                ((ImageView) view).setImageResource(R.mipmap.ic_delete_order_clicked);
                                view.setTag(true);
                            }
                        }
                    });
                    break;
                case VIEW_TYPE_HEADER:
                    final MyHeaderHolder headerHolder = (MyHeaderHolder) holder;
                    headerHolder.recyclerView.setLayoutManager(new LinearLayoutManager(PartyActivity.this));
                    headerHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                    MyAddressAdapter mAdapter = new MyAddressAdapter();
                    headerHolder.recyclerView.setAdapter(mAdapter);

                    final String mData = "这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。这是一个医院。";
                    final String displayData = mData.substring(0, 50) + "...";
                    headerHolder.tvIntro.setText(displayData);
                    headerHolder.tvSeeAll.setTag(false);
                    headerHolder.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!(boolean) view.getTag()) {
                                headerHolder.tvIntro.setText(mData);
                                view.setTag(true);
                                headerHolder.tvSeeAll.setText(R.string.see_part);
                            } else {
                                headerHolder.tvIntro.setText(displayData);
                                view.setTag(false);
                                headerHolder.tvSeeAll.setText(R.string.see_all);
                            }

                        }
                    });
                    headerHolder.mController.imageView().setImageResource(R.drawable.holder_city);
                    headerHolder.vvVideo.setUp(url, null);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return VIEW_TYPE_HEADER;
            }else{
                return VIEW_TYPE_POST;
            }
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
            if (isEdit)
                imageWidth = (int) (Tools.getScreenWidth(PartyActivity.this) - Tools.dpToPx(PartyActivity.this, 51));
            else
                imageWidth = Tools.getScreenWidth(PartyActivity.this);
            LinearLayout.LayoutParams layoutParams;
            switch (picCount) {
                case 1:
                    h = (int) (imageWidth * 0.46);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                    holder.mImageView.setLayoutParams(layoutParams);
                    holder.mImageView.setImageResource(R.drawable.placeholder_post1);
                    break;
                case 2:
                case 4:
                    h = (int) (imageWidth * 0.46);
                    layoutParams = new LinearLayout.LayoutParams(h, h);
                    holder.mImageView.setLayoutParams(layoutParams);
                    holder.mImageView.setImageResource(R.drawable.placeholder_post_2);
                    break;
                default:
                    h = (int) (imageWidth * 0.301);
                    layoutParams = new LinearLayout.LayoutParams(h, h);
                    holder.mImageView.setLayoutParams(layoutParams);
                    holder.mImageView.setImageResource(R.drawable.placeholder_post_2);
            }
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(PartyActivity.this, ImageViewerActivity.class));
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

    public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvAddress, tvDate;

            public MyViewHolder(View view) {
                super(view);
                tvAddress = (TextView) view.findViewById(R.id.tv_address);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
            }
        }

        public MyAddressAdapter() {
        }

        @Override
        public MyAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_party_address, parent, false);

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
