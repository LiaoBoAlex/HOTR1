package com.us.hotr.ui.activity.party;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.MyDatePicker;
import com.us.hotr.storage.bean.Contact;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.util.Tools;

/**
 * Created by Mloong on 2017/10/24.
 */

public class BuyerInfoActivity extends BaseActivity {

    private EditText etName, etDoc, etPhone;
    private TextView tvDocType, tvDocDate, tvSave;
    private ImageView ivMale, ivFemale;
    private LinearLayout llMale, llFemale;
    private ConstraintLayout clDocDate;
    private int type;
    private Contact mContact;
    private String ValideDate;
    // this is a dot "\u25cf"

    private boolean isMale = true, isDateSelected = false;
    @Override
    protected int getLayout() {
        return R.layout.activity_buy_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.buyer_info);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        mContact = (Contact) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        if(mContact == null)
            mContact = new Contact();
        initStaticView();
    }

    private void initStaticView(){
        etName = (EditText) findViewById(R.id.et_name);
        etDoc = (EditText) findViewById(R.id.et_doc);
        etPhone = (EditText) findViewById(R.id.et_phone);
        tvDocDate = (TextView) findViewById(R.id.tv_doc_date);
        tvDocType = (TextView) findViewById(R.id.tv_type);
        ivMale = (ImageView) findViewById(R.id.iv_male);
        ivFemale = (ImageView) findViewById(R.id.iv_female);
        llMale = (LinearLayout) findViewById(R.id.ll_male);
        llFemale = (LinearLayout) findViewById(R.id.ll_female);
        clDocDate = (ConstraintLayout) findViewById(R.id.cl_doc_date);
        tvSave = (TextView) findViewById(R.id.tv_save);

        if(type == Constants.REQUIRE_HK_PASS)
            tvDocType.setText(R.string.hk_pass);
        if(type == Constants.REQUIRE_PASSPORT)
            tvDocType.setText(R.string.passport);

        if(mContact!=null){
            if(mContact.getGender() == 0){
                isMale = true;
                ivMale.setImageResource(R.mipmap.ic_address_clicked);
                ivFemale.setImageResource(R.mipmap.ic_address_click);
            }else{
                isMale = false;
                ivFemale.setImageResource(R.mipmap.ic_address_clicked);
                ivMale.setImageResource(R.mipmap.ic_address_click);
            }
            if(mContact.getPurchaser_credentials_numb()!=null && !mContact.getPurchaser_credentials_numb().isEmpty())
                etDoc.setText(mContact.getPurchaser_credentials_numb());
            if(mContact.getPurchaser_name()!=null && !mContact.getPurchaser_name().isEmpty())
                etName.setText(mContact.getPurchaser_name());
            if(mContact.getTerm_of_validity()!=null && !mContact.getTerm_of_validity().isEmpty()) {
                tvDocDate.setText(mContact.getTerm_of_validity());
                tvDocDate.setTextColor(getResources().getColor(R.color.text_black));
                isDateSelected = true;
            }
            if(mContact.getPurchaser_phone()!=null && !mContact.getPurchaser_phone().isEmpty())
                etPhone.setText(mContact.getPurchaser_phone());
        }

        llMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMale = true;
                ivMale.setImageResource(R.mipmap.ic_address_clicked);
                ivFemale.setImageResource(R.mipmap.ic_address_click);
            }
        });
        llFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMale = false;
                ivFemale.setImageResource(R.mipmap.ic_address_clicked);
                ivMale.setImageResource(R.mipmap.ic_address_click);
            }
        });
        clDocDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                showSelectDateDialog();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString().trim().isEmpty())
                    Tools.Toast(BuyerInfoActivity.this, getString(R.string.key_in_name1));
                else if(etPhone.getText().toString().trim().isEmpty())
                    Tools.Toast(BuyerInfoActivity.this, getString(R.string.key_in_phone1));
                else if(etPhone.getText().toString().trim().length()!=11)
                    Tools.Toast(BuyerInfoActivity.this, getString(R.string.wrong_phone_number_format));
                else if(etDoc.getText().toString().trim().isEmpty())
                    Tools.Toast(BuyerInfoActivity.this, getString(R.string.key_in_doc));
                else if(!isDateSelected)
                    Tools.Toast(BuyerInfoActivity.this, getString(R.string.choose_doc_date));
                else{
                    setupContact();
                    Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, mContact);
                    i.putExtras(b);
                    setResult(100, i);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setupContact();
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, mContact);
        i.putExtras(b);
        setResult(101, i);
        super.onBackPressed();
    }

    private void setupContact(){
        mContact.setGender(isMale?0:1);
        mContact.setPurchaser_credentials_numb(etDoc.getText().toString().trim());
        mContact.setPurchaser_name(etName.getText().toString().trim());
        mContact.setPurchaser_phone(etPhone.getText().toString().trim());
        mContact.setTerm_of_validity(tvDocDate.getText().toString().trim());
        mContact.setValideDate(ValideDate);
    }

    private void showSelectDateDialog() {
        MyDatePicker datePicker = new MyDatePicker.Builder(BuyerInfoActivity.this)
                .build();
        datePicker.show();

        datePicker.setOnCityItemClickListener(new MyDatePicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                String s = String.format(getString(R.string.date1), citySelected[0], citySelected[1], citySelected[2]);
                ValideDate = citySelected[0]+"-"+citySelected[1]+"-"+citySelected[2]+" 24:00:00";
                tvDocDate.setText(s);
                tvDocDate.setTextColor(getResources().getColor(R.color.text_black));
                isDateSelected = true;
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }

}
