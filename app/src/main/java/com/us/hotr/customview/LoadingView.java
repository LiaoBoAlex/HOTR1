package com.us.hotr.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.R;

/**
 * Created by Mloong on 2017/11/13.
 */

public class LoadingView extends FrameLayout{

    private LinearLayout empty;
    private LinearLayout error;
    private LinearLayout loading;
    private TextView tvRefresh;
    private int state;
    private OnRetryListener listener;

    public static final int LOADING = 100;
    public static final int EMPTY = 101;
    public static final int ERROR = 102;
    public static final int DONE = 103;

    public interface OnRetryListener {
        void onRetry();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public LoadingView(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this);
        empty = (LinearLayout) findViewById(R.id.empty);
        loading = (LinearLayout) findViewById(R.id.loading);
        error = (LinearLayout)findViewById(R.id.error);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRetry();
            }
        });
        notifyDataChanged(DONE);
    }

    public void notifyDataChanged(int state) {
        this.state = state;
        switch (state) {
            case LOADING:
                setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                break;
            case EMPTY:
                setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                break;
            case ERROR:
                setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                break;
            case DONE:
                setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    public void setEmptyView(View view) {
        empty.removeAllViews();
        empty.addView(view);
    }
    public void setOnRetryListener(OnRetryListener listener) {
        this.listener = listener;
    }
}
