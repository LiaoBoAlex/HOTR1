package com.us.hotr.ui.fragment.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.ChangePasswordRequest;
import com.us.hotr.webservice.request.LoginAndRegisterRequest;
import com.us.hotr.webservice.request.RequestForValidationCodeRequest;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

/**
 * Created by Mloong on 2017/10/16.
 */

public class ForgotPasswordFragment extends Fragment {

    private EditText etPhone, etCode, etPassword;
    private TextView tvVerify, tvDone;
    private ImageView ivPasswordShow;

    private Boolean isShow = false;
    private CountDownTimer mCountDownTimer;
    private boolean isCountDownFinished = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        return forgotPasswordFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPhone = (EditText) view.findViewById(R.id.et_phone);
        etCode = (EditText) view.findViewById(R.id.et_code);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        tvVerify = (TextView) view.findViewById(R.id.tv_verify);
        tvDone = (TextView) view.findViewById(R.id.tv_login);
        ivPasswordShow = (ImageView) view.findViewById(R.id.iv_show_password);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isCountDownFinished) {
                    if (etPhone.getText().toString().trim().isEmpty()
                            || etPhone.getText().toString().trim().length() != 11) {
                        tvVerify.setTextColor(getResources().getColor(R.color.text_grey4));
                        tvVerify.setOnClickListener(null);
                    } else {
                        tvVerify.setTextColor(getResources().getColor(R.color.text_grey3));
                        tvVerify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestValidationCode();
                            }
                        });
                    }
                }
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvVerify.setText(String.format(getString(R.string.send_code_countdown), (int)(millisUntilFinished/1000)));
            }

            public void onFinish() {
                isCountDownFinished = true;
                tvVerify.setText(getString(R.string.send_code_again));
                tvVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestValidationCode();
                    }
                });
                if (etPhone.getText().toString().trim().isEmpty()
                        || etPhone.getText().toString().trim().length() != 11) {
                    tvVerify.setTextColor(getResources().getColor(R.color.text_grey4));
                    tvVerify.setOnClickListener(null);
                } else {
                    tvVerify.setTextColor(getResources().getColor(R.color.text_grey3));
                    tvVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestValidationCode();
                        }
                    });
                }
            }
        };

        ivPasswordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShow){
                    ivPasswordShow.setImageResource(R.mipmap.ic_password_hide);
                    etPassword.setInputType(129);
                    Tools.validatePasswordInput(etPassword);
                    isShow = false;
                }else {
                    ivPasswordShow.setImageResource(R.mipmap.ic_password_show);
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Tools.validatePasswordInput(etPassword);
                    isShow = true;
                }
            }
        });

        etPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {

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
    }

    private void changePassword() {
        if (etPhone.getText().toString().trim().isEmpty())
            Tools.Toast(getActivity(), getString(R.string.key_in_phone_number));
        else if (etPhone.getText().toString().trim().length() != 11)
            Tools.Toast(getActivity(), getString(R.string.wrong_phone_number_format));
        else if (etCode.getText().toString().trim().isEmpty())
            Tools.Toast(getActivity(), getString(R.string.key_in_code1));
        else if (etPassword.getText().toString().trim().length() < 6)
            Tools.Toast(getActivity(), getString(R.string.password_less_than_6));
        else {
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    Tools.Toast(getActivity(), getString(R.string.password_changed));
                    getActivity().finish();
                }
            };
            ServiceClient.getInstance().changePassword(new ProgressSubscriber(mListener, getContext()),
                    new ChangePasswordRequest(etPhone.getText().toString().trim(), etPassword.getText().toString().trim(), etCode.getText().toString().trim()));
        }
    }

    private void requestValidationCode(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(String result) {
                Tools.Toast(getActivity(), getString(R.string.validation_code_sent));
                tvVerify.setOnClickListener(null);
                startTimer();
            }
        };
        ServiceClient.getInstance().requestForValidationCodePassword(new ProgressSubscriber(mListener, getContext()),
                new RequestForValidationCodeRequest(etPhone.getText().toString().trim()));

    }

    private void startTimer(){
        mCountDownTimer.start();
        isCountDownFinished = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mCountDownTimer.cancel();
    }
}
