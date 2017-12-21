package com.us.hotr.ui.fragment.info;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.util.Tools;

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
    private View.OnClickListener verifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tvVerify.setOnClickListener(null);
            startTimer();
        }
    };
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
                        tvVerify.setOnClickListener(verifyListener);
                    }
                }
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPhone.getText().toString().trim().isEmpty())
                    Tools.Toast(getActivity(), getString(R.string.key_in_phone_number));
                else if(etPhone.getText().toString().trim().length() != 11)
                    Tools.Toast(getActivity(), getString(R.string.wrong_phone_number_format));
                else if(etCode.getText().toString().trim().isEmpty())
                    Tools.Toast(getActivity(), getString(R.string.key_in_code1));
                else if(etPassword.getText().toString().trim().length()<6)
                    Tools.Toast(getActivity(), getString(R.string.password_less_than_6));
                else{
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
