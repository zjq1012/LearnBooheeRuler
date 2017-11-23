package com.psi.learnbooheeruler;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by zjq on 2017/11/23 0023.
 */

public class HorizontalRuler extends InnerRuler {
    private float lastX;


    public HorizontalRuler(Context context, BooheeRuler ruler) {
        super(context, ruler);
    }


    @Override public boolean onTouchEvent(MotionEvent event) {
        float x = getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                velocityTracker.addMovement(event);
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceX = x - lastX;
                scrollTo((int) -distanceX, 0);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
                float xVelocity = velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > minFlingVelocity) {
                    fling((int) xVelocity);
                } else {

                }
                break;
        }

        return super.onTouchEvent(event);
    }


    private void fling(int xVelocity) {
        scroller.fling(getScrollX(), 0, xVelocity, 0, minXPosition, maxXPosition, 0, 0);
        invalidate();
    }


    @Override void goToScale(int scale) {

    }


    @Override public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        x = x < minXPosition ? minXPosition : x;
        x = x > maxXPosition ? maxXPosition : x;
    }
}
