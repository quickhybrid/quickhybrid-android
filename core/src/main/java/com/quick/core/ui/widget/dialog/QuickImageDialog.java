package com.quick.core.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quick.core.util.device.DeviceUtil;

import quick.com.core.R;


/**
 *  框架自定义带图片对话框
 */
public class QuickImageDialog extends Dialog {

    public QuickImageDialog(Context context) {
        super(context);
    }

    private QuickImageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private int image;
        private String message;
        private String positiveButtonText;
        private boolean cancelable = true;
        private OnDismissListener dismissListener;
        private OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
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
         * 设置标题
         *
         * @param image
         * @return
         */
        public Builder setImage(int image) {
            this.image = image;
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
        public Builder setOnDismissListener(OnDismissListener dismissListener) {
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
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
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
        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public QuickImageDialog create() {
            final QuickImageDialog dialog = new QuickImageDialog(context, R.style.quick_dialog);
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.frm_image_dialog, null);
            TextView tvTtitle = (TextView) layout.findViewById(R.id.tv_title);
            TextView tvMessage = (TextView) layout.findViewById(R.id.tv_message);
            ImageView iv = (ImageView) layout.findViewById(R.id.iv);
            ImageView cancel = (ImageView) layout.findViewById(R.id.iv_cancel);
            Button btnPositive = (Button) layout.findViewById(R.id.btn);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.addContentView(layout, params);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //设置是否可取消
            dialog.setCancelable(cancelable);

            //设置取消监听
            if (dismissListener != null) {
                dialog.setOnDismissListener(dismissListener);
            }
            //设置图片
            if (image > 0) {
                iv.setImageResource(image);
            }
            //设置标题
            if (!TextUtils.isEmpty(title)) {
                tvTtitle.setText(title);
                tvTtitle.setVisibility(View.VISIBLE);
            } else {
                tvTtitle.setVisibility(View.GONE);
            }
            //设置消息
            if (!TextUtils.isEmpty(message)) {
                tvMessage.setText(message);
                tvMessage.setVisibility(View.VISIBLE);
            } else {
                tvMessage.setVisibility(View.GONE);
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
