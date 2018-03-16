package com.us.hotr.ui.activity.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Case;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

/**
 * Created by Mloong on 2017/9/27.
 */

public class UploadCompareActivity2 extends BaseActivity {

    private TextView tvPost;
    private EditText etContent;
    private Case aCase;

    private View.OnClickListener postListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(etContent.getText().toString().trim().length()<50)
                Tools.Toast(getBaseContext(), getString(R.string.min_50));
            else {
                aCase.setContrastPhotoContent(etContent.getText().toString().trim());
                File fromPicBefore = new File(aCase.getFilePathBefore());
                File fromPicAfter = new File(aCase.getFilePathAfter());
                File toFileBefore = null, toFileAfter = null;
                try {
                    toFileBefore = new Compressor(UploadCompareActivity2.this)
                            .setDestinationDirectoryPath(Tools.getZipFileName(fromPicBefore.getName()))
                            .compressToFile(fromPicBefore);
                    aCase.setFilePathBefore(toFileBefore.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    toFileAfter = new Compressor(UploadCompareActivity2.this)
                            .setDestinationDirectoryPath(Tools.getZipFileName(fromPicAfter.getName()))
                            .compressToFile(fromPicAfter);
                    aCase.setFilePathAfter(toFileAfter.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final File finalToFileBefore = toFileBefore;
                final File finalToFileAfter = toFileAfter;
                SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<Long>() {
                    @Override
                    public void onNext(Long result) {
                        finalToFileBefore.delete();
                        finalToFileAfter.delete();
                        Tools.Toast(UploadCompareActivity2.this, getString(R.string.case_success));
                        Intent i = new Intent(UploadCompareActivity2.this, CaseActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                        b.putLong(Constants.PARAM_ID, result);
                        b.putBoolean(CaseActivity.PARAM_IS_NEW, true);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        finalToFileBefore.delete();
                        finalToFileAfter.delete();
                    }
                };
                ServiceClient.getInstance().uploadCase(new ProgressSubscriber(mListener, UploadCompareActivity2.this),
                        HOTRSharePreference.getInstance(UploadCompareActivity2.this.getApplicationContext()).getUserID(), aCase);
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
        aCase = (Case)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
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
