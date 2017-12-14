package com.quick.core.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by dailichun on 2017/12/6.
 * 自定义ImageView, 点击时半透明
 */
public class NbImageView extends ImageView {

    public NbImageView(Context context, AttributeSet attrs) {
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
