package com.example.douyink.parse.callback;

import com.example.douyink.parse.error.ParseError;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2021/3/15
 * 描    述：解析的回调
 * 修订历史：
 * ================================================
 */
public interface ParseCallback {

    /**
     * 出错的回调
     *
     * @param error 出错信息
     */
    void error(ParseError error);

    /**
     * 成功的回调
     *
     * @param url 资源的可访问地址
     */
    void success(String url);
}
