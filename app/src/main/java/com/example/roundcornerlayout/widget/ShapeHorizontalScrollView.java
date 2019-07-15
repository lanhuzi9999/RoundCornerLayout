package com.example.roundcornerlayout.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.annotation.Nullable;

import com.example.roundcornerlayout.R;
import com.example.roundcornerlayout.reflect.ReflectHelper;


public class ShapeHorizontalScrollView extends HorizontalScrollView {
    private AbstractShapeDelegate   mShapeDelegate;
    private boolean                 mCallInDraw;
    public ShapeHorizontalScrollView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ShapeHorizontalScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ShapeHorizontalScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs,defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShapeHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs,defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mShapeDelegate = createShapeDelegate(context, attrs, defStyleAttr, defStyleRes);
        if (mShapeDelegate != null) {
            mShapeDelegate.setup(attrs, defStyleAttr, defStyleRes);
            setWillNotDraw(false);
        }
    }

    protected AbstractShapeDelegate createShapeDelegate(Context context, AttributeSet attrs,
                                                        int defStyleAttr, int defStyle){
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeView, defStyle, 0);
            String shape = a.getString(R.styleable.ShapeView_shape);
            a.recycle();
            if (!TextUtils.isEmpty(shape)){
                String defShape = AbstractShapeDelegate.getDefaultShape(shape);
                if (!TextUtils.isEmpty(defShape)){
                    shape = defShape;
                }
                return (AbstractShapeDelegate) ReflectHelper.newInstance(shape,
                        new Class<?>[]{View.class},
                        new Object[]{this});
            }
        }
        return null ;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mCallInDraw){
            super.dispatchDraw(canvas);
        }else {
            mCallInDraw = true;
            if (mShapeDelegate == null ){
                super.dispatchDraw(canvas);
            }else{
                mShapeDelegate.beginDrawShape(canvas);
                super.dispatchDraw(canvas);
                mShapeDelegate.endDrawShape(canvas);
            }
        }
        mCallInDraw = false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mCallInDraw){
            super.draw(canvas);
        }else {
            mCallInDraw = true;
            if (mShapeDelegate == null || !mShapeDelegate.overrideDraw(canvas)) {
                super.draw(canvas);
            }
        }
        mCallInDraw = false;
    }

    public void updateShape (String shapeType){
        String defShape = AbstractShapeDelegate.getDefaultShape(shapeType);
        if (TextUtils.isEmpty(defShape)){
            return ;
        }
        if (mShapeDelegate != null){
            String className = mShapeDelegate.getClass().getName();
            if (className.equals(defShape)){
                return ;
            }
        }
        AbstractShapeDelegate shapeDelegate = (AbstractShapeDelegate) ReflectHelper.newInstance(defShape,
                new Class<?>[]{View.class},
                new Object[]{this});
        if (shapeDelegate != null){
            shapeDelegate.setup(null, 0, 0);
            mShapeDelegate = shapeDelegate;
        }
    }

    public void updateRadius(float topLeft, float topRight, float bottomLeft, float bottomRight){
        if (mShapeDelegate instanceof RoundViewDelegate){
            ((RoundViewDelegate)mShapeDelegate).updateRadius(topLeft,topRight, bottomLeft, bottomRight);
        }
        postInvalidate();
    }
}
