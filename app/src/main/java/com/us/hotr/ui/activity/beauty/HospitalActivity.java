package com.us.hotr.ui.activity.beauty;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.customview.HorizontalImageAdapter;
import com.us.hotr.customview.MyBaseAdapter;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.webservice.response.GetHospitalDetailResponse;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mloong on 2017/9/8.
 */

public class HospitalActivity extends BaseLoadingActivity {

    private RecyclerView mRecyclerView;
    private HospitalAdapter mAdapter;

    private int mHospitalId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHospitalId = getIntent().getExtras().getInt(Constants.PARAM_ID);

        setMyTitle(R.string.hospital_title);
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
                mAdapter = new HospitalAdapter(HospitalActivity.this, result);
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(mAdapter);
                myBaseAdapter.setFooterView();
                mRecyclerView.setAdapter(myBaseAdapter);
            }
        };
        if(type == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getHospitalDetail(new LoadingSubscriber(mListener, this),
                    mHospitalId);
        else if (type == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getHospitalDetail(new SilentSubscriber(mListener, this, refreshLayout),
                    mHospitalId);
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
                itemList.add(new Item(TYPE_CASE_SUBJECT));
                for(int i=0;i<hospitalDetail.getCaseList().size();i++)
                    itemList.add(new Item(TYPE_CASE, hospitalDetail.getCaseList().get(i)));
                if(hospitalDetail.getTotalCase()>3)
                    itemList.add(new Item(TYPE_CASE_FOOTER));
            }
        }


        public class CaseHolder extends RecyclerView.ViewHolder {
            ImageView imgBefore, imgAfter;
            TextView tvSeeMore;
            RelativeLayout rlSeeMore;

            public CaseHolder(View view) {
                super(view);
                imgBefore = (ImageView) view.findViewById(R.id.img_before);
                imgAfter = (ImageView) view.findViewById(R.id.imge_after);
                rlSeeMore = (RelativeLayout) view.findViewById(R.id.rl_see_more);
                tvSeeMore = (TextView) view.findViewById(R.id.tv_see_more);
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
                ivAvatar = (ImageView) view.findViewById(R.id.iv_product_avatar);
                ivAdd = (ImageView) view.findViewById(R.id.iv_add);
                ivMsg = (ImageView) view.findViewById(R.id.iv_msg);
                ivCertified = (ImageView) view.findViewById(R.id.iv_certified);
                tvNumConsult = (TextView) view.findViewById(R.id.tv_num_consult);
                tvNumCase = (TextView) view.findViewById(R.id.tv_num_case);
                tvNumPointment = (TextView) view.findViewById(R.id.tv_num_appointment);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvHospital = (TextView) view.findViewById(R.id.tv_product_fav);
                tvAddress = (TextView) view.findViewById(R.id.tv_address);
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
            TextView tvName, tvTitle1, tvTitle2, tvAppointment, tvCase;
            ImageView ivAvatar;
            FlowLayout flSubject;
            View vDivider;

            public DoctorHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
                tvTitle2 = (TextView) view.findViewById(R.id.tv_title2);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                tvCase = (TextView) view.findViewById(R.id.tv_case);
                ivAvatar = (ImageView) view.findViewById(R.id.img_avator);
                flSubject = (FlowLayout) view.findViewById(R.id.fl_subject);
                vDivider = view.findViewById(R.id.v_divider);

                flSubject.setHorizontalSpacing(6);
                flSubject.setTextPaddingH(14);
            }
        }

        public class ProductHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDoctor, tvHospital, tvAppointment, tvPriceBefore, tvPriceAfter, tvSoldOut;
            ImageView ivAvatar, ivGo, ivOnePrice, ivPromoPrice;
            View vDivider;

            public ProductHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDoctor = (TextView) view.findViewById(R.id.tv_product_doctor);
                tvHospital = (TextView) view.findViewById(R.id.tv_product_fav);
                tvAppointment = (TextView) view.findViewById(R.id.tv_appointment);
                tvPriceBefore = (TextView) view.findViewById(R.id.tv_price_before);
                tvPriceAfter = (TextView) view.findViewById(R.id.tv_pay_amount);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_product_avatar);
                ivGo = (ImageView) view.findViewById(R.id.iv_go);
                vDivider = view.findViewById(R.id.v_divider);
                ivOnePrice = (ImageView) view.findViewById(R.id.iv_one_price);
                ivPromoPrice = (ImageView) view.findViewById(R.id.iv_promo_price);
                tvSoldOut = (TextView) view.findViewById(R.id.tv_sold_out);
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
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compare, parent, false);
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
                            b.putString(Constants.PARAM_TITLE, getString(R.string.product_list));
                            b.putInt(Constants.PARAM_TYPE, Constants.TYPE_PRODUCT);
                            b.putInt(Constants.PARAM_HOSPITAL_ID, hospitalDetail.getDetail().getHospital_id());
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
                            b.putInt(Constants.PARAM_HOSPITAL_ID, hospitalDetail.getDetail().getHospital_id());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    break;
                case TYPE_CASE_FOOTER:
                    ((FooterHolder) holder).textView.setText(String.format(getString(R.string.check_case), hospitalDetail.getTotalCase()));
                    break;


                case TYPE_CASE:
                    CaseHolder caseHolder = (CaseHolder) holder;
                    caseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    caseHolder.imgBefore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    caseHolder.imgAfter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    break;

                case TYPE_DOCTOR:
                    final Doctor doctor = (Doctor) itemList.get(position).getContent();
                    DoctorHolder doctorHolder = (DoctorHolder) holder;
                    doctorHolder.tvName.setText(doctor.getDoctor_name());
                    doctorHolder.tvTitle1.setText(doctor.getDoctor_job());
                    doctorHolder.tvTitle2.setText(doctor.getHospital_name());
                    doctorHolder.tvAppointment.setText(String.format(getString(R.string.num_of_appointment), doctor.getOrder_num()));
                    doctorHolder.tvCase.setText(String.format(getString(R.string.num_of_case), 345) );
                    Glide.with(mContext).load(doctor.getDoctor_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(doctorHolder.ivAvatar);
                    List<String> subjects = new ArrayList<>();
                    if(doctor.getGood_at_project_list()!=null && doctor.getGood_at_project_list().size()>0)
                        for(Doctor.Subject s:doctor.getGood_at_project_list())
                            subjects.add(s.getType_name());
                    doctorHolder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                        @Override
                        public void onItemClick(String content, int position) {

                        }
                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(HospitalActivity.this, DoctorActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable(Constants.PARAM_ID, doctor.getDoctor_id());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    if(position < getItemCount()-1 && !(itemList.get(position+1).getContent() instanceof Doctor))
                        doctorHolder.vDivider.setVisibility(View.GONE);
                    else
                        doctorHolder.vDivider.setVisibility(View.VISIBLE);
                    break;




                case TYPE_HEADER:
                    final HospitalHeaderHolder hospitalHeaderHolder = (HospitalHeaderHolder) holder;
                    hospitalHeaderHolder.tvHospital.setText(hospitalDetail.getDetail().getHospital_type());
                    hospitalHeaderHolder.tvAddress.setText(hospitalDetail.getDetail().getHospital_address());
                    hospitalHeaderHolder.tvName.setText(hospitalDetail.getDetail().getHospital_name());
                    hospitalHeaderHolder.tvNumCase.setText("1256");
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
                        subjects = new ArrayList<>();
                        for (GetHospitalDetailResponse.SubjectCount s : hospitalDetail.getTypeList())
                            subjects.add(s.getType_name() + " " + s.getOrder_num());
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
                    break;

                case TYPE_PRODUCT:
                    final Product product = (Product)itemList.get(position).getContent();
                    ProductHolder productHolder = (ProductHolder) holder;
                    productHolder.tvTitle.setText(product.getProduct_name() + product.getProduct_usp());
                    productHolder.tvDoctor.setText(product.getDoctor_name());
                    productHolder.tvHospital.setText(product.getHospital_name());
                    productHolder.tvAppointment.setText(String.format(getString(R.string.num_of_appointment1), product.getOrder_num()));
                    productHolder.tvPriceBefore.setText(String.format(getString(R.string.price), product.getShop_price()));
                    productHolder.tvPriceAfter.setText(product.getOnline_price()+"");
                    productHolder.tvPriceBefore.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                    Glide.with(mContext).load(product.getProduct_main_img()).placeholder(R.drawable.placeholder_post3).error(R.drawable.placeholder_post3).into(productHolder.ivAvatar);
                    if(product.getPayment_type() == Constants.FULL_PAYMENT)
                        productHolder.ivOnePrice.setVisibility(View.VISIBLE);
                    else
                        productHolder.ivOnePrice.setVisibility(View.GONE);
                    if(product.getProduct_type() == Constants.PROMOTION_PRODUCT){
                        productHolder.ivPromoPrice.setVisibility(View.VISIBLE);
                        if(product.getAmount()>0)
                            productHolder.tvSoldOut.setVisibility(View.GONE);
                        else
                            productHolder.tvSoldOut.setVisibility(View.GONE);
                    }else
                        productHolder.ivPromoPrice.setVisibility(View.GONE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(HospitalActivity.this, ProductActivity.class);
                            Bundle b= new Bundle();
                            b.putInt(Constants.PARAM_ID, product.getProductId());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    if(position < getItemCount()-1 && !(itemList.get(position+1).getContent() instanceof Product))
                        productHolder.vDivider.setVisibility(View.GONE);
                    else
                        productHolder.vDivider.setVisibility(View.VISIBLE);
                    break;


                case TYPE_CASE_SUBJECT:
                    SubjectHolder subjectHolder = (SubjectHolder) holder;
                    subjects = new ArrayList<>(Arrays.asList("鼻部 23", "面部轮廓 5", "胸部 7", "鼻部 23", "面部轮廓 5", "胸部 7"));
                    subjectHolder.flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                        @Override
                        public void onItemClick(String content, int position) {
                            mContext.startActivity(new Intent(mContext, ListWithCategoryActivity.class));
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
