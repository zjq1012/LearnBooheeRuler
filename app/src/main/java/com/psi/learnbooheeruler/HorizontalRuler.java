package com.psi.learnbooheeruler;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewTreeObserver;
import java.util.logging.Logger;

/**
 * Created by zjq on 2017/11/23 0023.
 */

public abstract class HorizontalRuler extends InnerRuler {
  private float lastX;

  public HorizontalRuler(Context context, BooheeRuler ruler) {
    super(context, ruler);
    Log.i("TAG","HorizontalRuler构造方法");
    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        Log.i("TAG","BooheeRuler onGlobalLayout");
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        goToScale(789);
      }
    });
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();
    if (velocityTracker == null) {
      velocityTracker = VelocityTracker.obtain();
    }
    velocityTracker.addMovement(event);
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (!scroller.isFinished()) {
          scroller.abortAnimation();
        }
        lastX = x;
        break;
      case MotionEvent.ACTION_MOVE:
        float distanceX = lastX - x;
        scrollBy((int) distanceX, 0);
        lastX = x;
        break;
      case MotionEvent.ACTION_UP:
        velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
        float xVelocity = velocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > minFlingVelocity) {
          fling(-(int) xVelocity);
        } else {

        }
        // Velocity回收
        if (velocityTracker != null) {
          velocityTracker.recycle();
          velocityTracker = null;
        }
        break;
    }

    return true;
  }

  /**
   * 惯性滑动
   *
   * @param xVelocity 起始x速度 与Velocity获得的的速度方向相反
   */
  private void fling(int xVelocity) {
    scroller.fling(getScrollX(), 0, xVelocity, 0, minXPosition, maxXPosition, 0, 0);
    invalidate();
  }

  @Override void goToScale(int scale) {
    currentScale = Math.round(scale);
    scrollTo(convertScaleToScrollX(currentScale), 0);
  }

  @Override public void scrollTo(int x, int y) {
    if (x < minXPosition) {
      x = minXPosition;
    }
    if (x > maxXPosition) {
      x = maxXPosition;
    }
    if (x != getScrollX()) {
      super.scrollTo(x, y);
    }
    currentScale = convertScrollXToScale(x);
  }

  /**
   * 根据当前滑动位置计算出对应的刻度
   */
  private float convertScrollXToScale(int x) {
    float scale = (x + getWidth() / 2) / (float) outerRuler.getInterval() + outerRuler.getMinScale();
    return scale;
  }

  /**
   * 根据刻度计算出位置
   * 因为只需屏幕中心滑动至刻度 所以-getWidt()/2
   */
  private int convertScaleToScrollX(float scale) {
    int i = (int) ((scale - outerRuler.getMinScale()) * outerRuler.getInterval() - getWidth() / 2);
    return i;
  }

  @Override public void computeScroll() {
    if (scroller.computeScrollOffset()) {
      scrollTo(scroller.getCurrX(), scroller.getCurrY());
      if (!scroller.computeScrollOffset()) {
        if (currentScale != Math.round(currentScale)) {
          scrollBackToCurrentScale();
        }
      }
      invalidate();
    }
  }

  //把移动后光标对准距离最近的刻度，就是回弹到最近刻度
  private void scrollBackToCurrentScale() {
    //渐变回弹
    currentScale = Math.round(currentScale);
    scroller.startScroll(getScrollX(), 0, convertScaleToScrollX(currentScale) - getScrollX(), 0, 1000);
    invalidate();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    Log.i("TAG","HorizontalRuler onSizeChanged");
    totalLength = (outerRuler.getMaxScale() - outerRuler.getMinScale()) * outerRuler.getInterval();
    // 最小只能滑到半屏
    minXPosition = -w / 2;
    maxXPosition = totalLength - w / 2;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    Log.i("TAG","HorizontalRuler onMeasure");
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    Log.i("TAG","HorizontalRuler onLayout");
  }
}
