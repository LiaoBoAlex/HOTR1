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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.CityPicker;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Address;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mloong on 2017/10/24.
 */

public class EditDeliverAddressActivity extends BaseActivity {

    private EditText etName, etPhone, etAddress, etEmail;
    private TextView tvCity, tvSave;
    private ConstraintLayout clCity, clDefault;
    private SwitchCompat swDefalut;
    private Address address;

    private boolean isCitySelected = false, isChanged = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_deliver_address;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(getIntent().getExtras().getInt(Constants.PARAM_TITLE));
        if(getIntent().getExtras()!=null)
            address = (Address)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        if(address == null) {
            address = new Address();
            address.setDefaultAddress(1);
        }
        initStaticView();
    }

    private void initStaticView(){
        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        etEmail = (EditText) findViewById(R.id.et_email);
        tvCity = (TextView) findViewById(R.id.tv_subjects);
        tvSave = (TextView) findViewById(R.id.tv_save);
        clCity = (ConstraintLayout) findViewById(R.id.cl_city);
        clDefault = (ConstraintLayout) findViewById(R.id.cl_defalut);
        swDefalut = (SwitchCompat) findViewById(R.id.sw_default);
        if(address.getPersonName()!=null && !address.getPersonName().isEmpty())
            etName.setText(address.getPersonName());
        if(address.getTelephone()!=null && !address.getTelephone().isEmpty())
            etPhone.setText(address.getTelephone());
        if(address.getDetailAddr()!=null && !address.getDetailAddr().isEmpty())
            etAddress.setText(address.getDetailAddr());
        if(address.getUserEmail()!=null && !address.getUserEmail().isEmpty())
            etEmail.setText(address.getUserEmail());
        if(address.getProvinceName()!=null && !address.getProvinceName().isEmpty()
                && address.getCityName()!=null && !address.getCityName().isEmpty()
                && address.getStreetName()!=null && !address.getStreetName().isEmpty()) {
            tvCity.setText(address.getProvinceName() + " "
                    + address.getCityName() + " "
                    + address.getStreetName());
            isCitySelected = true;
        }
        if(address.getDefaultAddress() == 1)
            swDefalut.setChecked(true);
        else
            swDefalut.setChecked(false);

        clCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                CityPicker cityPicker = new CityPicker.Builder(EditDeliverAddressActivity.this)
                        .province(address.getProvinceName())
                        .city(address.getCityName())
                        .district(address.getStreetName())
                        .build();
                cityPicker.show();

                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        address.setProvinceName(citySelected[0]);
                        address.setCityName(citySelected[1]);
                        address.setStreetName(citySelected[2]);
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
                else if(etEmail.getText().toString().trim().isEmpty())
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.key_in_email));
                else if(!validateEmail(etEmail.getText().toString().trim()))
                    Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.wrong_email_format));
                else{
                    saveAddress();
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

    private boolean validateEmail(String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void saveAddress(){
        hideKeyBoard();
        address.setPersonName(etName.getText().toString().trim());
        address.setDetailAddr(etAddress.getText().toString().trim());
        address.setTelephone(etPhone.getText().toString().trim());
        address.setUserEmail(etEmail.getText().toString().trim());
        if(swDefalut.isChecked())
            address.setDefaultAddress(1);
        else
            address.setDefaultAddress(0);
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                if(address.getDefaultAddress() == 1)
                    HOTRSharePreference.getInstance(getApplicationContext()).storeDefaultAddress(address);
                setResult(RESULT_OK);
                Tools.Toast(EditDeliverAddressActivity.this, getString(R.string.password_changed));
                finish();
            }
        };
        ServiceClient.getInstance().addDeliveryAddress(new ProgressSubscriber(mListener, this),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), address);
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
