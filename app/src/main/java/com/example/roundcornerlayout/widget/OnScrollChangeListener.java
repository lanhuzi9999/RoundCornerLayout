package com.example.roundcornerlayout.widget;

import android.view.View;

/**
 * 由于Android 22以前的版本缺少View.OnScrollChangeListener，故要用这个接口来处理这些View的滚动事件
 */
public interface OnScrollChangeListener {
    void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
}
