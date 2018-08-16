package com.us.hotr.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Module;
import com.us.hotr.storage.bean.Post;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Spa;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.ui.activity.WebViewActivity;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.ui.activity.beauty.DoctorActivity;
import com.us.hotr.ui.activity.beauty.HospitalActivity;
import com.us.hotr.ui.activity.beauty.ListActivity;
import com.us.hotr.ui.activity.beauty.ListWithSearchActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.activity.beauty.SelectSubjectActivity;
import com.us.hotr.ui.activity.beauty.SubjectActivity;
import com.us.hotr.ui.activity.found.AllGroupActivity;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.ui.activity.found.NearbyActivity;
import com.us.hotr.ui.activity.info.InviteFriendActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.activity.massage.MassageActivity;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.ui.activity.massage.SpaActivity;
import com.us.hotr.ui.activity.party.PartyActivity;
import com.us.hotr.ui.view.CaseView;
import com.us.hotr.ui.view.HospitalView;
import com.us.hotr.ui.view.MassageView;
import com.us.hotr.ui.view.MasseurView;
import com.us.hotr.ui.view.PostView;
import com.us.hotr.ui.view.ProductView;
import com.us.hotr.ui.view.SpaView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.response.GetHomePageResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Mloong on 2017/9/7.
 */

public class MainPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class Item{
        private int id;
        private Object content;

        public Item(int id){
            this.id = id;
        }

