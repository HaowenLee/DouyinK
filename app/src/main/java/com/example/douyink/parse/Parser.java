package com.example.douyink.parse;

import android.text.TextUtils;

import com.example.douyink.parse.callback.ConvertUrlCallback;
import com.example.douyink.parse.callback.ParseCallback;
import com.example.douyink.parse.error.ParseError;
import com.example.douyink.utils.UrlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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

    /**
     * 通用视频解析
     *
     * @param html               网页内容
     * @param parseCallback      解析结果回调
     * @param convertUrlCallback 链接转换
     * @return 是否解析成功
     */
    default boolean videoBaseParse(String html, ParseCallback parseCallback, ConvertUrlCallback convertUrlCallback) {
        Document document = Jsoup.parse(html);
        if (document == null) {
            if (parseCallback != null) {
                parseCallback.error(ParseError.HTML_CONTENT_NULL);
            }
            return false;
        }
        // 直接查找video标签
        Elements theVideo = document.getElementsByTag("video");
        if (theVideo == null) {
            if (parseCallback != null) {
                parseCallback.error(ParseError.VIDEO_TAG_NULL);
            }
            return false;
        }
        String videoUrl = theVideo.attr("src");
        if (TextUtils.isEmpty(videoUrl)) {
            // 第一次可能为空
            return false;
        }
        if (convertUrlCallback != null) {
            videoUrl = convertUrlCallback.convertUrl(videoUrl);
        }
        // 获取重定向的URL
        String finalVideoUrl = UrlUtil.getRedirectUrl(videoUrl);
        if (TextUtils.isEmpty(finalVideoUrl)) {
            if (parseCallback != null) {
                parseCallback.error(ParseError.REDIRECT_URL_NULL);
            }
            return false;
        }
        if (parseCallback != null) {
            parseCallback.success(finalVideoUrl);
        }
        return true;
    }
}
