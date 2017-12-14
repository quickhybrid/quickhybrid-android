package com.quick.core.ui.widget.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Calendar;

/**
 *  自定义日期选择对话框，支持选择年月，保持title不变。
 */
public class QuickDatePickerDialog extends DatePickerDialog {

    private String title;
    private Context con;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    public QuickDatePickerDialog(Context con, int themeResId, String title, Calendar calendar, OnDateSetListener listener) {
        super(con, themeResId, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        this.con = con;
        this.title = title;
        year = calendar.get(Calendar.YEAR);
        monthOfYear= calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        setTitle(title);
    }

    /**
     * 隐藏日，只选择年月
     */
    public void hideDay() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                final Field field = this.findField(DatePickerDialog.class,
                        DatePicker.class, "mDatePicker");

                DatePicker datePicker = (DatePicker) field.get(this);
                final Class<?> delegateClass = Class
                        .forName("android.widget.DatePicker$DatePickerDelegate");
                final Field delegateField = this.findField(DatePicker.class,
                        delegateClass, "mDelegate");

                final Object delegate = delegateField.get(datePicker);
                final Class<?> spinnerDelegateClass = Class
                        .forName("android.widget.DatePickerSpinnerDelegate");

                if (delegate.getClass() != spinnerDelegateClass) {
                    delegateField.set(datePicker, null);
                    datePicker.removeAllViews();

                    final Constructor spinnerDelegateConstructor = spinnerDelegateClass
                            .getDeclaredConstructor(DatePicker.class,
                                    Context.class, AttributeSet.class,
                                    int.class, int.class);
                    spinnerDelegateConstructor.setAccessible(true);

                    final Object spinnerDelegate = spinnerDelegateConstructor
                            .newInstance(datePicker, con, null,
                                    android.R.attr.datePickerStyle, 0);
                    delegateField.set(datePicker, spinnerDelegate);

                    datePicker.init(year, monthOfYear, dayOfMonth, this);
                    datePicker.setCalendarViewShown(false);
                    datePicker.setSpinnersShown(true);
                }
            } catch (Exception e) { /* Do nothing */
            }
        } else {
            ((ViewGroup) ((ViewGroup) getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
        super.onDateChanged(view, year, month, dayOfMonth);
        setTitle(title);
    }

    /**
     * Find Field with expectedName in objectClass. If not found, find first
     * occurrence of target fieldClass in objectClass.
     */
    private Field findField(Class objectClass, Class fieldClass,
                            String expectedName) {
        try {
            final Field field = objectClass.getDeclaredField(expectedName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) { /* Ignore */
        }

        // Search for it if it wasn't found under the expectedName.
        for (final Field field : objectClass.getDeclaredFields()) {
            if (field.getType() == fieldClass) {
                field.setAccessible(true);
                return field;
            }
        }

        return null;
    }
}
