package com.us.hotr.ui.activity.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.ItemPicker;
import com.us.hotr.customview.ShapedImageView;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.UpdateUserRequest;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;

/**
 * Created by Mloong on 2017/9/26.
 */

public class EditInfoActivity extends BaseActivity {

    private ConstraintLayout clAvatar, clGender, clAge, clCity, clSex;
    private ShapedImageView ivAvatar;
    private TextView tvGender, tvAge, tvCity, tvSex, tvSave;
    private EditText etName, etIntro;
    private Integer gender = null, age = null, sex = null;
    private String province = null, city = null, avatar = null;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        setMyTitle(R.string.edit_title);
        initStaticView();
    }

    private void initStaticView(){
        clAvatar = (ConstraintLayout) findViewById(R.id.cl_avatar);
        ivAvatar = (ShapedImageView) findViewById(R.id.iv_avatar);
        clGender = (ConstraintLayout) findViewById(R.id.cl_gender);
        clAge = (ConstraintLayout) findViewById(R.id.cl_age);
        clCity = (ConstraintLayout) findViewById(R.id.cl_city);
        clSex = (ConstraintLayout) findViewById(R.id.cl_sex);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvCity = (TextView) findViewById(R.id.tv_area);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvSave = (TextView) findViewById(R.id.tv_save);
        etName = (EditText) findViewById(R.id.et_nickname);
        etIntro = (EditText) findViewById(R.id.et_intro);
        if(mUser.getHead_portrait()!=null && !mUser.getHead_portrait().isEmpty()){
            Glide.with(this).load(mUser.getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
            avatar = mUser.getHead_portrait();
        }
        etName.setText(mUser.getNickname());
        if(mUser!=null) {
            tvGender.setText(getResources().getStringArray(R.array.gender)[mUser.getGender()]);
            tvSex.setText(getResources().getStringArray(R.array.sex)[mUser.getOrientation() + 1]);
            tvAge.setText(String.format(getString(R.string.age_number), mUser.getAge()));
            gender = mUser.getGender();
            age =mUser.getAge();
            sex =mUser.getOrientation();
        }
        if((mUser.getProvince_name()!= null && !mUser.getProvince_name().isEmpty())
                ||(mUser.getCity_name()!=null && !mUser.getProvince_name().isEmpty())) {
            tvCity.setText(mUser.getProvince_name() + mUser.getCity_name());
            province = mUser.getProvince_name();
            city = mUser.getCity_name();
        }
        etIntro.setText(mUser.getSignature());

        clAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxGalleryFinal
                        .with(EditInfoActivity.this)
                        .cropHideBottomControls(true)
                        .cropOvalDimmedLayer(true)
                        .image()
                        .radio()
                        .cropAspectRatioOptions(0, new AspectRatio("1:1", 10, 10))
                        .crop()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                Toast.makeText(getBaseContext(), "选中了图片路径：" + imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .openGallery();
            }
        });

        RxGalleryListener
                .getInstance()
                .setRadioImageCheckedListener(
                        new IRadioImageCheckedListener() {
                            @Override
                            public void cropAfter(Object t) {
                                File file = (File)t;
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                ivAvatar.setImageBitmap(bitmap);
                                avatar = "aaa";
                            }

                            @Override
                            public boolean isActivityFinish() {
                                return true;
                            }
                        });
        RxGalleryFinalApi.setImgSaveRxSDCard("HOTR");
        RxGalleryFinalApi.setImgSaveRxCropSDCard("HOTR/crop");

        clGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                ItemPicker itemPicker = new ItemPicker.Builder(EditInfoActivity.this)
                        .textSize(14)
                        .setData(getResources().getStringArray(R.array.gender))
                        .title("")
                        .titleBackgroundColor(getResources().getColor(R.color.divider2))
                        .confirTextColor(getResources().getColor(R.color.blue))
                        .cancelTextColor(getResources().getColor(R.color.blue))
                        .textColor(getResources().getColor(R.color.text_grey2))
                        .itemCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .defalutItem(getResources().getStringArray(R.array.gender)[mUser.getGender()])
                        .build();
                itemPicker.show();

                itemPicker.setOnItemClickListener(new ItemPicker.OnItemClickListener() {
                    @Override
                    public void onSelected(String itemSelected, int position) {
                        tvGender.setTextColor(getResources().getColor(R.color.text_black));
                        tvGender.setText(itemSelected);
                        gender = position;
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        clSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                ItemPicker itemPicker = new ItemPicker.Builder(EditInfoActivity.this)
                        .textSize(14)
                        .setData(getResources().getStringArray(R.array.sex))
                        .title("")
                        .titleBackgroundColor(getResources().getColor(R.color.divider2))
                        .confirTextColor(getResources().getColor(R.color.blue))
                        .cancelTextColor(getResources().getColor(R.color.blue))
                        .textColor(getResources().getColor(R.color.text_grey2))
                        .itemCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .defalutItem(getResources().getStringArray(R.array.sex)[mUser.getOrientation() + 1])
                        .build();
                itemPicker.show();

                itemPicker.setOnItemClickListener(new ItemPicker.OnItemClickListener() {
                    @Override
                    public void onSelected(String itemSelected, int position) {
                        tvSex.setText(itemSelected);
                        tvSex.setTextColor(getResources().getColor(R.color.text_black));
                        sex = position;
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        final String[] ageList = new String[82];
        for(int i = 0; i<82; i++)
            ageList[i] = String.format(getString(R.string.age_number), i+18);
        clAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                ItemPicker itemPicker = new ItemPicker.Builder(EditInfoActivity.this)
                        .textSize(14)
                        .setData(ageList)
                        .title("")
                        .titleBackgroundColor(getResources().getColor(R.color.divider2))
                        .confirTextColor(getResources().getColor(R.color.blue))
                        .cancelTextColor(getResources().getColor(R.color.blue))
                        .textColor(getResources().getColor(R.color.text_grey2))
                        .itemCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .defalutItem(String.format(getString(R.string.age_number), mUser.getAge()))
                        .build();
                itemPicker.show();

                itemPicker.setOnItemClickListener(new ItemPicker.OnItemClickListener() {
                    @Override
                    public void onSelected(String itemSelected, int position) {
                        tvAge.setText(itemSelected);
                        tvAge.setTextColor(getResources().getColor(R.color.text_black));
                        age = position + 18;
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        clCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                CityPicker cityPicker = new CityPicker.Builder(EditInfoActivity.this)
                        .textSize(14)
                        .title("")
                        .titleBackgroundColor(getResources().getColor(R.color.divider2))
                        .confirTextColor(getResources().getColor(R.color.blue))
                        .cancelTextColor(getResources().getColor(R.color.blue))
                        .textColor(getResources().getColor(R.color.text_grey2))
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .provinceCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .province(mUser.getProvince_name())
                        .city(mUser.getCity_name())
                        .build();
                cityPicker.show();

                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        String s = "";
                        for(int i=0;i<citySelected.length - 1;i++)
                            s = s + citySelected[i] + " ";
                        tvCity.setText(s);
                        tvCity.setTextColor(getResources().getColor(R.color.text_black));
                        province = citySelected[0];
                        city = citySelected[1];
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString().trim().isEmpty())
                    Tools.Toast(EditInfoActivity.this, getString(R.string.key_in_name1));
                else if(gender==null)
                    Tools.Toast(EditInfoActivity.this, getString(R.string.choose_gender));
                else if(sex==null)
                    Tools.Toast(EditInfoActivity.this, getString(R.string.choose_sex));
                else if(age==null)
                    Tools.Toast(EditInfoActivity.this, getString(R.string.choose_age));
                else if(province==null || city==null)
                    Tools.Toast(EditInfoActivity.this, getString(R.string.choose_city));
                else if(etIntro.getText().toString().trim().isEmpty())
                    Tools.Toast(EditInfoActivity.this, getString(R.string.key_in_intro));
                else if(avatar==null)
                    Tools.Toast(EditInfoActivity.this, getString(R.string.choose_avatar));
                else{
                    updateUserInfo();
                }
            }
        });
    }

    private void updateUserInfo(){
        UpdateUserRequest request = new UpdateUserRequest();
        request.setAge(age);
        request.setCityName(city);
        request.setGender(gender);
        request.setNickname(etName.getText().toString().trim());
        request.setOrientation(sex);
        request.setProvinceName(province);
        request.setSignature(etIntro.getText().toString().trim());

        SubscriberListener mListener = new SubscriberListener<User>() {
            @Override
            public void onNext(User result) {
            }
        };
        ServiceClient.getInstance().updateUserDetail(new ProgressSubscriber(mListener, EditInfoActivity.this),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), request);
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_edit_personal_info;
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }
}
