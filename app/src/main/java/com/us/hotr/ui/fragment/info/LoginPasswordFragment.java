package com.us.hotr.ui.fragment.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.info.ForgotPasswordActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

/**
 * Created by Mloong on 2017/10/13.
 */

public class LoginPasswordFragment extends Fragment {

    private TextView tvManual, tvLogin, tvForgotpassword;
    private EditText etPassword, etPhone;
    private ImageView ivPasswordShow;

    private Boolean isShow = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_password, container, false);
    }

    public static LoginPasswordFragment newInstance() {
        LoginPasswordFragment loginPasswordFragment = new LoginPasswordFragment();
        return loginPasswordFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvManual = (TextView) view.findViewById(R.id.tv_manual);
        tvLogin = (TextView) view.findViewById(R.id.tv_login);
        tvForgotpassword = (TextView) view.findViewById(R.id.tv_forgot_password);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        ivPasswordShow = (ImageView) view.findViewById(R.id.iv_show_password);

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

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPhone.getText().toString().trim().isEmpty())
                    Tools.Toast(getActivity(), getString(R.string.key_in_phone_number));
                else if(etPhone.getText().toString().trim().length() != 11)
                    Tools.Toast(getActivity(), getString(R.string.wrong_phone_number_format));
                else if(etPassword.getText().toString().trim().isEmpty())
                    Tools.Toast(getActivity(), getString(R.string.key_in_password));
                else {
                    login(etPhone.getText().toString().trim(), etPassword.getText().toString().trim());
                }
            }
        });

        tvForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
            }
        });
    }

    private void login(String userName, String password){
        SubscriberListener mListener = new SubscriberListener<GetLoginResponse>() {
            @Override
            public void onNext(GetLoginResponse result) {
                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeUserID(result.getJsessionid());
//                HOTRSharePreference.newInstance(getContext()).storeUserID("aaa");
                ((LoginActivity)getActivity()).loginSuccess();
                getActivity().finish();
            }
        };
        ServiceClient.getInstance().login(new ProgressSubscriber(mListener, getContext()), userName, password);
    }

}
