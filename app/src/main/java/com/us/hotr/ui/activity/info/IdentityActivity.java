package com.us.hotr.ui.activity.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemPicker;
import com.us.hotr.customview.MyDatePicker;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.CreateNewMasseurRequest;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.storage.bean.MasseurExtraData;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;

public class IdentityActivity extends BaseActivity {
    public static final int TYPE_NEW = 1;
    public static final int TYPE_EDIT = 2;

    private EditText etNickname, etRealname, etId, etIntroduction;
    private RadioButton rbYes, rbNo;
    private ImageView ivHealthyCert, ivOtherCert, ivReject;
    private TextView tvHealthyCertDate, tvWorkTime, tvHeight, tvReject, tvSubmit;
    private RecyclerView rvSpecialize, rvPhoto;
    private RelativeLayout rlReject;
    private ConstraintLayout clTitle, clRealname, clId, clGender, clWorkTime;

    private String healthyCertFilePath = "", otherCertFilePath = "", healthyCertDate = "", workTime = "";
    private int height = 0;
    private SpecializeAdapter specializeAdapter;
    private PhotoAdapter photoAdapter;
    private List<String> photoList = new ArrayList<>();
    private MasseurExtraData masseurData;
    @Override
    protected int getLayout() {
        return R.layout.activity_identity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(getIntent().getExtras().getString(Constants.PARAM_TITLE));
        masseurData = (MasseurExtraData)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        initStaticView();
    }

