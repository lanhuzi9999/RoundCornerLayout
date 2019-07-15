package com.example.roundcornerlayout.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


public class OvalViewDelegate extends AbstractShapeDelegate {
    protected Paint mMaskPaint = new Paint();
    protected Paint mContentPaint = new Paint();
    public OvalViewDelegate(View view) {
        super(view);
    }

    @Override
    public void setup(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mContentPaint.setAntiAlias(true);
        mContentPaint.setColor(Color.WHITE);
    }

    @Override
    public void beginDrawShape(Canvas canvas) {
        View view = getView();
        int width = view.getMeasuredWidth() ;
        int height = view.getMeasuredHeight();
        if (Build.VERSION.SDK_INT < 21) {
            canvas.saveLayer(0, 0, width, height, mContentPaint, Canvas.ALL_SAVE_FLAG);
        }else{
            canvas.saveLayer(0, 0, width, height, mContentPaint);
        }
        int cw = width - view.getPaddingLeft() - view.getPaddingRight();
        int ch = height - view.getPaddingTop() - view.getPaddingBottom();

        RectF rect = new RectF(0, 0, cw, ch);
        rect.offset(view.getPaddingLeft(), view.getPaddingTop());
        canvas.drawOval(rect, mContentPaint);
        if (Build.VERSION.SDK_INT < 21) {
            canvas.saveLayer(0, 0, width, height, mMaskPaint, Canvas.ALL_SAVE_FLAG);
        }else{
            canvas.saveLayer(0, 0, width, height, mMaskPaint);
        }
    }

    @Override
    public void endDrawShape(Canvas canvas) {
        canvas.restore();
    }
}
