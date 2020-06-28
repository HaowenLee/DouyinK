package com.example.douyink;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2020/6/28
 * 描    述：网页加载
 * 修订历史：
 * ================================================
 */
public class KWebView extends WebView {

    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
    private HtmlCallback htmlCallback;

    public KWebView(Context context) {
        super(context);
        init();
    }

    private void init() {
        WebSettings settings = getSettings();
        getSettings().setDomStorageEnabled(true);
        // 设置支持javascript
        getSettings().setJavaScriptEnabled(true);
        // 启动缓存
        getSettings().setAppCacheEnabled(true);
        // 设置缓存模式
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        addJavascriptInterface(new InJavaScriptLocalObj(this), "java_obj");
        //允许混合模式（http与https）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }

    public KWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setHtmlCallback(HtmlCallback htmlCallback) {
        this.htmlCallback = htmlCallback;
    }

    interface HtmlCallback {
        void onHtmlGet(String html);
    }

    /**
     * 逻辑处理
     *
     * @author linzewu
     */
    class InJavaScriptLocalObj {

        public KWebView webView;

        public InJavaScriptLocalObj(KWebView webView) {
            this.webView = webView;
        }

        @JavascriptInterface
        public void getSource(String html) {
            if (htmlCallback != null) {
                htmlCallback.onHtmlGet(html);
            }
        }
    }
}
