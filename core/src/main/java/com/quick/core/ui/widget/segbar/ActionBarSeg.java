
package com.quick.core.ui.widget.segbar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.quick.core.util.device.DensityUtil;

import quick.com.core.R;


/**
 * Created by dailichun on 2017/12/6.
 * 框架ActionBar提供的自定义组件，用于在导航栏显示选项卡式标签。
 * 注意，手机上设置2个标题时效果最佳
 * 在平板上设置可以超过3个，具体个数因设备而异。
 */
public class ActionBarSeg extends RelativeLayout implements OnClickListener {

    private SegActionCallBack action;

    private LinearLayout linearLayout;

    private View vTrans;

    private Context context;

    private int nowIndex;

    private float fromX;

    /**
     * 按钮默认宽度
     */
    private int buttonWidthPX = 90;

    /**
     * 按钮默认高度
     */
    private int buttonHeightPX = 28;

    /**
     * 按钮默认文字大小
     */
    private float textSize = 17;

    private int buttonWidthDip;

    private int buttonHeightDip;

    private int moveLength;

    private String[] titles;

    /**
     * 未选中时按钮文字颜色
     */
    private int textUnSelectedColor = R.color.text_blue;

    public ActionBarSeg(Context context) {
        super(context);
    }

    public ActionBarSeg(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * @param context 调用上下文，一般指Activity实例
     * @param titles  顶部设置的tab按钮标题，数组类型
     * @param action  索引选择事件回调
     */
    public ActionBarSeg(Context context, String[] titles, SegActionCallBack action) {

        super(context);

        this.context = context;

        this.action = action;

        this.titles = titles;

        this.nowIndex = 0;

        buttonWidthPX=context.getResources().getDimensionPixelSize(R.dimen.frm_nb_tab_width);
        buttonHeightPX=context.getResources().getDimensionPixelSize(R.dimen.frm_nb_tab_height);

    }

    public ActionBarSeg create() {
        int childCount = titles.length;
        if (childCount < 1) {
            return null;
        }

        setBackground();

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        //标题选中时的背景
        int textSelectedBg = R.drawable.frm_nav_tab_btn_bg;

        LinearLayout llBG = new LinearLayout(context);
        llBG.setOrientation(LinearLayout.HORIZONTAL);
        llBG.setGravity(Gravity.CENTER);

        //默认选中第一个
        int defaultIndex = 0;
        for (int i = 0; i < childCount; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonWidthDip, buttonHeightDip);
            View btn = new View(context);
            btn.setPadding(0, 0, 0, 0);
            if (i != defaultIndex) {
                btn.setBackgroundColor(Color.TRANSPARENT);
            } else {
                btn.setBackgroundResource(textSelectedBg);
                vTrans = btn;
            }
            btn.setLayoutParams(params);
            llBG.addView(btn);
        }

        LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        llParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(llBG, llParams);

        for (int i = 0; i < childCount; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonWidthDip, buttonHeightDip);
            Button btn = new Button(context);
            btn.setPadding(0, 0, 0, 0);
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setOnClickListener(this);
            btn.setLayoutParams(params);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            btn.setGravity(Gravity.CENTER);
            btn.setText(titles[i]);
            if (i == defaultIndex) {
                btn.setTextColor(Color.WHITE);
            } else {
                btn.setTextColor(getResources().getColor(textUnSelectedColor));
            }
            linearLayout.addView(btn);
        }

        addView(linearLayout, llParams);

        return this;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            if (v == child) {
                clickAction(i);
                ((Button) child).setTextColor(Color.WHITE);
            } else {
                ((Button) child).setTextColor(getResources().getColor(textUnSelectedColor));
            }
        }
    }

    public void clickAction(int i) {
        float toX = fromX + (i - nowIndex) * moveLength;
        trans(200, fromX, toX);
        fromX = toX;
        nowIndex = i;
        action.segAction(i);
    }

    /**
     * 设置按钮文字大小
     *
     * @param textSize
     * @return
     */
    public ActionBarSeg setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * 设置按钮宽度
     *
     * @param buttonWidthPX
     * @return
     */
    public ActionBarSeg setButtonWidthPX(int buttonWidthPX) {
        this.buttonWidthPX = buttonWidthPX;
        return this;
    }

    /**
     * 设置按钮高度
     *
     * @param buttonHeightPX
     * @return
     */
    public ActionBarSeg setButtonHeightPX(int buttonHeightPX) {
        this.buttonHeightPX = buttonHeightPX;
        return this;
    }

    private void setBackground() {
        buttonWidthDip = DensityUtil.dip2px(context, buttonWidthPX);
        buttonHeightDip = DensityUtil.dip2px(context, buttonHeightPX);
        moveLength = buttonWidthDip;

        //设置居中
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, buttonHeightDip);
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);

        if (titles.length < 3) {
            //设置背景边框
            setBackgroundResource(R.drawable.frm_nav_tab_bg);
        }
    }

    private void trans(long dura, float frX, float toX) {
        TranslateAnimation animation = new TranslateAnimation(frX, toX, 0.0F, 0.0F);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(dura);
        animation.setFillAfter(true);
        vTrans.startAnimation(animation);
    }
}
