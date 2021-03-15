package com.example.douyink.parse.parser;

import android.text.TextUtils;

import com.example.douyink.utils.UrlUtil;
import com.example.douyink.parse.Parser;
import com.example.douyink.parse.callback.ParseCallback;
import com.example.douyink.parse.error.ParseError;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2021/3/15
 * 描    述：快手解析器
 * 修订历史：
 * ================================================
 */
public class KuaiShouParser implements Parser {

    @Override
    public boolean parseHtml(String html, ParseCallback callback) {
        Document document = Jsoup.parse(html);
        if (document == null) {
            if (callback != null) {
                callback.error(ParseError.HTML_CONTENT_NULL);
            }
            return false;
        }
        // 直接查找video标签
        Elements theVideo = document.getElementsByTag("video");
        if (theVideo == null) {
            if (callback != null) {
                callback.error(ParseError.VIDEO_TAG_NULL);
            }
            return false;
        }
        String videoUrl = theVideo.attr("src");
        if (TextUtils.isEmpty(videoUrl)) {
            // 第一次可能为空
            return false;
        }
        // 获取重定向的URL
        String finalVideoUrl = UrlUtil.getRedirectUrl(videoUrl);
        if (TextUtils.isEmpty(finalVideoUrl)) {
            if (callback != null) {
                callback.error(ParseError.REDIRECT_URL_NULL);
            }
            return false;
        }
        if (callback != null) {
            callback.success(finalVideoUrl);
        }
        return true;
    }
}
