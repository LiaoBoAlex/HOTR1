package com.us.hotr.ui.activity.info;

import android.os.Bundle;
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
 * Created by Mloong on 2017/10/13.
 */

public class ChangePasswordActivity extends BaseActivity {

    private EditText etPassword, etPasswordAgain, etCode;
    private TextView tvVerify, tvConfirm;

    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!etPassword.getText().toString().trim().equals(etPasswordAgain.getText().toString().trim()))
                Tools.Toast(ChangePasswordActivity.this, getString(R.string.password_not_match));
            else if(etPassword.getText().toString().trim().length()<6)
                Tools.Toast(ChangePasswordActivity.this, getString(R.string.password_less_than_6));
            else{

            }
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.update_password);
        initStaticView();
    }

    private void initStaticView(){
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordAgain = (EditText) findViewById(R.id.et_password_again);
        etCode = (EditText) findViewById(R.id.et_code);
        tvVerify = (TextView) findViewById(R.id.tv_verify);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        etPassword.addTextChangedListener(new TextWatcher() {
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

        etPasswordAgain.addTextChangedListener(new TextWatcher() {
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
                tvVerify.setOnClickListener(null);
                startTimer();
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
                        tvVerify.setOnClickListener(null);
                        startTimer();
                    }
                });
            }
        }.start();
    }

    private void validateSubmitButton(){
        if(etPassword.getText().toString().trim().isEmpty()
                ||etPasswordAgain.getText().toString().trim().isEmpty()
                ||etCode.getText().toString().trim().isEmpty()){
            tvConfirm.setBackgroundResource(R.color.text_grey);
            tvConfirm.setOnClickListener(null);
        }else{
            tvConfirm.setBackgroundResource(R.color.text_black);
            tvConfirm.setOnClickListener(confirmListener);
        }
    }
}
