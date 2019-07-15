package com.example.roundcornerlayout.widget;

import android.graphics.Canvas;
import android.os.Build;
import android.view.View;


public class CircleViewDelegate extends OvalViewDelegate {

    public CircleViewDelegate(View view) {
        super(view);
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
        int cw = width - - view.getPaddingLeft() - view.getPaddingRight();
        int ch = height - view.getPaddingTop() - view.getPaddingBottom() ;
        float radius = Math.min(cw,
                ch)/2.0f;
        float cx, cy ;
        cx = width /2.0f;
        cy = height/2.0f;
        canvas.drawCircle(cx, cy, radius, mContentPaint);
        if (Build.VERSION.SDK_INT < 21) {
            canvas.saveLayer(0, 0, width, height, mMaskPaint, Canvas.ALL_SAVE_FLAG);
        }else{
            canvas.saveLayer(0, 0, width, height, mMaskPaint);
        }
    }
}
