package com.us.hotr.ui.activity.massage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.HorizontalImageAdapter;
import com.us.hotr.customview.ImageBanner;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Massage;
import com.us.hotr.storage.bean.Masseur;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MapViewActivity;
import com.us.hotr.ui.activity.beauty.ListActivity;
import com.us.hotr.ui.activity.beauty.ListWithCategoryActivity;
import com.us.hotr.ui.view.MassageView;
import com.us.hotr.ui.view.MasseurView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetSpaDetailResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Mloong on 2017/9/29.
 */

public class SpaActivity extends BaseLoadingActivity {

    private RecyclerView mRecyclerView;
    private SpaAdapter mAdapter;
    private ConstraintLayout clPay;
    private float offset = 0;
    private long mSpaId;
    private boolean isCollected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpaId = getIntent().getExtras().getLong(Constants.PARAM_ID);

        initStaticView();
        setMyTitle(R.string.spa_detail);
        ivShare.setVisibility(View.GONE);
        loadData(Constants.LOAD_PAGE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Properties prop = new Properties();
        prop.setProperty("id", mSpaId+"");
        StatService.trackCustomKVEvent(this, Constants.MTA_ID_SPA_DETAIL_SCREEN, prop);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_masseur;
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        clPay = (ConstraintLayout) findViewById(R.id.cl_pay);
        clPay.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        enableLoadMore(false);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offset = offset + dy;
                if(offset > 300){
                    ivBack.setImageResource(R.mipmap.ic_back);
                    ivShare.setImageResource(R.mipmap.ic_share);
                    findViewById(R.id.tb_title).setAlpha(1);
                    findViewById(R.id.v_divider).setAlpha(1);
                }else{
                    findViewById(R.id.tb_title).setAlpha(offset / 300);
                    findViewById(R.id.v_divider).setAlpha(offset / 300);
                    ivBack.setImageResource(R.mipmap.ic_back_dark);
                    ivShare.setImageResource(R.mipmap.ic_share_dark);
                }
            }
        });
    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<GetSpaDetailResponse>() {
            @Override
            public void onNext(GetSpaDetailResponse result) {
                if(result == null || result.getMassage() == null){
                    showErrorPage();
                    return;
                }
                isCollected = result.getIs_collected()==1?true:false;
                mAdapter = new SpaAdapter(SpaActivity.this, result);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                mRecyclerView.setAdapter(myBaseAdapter);
            }
        };
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getSpaDetail(new LoadingSubscriber(mListener, this),
                    mSpaId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getSpaDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    mSpaId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    public class SpaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private GetSpaDetailResponse spaDetail;
        private List<Item> itemList = new ArrayList<>();
        private Context mContext;

        private final int TYPE_HEADER = 101;
        private final int TYPE_MASSAGE_HEADER = 102;
        private final int TYPE_MASSAGE = 103;
        private final int TYPE_MASSAGE_FOOTER = 104;
        private final int TYPE_MASSEUR_HEADER = 105;
        private final int TYPE_MASSEUR = 106;
        private final int TYPE_MASSEUR_FOOTER = 107;
        private final int TYPE_POST_HEADER = 108;
        private final int TYPE_POST = 109;
        private final int TYPE_POST_FOOTER = 110;

        public SpaAdapter(Context mContext, GetSpaDetailResponse spaDetail) {
            this.spaDetail = spaDetail;
            this.mContext = mContext;
            itemList.add(new Item(TYPE_HEADER));
            if(spaDetail.getMasseurList()!=null && spaDetail.getMasseurList().size()>0) {
                itemList.add(new Item(TYPE_MASSEUR_HEADER));
                for(int i=0;i<spaDetail.getMasseurList().size();i++)
                    itemList.add(new Item(TYPE_MASSEUR, spaDetail.getMasseurList().get(i)));
                if(spaDetail.getTotalMasseurCount()>4)
                    itemList.add(new Item(TYPE_MASSEUR_FOOTER));
            }
            if(spaDetail.getMassageList()!=null && spaDetail.getMassageList().size()>0){
                itemList.add(new Item(TYPE_MASSAGE_HEADER));
                for(int i=0;i<spaDetail.getMassageList().size();i++)
                    itemList.add(new Item(TYPE_MASSAGE, spaDetail.getMassageList().get(i)));
                if(spaDetail.getTotalMassageCount()>3)
                    itemList.add(new Item(TYPE_MASSAGE_FOOTER));
            }
//            if(hospitalDetail.getCaseList()!=null && hospitalDetail.getCaseList().size()>0){
//                itemList.add(new Item(TYPE_CASE_HEADER));
//                itemList.add(new Item(TYPE_CASE_SUBJECT));
//                for(int i=0;i<hospitalDetail.getCaseList().size();i++)
//                    itemList.add(new Item(TYPE_CASE, hospitalDetail.getCaseList().get(i)));
//                if(hospitalDetail.getTotalCase()>3)
//                    itemList.add(new Item(TYPE_CASE_FOOTER));
//            }
        }


        public class SpaHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivAdd, ivMsg;
            TextView tvName, tvTime, tvAppointment, tvAddress, tvIntroduction, tvShowAll;
            FlowLayout flSubject;
            RecyclerView recyclerView;
            ImageBanner banner;
            RelativeLayout rlAddress;
            ConstraintLayout clSubject;

            public SpaHeaderHolder(View view) {
                super(view);
                banner = (ImageBanner) view.findViewById(R.id.banner);
                ivAdd = (ImageView) view.findViewById(R.id.iv_add);
                ivMsg = (ImageView) view.findViewById(R.id.iv_msg);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTime = (TextView) view.findViewById(R.id.tv_time);
                tvAddress = (TextView) view.findViewById(R.id.tv_place);
                tvIntroduction = (TextView) view.findViewById(R.id.tv_introduction);
                tvShowAll = (TextView) view.findViewById(R.id.tv_expend);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                rlAddress = (RelativeLayout) view.findViewById(R.id.rl_address);
                clSubject = (ConstraintLayout) view.findViewById(R.id.cl_subject);
            }
        }

        public class HeaderHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public HeaderHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public class FooterHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public FooterHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.tv_title);
            }
        }

        public class MasseurHolder extends RecyclerView.ViewHolder {
            MasseurView masseurView;
            public MasseurHolder(View view) {
                super(view);
                masseurView = (MasseurView) view;
            }
        }

        public class MassageHolder extends RecyclerView.ViewHolder {
            MassageView massageView;
            public MassageHolder(View view) {
                super(view);
                massageView = (MassageView) view;
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa_header, parent, false);
                    return new SpaHeaderHolder(view);
                case TYPE_MASSAGE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_massage, parent, false);
                    return new MassageHolder(view);
                case TYPE_MASSEUR:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur, parent, false);
                    return new MasseurHolder(view);
