package com.us.hotr.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.BeautyItem;
import com.us.hotr.ui.activity.beauty.ListWithCategoryActivity;
import com.us.hotr.ui.activity.beauty.DoctorInfoActivity;
import com.us.hotr.ui.activity.beauty.HospitalActivity;
import com.us.hotr.ui.activity.beauty.ListWithSearchActivity;
import com.us.hotr.ui.activity.beauty.SelectSubjectActivity;
import com.us.hotr.ui.activity.found.NearbyActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Mloong on 2017/9/7.
 */

public class MainPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BeautyItem> itemList;

    private final int TYPE_BANNER = 101;
    private final int TYPE_MODULE = 102;
    private final int TYPE_NEWS = 103;
    private final int TYPE_DIVIDER = 104;
    private final int TYPE_AD1 = 105;
    private final int TYPE_AD3 = 106;
    private final int TYPE_TITLE_IMAGE = 107;
    private final int TYPE_COMPARE = 108;
    private final int TYPE_POST = 109;
    private final int TYPE_MASSEUR = 117;
    private final int TYPE_INTERVIEW_LIST = 118;
    private final int TYPE_SPA = 119;
    private final int TYPE_GROUP = 123;

    int postPhotoCount;
    int postColumn;
    int masseurPosition, spaPosition;

    Context mContext;


    public MainPageAdapter(Context mContext, List<BeautyItem> itemList) {

        this.itemList = itemList;
        this.mContext = mContext;

        Random rand = new Random();
        postPhotoCount = rand.nextInt(9) + 1;
        if (postPhotoCount == 1)
            postColumn = 1;
        else if (postPhotoCount == 2 || postPhotoCount == 4)
            postColumn = 2;
        else
            postColumn = 3;
        for(int i=0;i<itemList.size();i++){
            if(itemList.get(i).getId().equals(Constants.MASSEUR_ID)){
                masseurPosition = i%2;
                break;
            }
        }
        for(int i=0;i<itemList.size();i++){
            if(itemList.get(i).getId().equals(Constants.SPA_ID)){
                spaPosition = i%2;
                break;
            }
        }
    }

    public class BannerHolder extends RecyclerView.ViewHolder {
        ImageBanner mBanner;

        public BannerHolder(View view) {
            super(view);
            mBanner = (ImageBanner) view.findViewById(R.id.banner);
        }
    }

    public class ModuleHolder extends RecyclerView.ViewHolder {
        ScrollThroughRecyclerView recyclerView;

        public ModuleHolder(View view) {
            super(view);
            recyclerView = (ScrollThroughRecyclerView) view.findViewById(R.id.recyclerview);
        }
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        UPMarqueeView mUMView;

        public NewsHolder(View view) {
            super(view);
            mUMView = (UPMarqueeView) view.findViewById(R.id.upview);
        }
    }

    public class DividerHolder extends RecyclerView.ViewHolder {
        View mView;

        public DividerHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.view);
        }
    }

    public class Ad1Holder extends RecyclerView.ViewHolder {
        ImageView mIv;

        public Ad1Holder(View view) {
            super(view);
            mIv = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public class Ad3Holder extends RecyclerView.ViewHolder {
        List<ImageView> mImageViewList = new ArrayList<>();

        public Ad3Holder(View view) {
            super(view);
            mImageViewList.add((ImageView) view.findViewById(R.id.img_1));
            mImageViewList.add((ImageView) view.findViewById(R.id.img_2));
            mImageViewList.add((ImageView) view.findViewById(R.id.img_3));
        }
    }

    public class TitleImageHolder extends RecyclerView.ViewHolder {
        ImageView mIv;

        public TitleImageHolder(View view) {
            super(view);
            mIv = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public class CompareHolder extends RecyclerView.ViewHolder {
        ImageView imgBefore, imgAfter;
        TextView tvSeeMore;
        RelativeLayout rlSeeMore;

        public CompareHolder(View view) {
            super(view);
            imgBefore = (ImageView) view.findViewById(R.id.img_before);
            imgAfter = (ImageView) view.findViewById(R.id.imge_after);
            rlSeeMore = (RelativeLayout) view.findViewById(R.id.rl_see_more);
            tvSeeMore = (TextView) view.findViewById(R.id.tv_see_more);
        }
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ScrollThroughRecyclerView recyclerView;

        public PostHolder(View view) {
            super(view);
            recyclerView = (ScrollThroughRecyclerView) view.findViewById(R.id.recyclerview);
        }
    }

    public class MasseurHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvAppointment;
        ImageView ivAvatar, ivLike;

        public MasseurHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            ivLike = (ImageView) view.findViewById(R.id.iv_like);
        }
    }

    public class InterViewListHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;

        public InterViewListHolder(View view) {
            super(view);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        }
    }


    public class SPAHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvAppointment;
        ImageView ivAvatar;

        public SPAHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvAppointment = (TextView) view.findViewById(R.id.tv_number);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        }
    }


    public class GroupListHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        LinearLayout llAllGroup;

        public GroupListHolder(View view) {
            super(view);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            llAllGroup = (LinearLayout) view.findViewById(R.id.ll_all_group);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_BANNER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
                return new BannerHolder(view);
            case TYPE_MODULE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
                return new ModuleHolder(view);
            case TYPE_NEWS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
                return new NewsHolder(view);
            case TYPE_DIVIDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_divider, parent, false);
                return new DividerHolder(view);
            case TYPE_AD1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad1, parent, false);
                return new Ad1Holder(view);
            case TYPE_AD3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad3, parent, false);
                return new Ad3Holder(view);
            case TYPE_TITLE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_image, parent, false);
                return new TitleImageHolder(view);
            case TYPE_COMPARE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compare, parent, false);
                return new CompareHolder(view);
            case TYPE_POST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                PostHolder p = new PostHolder(view);
                p.recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(12, postColumn));
                return p;
            case TYPE_MASSEUR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur, parent, false);
                return new MasseurHolder(view);
            case TYPE_INTERVIEW_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_interview_list, parent, false);
                return new InterViewListHolder(view);
            case TYPE_SPA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa, parent, false);
                return new SPAHolder(view);
            case TYPE_GROUP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, parent, false);
                return new GroupListHolder(view);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (itemList.get(position).getId()) {
            case Constants.BANNER_ID:
                return TYPE_BANNER;
            case Constants.MODULE_ID:
                return TYPE_MODULE;
            case Constants.NEWS_ID:
                return TYPE_NEWS;
            case Constants.DIVIDER_ID:
                return TYPE_DIVIDER;
            case Constants.AD1_ID:
                return TYPE_AD1;
            case Constants.AD3_ID:
                return TYPE_AD3;
            case Constants.TITLE_IMGE_ID:
                return TYPE_TITLE_IMAGE;
            case Constants.COMPARE_ID:
                return TYPE_COMPARE;
            case Constants.POST_ID:
                return TYPE_POST;
            case Constants.MASSEUR_ID:
                return TYPE_MASSEUR;
            case Constants.INTERVIEW_LIST_ID:
                return TYPE_INTERVIEW_LIST;
            case Constants.SPA_ID:
                return TYPE_SPA;
            case Constants.GROUP_ID:
                return TYPE_GROUP;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_BANNER:
                List<String> urls = new ArrayList<>(Arrays.asList(
                        "http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg",
                        "http://img.zcool.cn/community/01996b57a7f6020000018c1bedef97.jpg",
                        "http://img.zcool.cn/community/01sdfsdfsdf.jpg"));

                BannerHolder bannerHolder = (BannerHolder) holder;
                bannerHolder.mBanner.setRatio(0.419);
                bannerHolder.mBanner.setSource(urls);
                bannerHolder.mBanner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
                    @Override
                    public void onBannerItemClicked(int position) {
                        Toast.makeText(mContext, "Banner " + position + " clicked!", Toast.LENGTH_SHORT).show();
                    }
                });
                bannerHolder.mBanner.startScroll();
                break;


            case TYPE_MODULE:
                ModuleHolder moduleHolder = (ModuleHolder) holder;
                moduleHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
                ModuleAdapter mAdapter = new ModuleAdapter(5, position);
                moduleHolder.recyclerView.setAdapter(mAdapter);
                break;


            case TYPE_NEWS:
                List<String> data = new ArrayList<>();
                data.add("家人给2岁孩子喝这个，孩子智力倒退10岁!!!");
                data.add("iPhone8最感人变化成真，必须买买买买!!!!");
                data.add("简直是白菜价！日本玩家33万甩卖15万张游戏王卡");

                NewsHolder newsHolder = (NewsHolder) holder;
                List<View> views = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_news_item, null);
                    TextView tv = (TextView) moreView.findViewById(R.id.tv_code);
                    tv.setText(data.get(i).toString());
                    views.add(moreView);
                }
                newsHolder.mUMView.setViews(views);
                newsHolder.mUMView.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Toast.makeText(mContext, "你点击了第几个items" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                break;


            case TYPE_DIVIDER:
                DividerHolder dividerHolder = (DividerHolder) holder;
                dividerHolder.mView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 24));
                dividerHolder.mView.setBackgroundResource(R.color.bg_grey);
                break;


            case TYPE_AD1:
                Ad1Holder ad1Holder = (Ad1Holder) holder;
                ad1Holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Ad1 Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                break;


            case TYPE_AD3:
                Ad3Holder ad3Holder = (Ad3Holder) holder;
                for (int i = 0; i < ad3Holder.mImageViewList.size(); i++) {
                    final int finalI = i;
                    ad3Holder.mImageViewList.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mContext, "Ad number " + finalI + " Clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;


            case TYPE_TITLE_IMAGE:
                TitleImageHolder titleHolder = (TitleImageHolder) holder;
                titleHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Title Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                break;



            case TYPE_POST:
                PostHolder postHolder = (PostHolder) holder;
                postHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, postColumn));
                PicGridAdapter adapter = new PicGridAdapter(postPhotoCount, mContext);
                postHolder.recyclerView.setAdapter(adapter);

                postHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Post Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                break;



            case TYPE_MASSEUR:
                MasseurHolder masseurHolder = (MasseurHolder) holder;

                if(position%2==masseurPosition) {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) masseurHolder.ivAvatar.getLayoutParams();
                    lp.setMargins(12, 0, 6, 0);
                    masseurHolder.ivAvatar.setLayoutParams(lp);
                }
                else {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) masseurHolder.ivAvatar.getLayoutParams();
                    lp.setMargins(6, 0, 12, 0);
                    masseurHolder.ivAvatar.setLayoutParams(lp);
                }
                break;


            case TYPE_INTERVIEW_LIST:
                final InterViewListHolder interViewListHolder = (InterViewListHolder) holder;

                interViewListHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HorizontalHInterviewAdapter hInterviewAdapter = new HorizontalHInterviewAdapter();
                interViewListHolder.mRecyclerView.setAdapter(hInterviewAdapter);
                break;


            case TYPE_SPA:
                SPAHolder spaHolder = (SPAHolder) holder;
                if(position%2==spaPosition) {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) spaHolder.ivAvatar.getLayoutParams();
                    lp.setMargins(12, 0, 6, 0);
                    spaHolder.ivAvatar.setLayoutParams(lp);
                }
                else {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) spaHolder.ivAvatar.getLayoutParams();
                    lp.setMargins(6, 0, 12, 0);
                    spaHolder.ivAvatar.setLayoutParams(lp);
                }
                break;


            case TYPE_GROUP:
                final GroupListHolder groupListHolder = (GroupListHolder) holder;

                groupListHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HorizontalGroupListAdapter groupListAdapter = new HorizontalGroupListAdapter();
                groupListHolder.mRecyclerView.setAdapter(groupListAdapter);

                groupListHolder.llAllGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_MASSEUR:
                        case TYPE_SPA:
                            return 1;
                        case TYPE_MODULE:
                        case TYPE_INTERVIEW_LIST:
                        case TYPE_BANNER:
                        case TYPE_NEWS:
                        case TYPE_DIVIDER:
                        case TYPE_AD1:
                        case TYPE_AD3:
                        case TYPE_TITLE_IMAGE:
                        case TYPE_COMPARE:
                        case TYPE_POST:
                        case TYPE_GROUP:
                            return 2;
                        default:
                            return 2;
                    }
                }
            });
        }
    }

    public class HorizontalHInterviewAdapter extends RecyclerView.Adapter<HorizontalHInterviewAdapter.ViewHolder> {

        public HorizontalHInterviewAdapter() {

        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interview_listitem, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(position == 6)
                holder.itemView.setPadding(0,0,0,0);
            else
                holder.itemView.setPadding(0,0,12,0);
        }

        @Override
        public int getItemCount() {
            return 4;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView ivAvatar;
            public TextView tvName, tvNumber;

            public ViewHolder(View itemView) {
                super(itemView);
                ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            }
        }
    }

    public class HorizontalGroupListAdapter extends RecyclerView.Adapter<HorizontalGroupListAdapter.ViewHolder> {

        public HorizontalGroupListAdapter() {

        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_image, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(position == 4)
                holder.itemView.setPadding(0,0,0,0);
            else
                holder.itemView.setPadding(0,0,12,0);
        }

        @Override
        public int getItemCount() {
            return 4;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView ivAvatar;

            public ViewHolder(View itemView) {
                super(itemView);
                ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            }
        }
    }



    public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {

        private int picCount, position;

        public ModuleAdapter(int picCount, int position) {
            this.picCount = picCount;
            this.position = position;
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int p) {
            if(position==1) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (p == 0) {
                            Intent i = new Intent(mContext, SelectSubjectActivity.class);
                            mContext.startActivity(i);
                        }
                        if (p == 1) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "项目报点");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_DOCTOR);
                            mContext.startActivity(i);
                        }
                        if (p == 2) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "项目报点");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_HOSPITAL);
                            mContext.startActivity(i);
                        }
                        if (p == 3) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "医美日记");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                            mContext.startActivity(i);
                        }
                        if (p == 4) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "医美日记");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_OFFICIAL_POST);
                            mContext.startActivity(i);
                        }
                    }
                });
            }
            if(position==2){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(mContext, "Subject " + p + " clicked!", Toast.LENGTH_SHORT).show();
                        if (p == 0) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "项目报点");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                            mContext.startActivity(i);
                        }
                        if (p == 1) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "项目报点");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSEUR);
                            mContext.startActivity(i);
                        }
                        if (p == 2) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "项目报点");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_SPA);
                            mContext.startActivity(i);
                        }
                        if (p == 3) {
                            Intent i = new Intent(mContext, ListWithSearchActivity.class);
                            i.putExtra(Constants.PARAM_TITLE, "医美日记");
                            i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_INTERVIEW);
                            mContext.startActivity(i);
                        }
                        if (p == 4) {
                            Intent i = new Intent(mContext, NearbyActivity.class);
                            mContext.startActivity(i);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return picCount;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                mTextView = (TextView) itemView.findViewById(R.id.tv_title);
            }
        }

    }
}
