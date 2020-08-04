package com.example.douyink;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2020/6/28
 * 描    述：网页加载
 * 修订历史：
 * ================================================
 */
public class KWebView extends WebView {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private HtmlCallback htmlCallback;

    public KWebView(Context context) {
        super(context);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        // Dom存储支持
        getSettings().setDomStorageEnabled(true);
        // 设置支持javascript
        getSettings().setJavaScriptEnabled(true);
        // 启动缓存
        getSettings().setAppCacheEnabled(true);
        // 设置缓存模式
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 允许混合模式（http与https）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 获取网页内容的Js调用
        addJavascriptInterface(new InJavaScriptLocalObj(this), "java_obj");
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
                dispose();
                // 定时轮询获取网页内容，直到获取到有效信息
                compositeDisposable.add(Observable.interval(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(integer -> view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                                "document.getElementsByTagName('html')[0].innerHTML+'</head>');")));
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }

    /**
     * 取消订阅
     */
    public void dispose() {
        compositeDisposable.clear();
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dispose();
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
            // 加载出了地址
            if (!html.contains("playwm")) {
                return;
            }
            // 主线程运行取消
            ((Activity) webView.getContext()).runOnUiThread(() -> webView.dispose());
            // 回调
            if (htmlCallback != null) {
                htmlCallback.onHtmlGet(html);
            }
        }
    }
}