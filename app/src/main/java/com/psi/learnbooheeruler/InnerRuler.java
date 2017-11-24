package com.psi.learnbooheeruler;

import android.content.Context;
import android.graphics.Paint;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

/**
 * Created by dorado on 2017/11/23.
 */

public abstract class InnerRuler extends View {

  protected Context context;
  protected VelocityTracker velocityTracker;
  // Scroller
  protected OverScroller scroller;
  // 最小滑动速度 最大滑动速度
  protected int minFlingVelocity, maxFlingVelocity;
  // Paint
  protected Paint smallScalePaint, bigSmallPaint, textPaint;
  // BooheeRuler
  protected BooheeRuler outerRuler;
  // 可滑动至的最小位置 因为指针在中间 所以最小滑动位置为-的屏幕宽度
  protected int minXPosition;
  // 可滑动的最大位置 根据minScale和maxScale计算得出
  protected int maxXPosition;
  // 提前刻画量
  protected int drawOffset = 0;
  // 刻度总长度
  protected int totalLength;
  // 当前刻度值
  protected float currentScale;

  public InnerRuler(Context context, BooheeRuler ruler) {
    super(context);
    this.context = context;
    this.outerRuler = ruler;

    velocityTracker = VelocityTracker.obtain();
    minFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();

    drawOffset = outerRuler.getCount() * outerRuler.getInterval();
    scroller = new OverScroller(this.context);

    initPaint();
  }

  private void initPaint() {
    smallScalePaint = new Paint();
    smallScalePaint.setAntiAlias(true);
    smallScalePaint.setStrokeWidth(outerRuler.getSmallScaleWidth());
    smallScalePaint.setColor(outerRuler.getSmallScaleColor());
    smallScalePaint.setStrokeCap(Paint.Cap.ROUND);

    bigSmallPaint = new Paint();
    bigSmallPaint.setAntiAlias(true);
    bigSmallPaint.setStrokeWidth(outerRuler.getBigScaleWidth());
    bigSmallPaint.setColor(outerRuler.getBigScaleColor());
    bigSmallPaint.setStrokeCap(Paint.Cap.ROUND);

    textPaint = new Paint();
    textPaint.setAntiAlias(true);
    textPaint.setTextSize(outerRuler.getNumberTextSize());
    textPaint.setTextSize(outerRuler.getNumberTextColor());
    textPaint.setTextAlign(Paint.Align.CENTER);
  }

  // 跳转至某一刻度
  abstract void goToScale(int scale);
}