//                case TYPE_CASE:
//                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_case, parent, false);
//                    return new CaseHolder(view);
                case TYPE_MASSEUR_HEADER:
                case TYPE_POST_HEADER:
                case TYPE_MASSAGE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_text, parent, false);
                    return new HeaderHolder(view);
                case TYPE_MASSEUR_FOOTER:
                case TYPE_POST_FOOTER:
                case TYPE_MASSAGE_FOOTER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_see_more, parent, false);
                    return new FooterHolder(view);
//                case TYPE_CASE_SUBJECT:
//                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
//                    return new SubjectHolder(view);
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
                case TYPE_MASSAGE_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.popular_massage));
                    break;
                case TYPE_MASSEUR_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.masseur));
                    break;
                case TYPE_POST_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.post_title));
                    break;
                case TYPE_MASSAGE_FOOTER:
                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_product), spaDetail.getTotalMassageCount()));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(SpaActivity.this, ListWithCategoryActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Constants.PARAM_TITLE, getString(R.string.spa_appointment));
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                            b.putLong(Constants.PARAM_SPA_ID, spaDetail.getMassage().getKey());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                    break;
                case TYPE_MASSEUR_FOOTER:
                    ((FooterHolder) holder).textView.setText(getString(R.string.all_masseur));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(SpaActivity.this, ListActivity.class);
                            Bundle b = new Bundle();
                            b.putLong(Constants.PARAM_SPA_ID, spaDetail.getMassage().getKey());
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSEUR);
                            b.putString(Constants.PARAM_TITLE, getString(R.string.masseur_list));
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    break;
//                case TYPE_CASE_FOOTER:
//                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_case), hospitalDetail.getTotalCase()));
//                    break;


