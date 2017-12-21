package com.us.hotr.ui.fragment.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.util.Tools;

/**
 * Created by Mloong on 2017/10/13.
 */

public class LoginPhoneFragment extends Fragment {

    private EditText etPhone, etCode;
    private TextView tvVerify, tvManual, tvLogin;
    private CountDownTimer mCountDownTimer;
    private boolean isCountDownFinished = true;

    private View.OnClickListener verifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tvVerify.setOnClickListener(null);
            startTimer();
        }
    };
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
                        tvVerify.setOnClickListener(verifyListener);
                    }
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPhone.getText().toString().trim().isEmpty())
                    Tools.Toast(getActivity(), getString(R.string.key_in_phone_number));
                else if(etPhone.getText().toString().trim().length() != 11)
                    Tools.Toast(getActivity(), getString(R.string.wrong_phone_number_format));
                else if(etCode.getText().toString().trim().isEmpty())
                    Tools.Toast(getActivity(), getString(R.string.key_in_code1));
                else{
                    HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeUserID(etPhone.getText().toString().trim());
                    ((LoginActivity)getActivity()).loginSuccess();
                    getActivity().finish();
                }
            }
        });

        mCountDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvVerify.setText(String.format(getString(R.string.send_code_countdown), (int)(millisUntilFinished/1000)));
            }

            public void onFinish() {
                isCountDownFinished = true;
                tvVerify.setText(getString(R.string.send_code_again));
                tvVerify.setOnClickListener(verifyListener);
                if (etPhone.getText().toString().trim().isEmpty()
                        || etPhone.getText().toString().trim().length() != 11) {
                    tvVerify.setTextColor(getResources().getColor(R.color.text_grey4));
                    tvVerify.setOnClickListener(null);
                } else {
                    tvVerify.setTextColor(getResources().getColor(R.color.text_grey3));
                    tvVerify.setOnClickListener(verifyListener);
                }
            }
        };
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
