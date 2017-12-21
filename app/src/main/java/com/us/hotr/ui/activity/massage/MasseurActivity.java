package com.us.hotr.ui.activity.massage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.ImageBanner;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.webservice.response.GetMasseurDetailResponse;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.ImageViewerActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mloong on 2017/9/29.
 */

public class MasseurActivity extends BaseLoadingActivity {

    private RecyclerView mRecyclerView;
    private MasseurAdapter mAdapter;
    private float offset = 0;

    private int mMasseurId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMasseurId = getIntent().getExtras().getInt(Constants.PARAM_ID);
        initStaticView();
        setMyTitle(R.string.masseur_detail);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_masseur;
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        enableLoadMore(false);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    findViewById(R.id.v_divider).setAlpha(1);
                    findViewById(R.id.tb_title).setAlpha(1);
                }else{
                    findViewById(R.id.tb_title).setAlpha(offset / 300);
                    ivBack.setImageResource(R.mipmap.ic_back_dark);
                    ivShare.setImageResource(R.mipmap.ic_share_dark);
                    findViewById(R.id.v_divider).setAlpha(offset / 300);
                }
            }
        });

        loadData(Constants.LOAD_PAGE);
    }


    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<GetMasseurDetailResponse>() {
            @Override
            public void onNext(GetMasseurDetailResponse result) {
                if(result == null || result.getMassagist() == null){
                    showErrorPage();
                    return;
                }
                mAdapter = new MasseurAdapter(MasseurActivity.this, result);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                mRecyclerView.setAdapter(myBaseAdapter);
            }
        };
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getMasseurDetail(new LoadingSubscriber(mListener, this),
                    mMasseurId);
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getMasseurDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    mMasseurId);
    }

    public class MasseurAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private GetMasseurDetailResponse masseurDetail;
        private List<Item> itemList = new ArrayList<>();
        private Context mContext;
        private int selectedPosition = 2;

        private final int TYPE_HEADER = 101;
        private final int TYPE_MASSAGE_HEADER = 102;
        private final int TYPE_MASSAGE = 103;
        private final int TYPE_SPA = 104;
        private final int TYPE_INTERVIEW= 105;
        private final int TYPE_POST_HEADER = 106;
        private final int TYPE_POST = 107;
        private final int TYPE_POST_FOOTER = 108;

        public MasseurAdapter(Context mContext, GetMasseurDetailResponse masseurDetail) {
            this.masseurDetail = masseurDetail;
            this.mContext = mContext;
            itemList.add(new Item(TYPE_HEADER));
            if(masseurDetail.getProductList()!=null && masseurDetail.getProductList().size()>0){
                itemList.add(new Item(TYPE_MASSAGE_HEADER));
                for(int i=0;i<masseurDetail.getProductList().size();i++)
                    itemList.add(new Item(TYPE_MASSAGE, masseurDetail.getProductList().get(i)));
            }
            if(masseurDetail.getMassage()!=null)
                itemList.add(new Item(TYPE_SPA, masseurDetail.getMassage()));
//            if(doctorDetail.getCaseList()!=null && doctorDetail.getCaseList().size()>0){
//                itemList.add(new Item(TYPE_CASE_HEADER));
//                itemList.add(new Item(TYPE_CASE_SUBJECT));
//                for(int i=0;i<doctorDetail.getCaseList().size();i++)
//                    itemList.add(new Item(TYPE_CASE, doctorDetail.getCaseList().get(i)));
//                if(doctorDetail.getTotalCase()>3)
//                    itemList.add(new Item(TYPE_CASE_FOOTER));
//            }
        }

        public int getSelectedProductId(){
            return masseurDetail.getProductList().get(selectedPosition-2).getId();
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

        public class MasseurHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivAdd, ivMsg;
            TextView tvName, tvHeight, tvExperience, tvAppointment;
            FlowLayout flSubject;
            RecyclerView recyclerView;
            ImageBanner banner;
            ConstraintLayout clSubject;

            public MasseurHeaderHolder(View view) {
                super(view);
                banner = (ImageBanner) view.findViewById(R.id.banner);
                ivAdd = (ImageView) view.findViewById(R.id.iv_add);
                ivMsg = (ImageView) view.findViewById(R.id.iv_msg);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvHeight = (TextView) view.findViewById(R.id.tv_height);
                tvExperience = (TextView) view.findViewById(R.id.tv_time);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
                clSubject = (ConstraintLayout) view.findViewById(R.id.cl_subject);
            }
        }

        public class MassageHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDoctor, tvHospital, tvAppointment, tvPriceBefore, tvPriceAfter, tvMin;
            ImageView ivAvatar, ivGo, ivDelete, ivPromoPrice, ivOnePrice;

            public MassageHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDoctor = (TextView) view.findViewById(R.id.tv_product_doctor);
                tvHospital = (TextView) view.findViewById(R.id.tv_product_fav);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                tvPriceBefore = (TextView) view.findViewById(R.id.tv_price_before);
                tvPriceAfter = (TextView) view.findViewById(R.id.tv_pay_amount);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_product_avatar);
                ivGo = (ImageView) view.findViewById(R.id.iv_go);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
                tvMin = (TextView) view.findViewById(R.id.tv_min);
                ivPromoPrice = (ImageView) view.findViewById(R.id.iv_promo_price);
                ivOnePrice = (ImageView) view.findViewById(R.id.iv_one_price);
            }
        }

        public class SpaHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvTime, tvAddress, tvQuery;
            ImageView ivAvatar;

            public SpaHolder(View view){
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_hospital1);
                tvTime = (TextView) view.findViewById(R.id.tv_hospital2);
                tvAddress = (TextView) view.findViewById(R.id.tv_hospital3);
                tvQuery = (TextView) view.findViewById(R.id.tv_query);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_hospital_avatar);

            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masseur_header, parent, false);
                    return new MasseurHeaderHolder(view);
                case TYPE_MASSAGE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                    return new MassageHolder(view);
                case TYPE_MASSAGE_HEADER:
                case TYPE_POST_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_text, parent, false);
                    return new HeaderHolder(view);
                case TYPE_POST_FOOTER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_see_more, parent, false);
                    return new FooterHolder(view);
                case TYPE_SPA:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spa1, parent, false);
                    return new SpaHolder(view);
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
//                case TYPE_POST_HEADER:
//                    ((HeaderHolder) holder).textView.setText(getString(R.string.post_title));
//                    break;
//                case TYPE_POST_FOOTER:
//                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_post), doctorDetail.getTotalCase()));
//                    break;
                case TYPE_MASSAGE:
                    final MassageHolder massageHolder = (MassageHolder) holder;
                    final GetMasseurDetailResponse.Product massage = (GetMasseurDetailResponse.Product)itemList.get(position).getContent();
                    massageHolder.tvTitle.setText(getString(R.string.bracket_left)+massage.getProduct_name()+getString(R.string.bracket_right)+massage.getProduct_usp());
                    massageHolder.tvDoctor.setText(masseurDetail.getMassage().getMassageName());
                    massageHolder.tvHospital.setText("");
                    massageHolder.tvAppointment.setText(String.format(getString(R.string.num_of_appointment1), massage.getOrder_num()));
                    massageHolder.tvPriceBefore.setText(String.format(getString(R.string.price), massage.getShop_price()));
