package com.us.hotr.ui.activity.beauty;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Compare;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.dialog.CommentDialogFragment;
import com.us.hotr.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/15.
 */

public class CompareActivity extends BaseActivity {

    private int PRODUCT_OFFSET;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView tvCommentHint;
    private ConstraintLayout clBanner;
    private RefreshLayout refreshLayout;

    private List<Compare> compareList;

    private int type;
    private boolean isBannerShown = false;
    private int offset = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.content_title);
        type = getIntent().getIntExtra(Constants.PARAM_TYPE, -1);
        PRODUCT_OFFSET = (int)Tools.dpToPx(this, 365);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_compare;
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tvCommentHint = (TextView) findViewById(R.id.tv_comment);
        clBanner = (ConstraintLayout) findViewById(R.id.cl_banner);
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

        tvCommentHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialogFragment.Builder alertDialogBuilder = new CommentDialogFragment.Builder(CompareActivity.this);
                alertDialogBuilder.setPositiveButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.create().show();
            }
        });

        compareList = new ArrayList<>();
        Compare c = new Compare();
        for(int i=0;i<10;i++)
            compareList.add(c);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyAdapter(compareList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offset = offset + dy;
                if(offset >= PRODUCT_OFFSET && !isBannerShown)
                    showBanner(true);
                if(offset < PRODUCT_OFFSET && isBannerShown)
                    showBanner(false);
            }
        });
    }

    private void showBanner(boolean isShow){
        if(type != Constants.TYPE_CASE)
            return;
        if(isShow) {
            isBannerShown = true;
            Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_menu_in);
            animIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            clBanner.setVisibility(View.VISIBLE);
            clBanner.startAnimation(animIn);
        }
        else {
            isBannerShown = false;
            Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_menu_out);
            animOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clBanner.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            clBanner.startAnimation(animOut);
        }
        clBanner.setAlpha(0.94F);
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int TYPE_HEADER_CASE = 101;
        private final int TYPE_HEADER_POST = 103;
        private final int TYPE_HEADER_INTERVIEW = 104;
        private final int TYPE_COMMENT = 102;

        private List<Compare> compareList;

        public class CompareHeaderHolder extends RecyclerView.ViewHolder {
            public CompareHeaderHolder(View view){
                super(view);
            }
        }

        public class PostHeaderHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerView;
            public PostHeaderHolder(View view){
                super(view);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            }
        }

        public class WebHolder extends RecyclerView.ViewHolder {
            public WebHolder(View itemView) {
                super(itemView);
            }
        }

        public class CommentHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerView;
            TextView tvContent;
            public CommentHolder(View view){
                super(view);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                tvContent = (TextView) view.findViewById(R.id.tv_content);
            }
        }


        public MyAdapter(List<Compare> compareList) {
            this.compareList = compareList;
        }

        public void setItems(List<Compare> compareList){
            this.compareList = compareList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_HEADER_CASE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_compare_header, parent, false);
                    return new CompareHeaderHolder(view);
                case TYPE_COMMENT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                    return new CommentHolder(view);
                case TYPE_HEADER_POST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_header, parent, false);
                    return new PostHeaderHolder(view);
                case TYPE_HEADER_INTERVIEW:
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    WebView web = new WebView(parent.getContext());
                    web.setLayoutParams(lp);
                    String url = "https://mp.weixin.qq.com/s?__biz=MjM5MDczMjM2MA==&mid=2652388593&idx=1&sn=7c08949e42d61de2f022ffcee552738a&chksm=bdacc5d68adb4cc0a038f92efca95acf17ef1facbc86553441e8f47dbb0316104b80b6379485&scene=0&key=bda634fb2c7300a3b6c583ff2fe7827c6ddb195c74ff4c744b765cd8a48e9f4e67360531050b8b7addce3f97d0b9440e5a0b6bc1948c635320267447fc4b8075deffdf61ea7ecc241bf9a9f120f378cd&ascene=0&uin=OTYwOTY3Njgw&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.6+build(15G31)&version=12010210&nettype=WIFI&fontScale=100&pass_ticket=kCRObwEpa%2BTF24xhAVuiq%2FBQ2Ki1t8IcSMer1q5hQg2vFO41c4RQRrTB236TDGFU";
                    WebSettings settings = web.getSettings();
                    settings.setJavaScriptEnabled(true);
                    settings.setDomStorageEnabled(true);
                    web.loadUrl(url);
                    return new WebHolder(web);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case TYPE_HEADER_CASE:
                    break;
                case TYPE_HEADER_INTERVIEW:
                    break;
                case TYPE_HEADER_POST:
                    PostHeaderHolder postHeaderHolder = (PostHeaderHolder)holder;
                    postHeaderHolder.recyclerView.setLayoutManager(new LinearLayoutManager(CompareActivity.this));
                    postHeaderHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                    ImageAdapter imageAdapter = new ImageAdapter();
                    postHeaderHolder.recyclerView.setAdapter(imageAdapter);
                    break;
                case TYPE_COMMENT:
                    CommentHolder commentHolder = (CommentHolder)holder;
                    commentHolder.recyclerView.setLayoutManager(new LinearLayoutManager(CompareActivity.this));
                    commentHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                    ReplyAdapter replyAdapter = new ReplyAdapter();
                    commentHolder.recyclerView.setAdapter(replyAdapter);
                    commentHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(CompareActivity.this, "Comment clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            if(compareList == null)
                return 0;
            return compareList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0) {
                switch (type) {
                    case Constants.TYPE_CASE:
                        return TYPE_HEADER_CASE;
                    case Constants.TYPE_POST:
                        return TYPE_HEADER_POST;
                    case Constants.TYPE_INTERVIEW:
                        return TYPE_HEADER_INTERVIEW;
                    default:
                        return -1;
                }
            }
            else
                return TYPE_COMMENT;
        }
    }

    public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyHolder> {


        public class ReplyHolder extends RecyclerView.ViewHolder {
            TextView textview;
            public ReplyHolder(View view){
                super(view);
                textview = (TextView) view.findViewById(R.id.tv_page);
            }
        }

        public ReplyAdapter() {
        }

        @Override
        public ReplyAdapter.ReplyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment_comment, parent, false);

            return new ReplyHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ReplyHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CompareActivity.this, "Comment reply clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


        public ImageAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_image, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (position){
                case 1:
                    holder.mImageView.setImageResource(R.drawable.placeholder_banner);
                    break;
                case 2:
                    holder.mImageView.setImageResource(R.drawable.placeholder_title);
                    break;
                case 0:
                    holder.mImageView.setImageResource(R.drawable.placeholder_ad3);
                    break;
                case 3:
                    holder.mImageView.setImageResource(R.drawable.placeholder_post3);
            }
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CompareActivity.this, "pic Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return 5;
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
