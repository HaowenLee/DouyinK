package com.example.douyink.parse;

import com.example.douyink.parse.parser.DouYinParser;
import com.example.douyink.parse.parser.KuaiShouParser;
import com.example.douyink.parse.parser.WeiShiParser;
import com.example.douyink.parse.parser.XiGuaParser;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2021/3/15
 * 描    述：
 * 修订历史：
 * ================================================
 */
public enum Platform {

    // 抖音
    DOUYIN("v.douyin.com", new DouYinParser()),
    // 快手
    KUAISHOU("v.kuaishou.com", new KuaiShouParser()),
    // 西瓜
    XIGUA("v.ixigua.com", new XiGuaParser()),
    // 腾讯微视
    WEISHI("isee.weishi.qq.com", new WeiShiParser());

    private final String domain;
    private final Parser parser;

    Platform(String domain, Parser parser) {
        this.domain = domain;
        this.parser = parser;
    }

    public String getDomain() {
        return domain == null ? "" : domain;
    }

    public Parser getParser() {
        return parser;
    }
}
