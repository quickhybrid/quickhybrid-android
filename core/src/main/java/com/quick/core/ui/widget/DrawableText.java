package com.quick.core.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Created by dailichun on 2017/12/6.
 * 带图片的TextView
 */
public class DrawableText extends TextView {

    public static final int DIRECTION_TOP = 1;
    public static final int DIRECTION_LEFT = 2;
    public static final int DIRECTION_RIGHT = 3;
    public static final int DIRECTION_BOTTOM = 4;

    private boolean clickAnimation = false;

    private LinearLayout.LayoutParams lp;

    public DrawableText(Context context) {
        super(context);
    }

    public DrawableText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawableText(Context context, String text) {
        this(context, text, -1, 0, 0, 0);
    }

    public DrawableText(Context context, String text, int drawableId, int direction, int width, int height) {
        this(context, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f), text, drawableId, direction, width, height);
    }

    public DrawableText(Context context, LinearLayout.LayoutParams layoutParams, String text, int drawableId, int direction, int width, int height) {
        super(context);
        this.lp = layoutParams;
        setLayoutParams(layoutParams);
        setGravity(Gravity.CENTER);
        setTextSize(16);
        setText(text);
        setDrawable(drawableId, direction, width, height);
    }

    public void setDrawable(int drawableId, int direction) {
        if (drawableId > 0) {
            Drawable image = getContext().getResources().getDrawable(drawableId);
            setDrawable(image, direction, -1, -1);
        }
    }

    public void setDrawable(int drawableId, int direction, int width, int height) {
        if (drawableId > 0) {
            Drawable image = getContext().getResources().getDrawable(drawableId);
            setDrawable(image, direction, width, height);
        }
    }

    public void setDrawable(Drawable image, int direction, int width, int height) {
        if (width == -1 || height == -1) {
            image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
        } else {
            image.setBounds(0, 0, width, height);
        }
        if (direction == DIRECTION_TOP) {
            setCompoundDrawables(null, image, null, null);
        } else if (direction == DIRECTION_RIGHT) {
            setCompoundDrawables(null, null, image, null);
        } else if (direction == DIRECTION_LEFT) {
            setCompoundDrawables(image, null, null, null);
        } else if (direction == DIRECTION_BOTTOM) {
            setCompoundDrawables(null, null, null, image);
        }
    }

    public LinearLayout.LayoutParams getMyLayoutParams() {
        return lp;
    }

    /**
     * 设置点击效果
     *
     * @param clickAnimation
     */
    public void setClickAnimation(boolean clickAnimation) {
        this.clickAnimation = clickAnimation;
        setClickable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clickAnimation) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                setAlpha(0.6f);
            } else {
                setAlpha(1.0f);
            }
        }
        return super.onTouchEvent(event);
    }
}
