package com.us.hotr.ui.activity.info;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.ChangePasswordRequest;
import com.us.hotr.webservice.request.RequestForValidationCodeRequest;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

/**
 * Created by Mloong on 2017/10/13.
 */

public class ChangePasswordActivity extends BaseActivity {

    private EditText etPassword, etPasswordAgain, etCode;
    private TextView tvVerify, tvConfirm;
    private String number;

    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changePassword();
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
        number = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo().getMobile();
        initStaticView();
        if(number == null || number.isEmpty())
            verifyPhoneNumber();
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

        etCode.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCode.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    changePassword();
                    return true;
                }
                return false;
            }
        });

        tvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number == null || number.isEmpty())
                    verifyPhoneNumber();
                else
                    requestValidationCode();
            }
        });
    }

    private void changePassword(){
        if(!etPassword.getText().toString().trim().equals(etPasswordAgain.getText().toString().trim()))
            Tools.Toast(ChangePasswordActivity.this, getString(R.string.password_not_match));
        else if(etPassword.getText().toString().trim().length()<6)
            Tools.Toast(ChangePasswordActivity.this, getString(R.string.password_less_than_6));
        else {
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    Tools.Toast(ChangePasswordActivity.this, getString(R.string.password_changed));
                    finish();
                }
            };
            ServiceClient.getInstance().changePassword(new ProgressSubscriber(mListener, ChangePasswordActivity.this),
                    new ChangePasswordRequest(number,
                            etPassword.getText().toString().trim(), etCode.getText().toString().trim()));
        }
    }

    private void requestValidationCode(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                Tools.Toast(ChangePasswordActivity.this, getString(R.string.validation_code_sent));
                tvVerify.setOnClickListener(null);
                startTimer();
            }
        };
        ServiceClient.getInstance().requestForValidationCodePassword(new ProgressSubscriber(mListener, ChangePasswordActivity.this),
                new RequestForValidationCodeRequest(number));

    }

    private void verifyPhoneNumber(){
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(ChangePasswordActivity.this);
        alertDialogBuilder.setMessage(getString(R.string.verify_id));
        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(ChangePasswordActivity.this, ChangePhoneNumberActivity.class));
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialogBuilder.create().show();
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
                        if(number == null || number.isEmpty())
                            verifyPhoneNumber();
                        else
                            requestValidationCode();
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
