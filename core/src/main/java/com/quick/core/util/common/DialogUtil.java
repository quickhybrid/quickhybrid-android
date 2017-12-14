package com.quick.core.util.common;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;


import com.quick.core.ui.widget.dialog.DialogSelectAdapter;
import com.quick.core.ui.widget.dialog.QuickDatePickerDialog;
import com.quick.core.ui.widget.dialog.QuickDialog;
import com.quick.core.ui.widget.dialog.QuickImageDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import quick.com.core.R;

/**
 * Created by dailichun on 2017/12/6.
 * 对话框工具类。所有的对话框按钮跟系统保持一致，左边取消右边确认。
 */
public class DialogUtil {

    static int themeId = AlertDialog.THEME_HOLO_LIGHT;

    /**
     * 确认对话框
     *
     * @param con
     * @param title           标题
     * @param titleGravity    标题对齐方式
     * @param message         提示内容
     * @param messageGravity  内容对齐方式
     * @param cancelable      返回键是否可取消
     * @param button1         按钮1名称
     * @param button2         按钮2名称
     * @param listener1       按钮1触发的事件
     * @param listener2       按钮2触发的事件
     * @param dismissListener 隐藏对话框触发的事件
     */
    public static void showConfirmDialog(Context con, String title, int titleGravity, String message, int messageGravity, boolean cancelable, String button1,
                                         String button2, DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2, DialogInterface.OnDismissListener dismissListener) {
        QuickDialog.Builder builder = new QuickDialog.Builder(con);
        // 设置返回键是否退出
        builder.setCancelable(cancelable);
        //设置标题
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
            if (titleGravity > 0) {
                builder.setTitleGravity(titleGravity);
            }
        }
        //设置提醒信息
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
            if (messageGravity > 0) {
                builder.setMessageGravity(messageGravity);
            }
        }
        //设置右侧按钮
        if (!TextUtils.isEmpty(button1)) {
            builder.setPositiveButton(button1, listener1);
        }
        //设置左侧按钮
        if (!TextUtils.isEmpty(button2)) {
            builder.setNegativeButton(button2, listener2);
        }
        if (dismissListener != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                builder.setOnDismissListener(dismissListener);
            }
        }
        builder.create().show();
    }

    public static void showConfirmDialog(Context con, String title, String message, boolean cancelable, String button1,
                                         String button2, DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2) {
        showConfirmDialog(con, title, message, cancelable, button1, button2, listener1, listener2, null);
    }

    public static void showConfirmDialog(Context con, String title, String message, boolean cancelable, String button1,
                                         String button2, DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2, DialogInterface.OnDismissListener dismissListener) {
        showConfirmDialog(con, title, 0, message, 0, cancelable, button1, button2, listener1, listener2, dismissListener);
    }

    public static void showConfirmDialog(Context con, String title, String message, boolean cancelable, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener2) {
        showConfirmDialog(con, title, message, cancelable, con.getString(R.string.confirm), con.getString(R.string.cancel), listener, listener2);
    }

    public static void showConfirmDialog(Context con, String title, String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener2) {
        showConfirmDialog(con, title, message, true, listener, listener2);
    }

    public static void showConfirmDialog(Context con, String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener2) {
        showConfirmDialog(con, message, "", true, listener, listener2);
    }

    public static void showConfirmDialog(Context con, String title, String message, boolean cancelable, DialogInterface.OnClickListener listener) {
        showConfirmDialog(con, title, message, cancelable, con.getString(R.string.confirm), null, listener, null);
    }

    public static void showConfirmDialog(Context con, String title, String message, boolean cancelable, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener2, DialogInterface.OnDismissListener dismissListener) {
        showConfirmDialog(con, title, message, cancelable, con.getString(R.string.confirm), null, listener, listener2, dismissListener);
    }

    public static void showConfirmDialog(Context con, String title, String message, DialogInterface.OnClickListener listener) {
        showConfirmDialog(con, title, message, true, listener);
    }

    public static void showConfirmDialog(Context con, String title, String message, String btnText) {
        showConfirmDialog(con, title, message, true, btnText, null, null, null);
    }

    public static void showConfirmDialog(Context con, String title, String message) {
        showConfirmDialog(con, title, message, con.getString(R.string.confirm));
    }

    /**
     * 有图片的确认对话框
     *
     * @param con
     * @param title           标题
     * @param message         提示内容
     * @param cancelable      返回键是否可取消
     * @param btnText         按钮1名称
     * @param listener        按钮2名称
     * @param dismissListener 隐藏对话框触发的事件
     */
    public static void showImageDialog(Context con, int image, String title, String message, boolean cancelable, String btnText, DialogInterface.OnClickListener listener, DialogInterface.OnDismissListener dismissListener) {
        QuickImageDialog.Builder builder = new QuickImageDialog.Builder(con);
        // 设置返回键是否退出
        builder.setCancelable(cancelable);
        //设置图片
        builder.setImage(image);
        //设置标题
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        //设置提醒信息
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        //设置按钮
        if (!TextUtils.isEmpty(btnText)) {
            builder.setPositiveButton(btnText, listener);
        }
        //设置对话框取消监听
        if (dismissListener != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                builder.setOnDismissListener(dismissListener);
            }
        }
        builder.create().show();
    }

    /**
     * 发送对话框
     *
     * @param con
     * @param title
     * @param message
     */
    public static void showSendDialog(final Context con, final String title, final String message) {
        DialogUtil.showConfirmDialog(con, title, Gravity.CENTER, message, Gravity.START, true, con.getString(R.string.copy), con.getString(R.string.send), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialo, int which) {
                ClipboardManager cbm = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
                cbm.setPrimaryClip(ClipData.newPlainText("simple text", TextUtils.isEmpty(message) ? title : message));
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, TextUtils.isEmpty(message) ? title : message);
                sendIntent.setType("text/plain");
                con.startActivity(Intent.createChooser(sendIntent, con.getString(R.string.send)));
            }
        }, null);
    }

    /**
     * 单选对话框
     *
     * @param con        上下文
     * @param title      标题，可为空
     * @param cancelable 是否可取消
     * @param menuitems  选项
     * @param columns    几列
     * @param listener   点击事件
     * @return
     */
    public static void showMenuDialog(Context con, String title, boolean cancelable, String menuitems[], int columns,
                                      final DialogInterface.OnClickListener listener) {
        //创建自定义根布局
        LinearLayout ll = new LinearLayout(con);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(layoutParams);
        ll.setOrientation(LinearLayout.VERTICAL);
        if (!TextUtils.isEmpty(title)) {
            //如果有title则添加一条线
            LinearLayout line = new LinearLayout(con);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            line.setBackgroundColor(con.getResources().getColor(R.color.line));
            line.setLayoutParams(lp);
            ll.addView(line);
        }
        //定义数据源
        ArrayList<HashMap<String, Object>> meumList = new ArrayList<>();
        for (String item : menuitems) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("text", item);
            meumList.add(map);
        }
        //添加ListView控件
        AbsListView listView;
        if (columns <= 1) {
            listView = new ListView(con);
            listView.setLayoutParams(layoutParams);
            DialogSelectAdapter adapter = new DialogSelectAdapter(con, meumList);
            listView.setAdapter(adapter);
        } else {
            listView = new GridView(con);
            ((GridView) listView).setNumColumns(columns);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 15, 0, 15);
            listView.setLayoutParams(lp);
            SimpleAdapter sim_adapter = new SimpleAdapter(con, meumList, R.layout.frm_simple_adapter, new String[]{"text"}, new int[]{R.id.tv});
            listView.setAdapter(sim_adapter);
        }
        ll.addView(listView);
        //创建对话框
        QuickDialog.Builder builder = new QuickDialog.Builder(con);
        builder.setContentView(ll);
        builder.setTitle(title);
        builder.highlightTitle(true);
        builder.setCancelable(cancelable);
        builder.setGravity(columns <= 1 ? Gravity.LEFT : Gravity.CENTER);
        builder.setPositiveButton("", null);
        final QuickDialog dialog = builder.create();
        //设置列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onClick(dialog, position);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        //显示对话框
        dialog.show();
    }

    /**
     * 选择对话框
     *
     * @param con       上下文
     * @param menuitems 选项
     * @param listener  点击事件
     */
    public static void showMenuDialog(Context con, String menuitems[], DialogInterface.OnClickListener listener) {
        showMenuDialog(con, "", true, menuitems, 1, listener);
    }

    /**
     * 选择对话框
     *
     * @param con        上下文
     * @param cancelable 是否可取消
     * @param menuitems  选项
     * @param listener   点击事件
     */
    public static void showMenuDialog(Context con, boolean cancelable, String menuitems[], DialogInterface.OnClickListener listener) {
        showMenuDialog(con, "", cancelable, menuitems, 1, listener);
    }

    /**
     * 单选列表对话框
     *
     * @param con        上下文
     * @param title      标题，可为空
     * @param cancelable 是否可取消
     * @param menuitems  选项
     * @param listener   点击事件
     * @return
     */
    public static void showMenuDialog(Context con, String title, boolean cancelable, String menuitems[], DialogInterface.OnClickListener listener) {
        showMenuDialog(con, title, cancelable, menuitems, 1, listener);
    }

    /**
     * 多选对话框
     *
     * @param con        上下文
     * @param title      标题
     * @param cancelable 是否可取消
     * @param meumList   选项,key值必须包涵text，isChecked
     * @param listener   点击确认按钮
     */
    public static void showMultiMenuDialog(Context con, String title, boolean cancelable, List<HashMap<String, Object>> meumList, DialogInterface.OnClickListener listener) {
        //创建自定义布局
        LinearLayout ll = new LinearLayout(con);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(layoutParams);
        ll.setOrientation(LinearLayout.VERTICAL);
        //添加一条线
        LinearLayout line = new LinearLayout(con);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        line.setBackgroundColor(con.getResources().getColor(R.color.line));
        line.setLayoutParams(lp);
        ll.addView(line);
        //添加列表布局
        ListView lv = new ListView(con);
        lv.setLayoutParams(layoutParams);
        final DialogSelectAdapter adapter = new DialogSelectAdapter(con, meumList, true);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.onItemClick(view, position);
            }
        });
        ll.addView(lv);
        //创建对话框
        QuickDialog.Builder builder = new QuickDialog.Builder(con);
        builder.setContentView(ll);
        builder.setTitle(title);
        builder.setGravity(Gravity.LEFT);
        builder.setCancelable(cancelable);
        builder.setPositiveButton("确定", listener);
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 日期和时间选择对话框，选选择日期后选择时间
     *
     * @param con      上下文
     * @param title1   日期选择标题
     * @param title2   事件选择标题
     * @param calendar 默认日期
     * @param listener 时间选择事件
     */
    public static void pickDateTime(final Context con, String title1, final String title2, final Calendar calendar, final OnTimeSetListener listener) {
        // 增加判断，解决4.X系统可能存在的弹出多个时间选择器的问题
        final boolean[] isShowTime = {false};
        pickDate(con, title1, calendar, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                if (!isShowTime[0]) {
                    isShowTime[0] = true;
                    pickTime(con, title2, calendar, listener);
                }
            }
        });
    }

    /**
     * 创建日期和时间选择对话框
     *
     * @param con      上下文
     * @param title1   选择日期标题
     * @param tltle2   选择时间标题
     * @param views    如果是TextView会自动设置上选择的时间
     * @param calendar 默认时间
     */
    public static void pickDateTime(final Context con, final String title1, final String tltle2, final View[] views, final Calendar calendar) {
        // 增加判断，解决4.X系统可能存在的弹出多个时间选择器的问题
        final boolean[] isShowTime = {false};
        QuickDatePickerDialog dialog = new QuickDatePickerDialog(con, themeId, title1, calendar, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                if (!isShowTime[0]) {
                    isShowTime[0] = true;
                    TimePickerDialog timePick = new TimePickerDialog(con, themeId, new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            cal.set(Calendar.MINUTE, minute);
                            String chooseDate = DateUtil.convertDate(cal.getTime(), con.getString(R.string.date_format));
                            for (View tv : views) {
                                if (tv != null && tv instanceof TextView) {
                                    ((TextView) tv).setText(chooseDate);
                                }
                            }
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePick.setTitle(tltle2);
                    timePick.show();
                }
            }
        });
        dialog.show();
    }

    public static void pickDateTime(final Context con, final View[] views, final Calendar calendar) {
        pickDateTime(con, con.getString(R.string.pick_date), con.getString(R.string.pick_time), views, calendar);
    }

    public static void pickDateTime(Context con, Calendar calendar, OnTimeSetListener listener) {
        pickDateTime(con, con.getString(R.string.pick_date), con.getString(R.string.pick_time), calendar, listener);
    }

    public static void pickDateTime(Context con, View v) {
        pickDateTime(con, v, Calendar.getInstance());
    }

    public static void pickDateTime(Context con, View v, Calendar calendar) {
        pickDateTime(con, new View[]{v}, calendar);
    }

    /**
     * 时间选择对话框
     *
     * @param con      上下文
     * @param title    标题
     * @param listener 选择事件
     * @param cal      默认时间
     */
    public static void pickTime(Context con, String title, Calendar cal, final OnTimeSetListener listener) {
        LinearLayout ll = new LinearLayout(con);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(layoutParams);
        ll.setOrientation(LinearLayout.VERTICAL);
        //添加一条线
        LinearLayout line = new LinearLayout(con);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        line.setBackgroundColor(con.getResources().getColor(R.color.line));
        line.setLayoutParams(lp);
        ll.addView(line);
        //添加选择器控件
        final TimePicker timePicker = new TimePicker(con, null, themeId);
        timePicker.setLayoutParams(layoutParams);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        timePicker.setIs24HourView(true);
        ll.addView(timePicker);
        QuickDialog.Builder builder = new QuickDialog.Builder(con);
        builder.setContentView(ll);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onTimeSet(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            }
        });
        builder.create().show();
    }

    public static void pickTime(Context con, String title, OnTimeSetListener listener) {
        final Calendar cal = Calendar.getInstance();
        pickTime(con, title, cal, listener);
    }

    public static void pickTime(Context con, OnTimeSetListener listener) {
        final Calendar cal = Calendar.getInstance();
        pickTime(con, con.getString(R.string.pick_time), cal, listener);
    }

    /**
     * 日期选择对话框
     *
     * @param con      上下文
     * @param title    标题
     * @param calendar 日期
     * @param listener 选择事件
     */
    public static void pickDate(Context con, String title, Calendar calendar, OnDateSetListener listener) {
        pickMonth(con, title, calendar, listener);
    }

    public static void pickDate(Context con, String title, OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        pickDate(con, title, calendar, listener);
    }

    public static void pickDate(Context con, OnDateSetListener listener) {
        pickDate(con, con.getString(R.string.pick_date), listener);
    }

    public static void pickDate(Context con, Calendar calendar, OnDateSetListener listener) {
        pickDate(con, con.getString(R.string.pick_date), calendar, listener);
    }

    /**
     * 年月选择对话框
     *
     * @param con
     * @param title
     * @param calendar
     * @param listener
     */
    public static void pickMonth(Context con, String title, Calendar calendar, final OnDateSetListener listener) {
        LinearLayout ll = new LinearLayout(con);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(layoutParams);
        ll.setOrientation(LinearLayout.VERTICAL);
        //添加一条线
        LinearLayout line = new LinearLayout(con);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        line.setBackgroundColor(con.getResources().getColor(R.color.line));
        line.setLayoutParams(lp);
        ll.addView(line);
        //添加选择器控件
        final DatePicker datePicker = new DatePicker(con, null, themeId);
        datePicker.setLayoutParams(layoutParams);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        datePicker.setCalendarViewShown(false);
        ll.addView(datePicker);
        //初始化对话框
        QuickDialog.Builder builder = new QuickDialog.Builder(con);
        builder.setContentView(ll);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            }
        });
        builder.create().show();
    }

    public static void pickMonth(Context con, Calendar calendar, OnDateSetListener listener) {
        pickMonth(con, "", calendar, listener);
    }

    public static void pickMonth(Context con, OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        pickMonth(con, calendar, listener);
    }

    /**
     * 自定义对话框
     *
     * @param con
     * @param title
     * @param cancelable
     * @param view
     * @param listener1
     * @param listener2
     */
    public static void showCustomeDialog(Context con, String title, boolean cancelable, View view,
                                         DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2) {
        showCustomeDialog(con, title, cancelable, view, 0, con.getString(R.string.confirm), con.getString(R.string.cancel), listener1, listener2);
    }

    /**
     * 自定义对话框
     *
     * @param con
     * @param title
     * @param cancelable
     * @param view
     * @param gravity
     * @param listener1
     * @param listener2
     */
    public static void showCustomeDialog(Context con, String title, boolean cancelable, View view, int gravity,
                                         DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2) {
        showCustomeDialog(con, title, cancelable, view, gravity, con.getString(R.string.confirm), con.getString(R.string.cancel), listener1, listener2);
    }

    /**
     * 自定义对话框
     *
     * @param con
     * @param title
     * @param cancelable
     * @param view
     * @param btn1
     * @param btn2
     * @param listener1
     * @param listener2
     */
    public static void showCustomeDialog(Context con, String title, boolean cancelable, View view, int gravity, String btn1, String btn2,
                                         DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2) {
        QuickDialog.Builder builder = new QuickDialog.Builder(con);
        builder.setContentView(view);
        builder.setTitle(title);
        builder.setCancelable(cancelable);
        if (gravity > 0) {
            builder.setGravity(gravity);
        }
        builder.setPositiveButton(btn1, listener1);
        builder.setNegativeButton(btn2, listener2);
        builder.create().show();
    }

    /**
     * 检测EditText内容是否为空，若为空则进行警告提醒
     *
     * @param et
     * @param msg
     * @return
     */
    public static boolean showEditTextWarning(EditText et, String msg) {
        if (TextUtils.isEmpty(et.getText())) {
            et.setError(msg);
            et.requestFocus();
            et.setText("");
            return true;
        }
        return false;
    }

    /**
     * 保持对话框显示
     *
     * @param dialog
     */
    public static void Keep(DialogInterface dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏对话框
     *
     * @param dialog
     */
    public static void Hide(DialogInterface dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