    private void initStaticView() {
        etNickname = (EditText) findViewById(R.id.et_nickname);
        etRealname = (EditText) findViewById(R.id.et_realname);
        etId = (EditText) findViewById(R.id.et_id);
        etIntroduction = (EditText) findViewById(R.id.et_introduction);
        rbYes = (RadioButton) findViewById(R.id.rb_yes);
        rbNo = (RadioButton) findViewById(R.id.rb_no);
        ivHealthyCert = (ImageView) findViewById(R.id.iv_health_certificate);
        ivOtherCert = (ImageView) findViewById(R.id.iv_other_certificate);
        tvHealthyCertDate = (TextView) findViewById(R.id.tv_health_certificate_date);
        tvWorkTime = (TextView) findViewById(R.id.tv_work_time);
        tvHeight = (TextView) findViewById(R.id.tv_height);
        rvSpecialize = (RecyclerView) findViewById(R.id.rv_specialize);
        rvPhoto = (RecyclerView) findViewById(R.id.rv_photo);
        tvReject = (TextView) findViewById(R.id.tv_reject);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        ivReject = (ImageView) findViewById(R.id.iv_reject);
        rlReject = (RelativeLayout) findViewById(R.id.rl_reject);
        clTitle = (ConstraintLayout) findViewById(R.id.cl_title);
        clRealname = (ConstraintLayout) findViewById(R.id.cl_realname);
        clId = (ConstraintLayout) findViewById(R.id.cl_id);
        clGender = (ConstraintLayout) findViewById(R.id.cl_gender);
        clWorkTime = (ConstraintLayout) findViewById(R.id.cl_worktime);

        if(getIntent().getExtras().getInt(Constants.PARAM_TYPE) == TYPE_EDIT){
            clTitle.setVisibility(View.GONE);
            clRealname.setVisibility(View.GONE);
            clId.setVisibility(View.GONE);
            clGender.setVisibility(View.GONE);
            clWorkTime.setVisibility(View.GONE);
        }

        ivHealthyCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxGalleryFinal
                        .with(IdentityActivity.this)
                        .cropHideBottomControls(true)
                        .image()
                        .radio()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                healthyCertFilePath = imageRadioResultEvent.getResult().getOriginalPath();
                                Bitmap bitmap = Tools.decodeFile(imageRadioResultEvent.getResult().getOriginalPath(), ivHealthyCert.getWidth(), ivHealthyCert.getHeight());
                                ivHealthyCert.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ivHealthyCert.setImageBitmap(bitmap);
                            }
                        })
                        .openGallery();
                RxGalleryFinalApi.setImgSaveRxSDCard("HOTR");
            }
        });

        ivOtherCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxGalleryFinal
                        .with(IdentityActivity.this)
                        .cropHideBottomControls(true)
                        .image()
                        .radio()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                otherCertFilePath = imageRadioResultEvent.getResult().getOriginalPath();
                                Bitmap bitmap = Tools.decodeFile(imageRadioResultEvent.getResult().getOriginalPath(), ivOtherCert.getWidth(), ivOtherCert.getHeight());
                                ivOtherCert.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ivOtherCert.setImageBitmap(bitmap);
                            }
                        })
                        .openGallery();
                RxGalleryFinalApi.setImgSaveRxSDCard("HOTR");
            }
        });

        tvHealthyCertDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                showSelectCertTimeDialog();
            }
        });

        tvWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                showSelectWorkTimeDialog();
            }
        });

        tvHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                showSelectHeightDialog();
            }
        });

        rvSpecialize.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
        rvSpecialize.setItemAnimator(new DefaultItemAnimator());
        SubscriberListener mListener = new SubscriberListener<BaseListResponse<List<Subject>>>() {
            @Override
            public void onNext(BaseListResponse<List<Subject>> result) {
                if (result != null && result.getRows() != null && result.getRows().size() > 0) {
                    specializeAdapter = new SpecializeAdapter(result.getRows());
                    rvSpecialize.setAdapter(specializeAdapter);

                    if(masseurData!= null && masseurData.getTypeIdArr()!=null)
                        specializeAdapter.setSelectedSubjects(masseurData.getTypeIdArr().split(","));
                }
            }
        };
        ServiceClient.getInstance().getMassageTypeList(new SilentSubscriber(mListener, this, null));

        rvPhoto.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        rvPhoto.setItemAnimator(new DefaultItemAnimator());
        photoAdapter = new PhotoAdapter();
        rvPhoto.setAdapter(photoAdapter);

        if(masseurData != null){
            if(masseurData.getCheckState() == 2){
                rlReject.setVisibility(View.VISIBLE);
                tvReject.setText(String.format(getString(R.string.reject_reason), masseurData.getRejectReason()));
                ivReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlReject.setVisibility(View.GONE);
                    }
                });
            }else
                rlReject.setVisibility(View.GONE);
            if(masseurData.getHealthCertificateTime()!=null) {
                tvHealthyCertDate.setText(Tools.getDate(getApplicationContext(), masseurData.getHealthCertificateTime()));
                healthyCertDate = masseurData.getHealthCertificateTime();
            }
            if(masseurData.getHealthCertificate()!=null) {
                Glide.with(this).load(masseurData.getHealthCertificate()).error(R.mipmap.holder_post).into(ivHealthyCert);
                ivHealthyCert.setScaleType(ImageView.ScaleType.CENTER_CROP);
                healthyCertFilePath = masseurData.getHealthCertificate();
            }
            if(masseurData.getPracticeLicenseCode() == 1){
                rbYes.setChecked(true);
                rbNo.setChecked(false);
            }else {
                rbYes.setChecked(false);
                rbNo.setChecked(true);
            }
            if(masseurData.getIdCode()!=null)
                etId.setText(masseurData.getIdCode());
            if(masseurData.getJobStartTime()!=null) {
                tvWorkTime.setText(Tools.getDate(getApplicationContext(), masseurData.getJobStartTime()));
                workTime = masseurData.getJobStartTime();
            }
            if(masseurData.getMassagistHeight()!=null) {
                tvHeight.setText(masseurData.getMassagistHeight() + "");
                height = masseurData.getMassagistHeight();
            }
            if(masseurData.getMassagistInfo()!=null)
                etIntroduction.setText(masseurData.getMassagistInfo());
            if(masseurData.getRealName()!=null)
                etRealname.setText(masseurData.getRealName());
            if(masseurData.getMassagistName()!=null)
                etNickname.setText(masseurData.getMassagistName());
            if(masseurData.getPracticeLicense()!=null) {
                Glide.with(this).load(masseurData.getPracticeLicense()).error(R.mipmap.holder_post).into(ivOtherCert);
                ivHealthyCert.setScaleType(ImageView.ScaleType.CENTER_CROP);
                otherCertFilePath = masseurData.getPracticeLicense();
            }
            if(masseurData.getCheckState() == 2){
                if(masseurData.getPhotosUnchecked()!=null && !masseurData.getPhotosUnchecked().isEmpty()) {
                    photoList = Tools.mapToList(Tools.gsonStringToMap(masseurData.getPhotosUnchecked()));
                    photoAdapter.notifyDataSetChanged();
                }
            }else{
                if(masseurData.getMassagistPhotos()!=null && !masseurData.getMassagistPhotos().isEmpty()) {
                    photoList = Tools.mapToList(Tools.gsonStringToMap(masseurData.getMassagistPhotos()));
                    photoAdapter.notifyDataSetChanged();
                }
            }
        }

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit(){
        if(etNickname.getText().toString().trim().isEmpty())
            Tools.Toast(this, getString(R.string.input) + getString(R.string.nick_name1));
        else if(etRealname.getText().toString().trim().isEmpty())
            Tools.Toast(this, getString(R.string.input) + getString(R.string.real_name));
        else if(etId.getText().toString().trim().isEmpty())
            Tools.Toast(this, getString(R.string.input) + getString(R.string.id));
        else if (rbYes.isChecked() &&  (healthyCertFilePath.isEmpty() || healthyCertDate.isEmpty()))
            Tools.Toast(this, getString(R.string.please_upload_healthy_certificate1));
        else if (workTime.isEmpty())
            Tools.Toast(this, getString(R.string.pls_choose) + getString(R.string.work_time));
        else if (height == 0)
            Tools.Toast(this, getString(R.string.pls_choose) + getString(R.string.title_height1));
        else if (specializeAdapter!= null && specializeAdapter.getSelectedSubjects().isEmpty())
            Tools.Toast(this, getString(R.string.pls_choose) + getString(R.string.specialize));
        else if (photoList.size() == 0)
            Tools.Toast(this, getString(R.string.pls_upload_photo));
        else{
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    for(String path:photoList){
                        if(!path.contains("http"))
                            new File(path).delete();
                    }
                    saveMasseurToLocal();
                    Tools.Toast(IdentityActivity.this, result);
                }
            };
            CreateNewMasseurRequest request = new CreateNewMasseurRequest();
            request.setPracticeLicense(otherCertFilePath);
            request.setHealthCertificate(healthyCertFilePath);
            request.setHealthCertificateTime(healthyCertDate);
            request.setIdCode(etId.getText().toString().trim());
            request.setJobStartTime(workTime);
            request.setMassagistHeight(height);
            request.setMassagistInfo(etIntroduction.getText().toString().trim());
            request.setMassagistName(etNickname.getText().toString().trim());
            request.setRealName(etRealname.getText().toString().trim());
            if(masseurData!=null && masseurData.getKey()!=null)
                request.setKey(masseurData.getKey());
            if(rbYes.isChecked())
                request.setPracticeLicenseCode(1);
            else
                request.setPracticeLicenseCode(0);
            boolean isCreate;
            if(masseurData!=null && masseurData.getMassagistPhotos() != null)
                isCreate = false;
            else
                isCreate = true;
            ServiceClient.getInstance().editMasseur(new ProgressSubscriber(mListener, this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), request, photoList, specializeAdapter.getSelectedSubjects(), isCreate);
        }
    }

    private void saveMasseurToLocal(){
        SubscriberListener mListener = new SubscriberListener<MasseurExtraData>() {
            @Override
            public void onNext(MasseurExtraData result) {
                HOTRSharePreference.getInstance(getApplicationContext()).storeMasseurInfo(result);
                setResult(RESULT_OK);
                finish();
            }
        };
        ServiceClient.getInstance().masseurCheckState(new SilentSubscriber(mListener, this, null),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID());
    }

    private void openGallary(final ImageView myView, final int position){
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxGalleryListener
                        .getInstance()
                        .setRadioImageCheckedListener(
                                new IRadioImageCheckedListener() {
                                    @Override
                                    public void cropAfter(Object t){
                                        File fromPic = (File)t;
                                        if(position<photoList.size())
                                            photoList.set(position, fromPic.getAbsolutePath());
                                        else
                                            photoList.add(position, fromPic.getAbsolutePath());
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public boolean isActivityFinish() {
                                        return true;
                                    }
                                });
                RxGalleryFinalApi.setImgSaveRxSDCard("HOTR");
                RxGalleryFinalApi.setImgSaveRxCropSDCard("HOTR/crop");

                RxGalleryFinal
                        .with(IdentityActivity.this)
                        .cropHideBottomControls(true)
                        .image()
                        .radio()
                        .cropAspectRatioOptions(0, new AspectRatio("3:4", 30, 40))
                        .crop()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
//                                Toast.makeText(getBaseContext(), "选中了图片路径：" + imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .openGallery();
            }
        });

    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivPic, ivDelete;

            public MyViewHolder(View view) {
                super(view);
                ivPic = (ImageView) view.findViewById(R.id.iv_pic);
                ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            }
        }

        @Override
        public PhotoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_masseur_image, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if(position<photoList.size()) {
                final String photoPath = photoList.get(position);
                if(photoPath.contains("http")){
                    Glide.with(IdentityActivity.this).load(photoPath).error(R.mipmap.holder_post).into(holder.ivPic);
                }else {
                    Bitmap bitmap = Tools.decodeFile(photoPath, 226, 226);
                    holder.ivPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    holder.ivPic.setImageBitmap(bitmap);
                }
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File file = new File(photoList.get(position));
                        file.delete();
                        photoList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }else {
                holder.ivDelete.setVisibility(View.GONE);
                holder.ivPic.setImageResource(R.mipmap.holder_post);
                holder.ivPic.setScaleType(ImageView.ScaleType.CENTER);
                openGallary(holder.ivPic, position);
            }

        }

        @Override
        public int getItemCount() {
            if(photoList!=null && photoList.size()>=0){
                if(photoList.size() == 12)
                    return 12;
                else
                    return photoList.size() + 1;
            }else
                return 0;
        }
    }

    public class SpecializeAdapter extends RecyclerView.Adapter<SpecializeAdapter.SubjectViewHolder> {
        private List<Subject> subjectList;
        private List<Boolean> isSubjectSelectedList = new ArrayList<>();
        public class SubjectViewHolder extends RecyclerView.ViewHolder {
            CheckBox cbSubject;

            public SubjectViewHolder(View view) {
                super(view);
                cbSubject = (CheckBox) view.findViewById(R.id.cb_subject);
            }
        }

        public SpecializeAdapter(List<Subject> subjectList) {
            this.subjectList = subjectList;
            for(int i=0;i<subjectList.size();i++)
                isSubjectSelectedList.add(false);
        }

        public void setSelectedSubjects(String[] selectedSubjects){
            for(int i=0;i<subjectList.size();i++){
                boolean found = false;
                for(int j=0;j<selectedSubjects.length;j++){
                    if(subjectList.get(i).getKey() == Long.parseLong(selectedSubjects[j])){
                        found = true;
                        break;
                    }
                }
                if(found)
                    isSubjectSelectedList.set(i, true);
                else
                    isSubjectSelectedList.set(i, false);
            }
            notifyDataSetChanged();
        }

        public List<Long> getSelectedSubjects(){
            List<Long> result = new ArrayList<>();
            for(int i=0;i<subjectList.size();i++){
                if(isSubjectSelectedList.get(i)){
                    result.add(subjectList.get(i).getKey());
                }
            }
            return result;
        }

        @Override
        public SpecializeAdapter.SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_masseur_specialize, parent, false);

            return new SubjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SubjectViewHolder holder, final int position) {
            holder.cbSubject.setText(subjectList.get(position).getTypeName());
            if(isSubjectSelectedList.get(position))
                holder.cbSubject.setChecked(true);
            else
                holder.cbSubject.setChecked(false);
            holder.cbSubject.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isSubjectSelectedList.set(position, isChecked);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(subjectList!=null && subjectList.size()>=0)
                return subjectList.size();
            else
                return 0;
        }
    }

    private void showSelectHeightDialog(){
        final String[] heightList = new String[70];
        for(int i = 0; i<70; i++)
            heightList[i] = (i+150)+"";
        ItemPicker itemPicker = new ItemPicker.Builder(IdentityActivity.this)
                .setData(heightList)
                .defalutItem(tvHeight.getText().toString().trim())
                .build();
        itemPicker.show();

        itemPicker.setOnItemClickListener(new ItemPicker.OnItemClickListener() {
            @Override
            public void onSelected(String itemSelected, int position) {
                tvHeight.setText(itemSelected);
                tvHeight.setTextColor(getResources().getColor(R.color.text_black));
                height = position + 150;
            }

            @Override
            public void onCancel() {

            }
        });
    }