//                    if(massage.getProduct_type() == Constants.PROMOTION_PRODUCT) {
//                        massageHolder.tvPriceAfter.setText(massage.getActivity_price() + "/" + massage.getService_time());
//                        massageHolder.ivPromoPrice.setVisibility(View.VISIBLE);
//                    }
//                    else {
//                        massageHolder.tvPriceAfter.setText(massage.getOnline_price() + "/" + massage.getService_time());
//                        massageHolder.ivPromoPrice.setVisibility(View.GONE);
//                    }
                    massageHolder.tvPriceBefore.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    massageHolder.tvMin.setVisibility(View.VISIBLE);
                    massageHolder.ivOnePrice.setVisibility(View.GONE);
                    Glide.with(MasseurActivity.this).load(Tools.validatePhotoString(massage.getProduct_main_img())).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(massageHolder.ivAvatar);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(MasseurActivity.this, MassageActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_ID, massage.getId());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    if(position == selectedPosition)
                        massageHolder.ivGo.setImageResource(R.mipmap.ic_massage_clicked);
                    else
                        massageHolder.ivGo.setImageResource(R.mipmap.ic_massage_click);
                    massageHolder.ivGo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedPosition = position;
                            notifyDataSetChanged();
                        }
                    });
                    break;

                case TYPE_HEADER:
                    MasseurHeaderHolder masseurHeaderHolder = (MasseurHeaderHolder) holder;
                    masseurHeaderHolder.tvName.setText(masseurDetail.getMassagist().getMassagistName());
                    masseurHeaderHolder.tvHeight.setText(String.format(getString(R.string.height), masseurDetail.getMassagist().getMassagistHeight()));
                    masseurHeaderHolder.tvAppointment.setText(String.format(getString(R.string.masseur_appointment), masseurDetail.getSumOrderNum()));
                    masseurHeaderHolder.tvExperience.setText(String.format(getString(R.string.experience), masseurDetail.getJob_time()));
                    if(masseurDetail.getTypeList()!=null && masseurDetail.getTypeList().size()>0) {
                        List<String> subjects = new ArrayList<>();
                        for(GetMasseurDetailResponse.Type t:masseurDetail.getTypeList())
                            subjects.add(t.getType_name() + " " + t.getOrder_num());
                        masseurHeaderHolder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                            @Override
                            public void onItemClick(String content, int position) {

                            }
                        });
                    }else
                        masseurHeaderHolder.clSubject.setVisibility(View.GONE);

