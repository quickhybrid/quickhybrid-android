package com.quick.jsbridge.api;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.quick.core.ui.widget.dialog.ActionSheet;
import com.quick.core.ui.widget.popmenu.FrmPopMenu;
import com.quick.core.ui.widget.popmenu.PopClickListener;
import com.quick.core.util.common.DateUtil;
import com.quick.core.util.common.DialogUtil;
import com.quick.core.util.common.JsonUtil;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.view.IQuickFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import quick.com.jsbridge.R;

import static android.graphics.Color.parseColor;


/**
 * Created by dailichun on 2017/12/6.
 */
public class UIApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "ui";



    /**
     * 消息提示
     * <p>
     * 参数：
     * message： 需要提示的消息内容
     * duration：显示时长，long或short
     */
    public static void toast(IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String message = param.optString("message");
        String duration = param.optString("duration");
        if ("long".equalsIgnoreCase(duration)) {
            webLoader.getPageControl().toast(message);
        } else {
            webLoader.getPageControl().toast(message);
        }
        callback.applySuccess();
    }

    /**
     * 弹出确认对话框
     * <p>
     * 参数：
     * title：标题
     * message：消息
     * cancelable：是否可取消
     * buttonLabels：按钮数组，设置按钮的时候从右往左设置，最多设置2个按钮
     * 返回：
     * which：按钮id
     */
    public static void confirm(IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String title = param.optString("title");
        final String msg = param.optString("message");
        final boolean cancelable = !"0".equals(param.optString("cancelable"));
        final JSONArray btnLabels = param.optJSONArray("buttonLabels");
        if (TextUtils.isEmpty(msg)) {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
        } else {
            String[] btnItems = null;
            btnItems = JsonUtil.parseJSONArray(btnLabels, btnItems);
            String button1 = "";
            String button2 = "";
            if (btnItems != null) {
                if (btnItems.length == 1) {
                    button1 = btnItems[0];
                } else if (btnItems.length > 1) {
                    button2 = btnItems[0];
                    button1 = btnItems[1];
                }
            }
            DialogUtil.showConfirmDialog(webLoader.getPageControl().getActivity(), title, msg, cancelable, button1, button2, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("which", 1);
                    callback.applySuccess(map);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("which", 0);
                    callback.applySuccess(map);
                }
            });
        }
    }

    /**
     * 弹出输入框
     * <p>
     * 参数：
     * title：标题
     * hint: 输入框提示文字
     * cancelable:是否可以返回取消对话框
     * lines:显示多少行
     * maxLength:最大输入字符数
     * text:默认显示字符串
     * buttonLabels:按钮数组，设置按钮的时候从右往左设置，最多设置2个按钮
     * 返回：
     * which：按钮id
     * content：输入文本
     */
    public static void prompt(IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        boolean cancelable = !"0".equals(param.optString("cancelable"));
        String title = param.optString("title");
        String hint = param.optString("hint");
        int lines = param.optInt("lines", 1);
        int maxLength = param.optInt("maxLength");
        String text = param.optString("text");

        LayoutInflater inflater = LayoutInflater.from(webLoader.getPageControl().getContext());
        View view = inflater.inflate(R.layout.frm_prompt, null);
        final EditText et = (EditText) view.findViewById(R.id.et);
        et.setHint(hint);
        et.setText(text);
        et.setSelection(text.length());
        if (lines > 1) {
            et.setLines(lines);
        }
        if (maxLength > 0) {
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        JSONArray btnLabels = param.optJSONArray("buttonLabels");
        String[] btnItems = null;
        btnItems = JsonUtil.parseJSONArray(btnLabels, btnItems);
        String button1 = "";
        String button2 = "";
        if (btnItems != null) {
            if (btnItems.length == 1) {
                button1 = btnItems[0];
            } else if (btnItems.length > 1) {
                button2 = btnItems[0];
                button1 = btnItems[1];
            }
        }
        DialogUtil.showCustomeDialog(webLoader.getPageControl().getActivity(), title, cancelable, view, Gravity.LEFT, button1, button2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> map = new HashMap<>();
                map.put("which", 1);
                map.put("content", et.getText().toString().trim());
                callback.applySuccess(map);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, Object> object = new HashMap<>();
                object.put("which", 0);
                object.put("content", et.getText().toString().trim());
                callback.applySuccess(object);
            }
        });
    }


    /**
     * 弹出日期选择对话框
     * <p>
     * 参数：
     * title： 标题
     * datetime： 指定日期 yyyy-MM-dd
     * 返回：
     * date： 格式：yyyy-MM-dd
     */
    public static void pickDate(final IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        final String title = param.optString("title");
        String date = param.optString("datetime");
        final Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            calendar.setTime(DateUtil.convertString2Date(date, "yyyy-MM-dd"));
        }
        wv.post(new Runnable() {
            public void run() {
                DialogUtil.pickDate(webLoader.getPageControl().getActivity(), title, calendar, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String chooseDate = DateUtil.convertDate(calendar.getTime(), "yyyy-MM-dd");
                        Map<String, Object> map = new HashMap<>();
                        map.put("date", chooseDate);
                        callback.applySuccess(map);
                    }
                });
            }
        });
    }

    /**
     * 弹出年月选择对话框
     * <p>
     * 参数：
     * title： 标题
     * datetime： 指定日期 yyyy-MM
     * 返回：
     * month： 格式：yyyy-MM
     */
    public static void pickMonth(final IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String title = param.optString("title");
        String date = param.optString("datetime");
        final Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            calendar.setTime(DateUtil.convertString2Date(date, "yyyy-MM"));
        }
        DialogUtil.pickMonth(webLoader.getPageControl().getActivity(), title, calendar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String chooseDate = DateUtil.convertDate(calendar.getTime(), "yyyy-MM");
                Map<String, Object> map = new HashMap<>();
                map.put("month", chooseDate);
                callback.applySuccess(map);
            }
        });
    }

    /**
     * 弹出时间选择对话框
     * <p>
     * 参数：
     * title：标题
     * datetime 指定时间 yyyy-MM-dd HH:mm或者HH:mm
     * 返回：
     * time：格式：HH:mm
     */
    public static void pickTime(IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String title = param.optString("title");
        String date = param.optString("datetime");
        final Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            if (date.contains(" ")) {
                calendar.setTime(DateUtil.convertString2Date(date, "yyyy-MM-dd HH:mm"));
            } else {
                calendar.setTime(DateUtil.convertString2Date(date, "HH:mm"));
            }
        }
        DialogUtil.pickTime(webLoader.getPageControl().getActivity(), title, calendar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String chooseTime = DateUtil.convertDate(calendar.getTime(), "HH:mm");
                Map<String, Object> map = new HashMap<>();
                map.put("time", chooseTime);
                callback.applySuccess(map);
            }
        });
    }

    /**
     * 弹出日期时间选择对话框
     * <p>
     * 参数：
     * title：标题
     * datetime 指定时间 yyyy-MM-dd HH:mm
     * 返回：
     * datetime：格式：yyyy-MM-dd HH:mm
     */
    public static void pickDateTime(final IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String title1 = param.optString("title1");
        String title2 = param.optString("title2");
        String date = param.optString("datetime");
        final Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            calendar.setTime(DateUtil.convertString2Date(date, "yyyy-MM-dd HH:mm"));
        }
        DialogUtil.pickDateTime(webLoader.getPageControl().getActivity(), title1, title2, calendar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String chooseDate = DateUtil.convertDate(calendar.getTime(), "yyyy-MM-dd HH:mm");
                Map<String, Object> map = new HashMap<>();
                map.put("datetime", chooseDate);
                callback.applySuccess(map);
            }
        });
    }

    /**
     * 弹出底部选项按钮
     * <p>
     * 参数：
     * items：多个选项用,隔开
     * cancelable: 是否可取消
     * 返回：
     * which：选中的按钮id
     */
    public static void actionSheet(final IQuickFragment webLoader, WebView wv, final JSONObject param, final Callback callback) {
        boolean cancelable = !"0".equals(param.optString("cancelable"));
        String[] items = null;
        JSONArray itemsJsonObject = param.optJSONArray("items");
        items = JsonUtil.parseJSONArray(itemsJsonObject, items);
        if (items == null) {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
            return;
        }

        ActionSheet menuView = new ActionSheet(webLoader.getPageControl().getActivity());
        menuView.setCancelButtonTitle(webLoader.getPageControl().getContext().getString(R.string.cancel));
        menuView.addItems(items);
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int index) {
                Map<String, Object> object = new HashMap<>();
                object.put("which", index);
                callback.applySuccess(object);
            }
        });
        menuView.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Map<String, Object> object = new HashMap<>();
                object.put("which", -1);
                callback.applySuccess(object);
            }
        });
        menuView.setCancelableOnTouchMenuOutside(cancelable);
        menuView.showMenu();
    }

    /**
     * 弹出顶部选项按钮
     * <p>
     * 参数：
     * iconFilterColor：图标过滤色
     * titleItems：多个选项用,隔开
     * iconItems: 图标 多个,隔开
     * 返回：
     * which：点击按钮id
     */
    public static void popWindow(IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String iconFilterColor = param.optString("iconFilterColor");
        JSONArray titleJsonObject = param.optJSONArray("titleItems");
        JSONArray iconJsonObject = param.optJSONArray("iconItems");
        if (titleJsonObject == null) {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
            return;
        }
        String[] titleItems = null;
        String[] iconItems = null;
        titleItems = JsonUtil.parseJSONArray(titleJsonObject, titleItems);
        iconItems = JsonUtil.parseJSONArray(iconJsonObject, iconItems);
        if (iconItems != null && titleItems.length != iconItems.length) {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
            return;
        }

        int iconColor = 0;
        if (!TextUtils.isEmpty(iconFilterColor)) {
            iconColor = parseColor("#" + iconFilterColor);
        }
        FrmPopMenu popupWindow = new FrmPopMenu(webLoader.getPageControl().getActivity(), webLoader.getPageControl().getNbBar().getRootView(), titleItems, iconItems, new PopClickListener() {
            @Override
            public void onClick(int index) {
                Map<String, Object> object = new HashMap<>();
                object.put("which", index);
                callback.applySuccess(object);
            }
        });
        popupWindow.setIconFilterColor(iconColor);
        popupWindow.show();
    }

    /**
     * 显示loading图标
     */
    public static void showWaiting(final IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String message = param.optString("message");
        webLoader.getPageControl().showLoading(message);
        callback.applySuccess();
    }

    /**
     * 隐藏loading图标
     */
    public static void closeWaiting(final IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.post(new Runnable() {
            public void run() {
                webLoader.getPageControl().hideLoading();
                callback.applySuccess();
            }
        });
    }

}
