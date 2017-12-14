package com.quick.core.ui.widget.popmenu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quick.core.util.app.AppUtil;
import com.quick.core.util.device.DensityUtil;

import quick.com.core.R;

/**
 * Created by dailichun on 2017/12/6.
 * 导航栏弹出列表选择菜单，固定右上角
 */
public class FrmPopMenu implements AdapterView.OnItemClickListener {

    PopupWindow pop;

    /**
     * 弹出位置相对控件，传导航栏根布局即可
     */
    View anchor;

    /**
     * 上下文
     */
    Activity context;

    //标题
    String[] titles;

    //图片资源,Object如果是int则是本地图片资源id,如果是String则是网络图片
    Object[] imgs;

    /**
     * 点击事件
     */
    PopClickListener listener;

    /**
     * 图标过滤色，默认不设置
     */
    int iconFilterColor;

    /**
     * 选中项
     */
    int selected = -1;

    PopChangedListener changedListener;

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param anchor   导航栏根布局
     * @param titles   标题
     * @param imgs     图标
     * @param listener 点击事件
     */
    public FrmPopMenu(Activity context, View anchor, String[] titles, Object[] imgs, PopClickListener listener) {
        this(context, anchor, titles, imgs, -1, listener);
    }

    /**
     * 构造函数
     *
     * @param context  上下文
     * @param anchor   导航栏根布局
     * @param titles   标题
     * @param imgs     图标
     * @param listener 点击事件
     */
    public FrmPopMenu(Activity context, View anchor, String[] titles, Object[] imgs, int selected, PopClickListener listener) {
        this.context = context;
        this.anchor = anchor;
        this.titles = titles;
        this.imgs = imgs;
        this.listener = listener;
        this.selected = selected;
        if (imgs != null && imgs.length != this.titles.length) {
            try {
                throw new Exception("titles number can not match images count");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setPopChangedListener(PopChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    /**
     * 设置图标过滤色
     *
     * @param color
     */
    public void setIconFilterColor(int color) {
        iconFilterColor = color;
    }

    /**
     * 判断是否正在显示
     *
     * @return
     */
    public boolean isShowing() {
        return pop.isShowing();
    }

    /**
     * 显示菜单
     */
    public void show() {
        if (pop==null){
            setPopWindow();
        }
        if (!pop.isShowing()) {
            pop.showAsDropDown(anchor, anchor.getWidth() - pop.getWidth(), 0);
            if (changedListener != null) {
                changedListener.onShow(FrmPopMenu.this);
            }
        }
    }

    /**
     * 获取弹出视图
     *
     * @return
     */
    public void setPopWindow() {
        ListView lv = new ListView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 150), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 0, 15, 15);
        lv.setLayoutParams(lp);
        lv.setBackgroundColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lv.setElevation(15);
        }
        lv.setAdapter(new IconAdapter());
        lv.setOnItemClickListener(this);

        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, 200), ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.addView(lv);
        pop = new PopupWindow(ll, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        //必须添加背景，否则点击空白无法自动隐藏
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (changedListener != null) {
                    changedListener.onHide(FrmPopMenu.this);
                }
            }
        });
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        if (pop == null) {
            return;
        }
        if (pop.isShowing()) {
            pop.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        if (listener != null) {
            listener.onClick(position);
        }
    }

    class IconAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(R.layout.frm_list_pop_adapter, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
                holder.tvIcon = (TextView) convertView.findViewById(R.id.tvIcon);
                holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (imgs == null) {
                holder.ivIcon.setVisibility(View.GONE);
            } else {
                holder.ivIcon.setVisibility(View.VISIBLE);
                if (imgs[position] instanceof Integer) {
                    holder.ivIcon.setImageResource((int) imgs[position]);
                } else if (imgs[position] instanceof String) {
                    ImageLoader.getInstance().displayImage((String) imgs[position], holder.ivIcon, AppUtil.getImageLoaderOptions(0, 0, true, true));
                }

                if (iconFilterColor != 0) {
                    holder.ivIcon.setColorFilter(iconFilterColor);
                }
            }
            if (position == selected) {
                holder.tvIcon.setTextColor(context.getResources().getColor(R.color.text_blue));
                holder.ivSelected.setVisibility(View.VISIBLE);
            } else {
                holder.tvIcon.setTextColor(context.getResources().getColor(R.color.text_black));
                holder.ivSelected.setVisibility(View.INVISIBLE);
            }
            holder.tvIcon.setText(titles[position]);
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView ivIcon;
        public TextView tvIcon;
        public ImageView ivSelected;
    }
}
