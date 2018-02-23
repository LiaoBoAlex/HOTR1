package com.us.hotr.ui.fragment.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.info.ChangePhoneNumberActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.LoginAndRegisterRequest;
import com.us.hotr.webservice.request.RequestForValidationCodeRequest;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

/**
 * Created by Mloong on 2017/10/13.
 */

public class LoginPhoneFragment extends Fragment {

    private EditText etPhone, etCode;
    private TextView tvVerify, tvManual, tvLogin;
    private CountDownTimer mCountDownTimer;
    private boolean isCountDownFinished = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_phone, container, false);
    }

    public static LoginPhoneFragment newInstance() {
        LoginPhoneFragment loginPhoneFragment = new LoginPhoneFragment();
        return loginPhoneFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPhone = (EditText) view.findViewById(R.id.et_phone);
        etCode = (EditText) view.findViewById(R.id.et_code);
        tvVerify = (TextView) view.findViewById(R.id.tv_verify);
        tvManual = (TextView) view.findViewById(R.id.tv_manual);
        tvLogin = (TextView) view.findViewById(R.id.tv_login);

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
                            public void onClick(View view) {
                                requestValidationCode();
                            }
                        });
                    }
                }
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
                    login();
                    return true;
                }
                return false;
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
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
                    public void onClick(View view) {
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
                        public void onClick(View view) {
                            requestValidationCode();
                        }
                    });
                }
            }
        };
    }

    private void login() {
        if (etPhone.getText().toString().trim().isEmpty())
            Tools.Toast(getActivity(), getString(R.string.key_in_phone_number));
        else if (etPhone.getText().toString().trim().length() != 11)
            Tools.Toast(getActivity(), getString(R.string.wrong_phone_number_format));
        else if (etCode.getText().toString().trim().isEmpty())
            Tools.Toast(getActivity(), getString(R.string.key_in_code1));
        else {
            SubscriberListener mListener = new SubscriberListener<GetLoginResponse>() {
                @Override
                public void onNext(GetLoginResponse result) {
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeUserID(result.getJsessionid());
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeUserInfo(result.getUser());
                    ((LoginActivity) getActivity()).loginSuccess();
                    getActivity().finish();
                }
            };
            ServiceClient.getInstance().loginAndRegister(new ProgressSubscriber(mListener, getContext()),
                    new LoginAndRegisterRequest(etPhone.getText().toString().trim(), etCode.getText().toString().trim()));
        }
    }

    private void requestValidationCode(){
        if(etPhone.getText().toString().trim().length() != 11)
            Tools.Toast(getActivity(), getString(R.string.wrong_phone_number_format));
        else {
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    Tools.Toast(getActivity(), getString(R.string.validation_code_sent));
                    tvVerify.setOnClickListener(null);
                    startTimer();
                }
            };
            ServiceClient.getInstance().requestForValidationCode(new ProgressSubscriber(mListener, getContext()),
                    new RequestForValidationCodeRequest(etPhone.getText().toString().trim()));
        }
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