//                masseurHeaderHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//                HorizontalImageAdapter mHAdapter = new HorizontalImageAdapter(mContext);
//                masseurHeaderHolder.recyclerView.setAdapter(mHAdapter);
                    final List<String> photoes = Arrays.asList(Tools.validatePhotoString(masseurDetail.getMassagist().getMassagistPhotos()).split("\\s*,\\s*"));
                    masseurHeaderHolder.banner.setRatio(0.96);
                    masseurHeaderHolder.banner.setSource(photoes);
                    masseurHeaderHolder.banner.setBannerItemClickListener(new ImageBanner.BannerClickListener() {
                        @Override
                        public void onBannerItemClicked(int position) {
                            Intent i = new Intent(MasseurActivity.this, ImageViewerActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.PARAM_DATA, (Serializable)photoes);
                            b.putInt(Constants.PARAM_ID, position);
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    masseurHeaderHolder.banner.startScroll();
                    break;
                case TYPE_SPA:
                    SpaHolder spaHolder = (SpaHolder) holder;
                    spaHolder.tvTitle.setText(masseurDetail.getMassage().getMassageName());
                    Glide.with(MasseurActivity.this).load(masseurDetail.getMassage().getMassageLogo()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(spaHolder.ivAvatar);
                    spaHolder.tvAddress.setText(masseurDetail.getMassage().getProvinceName() +
                            masseurDetail.getMassage().getCityName() +
                            masseurDetail.getMassage().getAreaName() +
                            masseurDetail.getMassage().getMassageAddress());
                    spaHolder.tvTime.setText(Tools.convertTime(masseurDetail.getMassage().getOpenTimeStart()) + "-" + Tools.convertTime(masseurDetail.getMassage().getOpenTimeOver()));
                    spaHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MasseurActivity.this, SpaActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_ID, masseurDetail.getMassage().getKey());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    break;
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
