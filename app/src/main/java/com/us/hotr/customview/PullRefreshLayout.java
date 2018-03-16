package com.us.hotr.customview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.us.hotr.R;

public class PullRefreshLayout extends ViewGroup {
    private View mHeader;
    private TextView mHeaderText;
    private CircularProgressView mHeaderProgressBar;

    private int mLayoutContentHeight;
    private int mEffectiveHeaderHeight;
    private int mlastMoveY;
    private int mLastYIntercept;
    private RecyclerView mTarget;
    private boolean enableRefresh = true;

    private Status mStatus = Status.NORMAL;

    private void updateStatus(Status status) {
        mStatus = status;
    }

    private enum Status {
        NORMAL, TRY_REFRESH, REFRESHING, TRY_LOADMORE, LOADING;
    }

    public interface OnRefreshListener {
        void refreshFinished();
    }

    private OnRefreshListener mRefreshListener;

    public void setRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public void enableRefresh(boolean value){
        enableRefresh = value;
    }

    public PullRefreshLayout(Context context) {
        super(context);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 当view的所有child从xml中被初始化后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {
                    mTarget =(RecyclerView) child;
                    break;
                }
            }
        }
        addHeader();

    }

    private void addHeader() {
        mHeader = LayoutInflater.from(getContext()).inflate(R.layout.pull_header, null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mHeader, params);

        mHeaderText = (TextView) findViewById(R.id.header_text);
        mHeaderProgressBar = (CircularProgressView) findViewById(R.id.header_progressbar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLayoutContentHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == mHeader) {
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
                mEffectiveHeaderHeight = child.getHeight();
            } else {
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
                if (i < getChildCount()) {
                    if (child instanceof ScrollView) {
                        mLayoutContentHeight += getMeasuredHeight();
                        continue;
                    }
                    mLayoutContentHeight += child.getMeasuredHeight();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();

        // 正在刷新或加载更多，避免重复
        if (mStatus == Status.REFRESHING || mStatus == Status.LOADING) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mlastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mlastMoveY - y;


                // 一直在下拉
                if (getScrollY() <= 0 && dy <= 0 && enableRefresh) {
                    if (mStatus == Status.TRY_LOADMORE) {
                        scrollBy(0, dy / 2);
                    } else {
                        scrollBy(0, dy / 4);
                    }
                }
                beforeRefreshing(dy);

                break;
            case MotionEvent.ACTION_UP:
                // 下拉刷新，并且到达有效长度
                if (getScrollY() <= -mEffectiveHeaderHeight) {
                    releaseWithStatusRefresh();
                    if (mRefreshListener != null) {
                        mRefreshListener.refreshFinished();
                    }
                } else {
                    releaseWithStatusTryRefresh();
                }
                break;
        }

        mlastMoveY = y;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        int y = (int) event.getY();

        if (mStatus == Status.REFRESHING || mStatus == Status.LOADING) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // 拦截时需要记录点击位置，不然下一次滑动会出错
                mlastMoveY = y;
                intercept = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (y > mLastYIntercept) {
                    View child = getChildAt(0);
                    intercept = getRefreshIntercept(child);

                    if (intercept) {
                        updateStatus(mStatus.TRY_REFRESH);
                    }
                } else {
                    intercept = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercept = false;
                break;
            }
        }

        mLastYIntercept = y;
        return intercept;
    }

    /*汇总判断 刷新和加载是否拦截*/
    private boolean getRefreshIntercept(View child) {
        boolean intercept = false;

        if (child instanceof AdapterView) {
            intercept = adapterViewRefreshIntercept(child);
        } else if (child instanceof ScrollView) {
            intercept = scrollViewRefreshIntercept(child);
        } else if (child instanceof RecyclerView) {
            intercept = recyclerViewRefreshIntercept(child);
        }
        return intercept;
    }

  /*汇总判断 刷新和加载是否拦截*/

    /*具体判断各种View是否应该拦截*/
    // 判断AdapterView下拉刷新是否拦截
    private boolean adapterViewRefreshIntercept(View child) {
        boolean intercept = true;
        AdapterView adapterChild = (AdapterView) child;
        if (adapterChild.getFirstVisiblePosition() != 0
                || adapterChild.getChildAt(0).getTop() != 0) {
            intercept = false;
        }
        return intercept;
    }

    // 判断ScrollView刷新是否拦截
    private boolean scrollViewRefreshIntercept(View child) {
        boolean intercept = false;
        if (child.getScrollY() <= 0) {
            intercept = true;
        }
        return intercept;
    }

    // 判断RecyclerView刷新是否拦截
    private boolean recyclerViewRefreshIntercept(View child) {
        boolean intercept = false;

        RecyclerView recyclerView = (RecyclerView) child;
        if (recyclerView.computeVerticalScrollOffset() <= 0) {
            intercept = true;
        }
        return intercept;
    }

  /*具体判断各种View是否应该拦截*/


    /*修改header和footer的状态*/
    public void beforeRefreshing(float dy) {
        mHeaderProgressBar.setVisibility(VISIBLE);
//        if (getScrollY() <= -mEffectiveHeaderHeight) {
//            mHeaderText.setText("松开刷新");
//        } else {
//            mHeaderText.setText("下拉刷新");
//        }
    }


    public void refreshFinished() {
        scrollTo(0, 0);
        mTarget.scrollBy(0, -mEffectiveHeaderHeight);
//        mHeaderText.setText("下拉刷新");
        mHeaderProgressBar.setVisibility(GONE);
        updateStatus(Status.NORMAL);
    }


    private void releaseWithStatusTryRefresh() {
        scrollBy(0, -getScrollY());
//        mHeaderText.setText("下拉刷新");
        updateStatus(Status.NORMAL);
    }

    private void releaseWithStatusRefresh() {
        scrollTo(0, -mEffectiveHeaderHeight);
        mHeaderProgressBar.setVisibility(VISIBLE);
//        mHeaderText.setText("正在刷新");
        updateStatus(Status.REFRESHING);
    }
  /*修改header和footer的状态*/


}