//    private void showSelectWorkTimeDialog(){
//        final String[] yearList = new String[30];
//        for(int i = 0; i<30; i++)
//            yearList[i] = String.format(getString(R.string.year_number), i+1);
//        ItemPicker itemPicker = new ItemPicker.Builder(IdentityActivity.this)
//                .setData(yearList)
//                .defalutItem(tvWorkTime.getText().toString().trim())
//                .build();
//        itemPicker.show();
//
//        itemPicker.setOnItemClickListener(new ItemPicker.OnItemClickListener() {
//            @Override
//            public void onSelected(String itemSelected, int position) {
//                tvWorkTime.setText(itemSelected);
//                tvWorkTime.setTextColor(getResources().getColor(R.color.text_black));
//                workYear = position + 1;
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
//    }

    private void showSelectCertTimeDialog() {
        Calendar calCreate=Calendar.getInstance();
        if(!healthyCertDate.isEmpty()) {
            try {
                calCreate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(workTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        MyDatePicker datePicker = new MyDatePicker.Builder(IdentityActivity.this)
                .year(calCreate.get(Calendar.YEAR))
                .month(calCreate.get(Calendar.MONTH))
                .day(calCreate.get(Calendar.DATE))
                .build();
        datePicker.show();

        datePicker.setOnCityItemClickListener(new MyDatePicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                String s = String.format(getString(R.string.date1), citySelected[0], citySelected[1], citySelected[2]);
                healthyCertDate = citySelected[0]+"-"+citySelected[1]+"-"+citySelected[2];
                tvHealthyCertDate.setText(s);
                tvHealthyCertDate.setTextColor(getResources().getColor(R.color.text_black));
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void showSelectWorkTimeDialog() {
        Calendar calCreate=Calendar.getInstance();
        if(!workTime.isEmpty()) {
            try {
                calCreate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(workTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        MyDatePicker datePicker = new MyDatePicker.Builder(IdentityActivity.this)
                .year(calCreate.get(Calendar.YEAR))
                .month(calCreate.get(Calendar.MONTH))
                .day(calCreate.get(Calendar.DATE))
                .setForward(false)
                .build();
        datePicker.show();

        datePicker.setOnCityItemClickListener(new MyDatePicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                String s = String.format(getString(R.string.date1), citySelected[0], citySelected[1], citySelected[2]);
                workTime = citySelected[0]+"-"+citySelected[1]+"-"+citySelected[2];
                tvWorkTime.setText(s);
                tvWorkTime.setTextColor(getResources().getColor(R.color.text_black));
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etId.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        for(String path:photoList){
            if(!path.contains("http"))
                new File(path).delete();
        }
        super.onBackPressed();

    }
}
