package com.psi.learnbooheeruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.logging.Logger;

/**
 * Created by dorado on 2017/11/23.
 */

public class BooheeRuler extends ViewGroup {

  private static final int STYLE_TOP = 1, STYLE_BOTTOM = 2;

  // context
  private Context context;
  // 内部尺子
  private InnerRuler innerRuler;
  // 光标画笔
  private Paint cursorPaint;
  // 光标 Drawable
  private Drawable cursorDrawable;

  // 刻度间距
  private int interval = 18;
  // 大刻度包含的小刻度数量
  private int count = 10;
  // 大刻度宽度 长度
  private int bigScaleWidth = 5, bigScaleHeight = 60;
  // 小刻度宽度 长度
  private int smallScaleWidth = 3, smallScaleHeight = 30;
  // 光标宽度 长度
  private int cursorWidth = 8, cursorHeight = 70;
  // 最小刻度 最大刻度
  private int minScale = 400, maxScale = 2000;
  // 文字大小
  private int numberTextSize = 28;
  // 文字外边距
  private int textMargin = 10;
  // 小刻度颜色
  private @ColorInt int smallScaleColor =
      getContext().getResources().getColor(R.color.colorSmallScale);
  // 大刻度颜色
  private @ColorInt int bigScaleColor = getContext().getResources().getColor(R.color.colorBigScale);
  // 刻度文字颜色
  private @ColorInt int numberTextColor =
      getContext().getResources().getColor(R.color.colorNumberText);

  // 尺子两边的 padding
  private int paddingStartAndEnd = 0;
  // 尺子 padding
  private int paddingLeft, paddingRight, paddingTop, paddingBottom;
  // 尺子样式
  private int style = STYLE_TOP;

  public BooheeRuler(Context context) {
    super(context);
    Log.i("TAG", "BooheeRuler构造方法1");
    initRuler();
  }

  public BooheeRuler(Context context, AttributeSet attrs) {
    super(context, attrs);
    Log.i("TAG", "BooheeRuler构造方法2");
    initAttrs(context, attrs);
    initRuler();
  }

  public BooheeRuler(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    Log.i("TAG", "BooheeRuler构造方法3");
    initAttrs(context, attrs);
    initRuler();
  }

  // @formatter:off
  private void initAttrs(Context context,AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BooheeRuler);
    //TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BooheeRuler, 0, 0);
    smallScaleColor = typedArray.getColor(R.styleable.BooheeRuler_smallScaleColor, smallScaleColor);
    bigScaleColor = typedArray.getColor(R.styleable.BooheeRuler_bigScaleColor, bigScaleColor);
    numberTextColor = typedArray.getColor(R.styleable.BooheeRuler_numberTextColor, numberTextColor);
    cursorDrawable = typedArray.getDrawable(R.styleable.BooheeRuler_cursorBackground);
    if (cursorDrawable == null) {
      cursorDrawable = getResources().getDrawable(R.drawable.cursor_shape);
    }
    paddingStartAndEnd = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_paddingStartAndEnd, paddingStartAndEnd);
    minScale = typedArray.getInteger(R.styleable.BooheeRuler_minScale,minScale);
    maxScale = typedArray.getInteger(R.styleable.BooheeRuler_maxScale,maxScale);
    textMargin = typedArray.getDimensionPixelOffset(R.styleable.BooheeRuler_textMargin,textMargin);
    count = typedArray.getInteger(R.styleable.BooheeRuler_count,count);
    numberTextSize = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_numberTextSize,numberTextSize);
    typedArray.recycle();
  }

  private void initRuler() {
    this.context = getContext();
    switch (style) {
      case STYLE_TOP:
        innerRuler = new TopRuler(context, this);
        setHorizontalRulerPadding();
        break;
      case STYLE_BOTTOM:

        break;
    }
    // 添加至ViewGroup
    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    addView(innerRuler, lp);

    initCursorBounds();
  }

  private void setHorizontalRulerPadding() {
    paddingTop = 0;
    paddingBottom = 0;
    paddingLeft = paddingStartAndEnd;
    paddingRight = paddingStartAndEnd;
  }

  // @formatter:off
    private void initCursorBounds() {
      Log.i("TAG","BooheeRuler initCursorBounds");
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
              Log.i("TAG","BooheeRuler preDraw");
                getViewTreeObserver().removeOnPreDrawListener(this);
                switch (style) {
                    case STYLE_TOP:
                        cursorDrawable.setBounds(getWidth() / 2 - cursorWidth / 2, 0, getWidth() / 2 + cursorWidth / 2, cursorHeight);
                        break;
                    case STYLE_BOTTOM:
                        cursorDrawable.setBounds(getWidth() / 2 - cursorWidth / 2, getHeight() - cursorHeight, getWidth() / 2 + cursorWidth / 2, getHeight());
                        break;
                }
                return true;
            }
        });
    }

    // @formatter:on
    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
      Log.i("TAG", "BooheeRuler onLayout");
      Log.i("TAG", "changed:" + changed + ",l:" + l + ",t:" + t + ",r:" + r + ",b:" + b);
      // 定义InnerRuler的位置
      innerRuler.layout(paddingLeft, paddingTop, r - l - paddingRight, b - t - paddingBottom);
    }

  @Override protected void dispatchDraw(Canvas canvas) {
    Log.i("TAG", "BooheeRuler dispatchDraw");
    super.dispatchDraw(canvas);
    // 绘制游标 在这里绘制时因为要覆盖子View
    cursorDrawable.draw(canvas);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    Log.i("TAG","BooheeRuler onMeasure");
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    Log.i("TAG", "BooheeRuler onSizeChanged");
    initCursorBounds();
  }

  /**
   * Getter
   */
  public int getBigScaleWidth() {
    return bigScaleWidth;
  }

  public int getBigScaleHeight() {
    return bigScaleHeight;
  }

  public int getSmallScaleWidth() {
    return smallScaleWidth;
  }

  public int getSmallScaleHeight() {
    return smallScaleHeight;
  }

  public int getNumberTextSize() {
    return numberTextSize;
  }

  public int getInterval() {
    return interval;
  }

  public int getMinScale() {
    return minScale;
  }

  public int getMaxScale() {
    return maxScale;
  }

  public int getTextMargin() {
    return textMargin;
  }

  public int getCount() {
    return count;
  }

  public int getSmallScaleColor() {
    return smallScaleColor;
  }

  public int getBigScaleColor() {
    return bigScaleColor;
  }

  public int getNumberTextColor() {
    return numberTextColor;
  }
}
