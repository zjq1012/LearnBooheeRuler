package com.psi.learnbooheeruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by dorado on 2017/11/23.
 */

public class BooheeRuler extends ViewGroup {

    private static final int STYLE_TOP = 1, STYLE_BOTTOM = 2;

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
    // 文字大小
    private int textSize = 12;
    // 小刻度颜色
    private @ColorInt int smallScaleColor =
        getContext().getResources().getColor(R.color.colorSmallScale);
    // 大刻度颜色
    private @ColorInt int bigScaleColor = getContext().getResources()
        .getColor(R.color.colorBigScale);
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
        initRuler();
    }


    public BooheeRuler(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRuler();
        initAttrs(attrs);
    }


    public BooheeRuler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRuler();
        initAttrs(attrs);
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BooheeRuler);
        smallScaleColor = typedArray.getColor(R.styleable.BooheeRuler_smallScaleColor,
            smallScaleColor);
        bigScaleColor = typedArray.getColor(R.styleable.BooheeRuler_bigScaleColor, bigScaleColor);
        numberTextColor = typedArray.getColor(R.styleable.BooheeRuler_numberTextColor,
            numberTextColor);
        typedArray.recycle();
    }


    private void initRuler() {
        switch (style) {
            case STYLE_TOP:
                //innerRuler = new TopRuler(getContext());
                setHorizontalRulerPadding();
                break;
            case STYLE_BOTTOM:
                //innerRuler = new BottomRuler(getContext());
                setHorizontalRulerPadding();
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
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
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
        // 定义InnerRuler的位置
        innerRuler.layout(paddingLeft, paddingTop, r - l - paddingRight, b - t - paddingBottom);
    }


    @Override protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制游标 在这里绘制时因为要覆盖子View
        cursorDrawable.draw(canvas);
    }


    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initCursorBounds();
    }


    /**
     * Getter
     */
    protected int getBigScaleWidth() {
        return bigScaleWidth;
    }


    protected int getBigScaleHeight() {
        return bigScaleHeight;
    }


    protected int getSmallScaleWidth() {
        return smallScaleWidth;
    }


    protected int getSmallScaleHeight() {
        return smallScaleHeight;
    }


    public int getTextSize() {
        return textSize;
    }
}
