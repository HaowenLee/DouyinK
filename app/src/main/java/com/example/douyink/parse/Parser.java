package com.example.douyink.parse;

import com.example.douyink.parse.callback.ParseCallback;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2021/3/15
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface Parser {

    /**
     * 解析网页内容
     */
    boolean parseHtml(String html, ParseCallback callback);
}