//                case TYPE_CASE:
//                    CaseHolder caseHolder = (CaseHolder) holder;
//                    caseHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                        }
//                    });
//                    caseHolder.imgBefore.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                        }
//                    });
//                    caseHolder.imgAfter.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                        }
//                    });
//                    break;

                case TYPE_MASSEUR:
                    final Masseur masseur = (Masseur)itemList.get(position).getContent();
                    MasseurHolder masseurHolder = (MasseurHolder) holder;
                    masseurHolder.masseurView.setData(masseur, position, true);
                    break;
                case TYPE_HEADER:
                    final SpaHeaderHolder spaHeaderHolder = (SpaHeaderHolder) holder;
                    spaHeaderHolder.tvName.setText(spaDetail.getMassage().getMassageName());
                    spaHeaderHolder.tvTime.setText(Tools.convertTime(spaDetail.getMassage().getOpenTimeStart())+ "-" + Tools.convertTime(spaDetail.getMassage().getOpenTimeOver()));
//                    spaHeaderHolder.tvAppointment.setText(String.format(getString(R.string.masseur_appointment), spaDetail.get));
                    if(spaDetail.getTypeList()!=null && spaDetail.getTypeList().size()>0) {
                        List<String> subjects = new ArrayList<>();
                        for(Type t:spaDetail.getTypeList())
                            subjects.add(t.getTypeName() + " " + t.getProduct_num()+getString(R.string.appointment));
                        spaHeaderHolder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                            @Override
                            public void onItemClick(String content, int position) {

                            }
                        });
                    }else
                        spaHeaderHolder.clSubject.setVisibility(View.GONE);
                    final List<String> mUrls = Tools.mapToList(Tools.gsonStringToMap(spaDetail.getMassage().getMassagePhotos()));
                    List<String> bannerUrls = new ArrayList<>();
                    if(mUrls.size()<4)
                        bannerUrls = mUrls;
                    else{
                        for(int i=0;i<3;i++)
                            bannerUrls.add(mUrls.get(i));
                    }
                    spaHeaderHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    HorizontalImageAdapter mSAdapter = new HorizontalImageAdapter(mContext, mUrls);
                    spaHeaderHolder.recyclerView.setAdapter(mSAdapter);

                    spaHeaderHolder.banner.setRatio(1);
                    spaHeaderHolder.banner.setPlacehoderResource(R.drawable.placeholder_post_2);
                    if(bannerUrls!=null && bannerUrls.size()>0) {
                        spaHeaderHolder.banner.setSource(bannerUrls);
                        spaHeaderHolder.banner.startScroll();
                        if (bannerUrls.size()==1)
                            spaHeaderHolder.banner.pauseScroll();
                    }
