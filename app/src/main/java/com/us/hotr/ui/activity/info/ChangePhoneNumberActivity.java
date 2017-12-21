package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.util.Tools;

/**
 * Created by Mloong on 2017/10/10.
 */

public class ChangePhoneNumberActivity extends BaseActivity {

    private EditText etPhoneNumber, etCode;
    private TextView tvVerify, tvConfirm, tvMyNumber;

    private String myNumber;
    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(etPhoneNumber.getText().toString().trim().length() != 11)
                Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.wrong_phone_number_format));
            else if(etPhoneNumber.getText().toString().trim().equals(myNumber))
                Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.key_in_new_number));
            else{

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.update_phone_number);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_change_phone;
    }

    private void initStaticView(){
        etPhoneNumber = (EditText) findViewById(R.id.et_number);
        etCode = (EditText) findViewById(R.id.et_code);
        tvVerify = (TextView) findViewById(R.id.tv_verify);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvMyNumber = (TextView) findViewById(R.id.tv_my_number);

        myNumber = "13261695187";

        tvMyNumber.setText(String.format(getString(R.string.my_phone_number), myNumber.substring(0,3) + "****" + myNumber.substring(myNumber.length()-4, myNumber.length())));

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateSubmitButton();

            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateSubmitButton();

            }
        });

        tvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPhoneNumber.getText().toString().trim().length() != 11)
                    Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.wrong_phone_number_format));
                else if(etPhoneNumber.getText().toString().trim().equals(myNumber))
                    Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.key_in_new_number));
                else{
                    tvVerify.setOnClickListener(null);
                    startTimer();
                }
            }
        });

    }



    private void startTimer(){
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvVerify.setText(String.format(getString(R.string.get_again), (int)(millisUntilFinished/1000)));
            }

            public void onFinish() {
                tvVerify.setText(getString(R.string.get_code));
                tvVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(etPhoneNumber.getText().toString().trim().length() != 11)
                            Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.wrong_phone_number_format));
                        else if(etPhoneNumber.getText().toString().trim().equals(myNumber))
                            Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.key_in_new_number));
                        else{
                            tvVerify.setOnClickListener(null);
                            startTimer();
                        }
                    }
                });
            }
        }.start();
    }

    private void validateSubmitButton(){
        if(etPhoneNumber.getText().toString().trim().isEmpty()
                ||etCode.getText().toString().trim().isEmpty()){
            tvConfirm.setBackgroundResource(R.color.text_grey);
            tvConfirm.setOnClickListener(null);
        }else{
            tvConfirm.setBackgroundResource(R.color.text_black);
            tvConfirm.setOnClickListener(confirmListener);
        }
    }
}
