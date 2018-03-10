package com.us.hotr.ui.activity.beauty;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.view.CaseView;
import com.us.hotr.ui.view.ProductView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetDoctorDetailResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mloong on 2017/9/7.
 */

public class DoctorActivity extends BaseLoadingActivity {

    private RecyclerView mRecyclerView;
    private DoctorAdapter mAdapter;

    private long mDoctorId;
    private boolean isCollected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDoctorId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        ivShare.setVisibility(View.GONE);
        initStaticView();
        loadData(Constants.LOAD_PAGE);

    }

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<GetDoctorDetailResponse>() {
            @Override
            public void onNext(GetDoctorDetailResponse result) {
                if(result == null || result.getDetail() == null){
                    showErrorPage();
                    return;
                }
                isCollected = result.getIs_collected()==1?true:false;
                mAdapter = new DoctorAdapter(DoctorActivity.this, result);
                mRecyclerView.setAdapter(mAdapter);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                mRecyclerView.setAdapter(myBaseAdapter);
            }
        };
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getDoctorDetail(new LoadingSubscriber(mListener, this),
                    mDoctorId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getDoctorDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    mDoctorId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        else if (type == Constants.LOAD_DIALOG)
            ServiceClient.getInstance().getDoctorDetail(new ProgressSubscriber(mListener, this),
                    mDoctorId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_doctor;
    }

    private void initStaticView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        enableLoadMore(false);
    }

    public class DoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private GetDoctorDetailResponse doctorDetail;
        private List<Item> itemList = new ArrayList<>();
        private Context mContext;

        private final int TYPE_HEADER = 101;
        private final int TYPE_PRODUCT_HEADER = 102;
        private final int TYPE_PRODUCT = 103;
        private final int TYPE_PRODUCT_FOOTER = 104;
        private final int TYPE_CASE_HEADER = 108;
        private final int TYPE_CASE = 109;
        private final int TYPE_CASE_FOOTER = 110;
        private final int TYPE_CASE_SUBJECT = 111;

        public DoctorAdapter(Context mContext, GetDoctorDetailResponse doctorDetail) {
            this.doctorDetail = doctorDetail;
            this.mContext = mContext;
            itemList.add(new Item(TYPE_HEADER));
            if(doctorDetail.getCaseTypeList()!=null && doctorDetail.getCaseTypeList().size()>0){
                int count = 0;
                for(Type type:doctorDetail.getCaseTypeList())
                    count = count + type.getProduct_num();
                doctorDetail.getCaseTypeList().add(0, new Type(-1, count, getString(R.string.all)));
            }
            if(doctorDetail.getProductList()!=null && doctorDetail.getProductList().size()>0){
                itemList.add(new Item(TYPE_PRODUCT_HEADER));
                for(int i=0;i<doctorDetail.getProductList().size();i++)
                    itemList.add(new Item(TYPE_PRODUCT, doctorDetail.getProductList().get(i)));
                if(doctorDetail.getTotalProduct()>3)
                    itemList.add(new Item(TYPE_PRODUCT_FOOTER));
            }
            if(doctorDetail.getCaseList()!=null && doctorDetail.getCaseList().size()>0){
                itemList.add(new Item(TYPE_CASE_HEADER));
                itemList.add(new Item(TYPE_CASE_SUBJECT, doctorDetail.getCaseTypeList()));
                for(int i=0;i<doctorDetail.getCaseList().size();i++)
                    itemList.add(new Item(TYPE_CASE, doctorDetail.getCaseList().get(i)));
                if(doctorDetail.getTotalCase()>3)
                    itemList.add(new Item(TYPE_CASE_FOOTER));
            }
        }

        public class CaseHolder extends RecyclerView.ViewHolder {
            CaseView caseView;

            public CaseHolder(View view) {
                super(view);
                caseView = (CaseView) view;
            }
        }


        public class SubjectHolder extends RecyclerView.ViewHolder {
            FlowLayout flSubject;

            public SubjectHolder(View view) {
                super(view);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);
            }
        }

        public class DoctorHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar, ivAdd, ivMsg, ivCertified;
            TextView tvNumConsult, tvNumCase, tvNumPointment, tvName, tvTitle, tvHospital, tvHospitalInfo;
            FlowLayout flSubject;
            RelativeLayout rlDoctor, rlHospital;
            ConstraintLayout clSubject;

            public DoctorHeaderHolder(View view) {
                super(view);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
                ivAdd = (ImageView) view.findViewById(R.id.iv_add);
                ivMsg = (ImageView) view.findViewById(R.id.iv_msg);
                ivCertified = (ImageView) view.findViewById(R.id.iv_certified);
                tvNumConsult = (TextView) view.findViewById(R.id.tv_num_consult);
                tvNumCase = (TextView) view.findViewById(R.id.tv_num_case);
                tvNumPointment = (TextView) view.findViewById(R.id.tv_num_appointment);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvHospital = (TextView) view.findViewById(R.id.tv_address);
                tvHospitalInfo = (TextView) view.findViewById(R.id.tv_hospital_info);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);
                rlDoctor = (RelativeLayout) view.findViewById(R.id.rl_doctor_info);
                rlHospital = (RelativeLayout) view.findViewById(R.id.rl_hospital_info);
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

        public class ProductHolder extends RecyclerView.ViewHolder {
            ProductView productView;
            public ProductHolder(View view) {
                super(view);
                productView = (ProductView) view;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_header, parent, false);
                    return new DoctorHeaderHolder(view);
                case TYPE_PRODUCT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                    return new ProductHolder(view);
                case TYPE_CASE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
                    return new CaseHolder(view);
                case TYPE_CASE_HEADER:
                case TYPE_PRODUCT_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_text, parent, false);
                    return new HeaderHolder(view);
                case TYPE_PRODUCT_FOOTER:
                case TYPE_CASE_FOOTER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_see_more, parent, false);
                    return new FooterHolder(view);
                case TYPE_CASE_SUBJECT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
                    return new SubjectHolder(view);
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
                case TYPE_PRODUCT_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.hospital_product));
                    break;
                case TYPE_CASE_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.case_title));
                    break;
                case TYPE_PRODUCT_FOOTER:
                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_product), doctorDetail.getTotalProduct()));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(DoctorActivity.this, ListWithCategoryActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Constants.PARAM_TITLE, getString(R.string.case_title));
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                            b.putLong(Constants.PARAM_DOCTOR_ID, doctorDetail.getDetail().getDoctor_id());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                    break;
                case TYPE_CASE_FOOTER:
                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_case), doctorDetail.getTotalCase()));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(mContext, ListWithCategoryActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                            b.putLong(Constants.PARAM_SUBJECT_ID, -1);
                            b.putLong(Constants.PARAM_DOCTOR_ID, doctorDetail.getDetail().getDoctor_id());
                            b.putString(Constants.PARAM_TITLE, getString(R.string.case_title));
                            b.putSerializable(Constants.PARAM_DATA, (Serializable) doctorDetail.getCaseTypeList());
                            i.putExtras(b);
                            mContext.startActivity(i);
                        }
                    });
                    break;


                case TYPE_CASE:
                    CaseHolder caseHolder = (CaseHolder) holder;
                    caseHolder.caseView.setData((Case)itemList.get(position).getContent());
                    break;

                case TYPE_HEADER:
                    final DoctorHeaderHolder doctorHeaderHolder = (DoctorHeaderHolder) holder;

                    doctorHeaderHolder.tvHospital.setText(doctorDetail.getDetail().getHospital_name());
                    doctorHeaderHolder.tvHospitalInfo.setText(doctorDetail.getDetail().getHospital_name());
                    doctorHeaderHolder.tvName.setText(doctorDetail.getDetail().getDoctor_name());
                    doctorHeaderHolder.tvNumCase.setText("1256");
                    doctorHeaderHolder.tvNumConsult.setText("759");
                    doctorHeaderHolder.tvNumPointment.setText(doctorDetail.getDetail().getOrder_num()+"");
                    doctorHeaderHolder.tvTitle.setText(doctorDetail.getDetail().getDoctor_job());
                    (Glide.with(mContext).load(doctorDetail.getDetail().getDoctor_main_img())).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(doctorHeaderHolder.ivAvatar);
                    List<String> subjects = new ArrayList<>();
                    if(doctorDetail.getTypeList()!=null && doctorDetail.getTypeList().size()>0) {
                        doctorHeaderHolder.clSubject.setVisibility(View.VISIBLE);
                        for (Type s : doctorDetail.getTypeList())
                            subjects.add(s.getTypeName() + " " + s.getProduct_num()+getString(R.string.appointment));
                        doctorHeaderHolder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                            @Override
                            public void onItemClick(String content, int position) {
                            }
                        });
                    }else{
                        doctorHeaderHolder.clSubject.setVisibility(View.GONE);
                    }

                    doctorHeaderHolder.rlHospital.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(mContext, HospitalActivity.class);
                            Bundle b = new Bundle();
                            b.putLong(Constants.PARAM_ID, doctorDetail.getDetail().getHospital_id());
                            i.putExtras(b);
                            mContext.startActivity(i);
                        }
                    });
                    doctorHeaderHolder.rlDoctor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(mContext, DoctorInfoActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.PARAM_DATA, doctorDetail.getDetail());
                            i.putExtras(b);
                            mContext.startActivity(i);
                        }
                    });
                    if(isCollected)
                        doctorHeaderHolder.ivAdd.setImageResource(R.mipmap.ic_click);
                    else
                        doctorHeaderHolder.ivAdd.setImageResource(R.mipmap.ic_add);
                    doctorHeaderHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!isCollected) {
                                SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
                                    @Override
                                    public void onNext(String result) {
                                        isCollected = true;
                                        Tools.Toast(DoctorActivity.this, getString(R.string.fav_item_success));
                                        notifyItemChanged(position);
                                    }
                                    @Override
                                    public void reload() {
                                        loadData(Constants.LOAD_DIALOG);
                                    }
                                };
                                ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, DoctorActivity.this),
                                        HOTRSharePreference.getInstance(DoctorActivity.this.getApplicationContext()).getUserID(), doctorDetail.getDetail().getDoctor_id(), 2);
                            }else{
                                SubscriberListener mListener = new SubscriberListener<String>() {
                                    @Override
                                    public void onNext(String result) {
                                        isCollected = false;
                                        Tools.Toast(DoctorActivity.this, getString(R.string.remove_fav_item_success));
                                        notifyItemChanged(position);
                                    }
                                };
                                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, DoctorActivity.this),
                                        HOTRSharePreference.getInstance(DoctorActivity.this.getApplicationContext()).getUserID(), Arrays.asList(doctorDetail.getDetail().getDoctor_id()), 2);
                            }
                        }
                    });
                    break;

                case TYPE_PRODUCT:
                    final Product product = (Product)itemList.get(position).getContent();
                    ProductHolder productHolder = (ProductHolder) holder;
                    productHolder.productView.setData(product);
                    if(position < getItemCount()-1 && !(itemList.get(position+1).getContent() instanceof Product))
                        productHolder.productView.showDivider(false);
                    else
                        productHolder.productView.showDivider(true);
                    break;


                case TYPE_CASE_SUBJECT:
                    SubjectHolder subjectHolder = (SubjectHolder) holder;
                    List<String> list = new ArrayList<>();
                    final List<Type> typeList = (List<Type>)itemList.get(position).getContent();
                    for(Type type:typeList)
                        list.add(type.getTypeName() + " " + type.getProduct_num());
                    subjectHolder.flSubject.setFlowLayout(list, new FlowLayout.OnItemClickListener() {
                        @Override
                        public void onItemClick(String content, int p) {
                            Intent i = new Intent(mContext, ListWithCategoryActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                            b.putString(Constants.PARAM_TITLE, getString(R.string.case_title));
                            b.putLong(Constants.PARAM_DOCTOR_ID, doctorDetail.getDetail().getDoctor_id());
                            b.putLong(Constants.PARAM_SUBJECT_ID, typeList.get(p).getTypeId());
                            b.putSerializable(Constants.PARAM_DATA, (Serializable) typeList);
                            i.putExtras(b);
                            mContext.startActivity(i);
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
