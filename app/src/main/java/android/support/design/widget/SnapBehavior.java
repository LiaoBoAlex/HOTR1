package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Mloong on 2017/9/12.
 */

public class SnapBehavior extends AppBarLayout.Behavior {

    private static final int MAX_OFFSET_ANIMATION_DURATION = 600;

    private ValueAnimatorCompat mOffsetAnimator;
//    private Context mContext;
//    private TextView mTextView;
    private int lastOffset = 0;
    private int headerHeight;
    private int appBarLayoutOffset;
    private boolean firstTime = true;

    public SnapBehavior(Context context) {
        super();
//        mContext = context;
//        this.mTextView = mTextView;
    }

    public SnapBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
//        mContext = context;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        final int appBarHeight = child.getChildAt(0).getBottom();
        final int screenHeight = parent.getHeight();
//        if(MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_DOWN){
//            if(appBarLayoutOffset < appBarHeight - screenHeight) {
//                headerHeight = appBarHeight - screenHeight;
//            }
//            else {
//                headerHeight = -1;
//            }
//        }
        if(firstTime){
            headerHeight = appBarHeight - screenHeight;
            firstTime = false;
        }

        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    int getMaxDragOffset(AppBarLayout view) {
        if(headerHeight>=0)
            return -headerHeight;
        else
            return super.getMaxDragOffset(view);
    }

    @Override
    int getScrollRangeForDragFling(AppBarLayout view) {
        if(headerHeight>=0)
            return headerHeight;
        else
            return super.getScrollRangeForDragFling(view);
    }

    public void setAppBarLayoutOffset(int i){
        appBarLayoutOffset = i;
    }

    @Override
    void onFlingFinished(CoordinatorLayout parent, AppBarLayout layout) {
        super.onFlingFinished(parent, layout);
        snapToChild(parent, layout);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        super.onStopNestedScroll(coordinatorLayout, abl, target);
        snapToChild(coordinatorLayout, abl);
    }

    private void snapToChild(CoordinatorLayout coordinatorLayout, AppBarLayout abl) {
        boolean isScrollDown;
        if(getTopBottomOffsetForScrollingSibling()<=lastOffset)
            isScrollDown = true;
        else
            isScrollDown = false;
        lastOffset = getTopBottomOffsetForScrollingSibling();
        final int offset = Math.abs(getTopBottomOffsetForScrollingSibling());
        int appBarHeight = abl.getChildAt(0).getBottom();
        int screenHeight = coordinatorLayout.getHeight();
        if(appBarHeight <= screenHeight){
            if(offset <= abl.getTotalScrollRange()*0.5)
                animateOffsetTo(coordinatorLayout, abl, 0, 0);
            else if(offset>abl.getTotalScrollRange()*0.5)
                animateOffsetTo(coordinatorLayout, abl, -abl.getTotalScrollRange(), 0);
        }else{
            if((offset <= appBarHeight - screenHeight + 2 && offset >= appBarHeight - screenHeight - 2) && isScrollDown){
                headerHeight = -1;
            }else{
                headerHeight = appBarHeight - screenHeight;
            }
            if (offset > appBarHeight - screenHeight&& isScrollDown) {
                if(offset>appBarHeight - screenHeight + 200) {
                    animateOffsetTo(coordinatorLayout, abl, -(abl.getTotalScrollRange()), 0);
                    lastOffset = -(abl.getTotalScrollRange());
//                    mTextView.setText("上拉看主页");
                }else{
                    animateOffsetTo(coordinatorLayout, abl, -(appBarHeight - screenHeight), 0);
                    lastOffset = -(appBarHeight - screenHeight);
                }

            }
            if(offset >= appBarHeight - screenHeight && !isScrollDown) {
                animateOffsetTo(coordinatorLayout, abl, 0, 0);
//                mTextView.setText("下拉看更多");
                lastOffset = 0;
            }
        }
    }

    public void animateOffsetTo(final CoordinatorLayout coordinatorLayout,
                                 final AppBarLayout child, final int offset, float velocity) {
        final int distance = Math.abs(getTopBottomOffsetForScrollingSibling() - offset);

        final int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 3 * Math.round(1000 * (distance / velocity));
        } else {
            final float distanceRatio = (float) distance / child.getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }

        animateOffsetWithDuration(coordinatorLayout, child, offset, duration);
    }

    private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout,
                                           final AppBarLayout child, final int offset, final int duration) {
        final int currentOffset = getTopBottomOffsetForScrollingSibling();
        if (currentOffset == offset) {
            if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
                mOffsetAnimator.cancel();
            }
            return;
        }

        if (mOffsetAnimator == null) {
            mOffsetAnimator = ViewUtils.createAnimator();
            mOffsetAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
            mOffsetAnimator.addUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimatorCompat animator) {
                    setHeaderTopBottomOffset(coordinatorLayout, child,
                            animator.getAnimatedIntValue());
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }

        mOffsetAnimator.setDuration(Math.min(duration, MAX_OFFSET_ANIMATION_DURATION));
        mOffsetAnimator.setIntValues(currentOffset, offset);
        mOffsetAnimator.start();
    }
}