//                    spaHeaderHolder.banner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
//                        @Override
//                        public void onBannerItemClicked(int position) {
//                            Intent i = new Intent(SpaActivity.this, ImageViewerActivity.class);
//                            Bundle b = new Bundle();
//                            b.putSerializable(Constants.PARAM_DATA, (Serializable)mUrls);
//                            b.putInt(Constants.PARAM_ID, position);
//                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
//                            i.putExtras(b);
//                            startActivity(i);
//                        }
//                    });
                    spaHeaderHolder.rlAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(SpaActivity.this, MapViewActivity.class);
                            Bundle b= new Bundle();
                            b.putParcelable(Constants.PARAM_DATA, new LatLng(spaDetail.getMassage().getLat(),spaDetail.getMassage().getLon()));
                            b.putString(Constants.PARAM_TITLE, spaDetail.getMassage().getMassageName());
                            b.putString(Constants.PARAM_HOSPITAL_ID, spaDetail.getMassage().getMassageAddress());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    int total = 0;
                    for(Type t:spaDetail.getTypeList())
                        total = total + t.getProduct_num();
                    spaHeaderHolder.tvAppointment.setText(String.format(getString(R.string.spa_appointment1), total));
                    spaHeaderHolder.tvAddress.setText(spaDetail.getMassage().getProvinceName() +
                            spaDetail.getMassage().getCityName() +
                            spaDetail.getMassage().getAreaName() +
                            spaDetail.getMassage().getMassageAddress());
                    if(spaDetail.getMassage().getMassageInfo().length()<50){
                        spaHeaderHolder.tvIntroduction.setText(spaDetail.getMassage().getMassageInfo());
                        spaHeaderHolder.tvShowAll.setVisibility(View.GONE);
                    }else {
                        final String displayData1 = spaDetail.getMassage().getMassageInfo().substring(0, 50) + "...";
                        spaHeaderHolder.tvIntroduction.setText(displayData1);
                        spaHeaderHolder.tvShowAll.setTag(false);
                        spaHeaderHolder.tvShowAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!(boolean) view.getTag()) {
                                    spaHeaderHolder.tvIntroduction.setText(spaDetail.getMassage().getMassageInfo());
                                    view.setTag(true);
                                    spaHeaderHolder.tvShowAll.setText(R.string.see_part);
                                } else {
                                    spaHeaderHolder.tvIntroduction.setText(displayData1);
                                    view.setTag(false);
                                    spaHeaderHolder.tvShowAll.setText(R.string.see_all);
                                }

                            }
                        });
                    }
                    if(isCollected)
                        spaHeaderHolder.ivAdd.setImageResource(R.mipmap.ic_click);
                    else
                        spaHeaderHolder.ivAdd.setImageResource(R.mipmap.ic_add);
                    spaHeaderHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isCollected) {
                                SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
                                    @Override
                                    public void onNext(String result) {
                                        isCollected = false;
                                        Tools.Toast(SpaActivity.this, getString(R.string.remove_fav_item_success));
                                        notifyItemChanged(position);
                                    }
                                    @Override
                                    public void reload() {
                                        loadData(Constants.LOAD_DIALOG);
                                    }
                                };
                                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, SpaActivity.this),
                                        HOTRSharePreference.getInstance(SpaActivity.this.getApplicationContext()).getUserID(),  Arrays.asList(spaDetail.getMassage().getKey()), 4);
                            }else{
                                SubscriberListener mListener = new SubscriberListener<String>() {
                                    @Override
                                    public void onNext(String result) {
                                        Tools.Toast(SpaActivity.this, getString(R.string.fav_item_success));
                                        isCollected = true;
                                        notifyItemChanged(position);
                                    }
                                };
                                Properties prop = new Properties();
                                prop.setProperty("id", spaDetail.getMassage().getKey()+"");
                                StatService.trackCustomKVEvent(mContext, Constants.MTA_ID_FAV_SPA, prop);
                                ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, SpaActivity.this),
                                        HOTRSharePreference.getInstance(SpaActivity.this.getApplicationContext()).getUserID(), spaDetail.getMassage().getKey(), 4);
                            }
                        }
                    });
                    break;

                case TYPE_MASSAGE:
                    final Massage massage = (Massage) itemList.get(position).getContent();
                    final MassageHolder massageHolder = (MassageHolder) holder;
                    massageHolder.massageView.setData(massage, -1);
                    break;


//                case TYPE_CASE_SUBJECT:
//                    SubjectHolder subjectHolder = (SubjectHolder) holder;
//                    subjects = new ArrayList<>(Arrays.asList("鼻部 23", "面部轮廓 5", "胸部 7", "鼻部 23", "面部轮廓 5", "胸部 7"));
//                    subjectHolder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(String content, int position) {
//                            mContext.startActivity(new Intent(mContext, ListWithCategoryActivity.class));
//                        }
//                    });
//                    break;



            }

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
                                return 1;
                            default:
                                return 2;
                        }
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return itemList.size();
        }

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
    }
}
