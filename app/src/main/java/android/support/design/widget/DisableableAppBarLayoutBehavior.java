package android.support.design.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mloong on 2017/9/6.
 */

public class DisableableAppBarLayoutBehavior extends AppBarLayout.Behavior {

    private boolean mEnabled = true;

    private static final int TOP_CHILD_FLING_THRESHOLD = 1;
    private static final float OPTIMAL_FLING_VELOCITY = 3500;
    private static final float MIN_FLING_VELOCITY = 20;
    boolean shouldFling = false;
    float flingVelocityY = 0;

    public DisableableAppBarLayoutBehavior() {
        super();
    }

    public DisableableAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return mEnabled && super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
                                  int velocityX, int velocityY, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        if (velocityY > MIN_FLING_VELOCITY) {
            shouldFling = true;
            flingVelocityY = velocityY;
        } else {
            shouldFling = false;
        }
    }
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        super.onStopNestedScroll(coordinatorLayout, abl, target);
        if (shouldFling) {
            onNestedFling(coordinatorLayout, abl, target, 0, flingVelocityY, true);
        }
    }
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
                                 float velocityX, float velocityY, boolean consumed) {
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
        }
// prevent fling flickering when going up
        if (target instanceof NestedScrollView && velocityY > 0) {
            consumed = true;
        }
        if (Math.abs(velocityY) < OPTIMAL_FLING_VELOCITY) {
            velocityY = OPTIMAL_FLING_VELOCITY * (velocityY < 0 ? -1 : 1);
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

}
