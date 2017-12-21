package com.us.hotr.ui.activity.party;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lljjcoder.citypickerview.widget.CityPicker;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;

/**
 * Created by Mloong on 2017/10/24.
 */

public class EditDeliverAddressActivity extends BaseActivity {

    private EditText etName, etPhone, etAddress;
    private TextView tvCity, tvSave;
    private ConstraintLayout clCity, clDefault;
    private SwitchCompat swDefalut;

    private boolean isCitySelected = false, isChanged = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_deliver_address;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.add_new_address);
        initStaticView();
    }

    private void initStaticView(){
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        tvCity = (TextView) findViewById(R.id.tv_area);
        tvSave = (TextView) findViewById(R.id.tv_save);
        clCity = (ConstraintLayout) findViewById(R.id.cl_city);
        clDefault = (ConstraintLayout) findViewById(R.id.cl_defalut);
        swDefalut = (SwitchCompat) findViewById(R.id.sw_default);

        clCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                CityPicker cityPicker = new CityPicker.Builder(EditDeliverAddressActivity.this)
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
                        isCitySelected = true;
                        isChanged = true;
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
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.key_in_name));
                else if(etPhone.getText().toString().trim().isEmpty())
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.key_in_phone));
                else if(etPhone.getText().toString().trim().length()!=11)
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.wrong_phone_number_format));
                else if(!isCitySelected)
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.choose_city));
                else if(etAddress.getText().toString().trim().isEmpty())
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.key_in_address));
                else if(etAddress.getText().toString().trim().length()<5)
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.address_less_than_5));
                else{

                }
            }
        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isChanged = true;

            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isChanged = true;

            }
        });

        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isChanged = true;

            }
        });
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if(isChanged){
            TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(EditDeliverAddressActivity.this);
            alertDialogBuilder.setMessage(getString(R.string.address_changed_without_save));
            alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isChanged = false;
                            EditDeliverAddressActivity.this.onBackPressed();
                        }
                    });
            alertDialogBuilder.setNegativeButton(getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialogBuilder.create().show();
        }else
            super.onBackPressed();
    }
}
