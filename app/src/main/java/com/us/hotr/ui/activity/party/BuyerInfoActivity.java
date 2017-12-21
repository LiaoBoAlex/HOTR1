package com.us.hotr.ui.activity.party;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lljjcoder.citypickerview.widget.MyDatePicker;
import com.us.hotr.R;
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
        initStaticView();
    }

    private void initStaticView(){
        etName = (EditText) findViewById(R.id.et_name);
        etDoc = (EditText) findViewById(R.id.et_doc);
        etPhone = (EditText) findViewById(R.id.et_phone);
        tvDocDate = (TextView) findViewById(R.id.tv_doc_date);
        tvDocType = (TextView) findViewById(R.id.tv_area);
        ivMale = (ImageView) findViewById(R.id.iv_male);
        ivFemale = (ImageView) findViewById(R.id.iv_female);
        llMale = (LinearLayout) findViewById(R.id.ll_male);
        llFemale = (LinearLayout) findViewById(R.id.ll_female);
        clDocDate = (ConstraintLayout) findViewById(R.id.cl_doc_date);
        tvSave = (TextView) findViewById(R.id.tv_save);

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

                }
            }
        });
    }

    private void showSelectDateDialog() {
        MyDatePicker datePicker = new MyDatePicker.Builder(BuyerInfoActivity.this)
                .textSize(14)
                .title("")
                .titleBackgroundColor(getResources().getColor(R.color.divider2))
                .confirTextColor(getResources().getColor(R.color.blue))
                .cancelTextColor(getResources().getColor(R.color.blue))
                .textColor(getResources().getColor(R.color.text_grey2))
                .yearCyclic(false)
                .monthCyclic(false)
                .dayCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .build();
        datePicker.show();

        datePicker.setOnCityItemClickListener(new MyDatePicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                String s = String.format(getString(R.string.date1), citySelected[0], citySelected[1], citySelected[2]);
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
