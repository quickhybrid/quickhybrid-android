package com.quick.core.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quick.core.util.device.DeviceUtil;

import quick.com.core.R;


/**
 *  框架自定义对话框
 */
public class QuickDialog extends Dialog {


    public QuickDialog(Context context) {
        super(context);
    }

    private QuickDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private boolean isHighlightTitle = false;
        private int titleGravity = Gravity.CENTER;
        private int messageGravity = Gravity.CENTER;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean cancelable = true;
        private View contentView;
        private DialogInterface.OnDismissListener dismissListener;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
            this.positiveButtonText = context.getString(R.string.confirm);
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置消息内容
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 是否突出标题背景色
         *
         * @param isHighlightTitle
         * @return
         */
        public Builder highlightTitle(boolean isHighlightTitle) {
            this.isHighlightTitle = isHighlightTitle;
            return this;
        }

        /**
         * 设置标题和文本对齐方向
         *
         * @param gravity
         * @return
         */
        public Builder setGravity(int gravity) {
            this.titleGravity = gravity;
            this.messageGravity = gravity;
            return this;
        }

        /**
         * 设置标题对齐方向
         *
         * @param titleGravity
         * @return
         */
        public Builder setTitleGravity(int titleGravity) {
            this.titleGravity = titleGravity;
            return this;
        }

        /**
         * 设置文本对齐方向
         *
         * @param messageGravity
         * @return
         */
        public Builder setMessageGravity(int messageGravity) {
            this.messageGravity = messageGravity;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置自定义内容视图
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 设置是否可取消
         *
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * 设置取消监听
         *
         * @param dismissListener
         * @return
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
            this.dismissListener = dismissListener;
            return this;
        }

        /**
         * 设置确认按钮
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 设置确认按钮
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 设置取消按钮
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 设置取消按钮
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public QuickDialog create() {
            final QuickDialog dialog = new QuickDialog(context, R.style.quick_dialog);
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.frm_dialog, null);
            LinearLayout llContent = (LinearLayout) layout.findViewById(R.id.ll_content);
            View llTitle = layout.findViewById(R.id.ll_title);
            View line = layout.findViewById(R.id.line);
            TextView tvTtitle = (TextView) layout.findViewById(R.id.tv_title);
            TextView tvMessage = (TextView) layout.findViewById(R.id.tv_message);
            tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
            Button btnPositive = (Button) layout.findViewById(R.id.btn_positive);
            Button btnNegative = (Button) layout.findViewById(R.id.btn_negative);
            View llNegative = layout.findViewById(R.id.ll_negative);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.addContentView(layout, params);

            //设置是否可取消
            dialog.setCancelable(cancelable);
            //设置取消监听
            if (dismissListener != null) {
                dialog.setOnDismissListener(dismissListener);
            }
            //设置标题
            if (!TextUtils.isEmpty(title)) {
                tvTtitle.setText(title);
                tvTtitle.setGravity(titleGravity);
                tvTtitle.setVisibility(View.VISIBLE);
                if (isHighlightTitle) {
                    llTitle.setBackgroundResource(R.drawable.frm_dialog_title_bg);
                }
            } else {
                tvTtitle.setVisibility(View.GONE);
            }
            //设置消息
            if (!TextUtils.isEmpty(message)) {
                tvMessage.setText(message);
                tvMessage.setGravity(messageGravity);
                tvMessage.setVisibility(View.VISIBLE);
            } else {
                tvMessage.setVisibility(View.GONE);
                if (TextUtils.isEmpty(title)) {
                    llTitle.setVisibility(View.GONE);
                }
            }
            //设置确认按钮，如果为空则不设置
            if (!TextUtils.isEmpty(positiveButtonText)) {
                btnPositive.setText(positiveButtonText);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (positiveButtonClickListener != null) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    }
                });
            } else {
                btnPositive.setVisibility(View.GONE);
                btnNegative.setBackgroundResource(R.drawable.frm_click_dialog_right_btn_bg);
            }

            // 设置取消按钮，如果为空则不设置
            if (!TextUtils.isEmpty(negativeButtonText)) {
                btnNegative.setText(negativeButtonText);
                llNegative.setVisibility(View.VISIBLE);
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                });
            } else {
                llNegative.setVisibility(View.GONE);
                //如果按钮都隐藏了则把线也隐藏
                if (btnPositive.getVisibility() == View.GONE) {
                    line.setVisibility(View.GONE);
                } else {
                    btnPositive.setBackgroundResource(R.drawable.frm_click_dialog_single_btn_bg);
                }
            }
            //添加自定义控件
            if (contentView != null) {
                llContent.addView(contentView);
                tvMessage.setVisibility(View.GONE);
            }

            dialog.setContentView(layout);

            Window dialogWindow = dialog.getWindow();
            if (dialogWindow != null) {
                // 获取对话框当前的参数值
                WindowManager.LayoutParams p = dialogWindow.getAttributes();
                // 宽度设置为屏幕的0.8
                p.width = (int) (DeviceUtil.getPhoneWidth(context) * 0.8);
                dialogWindow.setAttributes(p);
            }
            return dialog;
        }
    }
}