        public Item(int id, Object content){
            this.id = id;
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }
    }

    private final int TYPE_BANNER = 1;
    private final int TYPE_BUTTON = 2;
    private final int TYPE_NEWS = 3;
    private final int TYPE_AD1 = 4;
    private final int TYPE_AD3 = 5;
    private final int TYPE_TITLE = 6;
    private final int TYPE_CASE = 7;
    public static final int TYPE_POST = 8;
    private final int TYPE_DIVIDER = 9;
    private final int TYPE_MASSEUR = 10;
    private final int TYPE_SPA = 11;
    private final int TYPE_PRODUCT = 12;
    private final int TYPE_MASSAGE = 13;
    private final int TYPE_HOSPITAL = 14;
    public static final int TYPE_GROUP = 15;

    public static final int PAGE_PRODUCT = 100;
    public static final int PAGE_MASSAGE = 101;
    public static final int PAGE_GROUP = 102;

    Context mContext;
    private List<Item> itemList;
    int masseurPosition, spaPosition, type;

    public MainPageAdapter(Context mContext, GetHomePageResponse response, int type) {
        this.mContext = mContext;
        this.type = type;
        itemList = new ArrayList<>();
        if(response.getListHomePageModule()!=null && response.getListHomePageModule().size()>0) {
            for(Module module:response.getListHomePageModule()) {
                if (module.getModuleTypeId() == TYPE_CASE) {
                    if (response.getRecommendContrastPhotoList() != null && response.getRecommendContrastPhotoList().size() > 0)
                        for (Case c : response.getRecommendContrastPhotoList())
                            itemList.add(new Item(TYPE_CASE, c));
                }else if(module.getModuleTypeId() == TYPE_POST){
                    if (response.getRecommendHotTopicList() != null && response.getRecommendHotTopicList().size() > 0)
                        for (Post c : response.getRecommendHotTopicList())
                            itemList.add(new Item(TYPE_POST, c));
                }else if(module.getModuleTypeId() == TYPE_MASSEUR){
                    masseurPosition = itemList.size();
                    if (response.getRecommendMassagistList() != null && response.getRecommendMassagistList().size() > 0)
                        for (Masseur c : response.getRecommendMassagistList())
                            itemList.add(new Item(TYPE_MASSEUR, c));
                }else if(module.getModuleTypeId() == TYPE_SPA) {
                    spaPosition = itemList.size();
                    if (response.getRecommendMassageList() != null && response.getRecommendMassageList().size() > 0)
                        for (Spa c : response.getRecommendMassageList())
                            itemList.add(new Item(TYPE_SPA, c));
                }else if(module.getModuleTypeId() == TYPE_PRODUCT){
                    if (response.getRecommendYmProductList() != null && response.getRecommendYmProductList().size() > 0)
                        for (Product p : response.getRecommendYmProductList())
                            itemList.add(new Item(TYPE_PRODUCT, p));
                }else if(module.getModuleTypeId() == TYPE_MASSAGE){
                    if (response.getRecommendAmProductList() != null && response.getRecommendAmProductList().size() > 0)
                        for (Massage m : response.getRecommendAmProductList())
                            itemList.add(new Item(TYPE_MASSAGE, m));
                }else if(module.getModuleTypeId() == TYPE_HOSPITAL){
                    if (response.getRecommendHospitalList() != null && response.getRecommendHospitalList().size() > 0)
                        for (Hospital h : response.getRecommendHospitalList())
                            itemList.add(new Item(TYPE_HOSPITAL, h));
                }else if (module.getModuleTypeId() == TYPE_GROUP) {
                    itemList.add(new Item(TYPE_GROUP, response.getMyGrouList()));
                }else
                    itemList.add(new Item(module.getModuleTypeId(), module));
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

    public class ButtonHolder extends RecyclerView.ViewHolder {
        ScrollThroughRecyclerView recyclerView;

        public ButtonHolder(View view) {
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

    public class TitleHolder extends RecyclerView.ViewHolder {
        ImageView mIv;

        public TitleHolder(View view) {
            super(view);
            mIv = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public class CaseHolder extends RecyclerView.ViewHolder {
        CaseView caseView;

        public CaseHolder(View view) {
            super(view);
            caseView = (CaseView) view;
        }
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        PostView postView;

        public PostHolder(View view) {
            super(view);
            postView = (PostView) view;
        }
    }

    public class MasseurHolder extends RecyclerView.ViewHolder {
        MasseurView masseurView;
        public MasseurHolder(View view) {
            super(view);
            masseurView = (MasseurView) view;
        }
    }

//    public class InterViewListHolder extends RecyclerView.ViewHolder {
//        RecyclerView mRecyclerView;
//
//        public InterViewListHolder(View view) {
//            super(view);
//            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
//        }
//    }


    public class SPAHolder extends RecyclerView.ViewHolder {
        SpaView spaView;
        public SPAHolder(View view) {
            super(view);
            spaView = (SpaView) view;
        }
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        ProductView productView;
        public ProductHolder(View view) {
            super(view);
            productView = (ProductView) view;
        }
    }

    public class MassageHolder extends RecyclerView.ViewHolder {
        MassageView massageView;
        public MassageHolder(View view) {
            super(view);
            massageView = (MassageView) view;
        }
    }

    public class HospitalHolder extends RecyclerView.ViewHolder {
        HospitalView hospitalView;
        public HospitalHolder(View view) {
            super(view);
            hospitalView = (HospitalView) view;
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
            case TYPE_BUTTON:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
                return new ButtonHolder(view);
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
            case TYPE_TITLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_image, parent, false);
                return new TitleHolder(view);
            case TYPE_CASE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
                return new CaseHolder(view);
            case TYPE_POST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostHolder(view);
            case TYPE_MASSEUR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur, parent, false);
                return new MasseurHolder(view);
//            case TYPE_INTERVIEW_LIST:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_interview_list, parent, false);
//                return new InterViewListHolder(view);
            case TYPE_SPA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa, parent, false);
                return new SPAHolder(view);
            case TYPE_PRODUCT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                return new ProductHolder(view);
            case TYPE_MASSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_massage, parent, false);
                return new MassageHolder(view);
            case TYPE_HOSPITAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false);
                return new HospitalHolder(view);
            case TYPE_GROUP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, parent, false);
                return new GroupListHolder(view);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getId();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_BANNER:
                final Module bannerModule = (Module) itemList.get(position).getContent();
                List<String> urls = new ArrayList<>();
                for(Module.ModuleContent m:bannerModule.getBannerList())
                    urls.add(m.getBannerImg());
                if(urls!=null && urls.size()>0) {
                    BannerHolder bannerHolder = (BannerHolder) holder;
                    bannerHolder.mBanner.setRatio(0.419);
                    bannerHolder.mBanner.setSource(urls);
                    bannerHolder.mBanner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
                        @Override
                        public void onBannerItemClicked(int p) {
                            Properties prop = new Properties();
                            prop.setProperty("page", (p+1)+"");
                            switch (type){
                                case PAGE_PRODUCT:
                                    StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_PRODUCT_BANNER, prop);
                                    break;
                                case PAGE_MASSAGE:
                                    StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_MASSAGE_BANNER, prop);
                                    break;
                                case PAGE_GROUP:
                                    StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_DISCOVERY_BANNER, prop);
                                    break;
                                default:
                                    break;
                            }

                            handleClickEvent(bannerModule.getBannerList().get(p), null);
                        }
                    });
                    bannerHolder.mBanner.startScroll();
                    if(urls.size()==1)
                        bannerHolder.mBanner.pauseScroll();
                }
                break;

            case TYPE_BUTTON:
                ButtonHolder moduleHolder = (ButtonHolder) holder;
                final Module buttonModule = (Module) itemList.get(position).getContent();
                moduleHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, buttonModule.getBannerList().size()));
                ModuleAdapter mAdapter = new ModuleAdapter(buttonModule.getBannerList(), buttonModule.getModuleSort());
                moduleHolder.recyclerView.setAdapter(mAdapter);
                break;

            case TYPE_NEWS:
                NewsHolder newsHolder = (NewsHolder) holder;
                final Module newsModule = (Module) itemList.get(position).getContent();
                List<View> views = new ArrayList<>();
                for (Module.ModuleContent moduleContent:newsModule.getBannerList()) {
                    LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_news_item, null);
                    TextView tv = (TextView) moreView.findViewById(R.id.tv_code);
                    tv.setText(moduleContent.getBannerName());
                    views.add(moreView);
                }
                newsHolder.mUMView.setViews(views);
                newsHolder.mUMView.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int p, View view) {
                        handleClickEvent(newsModule.getBannerList().get(p), null);
                    }
                });
                break;

            case TYPE_DIVIDER:
                DividerHolder dividerHolder = (DividerHolder) holder;
                final Module dividerModule = (Module) itemList.get(position).getContent();
                dividerHolder.mView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerModule.getModuleHeight()));
                try{
                    dividerHolder.mView.setBackgroundColor(Color.parseColor(dividerModule.getModuleBackgroundColor()));
                }catch (Throwable th){
                    dividerHolder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.divider2));
                }

                break;

            case TYPE_AD1:
                Ad1Holder ad1Holder = (Ad1Holder) holder;
                final Module ad1Module = (Module) itemList.get(position).getContent();
                Glide.with(mContext).load(ad1Module.getBannerList().get(0).getBannerImg()).dontAnimate().placeholder(R.drawable.placeholder_ad1).error(R.drawable.placeholder_ad1).into(ad1Holder.mIv);
                ad1Holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Properties prop = new Properties();
                        prop.setProperty("location", ad1Module.getModuleSort()+"");
                        switch (type){
                            case PAGE_PRODUCT:
                                StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_PRODUCT_ADV, prop);
                                break;
                            case PAGE_MASSAGE:
                                StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_MASSAGE_ADV, prop);
                                break;
                            default:
                                break;
                        }

                        handleClickEvent(ad1Module.getBannerList().get(0), null);
                    }
                });
                break;

            case TYPE_AD3:
                Ad3Holder ad3Holder = (Ad3Holder) holder;
                final Module ad3Module = (Module) itemList.get(position).getContent();
                for (int i = 0; i < ad3Holder.mImageViewList.size(); i++) {
                    final int finalI = i;
                    Glide.with(mContext).load(ad3Module.getBannerList().get(i).getBannerImg()).dontAnimate().placeholder(R.drawable.placeholder_ad3).error(R.drawable.placeholder_ad3).into(ad3Holder.mImageViewList.get(i));
                    ad3Holder.mImageViewList.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Properties prop = new Properties();
                            prop.setProperty("location", ad3Module.getModuleSort()+"-"+(finalI+1));
                            switch (type){
                                case PAGE_PRODUCT:
                                    StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_PRODUCT_ADV_3, prop);
                                    break;
                                case PAGE_MASSAGE:
                                    StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_MASSAGE_ADV_3, prop);
                                    break;
                                default:
                                    break;
                            }
                            handleClickEvent(ad3Module.getBannerList().get(finalI), null);
                        }
                    });
                }
                break;

            case TYPE_TITLE:
                TitleHolder titleHolder = (TitleHolder) holder;
                final Module titleModule = (Module) itemList.get(position).getContent();
                Glide.with(mContext).load(titleModule.getBannerList().get(0).getBannerImg()).dontAnimate().placeholder(R.drawable.placeholder_title).error(R.drawable.placeholder_title).into(titleHolder.mIv);
                titleHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleClickEvent(titleModule.getBannerList().get(0), null);
                    }
                });
                break;

            case TYPE_POST:
                PostHolder postHolder = (PostHolder) holder;
                postHolder.postView.setData((Post)itemList.get(position).getContent());
                postHolder.postView.enableEdit(false);
                if(itemList.size()>position+1 && itemList.get(position+1).getId()!=TYPE_POST)
                    postHolder.postView.showDivider(false);
                break;

            case TYPE_CASE:
                CaseHolder caseHolder = (CaseHolder) holder;
                caseHolder.caseView.setData((Case)itemList.get(position).getContent());
                caseHolder.caseView.enableEdit(false);
                if(itemList.size()>position+1 && itemList.get(position+1).getId()!=TYPE_CASE)
                    caseHolder.caseView.showDivider(false);
                break;

            case TYPE_MASSEUR:
                MasseurHolder masseurHolder = (MasseurHolder) holder;
                masseurHolder.masseurView.setLog(true);
                masseurHolder.masseurView.setData((Masseur)itemList.get(position).getContent(), position-masseurPosition, true);
                break;


