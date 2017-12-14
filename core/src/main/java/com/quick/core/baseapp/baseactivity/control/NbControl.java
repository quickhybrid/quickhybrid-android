package com.quick.core.baseapp.baseactivity.control;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quick.core.baseapp.theme.BaseThemeControl;
import com.quick.core.ui.app.INbControl;
import com.quick.core.ui.app.ThemeBean;
import com.quick.core.ui.widget.DrawableText;
import com.quick.core.ui.widget.NbImageView;
import com.quick.core.ui.widget.NbTextView;
import com.quick.core.util.reflect.ResManager;

import quick.com.core.R;



/**
 * Created by dailichun on 2017/12/8.
 * 框架默认导航栏控制器，对应布局文件R.layout.frm_nb_style1。需要传递参数nbStyle=1。
 */
public class NbControl implements INbControl, View.OnClickListener {

    public ViewHolder holder;

    public View rootView;

    public INbOnClick clickListener;

    public Context context;

    /**
     * @param context       上下文
     * @param clickListener 点击事件接口
     */
    public NbControl(Context context, INbOnClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;

        initView();

        setTheme(BaseThemeControl.getInstance().getSelectedTheme());
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.rootView = inflater.inflate(R.layout.frm_nb_style1, null);

        holder = new INbControl.ViewHolder();

        holder.nbRoot = rootView.findViewById(R.id.nbRoot);
        holder.line = (LinearLayout) rootView.findViewById(R.id.line);

        holder.nbBack = (NbImageView) rootView.findViewById(R.id.nbLeftIv1);
        holder.nbBack.setOnClickListener(this);
        holder.nbLeftTv1 = (DrawableText) rootView.findViewById(R.id.nbLeftTv1);
        holder.nbLeftTv1.setClickAnimation(true);
        holder.nbLeftTv1.setOnClickListener(this);
        holder.nbLeftTv2 = (NbTextView) rootView.findViewById(R.id.nbLeftTv2);
        holder.nbLeftTv2.setOnClickListener(this);
        holder.nbLeftIv2 = (NbImageView) rootView.findViewById(R.id.nbLeftIv2);
        holder.nbLeftIv2.setOnClickListener(this);

        holder.nbRightIvs = new NbImageView[4];
        holder.nbRightIvs[0] = (NbImageView) rootView.findViewById(R.id.nbRightIv1);
        holder.nbRightIvs[0].setOnClickListener(this);
        holder.nbRightIvs[1] = (NbImageView) rootView.findViewById(R.id.nbRightIv2);
        holder.nbRightIvs[1].setOnClickListener(this);
        holder.nbRightIvs[2] = (NbImageView) rootView.findViewById(R.id.nbRightIv3);
        holder.nbRightIvs[2].setOnClickListener(this);
        holder.nbRightIvs[3] = (NbImageView) rootView.findViewById(R.id.nbRightIv4);
        holder.nbRightIvs[3].setOnClickListener(this);
        holder.nbRightTvs = new NbTextView[2];
        holder.nbRightTvs[0] = (NbTextView) rootView.findViewById(R.id.nbRightTv1);
        holder.nbRightTvs[0].setOnClickListener(this);
        holder.nbRightTvs[1] = (NbTextView) rootView.findViewById(R.id.nbRightTv2);
        holder.nbRightTvs[1].setOnClickListener(this);

        holder.titleParent = rootView.findViewById(R.id.rl_title);
        holder.titleParent.setClickable(false);
        holder.titleParent.setOnClickListener(this);
        holder.nbCustomTitleLayout = (FrameLayout) rootView.findViewById(R.id.nbCustomTitleLayout);
        holder.nbTitle = (TextView) rootView.findViewById(R.id.nbTitle);
        holder.nbTitle2 = (TextView) rootView.findViewById(R.id.nbTitle2);
        holder.ivTitleArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void hide() {
        if (holder.nbRoot != null) {
            holder.nbRoot.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        if (holder.nbRoot != null) {
            holder.nbRoot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLine() {
        if (holder.line != null) {
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideNbBack() {
        if (holder.nbBack != null) {
            holder.nbBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNbBack() {
        if (holder.nbBack != null) {
            holder.nbBack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addNbCustomTitleView(View view) {
        if (holder.titleParent == null || holder.nbCustomTitleLayout == null) {
            return;
        }
        holder.titleParent.setVisibility(View.GONE);
        holder.nbCustomTitleLayout.setVisibility(View.VISIBLE);
        holder.nbCustomTitleLayout.removeAllViews();
        holder.nbCustomTitleLayout.addView(view);
    }

    @Override
    public void setTitleClickable(boolean clickable, int arrow) {
        if (holder.titleParent == null || holder.ivTitleArrow == null) {
            return;
        }
        holder.titleParent.setClickable(clickable);
        if (clickable) {
            holder.ivTitleArrow.setImageResource(arrow);
            holder.ivTitleArrow.setVisibility(View.VISIBLE);
        } else {
            holder.ivTitleArrow.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder getViewHolder() {
        return holder;
    }

    @Override
    public String getCondition() {
        return "";
    }

    @Override
    public void setTheme(ThemeBean bean) {
        //设置图片文字过滤色
        setColorFilter(bean.topbarFilterColor);
        //设置返回按钮图片
        setNbBackImage(bean.topbarBackImage);
        //设置背景色
        setNbBackground(bean.topbarImage);
    }

    @Override
    public void setNbBackImage(Object bg) {
        if (holder.nbBack != null) {
            if (bg instanceof Integer) {
                holder.nbBack.setImageResource((int) bg);
            } else if (bg instanceof String) {
                holder.nbBack.setImageResource(ResManager.getDrawableInt((String) bg));
            }
        }
    }

    @Override
    public void setNbBackground(Object bg) {
        if (holder.nbRoot != null) {
            if (bg instanceof Integer) {
                int themeColor = (int) bg;
                if (themeColor <= 0) {
                    holder.nbRoot.setBackgroundColor(themeColor);
                } else {
                    holder.nbRoot.setBackgroundResource(themeColor);
                }
            } else if (bg instanceof String) {
                String themeColor = (String) bg;
                if (themeColor.startsWith("#")) {
                    holder.nbRoot.setBackgroundColor(Color.parseColor(themeColor));
                } else {
                    Log.e("NbControl", "导航栏背景只能设置颜色不能设置图片");
//                    holder.nbRoot.setBackgroundResource(ResManager.getDrawableInt(themeColor));
                }
            }
        }
    }

    @Override
    public void setColorFilter(Object filterColor) {
        int filterColorId = Color.BLACK;

        if (filterColor instanceof Integer) {
            int themeColor = (int) filterColor;
            if (themeColor <= 0) {
                filterColorId = themeColor;
            } else {
                filterColorId = context.getResources().getColor(themeColor);
            }
        } else if (filterColor instanceof String) {
            String themeColor = (String) filterColor;
            if (themeColor.startsWith("#")) {
                filterColorId = Color.parseColor(themeColor);
            } else {
                filterColorId = context.getResources().getColor(ResManager.getColorInt(themeColor));
            }
        }
        if (holder.nbRightTvs != null) {
            for (NbTextView tv : holder.nbRightTvs) {
                if (tv != null) {
                    tv.setTextColor(filterColorId);
                }
            }
        }
        if (holder.nbRightIvs != null) {
            for (NbImageView iv : holder.nbRightIvs) {
                if (iv != null) {
                    iv.setColorFilter(filterColorId);
                }
            }
        }
        if (holder.nbBack != null) {
            holder.nbBack.setColorFilter(filterColorId);
        }
        if (holder.nbLeftTv1 != null) {
            holder.nbLeftTv1.setTextColor(filterColorId);
        }
        if (holder.nbLeftIv2 != null) {
            holder.nbLeftIv2.setColorFilter(filterColorId);
        }
        if (holder.nbLeftTv2 != null) {
            holder.nbLeftTv2.setTextColor(filterColorId);
        }
    }

    @Override
    public void setNbTitle(String title) {
        if (holder.nbTitle != null) {
            holder.nbTitle.setText(title);
        }
    }

    @Override
    public void setNbTitle(String title, String title2) {
        setNbTitle(title);
        if (holder.nbTitle2 != null) {
            if (TextUtils.isEmpty(title2)) {
                holder.nbTitle2.setVisibility(View.GONE);
            } else {
                holder.nbTitle2.setText(title2);
                holder.nbTitle2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            if (holder.nbRightTvs != null) {
                for (int i = 0; i < holder.nbRightTvs.length; i++) {
                    if (v == holder.nbRightTvs[i]) {
                        clickListener.onNbRight(v, i);
                        return;
                    }
                }
            }
            if (holder.nbRightIvs != null) {
                for (int i = 0; i < holder.nbRightIvs.length; i++) {
                    if (v == holder.nbRightIvs[i]) {
                        clickListener.onNbRight(v, i);
                        return;
                    }
                }
            }
            if (v == holder.titleParent) {
                clickListener.onNbTitle(v);
                return;
            }
            if (v == holder.nbBack) {
                clickListener.onNbBack();
                return;
            }
            if (v == holder.nbLeftTv1 || v == holder.nbLeftIv2 || v == holder.nbLeftTv2) {
                clickListener.onNbLeft(v);
            }
        }
    }
}
