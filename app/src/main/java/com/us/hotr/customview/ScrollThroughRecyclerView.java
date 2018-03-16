package com.us.hotr.customview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Mloong on 2017/9/21.
 */

public class ScrollThroughRecyclerView extends RecyclerView {

    private GestureDetectorCompat mDetector;


    public ScrollThroughRecyclerView(Context context) {
        super(context);
        mDetector = new GestureDetectorCompat(context, new MyGestureListener());
    }

    public ScrollThroughRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetectorCompat(context, new MyGestureListener());
    }

    public ScrollThroughRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDetector = new GestureDetectorCompat(context, new MyGestureListener());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(mDetector.onTouchEvent(ev)) {
            return super.dispatchTouchEvent(ev);
        }else
            return true;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    }
}