//            case TYPE_INTERVIEW_LIST:
//                final InterViewListHolder interViewListHolder = (InterViewListHolder) holder;
//
//                interViewListHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//                HorizontalHInterviewAdapter hInterviewAdapter = new HorizontalHInterviewAdapter();
//                interViewListHolder.mRecyclerView.setAdapter(hInterviewAdapter);
//                break;


            case TYPE_SPA:
                SPAHolder spaHolder = (SPAHolder) holder;
                spaHolder.spaView.setData((Spa)itemList.get(position).getContent(), position-spaPosition, true);
                break;

            case TYPE_PRODUCT:
                ProductHolder productHolder = (ProductHolder) holder;
                productHolder.productView.setData((Product)itemList.get(position).getContent());
                productHolder.productView.setLog(true);
                if(itemList.size()>position+1 && itemList.get(position+1).getId()!=TYPE_PRODUCT)
                    productHolder.productView.showDivider(false);
                break;

            case TYPE_MASSAGE:
                MassageHolder massageHolder = (MassageHolder) holder;
                massageHolder.massageView.setData((Massage) itemList.get(position).getContent(), -1);
                massageHolder.massageView.setLog(true);
                if(itemList.size()>position+1 && itemList.get(position+1).getId()!=TYPE_MASSAGE)
                    massageHolder.massageView.showDivider(false);
                break;

            case TYPE_HOSPITAL:
                HospitalHolder hospitalHolder = (HospitalHolder) holder;
                hospitalHolder.hospitalView.setData((Hospital) itemList.get(position).getContent());
                hospitalHolder.hospitalView.setLog(true);
                if(itemList.size()>position+1 && itemList.get(position+1).getId()!=TYPE_HOSPITAL)
                    hospitalHolder.hospitalView.showDivider(false);
                break;

            case TYPE_GROUP:
                final GroupListHolder groupListHolder = (GroupListHolder) holder;

                groupListHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                HorizontalGroupListAdapter groupListAdapter = new HorizontalGroupListAdapter((List<Group>)itemList.get(position).getContent());
                groupListHolder.mRecyclerView.setAdapter(groupListAdapter);
                groupListHolder.llAllGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.startActivity(new Intent(mContext, AllGroupActivity.class));
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
                        case TYPE_BUTTON:
