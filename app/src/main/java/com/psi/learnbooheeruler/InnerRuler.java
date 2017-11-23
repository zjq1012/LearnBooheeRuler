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
    protected BooheeRuler parent;
    //
    protected int minXPosition, maxXPosition;


    public InnerRuler(Context context, BooheeRuler ruler) {
        super(context);
        this.context = context;
        this.parent = ruler;

        velocityTracker = VelocityTracker.obtain();
        minFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();

        scroller = new OverScroller(this.context);

        initPaint();
    }


    private void initPaint() {
        smallScalePaint = new Paint();
        smallScalePaint.setAntiAlias(true);
        smallScalePaint.setStrokeWidth(parent.getSmallScaleWidth());
        smallScalePaint.setStrokeCap(Paint.Cap.ROUND);

        bigSmallPaint = new Paint();
        bigSmallPaint.setAntiAlias(true);
        bigSmallPaint.setStrokeWidth(parent.getBigScaleWidth());
        bigSmallPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(parent.getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);
    }


    // 跳转至某一刻度
    abstract void goToScale(int scale);
}
