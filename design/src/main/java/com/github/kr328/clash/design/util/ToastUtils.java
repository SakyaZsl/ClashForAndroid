package com.github.kr328.clash.design.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kr328.clash.design.R;


/**
 * Create by Carson on 2021/12/22.
 * Toast 提示
 */
public class ToastUtils {

    private static Toast toast;

    /**
     * Toast 文字居中显示
     */
    public static void showMessage(Context context, String msg) {
        if (toast == null) {
            toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            TextView messageTv = (TextView) view.findViewById(R.id.tv_message);
            messageTv.setText(msg);
            toast.setView(view);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            TextView messageTv = (TextView) toast.getView().findViewById(R.id.tv_message);
            messageTv.setText(msg);
        }
        toast.show();
    }
}
