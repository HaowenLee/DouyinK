package com.example.douyink.parse;

import com.example.douyink.parse.Parser;
import com.example.douyink.parse.parser.DouYinParser;
import com.example.douyink.parse.parser.KuaiShouParser;

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
    DOUYIN(1, "v.douyin.com", new DouYinParser()),
    // 快手
    KUAISHOU(2, "v.kuaishou.com", new KuaiShouParser());

    private final int platformType;
    private final String domain;
    private final Parser parser;

    Platform(int platformType, String domain, Parser parser) {
        this.platformType = platformType;
        this.domain = domain;
        this.parser = parser;
    }

    public int getPlatformType() {
        return platformType;
    }

    public String getDomain() {
        return domain == null ? "" : domain;
    }

    public Parser getParser() {
        return parser;
    }
}
