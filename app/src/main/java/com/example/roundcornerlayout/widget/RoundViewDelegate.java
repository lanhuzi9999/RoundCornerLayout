package com.example.roundcornerlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.roundcornerlayout.R;


public class RoundViewDelegate extends AbstractShapeDelegate {
    private float mRadius;
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;
    private Path mPath;
    private Paint mMaskPaint = new Paint();
    private Paint mContentPaint = new Paint();

    public RoundViewDelegate(View view){
        super(view);
    }

    @Override
    public void setup(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mContentPaint.setAntiAlias(true);
        mContentPaint.setColor(Color.WHITE);
        mPath = new Path();
        if (attrs != null) {
            Context context = getView().getContext();
            Resources res = context.getResources();
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeView,
                    defStyleRes, 0);
            mBottomLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeView_bottomLeftRadius, -1);
            mBottomRightRadius = a.getDimensionPixelSize(R.styleable.ShapeView_bottomRightRadius, -1);
            mTopLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeView_topLeftRadius, -1);
            mTopRightRadius = a.getDimensionPixelSize(R.styleable.ShapeView_topRightRadius, -1);
            mRadius = a.getDimensionPixelSize(R.styleable.ShapeView_radius, -1);
            a.recycle();
        }
        if (mRadius > 0) {
            if (mBottomLeftRadius < 0) {
                mBottomLeftRadius = mRadius;
            }
            if (mBottomRightRadius < 0) {
                mBottomRightRadius = mRadius;
            }
            if (mTopLeftRadius < 0) {
                mTopLeftRadius = mRadius;
            }
            if (mTopRightRadius < 0) {
                mTopRightRadius = mRadius;
            }
        }
    }

    public void updateRadius(float topLeft, float topRight, float bottomLeft, float bottomRight){
        boolean dirty =false;
        if (topLeft >= 0){
            dirty = mTopLeftRadius != topLeft;
            mTopLeftRadius = topLeft;
        }
        if (topRight >= 0){
            dirty |= mTopRightRadius != topRight;
            mTopRightRadius = topRight;
            dirty = true;
        }
        if (bottomLeft >= 0){
            dirty |= mBottomLeftRadius != bottomLeft;
            mBottomLeftRadius = bottomLeft;
        }
        if (bottomRight >= 0){
            dirty = mBottomRightRadius != bottomRight;;
            mBottomRightRadius = bottomRight;
        }
        if (dirty){
            View view = getView();
            view.postInvalidate();
        }
    }

    private void addRoundPath(int width, int height) {
        View view = getView();
        int padL = view.getPaddingLeft();
        int padT = view.getPaddingTop();
        int padR = view.getPaddingRight();
        int padB = view.getPaddingBottom();
        //topleft path
        mPath.reset();
        if (mTopLeftRadius > 0) {
            mPath.moveTo(padL,padT +mTopLeftRadius);
            RectF arc = new RectF(padL, padT,
                    padL + mTopLeftRadius * 2,
                    padT + mTopLeftRadius * 2);
            mPath.arcTo(arc, 180, 90);
        }else{
            mPath.moveTo(padL, padT);
        }
        if (mTopRightRadius > 0) {
            mPath.lineTo(width - mTopRightRadius - padR, padT);
            RectF arc = new RectF(width - mTopRightRadius * 2 - padR,
                    padT,
                    width - padR,
                    mTopRightRadius * 2 + padT);
            mPath.arcTo(arc, -90, 90);
        }else{
            mPath.lineTo(width - padR, padT);
        }

        //bottomRight path
        if (mBottomRightRadius > 0) {
            mPath.lineTo(width - padR, height - mBottomRightRadius - padB);

            RectF arc = new RectF(width - mBottomRightRadius * 2 - padR,
                    height - mBottomRightRadius * 2 - padB,
                    width - padR,
                    height - padB);
            mPath.arcTo(arc, 0, 90);
        }else{
            mPath.lineTo(width - padR, height - padB);
        }

        //bottomLeft path
        if (mBottomLeftRadius > 0) {
            mPath.lineTo(mBottomLeftRadius + padL, height - padB);
            RectF arc = new RectF(padL, height - mBottomLeftRadius * 2 - padB,
                    mBottomLeftRadius * 2 + padL,
                    height - padB);

            mPath.arcTo(arc, 90, 90);
        }else{
            mPath.lineTo(padL, height - padB);
        }
        mPath.close();
    }

    private boolean canDrawRound(){
        if (mBottomLeftRadius <= 0 && mBottomRightRadius <= 0 && mTopRightRadius <= 0 && mTopLeftRadius <= 0) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void beginDrawShape(Canvas canvas){
        if (!canDrawRound()) {
            return;
        }
        View view = getView();
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        addRoundPath(width, height);
        if (Build.VERSION.SDK_INT < 21){
            canvas.saveLayer(0, 0, width, height, mContentPaint, Canvas.ALL_SAVE_FLAG);
        }else{
            canvas.saveLayer(0, 0, width, height, mContentPaint);
        }
        canvas.drawPath(mPath, mContentPaint);
        if (Build.VERSION.SDK_INT < 21) {
            canvas.saveLayer(0, 0, width, height, mMaskPaint, Canvas.ALL_SAVE_FLAG);
        }else{
            canvas.saveLayer(0, 0, width, height, mMaskPaint);
        }
    }

    @Override
    public void endDrawShape(Canvas canvas){
        if (!canDrawRound()) {
            return;
        }
        canvas.restore();
    }
}
