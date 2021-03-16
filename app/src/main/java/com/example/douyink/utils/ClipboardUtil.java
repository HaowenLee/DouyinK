package com.example.douyink.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2021/3/15
 * 描    述：剪切板工具类
 * 修订历史：
 * ================================================
 */
public class ClipboardUtil {

    /**
     * 获取剪切板最新内容
     *
     * @param context 上下文
     */
    public static String getClipboardText(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = null;
        if (cm != null) {
            data = cm.getPrimaryClip();
        }
        ClipData.Item item = null;
        if (data != null) {
            item = data.getItemAt(0);
        }
        String content = null;
        if (item != null) {
            content = item.getText().toString();
        }
        return content;
    }

    /**
     * 复制文字到剪切板
     *
     * @param context 上下文
     * @param text    文本内容
     */
    public static void setPrimaryClip(Context context, String text) {
        ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
    }
}
