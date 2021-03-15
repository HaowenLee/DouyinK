package com.example.douyink.parse.error;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2021/3/15
 * 描    述：
 * 修订历史：
 * ================================================
 */
public enum ParseError {

    HTML_CONTENT_NULL(1, "网页内容获取失败"),
    VIDEO_TAG_NULL(2, "视频标签获取失败"),
    REDIRECT_URL_NULL(3, "重定向地址获取失败");

    private final int code;
    private final String msg;

    ParseError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }
}