//                        case TYPE_INTERVIEW_LIST:
                        case TYPE_BANNER:
                        case TYPE_NEWS:
                        case TYPE_DIVIDER:
                        case TYPE_AD1:
                        case TYPE_AD3:
                        case TYPE_TITLE:
                        case TYPE_CASE:
                        case TYPE_POST:
                        case TYPE_GROUP:
                        case TYPE_PRODUCT:
                        case TYPE_MASSAGE:
                        case TYPE_HOSPITAL:
                            return 2;
                        default:
                            return 2;
                    }
                }
            });
        }
    }

    public int getPostCount(){
        int count = 0;
        for(Item item:itemList)
            if(item.getId() == TYPE_POST)
                count ++;
        return count;
    }

    public void addPost(List<Post> postList){
        for(Post post:postList)
            itemList.add(new Item(TYPE_POST, post));
        notifyDataSetChanged();
    }

    private void handleClickEvent(final Module.ModuleContent content, final String title){
        switch (content.getLinkTypeId()){
            case 1:
                Intent i = new Intent(mContext, SelectSubjectActivity.class);
                i.putExtra(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.product1):title);
                mContext.startActivity(i);
                break;
            case 2:
                i = new Intent(mContext, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.doctor_list_title):title);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_DOCTOR);
                mContext.startActivity(i);
                break;
            case 3:
                i = new Intent(mContext, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.hospital_list_title):title);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_HOSPITAL);
                mContext.startActivity(i);
                break;
            case 4:
                i = new Intent(mContext, SubjectActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                b.putBoolean(Constants.PARAM_TYPE, false);
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 5:
                i = new Intent(mContext, ProductActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 6:
                i = new Intent(mContext, DoctorActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 7:
                i = new Intent(mContext, HospitalActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 8:
                i = new Intent(mContext, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.massage_list_title):title);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                mContext.startActivity(i);
                break;
            case 9:
                i = new Intent(mContext, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.masseur_list_title):title);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSEUR);
                mContext.startActivity(i);
                break;
            case 10:
                i = new Intent(mContext, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.spa_list_title):title);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_SPA);
                mContext.startActivity(i);
                break;
            case 11:
                i = new Intent(mContext, MasseurActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 12:
                i = new Intent(mContext, SpaActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 13:
                i = new Intent(mContext, MassageActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 14:
                SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<BaseListResponse<List<Subject>>>() {
                    @Override
                    public void onNext(BaseListResponse<List<Subject>> result) {
                        String t = mContext.getString(R.string.massage_list_title);
                        for(Subject s:result.getRows()){
                            if(s.getKey() == Integer.parseInt(content.getLinkUrl().trim()))
                                t = s.getTypeName();
                        }
                        Intent i = new Intent(mContext, ListWithSearchActivity.class);
                        i.putExtra(Constants.PARAM_TITLE, t);
                        i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                        i.putExtra(Constants.PARAM_ID, Integer.parseInt(content.getLinkUrl().trim()));
                        mContext.startActivity(i);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Intent i = new Intent(mContext, ListWithSearchActivity.class);
                        i.putExtra(Constants.PARAM_TITLE, mContext.getString(R.string.massage_list_title));
                        i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                        i.putExtra(Constants.PARAM_ID, Integer.parseInt(content.getLinkUrl().trim()));
                        mContext.startActivity(i);
                    }
                };
                ServiceClient.getInstance().getMassageTypeList(new SilentSubscriber(mListener, mContext, null));
                break;
            case 15:
                i = new Intent(mContext, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.case_list_title):title);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                mContext.startActivity(i);
                break;
            case 16:
                i = new Intent(mContext, CaseActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 17:
                i = new Intent(mContext, GroupDetailActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 18:
                i = new Intent(mContext, CaseActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 19:
                if(Constants.INVITE_FRIENDS_SHARE_URL.equals(content.getLinkUrl().trim())){
                    if(Tools.isUserLogin(mContext)){
                        mContext.startActivity(new Intent(mContext, InviteFriendActivity.class));
                    }else{
                        LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                            @Override
                            public void onLoginSuccess() {
                                mContext.startActivity(new Intent(mContext, InviteFriendActivity.class));
                            }

                            @Override
                            public void onLoginCancel() {

                            }
                        });
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }else {
                    i = new Intent(mContext, WebViewActivity.class);
                    b = new Bundle();
                    b.putString(Constants.PARAM_DATA, content.getLinkUrl().trim());
                    b.putInt(Constants.PARAM_TYPE, WebViewActivity.TYPE_URL);
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
                break;
            case 20:
                i = new Intent(mContext, NearbyActivity.class);
                mContext.startActivity(i);
                break;
            case 21:
                i = new Intent(mContext, ListActivity.class);
                b = new Bundle();
                b.putString(Constants.PARAM_TITLE, title==null?mContext.getString(R.string.discovery_title):title);
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_OFFICIAL_POST);
                i.putExtras(b);
                mContext.startActivity(i);
                break;
            case 23:
                i = new Intent(mContext, PartyActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(content.getLinkUrl().trim()));
                i.putExtras(b);
                mContext.startActivity(i);
                break;
        }
    }

//    public class HorizontalHInterviewAdapter extends RecyclerView.Adapter<HorizontalHInterviewAdapter.ViewHolder> {
//
//        public HorizontalHInterviewAdapter() {
//
//        }
//
//        // inflates the cell layout from xml when needed
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interview_listitem, parent, false);
//            ViewHolder viewHolder = new ViewHolder(view);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            if(position == 6)
//                holder.itemView.setPadding(0,0,0,0);
//            else
//                holder.itemView.setPadding(0,0,12,0);
//        }
//
//        @Override
//        public int getItemCount() {
//            return 4;
//        }
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public ImageView ivAvatar;
//            public TextView tvName, tvNumber;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
//                tvName = (TextView) itemView.findViewById(R.id.tv_name);
//                tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
//            }
//        }
//    }

    public class HorizontalGroupListAdapter extends RecyclerView.Adapter<HorizontalGroupListAdapter.ViewHolder> {
        private List<Group> groupList;
        public HorizontalGroupListAdapter(List<Group> groupList) {
            this.groupList = groupList;
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
            final Group group = groupList.get(position);
            Glide.with(mContext).load(group.getSmall_img()).dontAnimate().placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.ivAvatar);
            if(group.getIs_recommend() == 1)
                holder.tvRecommand.setVisibility(View.VISIBLE);
            else
                holder.tvRecommand.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, GroupDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, group);
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(groupList == null)
                return 0;
            else
                return groupList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            TextView tvRecommand;
            public ViewHolder(View itemView) {
                super(itemView);
                ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
                tvRecommand = (TextView) itemView.findViewById(R.id.tv_recommanded);
            }
        }
    }


    public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {

        private List<Module.ModuleContent> moduleContentList;
        private int location;

        public ModuleAdapter(List<Module.ModuleContent> moduleContentList, int location) {
            this.moduleContentList = moduleContentList;
            this.location = location;
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
            final Module.ModuleContent moduleContent = moduleContentList.get(p);
            Glide.with(mContext).load(moduleContent.getBannerImg()).dontAnimate().placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(holder.mImageView);
            holder.mTextView.setText(moduleContent.getBannerName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("location", location+"-"+(p+1));
                    switch (type){
                        case PAGE_PRODUCT:
                            StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_PRODUCT_MODULE, prop);
                            break;
                        case PAGE_MASSAGE:
                            StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_CLICK_MASSAGE_MODULE, prop);
                            break;
                        default:
                            break;
                    }
                    handleClickEvent(moduleContent, moduleContent.getBannerName());
                }
            });
        }

        @Override
        public int getItemCount() {
            if(moduleContentList == null)
                return 0;
            else
                return moduleContentList.size();
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
