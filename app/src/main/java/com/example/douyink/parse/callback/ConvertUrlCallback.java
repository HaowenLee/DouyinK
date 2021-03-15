package com.example.douyink.parse.callback;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2021/3/15
 * 描    述：转换
 * 修订历史：
 * ================================================
 */
public interface ConvertUrlCallback {

    /**
     * 地址转换
     *
     * @param url 地址
     * @return 转换后的地址
     */
    String convertUrl(String url);
}
