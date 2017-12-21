package com.us.hotr.customview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.us.hotr.R;

/**
 * Created by Mloong on 2017/11/14.
 */

public class CustomProgressDialog extends ProgressDialog {

    public CustomProgressDialog(Context context)
    {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme)
    {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context)
    {
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.dialog_loading);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }
}