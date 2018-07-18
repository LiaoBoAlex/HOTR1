package com.us.hotr.ui.activity.info;

import android.content.Context;
import android.content.DialogInterface;
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

import com.tencent.stat.StatMultiAccount;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.dialog.VoucherDialog;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.BoundMobileRequest;
import com.us.hotr.webservice.request.LoginWithWechatRequest;
import com.us.hotr.webservice.request.RequestForValidationCodeRequest;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Mloong on 2017/10/10.
 */

public class ChangePhoneNumberActivity extends BaseActivity {
    public static final int TYPE_CHANGE_PASSWORD = 1;
    public static final int TYPE_SET_PASSWORD = 2;

    private EditText etPhoneNumber, etCode;
    private TextView tvVerify, tvConfirm, tvMyNumber;

    private String myNumber;
    private int type;
    private GetLoginResponse.WechatUser mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras()!=null) {
            type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        }
        if(type == TYPE_SET_PASSWORD) {
            setMyTitle(R.string.bound_mobile);
            mUser = (GetLoginResponse.WechatUser)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        }else{
            setMyTitle(R.string.update_phone_number);
            myNumber = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo().getMobile();
        }
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
        if(myNumber!= null && !myNumber.isEmpty())
            tvMyNumber.setText(String.format(getString(R.string.my_phone_number), myNumber.substring(0,3) + "****" + myNumber.substring(myNumber.length()-4, myNumber.length())));
        else
            tvMyNumber.setText(R.string.bound_mobile);
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
                    requestValidationCode();
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
                    boundMobile();
                    return true;
                }
                return false;
            }
        });

    }

    private void boundMobile(){
        if(etPhoneNumber.getText().toString().trim().length() != 11)
            Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.wrong_phone_number_format));
        else if(etPhoneNumber.getText().toString().trim().equals(myNumber))
            Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.key_in_new_number));
        else {
            if(type == TYPE_SET_PASSWORD){
                SubscriberListener mListener = new SubscriberListener<GetLoginResponse>() {
                    @Override
                    public void onNext(final GetLoginResponse result) {
                        JMessageClient.register("user" + result.getUser().getUserId(), "123456", new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0 || i == 898001) {
                                    JMessageClient.login("user" + result.getUser().getUserId(), "123456", new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i == 0) {
                                                UserInfo userInfo = JMessageClient.getMyInfo();
                                                userInfo.setNickname(result.getUser().getNickname());
                                                userInfo.setAddress(result.getUser().getHead_portrait());
                                                JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                                    @Override
                                                    public void gotResult(int i, String s) {
                                                    }
                                                });
                                                JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
                                                    @Override
                                                    public void gotResult(int i, String s) {
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        HOTRSharePreference.getInstance(getApplicationContext()).storeUserID(result.getJsessionid());
                        HOTRSharePreference.getInstance(getApplicationContext()).storeUserInfo(result.getUser());
                        StatMultiAccount account = new StatMultiAccount(
                                StatMultiAccount.AccountType.PHONE_NO, result.getUser().getMobile());
                        long time = System.currentTimeMillis() / 1000;
                        account.setLastTimeSec(time);
                        account.setExpireTimeSec(time + 60*60*24*365*10);
                        StatService.reportMultiAccount(ChangePhoneNumberActivity.this, account);

                        getNewUserVoucher();
                    }
                };
                LoginWithWechatRequest request = new LoginWithWechatRequest(mUser.getName(),
                        mUser.getHeadImgUrl(),
                        mUser.getOpenId(),
                        mUser.getSex(),
                        etPhoneNumber.getText().toString().trim(),
                        etCode.getText().toString().trim());
                ServiceClient.getInstance().registerWithWechat(new ProgressSubscriber(mListener, ChangePhoneNumberActivity.this),
                        request);
            }else{
                SubscriberListener mListener = new SubscriberListener<String>() {
                    @Override
                    public void onNext(String result) {
                        Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.password_changed));
                        User user = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo();
                        user.setMobile(etPhoneNumber.getText().toString().trim());
                        HOTRSharePreference.getInstance(getApplicationContext()).storeUserInfo(user);
                        finish();
                    }
                };
                ServiceClient.getInstance().boundMobile(new ProgressSubscriber(mListener, ChangePhoneNumberActivity.this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(),
                        new BoundMobileRequest(etPhoneNumber.getText().toString().trim(), etCode.getText().toString().trim()));
            }
        }
    }

    private void requestValidationCode(){
        if(etPhoneNumber.getText().toString().trim().length() != 11)
            Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.wrong_phone_number_format));
        else if(etPhoneNumber.getText().toString().trim().equals(myNumber))
            Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.key_in_new_number));
        else {
            tvVerify.setOnClickListener(null);
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(String result) {
                    Tools.Toast(ChangePhoneNumberActivity.this, getString(R.string.validation_code_sent));
                    startTimer();
                }
            };
            ServiceClient.getInstance().requestForValidationCodePhone(new ProgressSubscriber(mListener, ChangePhoneNumberActivity.this),
                    new RequestForValidationCodeRequest(etPhoneNumber.getText().toString().trim()));
        }
    }

    private void getNewUserVoucher(){
        SubscriberListener mListener = new SubscriberListener<String>() {
            @Override
            public void onNext(final String result) {
                VoucherDialog dialog = new VoucherDialog(ChangePhoneNumberActivity.this, R.style.CustomDialog);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        };
        ServiceClient.getInstance().addAllVoucher(new ProgressSubscriber(mListener, ChangePhoneNumberActivity.this),
                HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), Constants.NEW_USER_VOUCHER);
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
                        requestValidationCode();
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
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boundMobile();
                }
            });
        }
    }
}
