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

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.HorizontalImageAdapter;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.MapViewActivity;
import com.us.hotr.ui.view.CaseView;
import com.us.hotr.ui.view.DoctorView;
import com.us.hotr.ui.view.ProductView;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetHospitalDetailResponse;
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
 * Created by Mloong on 2017/9/8.
 */

public class HospitalActivity extends BaseLoadingActivity {

    private RecyclerView mRecyclerView;
    private HospitalAdapter mAdapter;

    private long mHospitalId;
    private boolean isCollected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHospitalId = getIntent().getExtras().getLong(Constants.PARAM_ID);

        setMyTitle(R.string.hospital_title);
        ivShare.setVisibility(View.GONE);
        initStaticView();
        loadData(Constants.LOAD_PAGE);

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

    @Override
    protected void loadData(int type) {
        SubscriberListener mListener = new SubscriberListener<GetHospitalDetailResponse>() {
            @Override
            public void onNext(GetHospitalDetailResponse result) {
                if(result == null || result.getDetail() == null){
                    showErrorPage();
                    return;
                }
                isCollected = result.getIs_collected()==1?true:false;
                mAdapter = new HospitalAdapter(HospitalActivity.this, result);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                mRecyclerView.setAdapter(myBaseAdapter);
            }
        };
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getHospitalDetail(new LoadingSubscriber(mListener, this),
                    mHospitalId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getHospitalDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    mHospitalId, HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    public class HospitalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private GetHospitalDetailResponse hospitalDetail;
        private List<Item> itemList = new ArrayList<>();
        private Context mContext;

        private final int TYPE_HEADER = 101;
        private final int TYPE_PRODUCT_HEADER = 102;
        private final int TYPE_PRODUCT = 103;
        private final int TYPE_PRODUCT_FOOTER = 104;
        private final int TYPE_DOCTOR_HEADER = 105;
        private final int TYPE_DOCTOR = 106;
        private final int TYPE_DOCTOR_FOOTER = 107;
        private final int TYPE_CASE_HEADER = 108;
        private final int TYPE_CASE = 109;
        private final int TYPE_CASE_FOOTER = 110;
        private final int TYPE_CASE_SUBJECT = 111;

        public HospitalAdapter(Context mContext, GetHospitalDetailResponse hospitalDetail) {
            this.hospitalDetail = hospitalDetail;
            this.mContext = mContext;
            if(hospitalDetail.getCaseTypeList()!=null && hospitalDetail.getCaseTypeList().size()>0){
                int count = 0;
                for(Type type:hospitalDetail.getCaseTypeList())
                    count = count + type.getProduct_num();
                hospitalDetail.getCaseTypeList().add(0, new Type(-1, count, getString(R.string.all)));
            }
            itemList.add(new Item(TYPE_HEADER));
            if(hospitalDetail.getDoctorList()!=null && hospitalDetail.getDoctorList().size()>0) {
                itemList.add(new Item(TYPE_DOCTOR_HEADER));
                for(int i=0;i<hospitalDetail.getDoctorList().size();i++)
                    itemList.add(new Item(TYPE_DOCTOR, hospitalDetail.getDoctorList().get(i)));
                if(hospitalDetail.getTotalDoctor()>3)
                    itemList.add(new Item(TYPE_DOCTOR_FOOTER));
            }
            if(hospitalDetail.getProductList()!=null && hospitalDetail.getProductList().size()>0){
                itemList.add(new Item(TYPE_PRODUCT_HEADER));
                for(int i=0;i<hospitalDetail.getProductList().size();i++)
                    itemList.add(new Item(TYPE_PRODUCT, hospitalDetail.getProductList().get(i)));
                if(hospitalDetail.getTotalProduct()>3)
                    itemList.add(new Item(TYPE_PRODUCT_FOOTER));
            }
            if(hospitalDetail.getCaseList()!=null && hospitalDetail.getCaseList().size()>0){
                itemList.add(new Item(TYPE_CASE_HEADER));
                itemList.add(new Item(TYPE_CASE_SUBJECT, hospitalDetail.getCaseTypeList()));
                for(int i=0;i<hospitalDetail.getCaseList().size();i++)
                    itemList.add(new Item(TYPE_CASE, hospitalDetail.getCaseList().get(i)));
                if(hospitalDetail.getTotalCase()>3)
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

        public class HospitalHeaderHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar, ivAdd, ivMsg, ivCertified;
            TextView tvNumConsult, tvNumCase, tvNumPointment, tvName, tvTitle, tvHospital, tvAddress, tvIndroduction, tvShowAll;
            FlowLayout flSubject;
            RelativeLayout rlAddress;
            RecyclerView recyclerView;
            ConstraintLayout clSubject;

            public HospitalHeaderHolder(View view) {
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
                tvAddress = (TextView) view.findViewById(R.id.tv_place);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);
                rlAddress = (RelativeLayout) view.findViewById(R.id.rl_address);
                tvIndroduction = (TextView) view.findViewById(R.id.tv_introduction);
                tvShowAll = (TextView) view.findViewById(R.id.tv_expend);
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
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

        public class DoctorHolder extends RecyclerView.ViewHolder {
            DoctorView doctorView;
            public DoctorHolder(View view) {
                super(view);
                doctorView = (DoctorView) view;
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
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital_header, parent, false);
                    return new HospitalHeaderHolder(view);
                case TYPE_PRODUCT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
                    return new ProductHolder(view);
                case TYPE_DOCTOR:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
                    return new DoctorHolder(view);
                case TYPE_CASE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
                    return new CaseHolder(view);
                case TYPE_DOCTOR_HEADER:
                case TYPE_CASE_HEADER:
                case TYPE_PRODUCT_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_text, parent, false);
                    return new HeaderHolder(view);
                case TYPE_DOCTOR_FOOTER:
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
                case TYPE_DOCTOR_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.hospital_doctor));
                    break;
                case TYPE_CASE_HEADER:
                    ((HeaderHolder) holder).textView.setText(getString(R.string.case_title));
                    break;
                case TYPE_PRODUCT_FOOTER:
                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_product), hospitalDetail.getTotalProduct()));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(HospitalActivity.this, ListWithCategoryActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Constants.PARAM_TITLE, getString(R.string.hospital_appointment));
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
                            b.putLong(Constants.PARAM_HOSPITAL_ID, hospitalDetail.getDetail().getHospital_id());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                    break;
                case TYPE_DOCTOR_FOOTER:
                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_doctor), hospitalDetail.getTotalDoctor()));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(HospitalActivity.this, ListActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Constants.PARAM_TITLE, getString(R.string.doctor_list));
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_DOCTOR);
                            b.putLong(Constants.PARAM_HOSPITAL_ID, hospitalDetail.getDetail().getHospital_id());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    break;
                case TYPE_CASE_FOOTER:
                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_case), hospitalDetail.getTotalCase()));
                    ((FooterHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(mContext, ListWithCategoryActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                            b.putLong(Constants.PARAM_SUBJECT_ID, -1);
                            b.putLong(Constants.PARAM_HOSPITAL_ID, hospitalDetail.getDetail().getHospital_id());
                            b.putString(Constants.PARAM_TITLE, getString(R.string.case_title));
                            b.putSerializable(Constants.PARAM_DATA, (Serializable) hospitalDetail.getCaseTypeList());
                            i.putExtras(b);
                            mContext.startActivity(i);
                        }
                    });
                    break;


                case TYPE_CASE:
                    CaseHolder caseHolder = (CaseHolder) holder;
                    caseHolder.caseView.setData((Case)itemList.get(position).getContent());
                    caseHolder.caseView.enableEdit(false);
                    break;

                case TYPE_DOCTOR:
                    final Doctor doctor = (Doctor) itemList.get(position).getContent();
                    DoctorHolder doctorHolder = (DoctorHolder) holder;
                    doctorHolder.doctorView.setData(doctor);
                    if(position < getItemCount()-1 && !(itemList.get(position+1).getContent() instanceof Doctor))
                        doctorHolder.doctorView.showDivider(false);
                    else
                        doctorHolder.doctorView.showDivider(true);
                    break;




                case TYPE_HEADER:
                    final HospitalHeaderHolder hospitalHeaderHolder = (HospitalHeaderHolder) holder;
                    hospitalHeaderHolder.tvHospital.setText(hospitalDetail.getDetail().getHospital_type());
                    hospitalHeaderHolder.tvAddress.setText(hospitalDetail.getDetail().getHospital_address());
                    hospitalHeaderHolder.rlAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(HospitalActivity.this, MapViewActivity.class);
                            Bundle b= new Bundle();
                            b.putParcelable(Constants.PARAM_DATA, new LatLng(hospitalDetail.getDetail().getLat(),hospitalDetail.getDetail().getLon()));
                            b.putString(Constants.PARAM_TITLE, hospitalDetail.getDetail().getHospital_name());
                            b.putString(Constants.PARAM_HOSPITAL_ID, hospitalDetail.getDetail().getHospital_address());
                            i.putExtras(b);
                            startActivity(i);

                        }
                    });
                    hospitalHeaderHolder.tvName.setText(hospitalDetail.getDetail().getHospital_name());
                    hospitalHeaderHolder.tvNumCase.setText(hospitalDetail.getDetail().getCase_num()+"");
                    hospitalHeaderHolder.tvNumConsult.setText("759");
                    hospitalHeaderHolder.tvNumPointment.setText(hospitalDetail.getDetail().getOrder_num()+"");
                    hospitalHeaderHolder.tvTitle.setText(hospitalDetail.getDetail().getHospital_style());
                    Glide.with(mContext)
                            .load(hospitalDetail.getDetail().getHospital_logo())
                            .error(R.drawable.placeholder_post3)
                            .placeholder(R.drawable.placeholder_post3)
                            .into(hospitalHeaderHolder.ivAvatar);
                    if(hospitalDetail.getTypeList()!=null && hospitalDetail.getTypeList().size()>0) {
                        hospitalHeaderHolder.clSubject.setVisibility(View.VISIBLE);
                        List<String> subjects = new ArrayList<>();
                        for (Type s : hospitalDetail.getTypeList())
                            subjects.add(s.getTypeName() + " " + s.getProduct_num()+getString(R.string.appointment));
                        hospitalHeaderHolder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                            @Override
                            public void onItemClick(String content, int position) {

                            }
                        });
                    }else{
                        hospitalHeaderHolder.clSubject.setVisibility(View.GONE);
                    }
                    if(hospitalDetail.getDetail().getHospital_photos()!=null && !hospitalDetail.getDetail().getHospital_photos().isEmpty()) {
                        hospitalHeaderHolder.recyclerView.setVisibility(View.VISIBLE);
                        List<String> photoes = Arrays.asList(hospitalDetail.getDetail().getHospital_photos().split("\\s*,\\s*"));
                        hospitalHeaderHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        HorizontalImageAdapter hAdapter = new HorizontalImageAdapter(mContext, photoes);
                        hospitalHeaderHolder.recyclerView.setAdapter(hAdapter);
                    }else
                        hospitalHeaderHolder.recyclerView.setVisibility(View.GONE);
                    if(hospitalDetail.getDetail().getHospital_info() !=null && !hospitalDetail.getDetail().getHospital_info().isEmpty()) {
                        hospitalHeaderHolder.tvIndroduction.setVisibility(View.VISIBLE);
                        final String mData = hospitalDetail.getDetail().getHospital_info();
                        if (mData.length() > 50) {
                            final String displayData = mData.substring(0, 50) + "...";
                            hospitalHeaderHolder.tvIndroduction.setText(displayData);
                            hospitalHeaderHolder.tvShowAll.setTag(false);
                            hospitalHeaderHolder.tvShowAll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!(boolean) view.getTag()) {
                                        hospitalHeaderHolder.tvIndroduction.setText(mData);
                                        view.setTag(true);
                                        hospitalHeaderHolder.tvShowAll.setText(R.string.see_part);
                                    } else {
                                        hospitalHeaderHolder.tvIndroduction.setText(displayData);
                                        view.setTag(false);
                                        hospitalHeaderHolder.tvShowAll.setText(R.string.see_all);
                                    }

                                }
                            });
                        } else {
                            hospitalHeaderHolder.tvShowAll.setVisibility(View.GONE);
                            hospitalHeaderHolder.tvIndroduction.setText(mData);
                        }
                    }else
                        hospitalHeaderHolder.tvIndroduction.setVisibility(View.GONE);
                    if(isCollected)
                        hospitalHeaderHolder.ivAdd.setImageResource(R.mipmap.ic_click);
                    else
                        hospitalHeaderHolder.ivAdd.setImageResource(R.mipmap.ic_add);
                    hospitalHeaderHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!isCollected) {
                                SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
                                    @Override
                                    public void onNext(String result) {
                                        isCollected = true;
                                        Tools.Toast(HospitalActivity.this, getString(R.string.fav_item_success));
                                        notifyItemChanged(position);
                                    }
                                    @Override
                                    public void reload() {
                                        loadData(Constants.LOAD_DIALOG);
                                    }
                                };
                                ServiceClient.getInstance().favoriteItem(new ProgressSubscriber(mListener, HospitalActivity.this),
                                        HOTRSharePreference.getInstance(HospitalActivity.this.getApplicationContext()).getUserID(), hospitalDetail.getDetail().getHospital_id(), 1);
                            }else{
                                SubscriberListener mListener = new SubscriberListener<String>() {
                                    @Override
                                    public void onNext(String result) {
                                        isCollected = false;
                                        Tools.Toast(HospitalActivity.this, getString(R.string.remove_fav_item_success));
                                        notifyItemChanged(position);
                                    }
                                };
                                ServiceClient.getInstance().removeFavoriteItem(new ProgressSubscriber(mListener, HospitalActivity.this),
                                        HOTRSharePreference.getInstance(HospitalActivity.this.getApplicationContext()).getUserID(), Arrays.asList(hospitalDetail.getDetail().getHospital_id()), 1);
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
                            b.putLong(Constants.PARAM_HOSPITAL_ID, hospitalDetail.getDetail().getHospital_id());
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
