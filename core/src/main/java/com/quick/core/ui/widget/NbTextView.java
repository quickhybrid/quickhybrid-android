package com.quick.core.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by dailichun on 2017/12/6.
 * 自定义TextView, 点击时半透明
 */
public class NbTextView extends TextView {

    public NbTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            setAlpha(0.6f);
        } else {
            setAlpha(1.0f);
        }
        return super.onTouchEvent(event);
    }
}
