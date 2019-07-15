package com.example.roundcornerlayout.widget;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractShapeDelegate {
    private final static Map<String, String> DEFAULT = new HashMap<String, String>();
    static{
        DEFAULT.put("round", RoundViewDelegate.class.getName());
        DEFAULT.put("circle", CircleViewDelegate.class.getName());
        DEFAULT.put("oval", OvalViewDelegate.class.getName());
    }
    public static String getDefaultShape(String shape){
        return DEFAULT.get(shape);
    }
    public static void addDefaultShape(String shapeName, Class<? extends AbstractShapeDelegate> shapeClass){
        DEFAULT.put(shapeName, shapeClass.getName());
    }

    private View mView ;
    public AbstractShapeDelegate(View view){
        mView = view ;
    }
    public View getView(){
        return mView ;
    }
    abstract public void setup(AttributeSet attrs, int defStyleAttr, int defStyleRes);
    abstract public void beginDrawShape(Canvas canvas);
    abstract public void endDrawShape(Canvas canvas);
    public boolean overrideDraw(Canvas canvas){
        beginDrawShape(canvas);
        mView.draw(canvas);
        endDrawShape(canvas);
        return true;
    }
}
