package com.psi.learnbooheeruler;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by dorado on 2017/11/24.
 */

public class TopRuler extends HorizontalRuler {

  public TopRuler(Context context, BooheeRuler ruler) {
    super(context, ruler);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawScale(canvas);
  }

  // 画刻度和字
  // @formatter:on
  private void drawScale(Canvas canvas) {
    for (float i = outerRuler.getMinScale(); i <= outerRuler.getMaxScale(); i++) {
      float locationX = (i - outerRuler.getMinScale()) * outerRuler.getInterval();
      // 屏幕范围内的坐标[getScrollX(),getScrollX() + getWidth()]
      // 所以提前绘制的范围是[getScrollX()-drawOffset,getScrollX() + canvas.getWidth() + draOffset];
      if (locationX > getScrollX() - drawOffset && locationX < (getScrollX() + canvas.getWidth() + drawOffset)) {
        if (i % outerRuler.getCount() == 0) {
          canvas.drawLine(locationX, 0, locationX, outerRuler.getBigScaleHeight(), bigSmallPaint);
          canvas.drawText(String.valueOf(i / 10), locationX, outerRuler.getBigScaleHeight() + outerRuler.getTextMargin(), textPaint);
        } else {
          canvas.drawLine(locationX, 0, locationX, outerRuler.getSmallScaleHeight(), smallScalePaint);
        }
        //画轮廓线
        //canvas.drawLine(getScrollX(), 0, getScrollX() + canvas.getWidth(), 0, mOutLinePaint);
      }
    }
  }
}
