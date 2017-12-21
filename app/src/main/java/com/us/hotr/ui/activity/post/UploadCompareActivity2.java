package com.us.hotr.ui.activity.post;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.beauty.CompareActivity;
import com.us.hotr.util.Tools;

/**
 * Created by Mloong on 2017/9/27.
 */

public class UploadCompareActivity2 extends BaseActivity {

    private TextView tvPost;
    private EditText etContent;

    private View.OnClickListener postListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(etContent.getText().toString().trim().length()<50)
                Tools.Toast(getBaseContext(), getString(R.string.min_50));
            else {
                Intent i = new Intent(UploadCompareActivity2.this, CompareActivity.class);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                startActivity(i);
            }
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_upload_compare2;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.upload_compare);
        initStaticView();
    }

    private void initStaticView() {

        tvPost = (TextView) findViewById(R.id.tv_edit);
        etContent = (EditText) findViewById(R.id.et_content);

        tvPost.setText(R.string.post);
        tvPost.setTextColor(getResources().getColor(R.color.text_grey2));

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().isEmpty()) {
                    tvPost.setTextColor(getResources().getColor(R.color.text_grey2));
                    tvPost.setOnClickListener(null);
                }else{
                    tvPost.setTextColor(getResources().getColor(R.color.cyan));
                    tvPost.setOnClickListener(postListener);
                }

            }
        });
    }
}
