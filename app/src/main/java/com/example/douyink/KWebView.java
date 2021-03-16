package com.example.douyink;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
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
                Log.e("web", "onPageFinished " + url);
                if (TextUtils.equals(url, view.getUrl())) {
                    getHtml();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }

    /**
     * 延迟获取网页内容
     */
    private synchronized void getHtml() {
        dispose();
        // 延迟获取网页内容
        compositeDisposable.add(Observable.timer(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(integer -> loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');")));
    }

    /**
     * 取消订阅
     */
    public void dispose() {
        ((Activity) getContext()).runOnUiThread(compositeDisposable::clear);
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
        /**
         * 网页内容获取成功回调
         *
         * @param html 网页内容
         * @return 是否获取到了真实地址
         */
        boolean onHtmlGet(String html);
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
            boolean isSuccess = htmlCallback.onHtmlGet(html);
            Log.e("web", "getSource" + html + isSuccess);
            if (!isSuccess) {
                getHtml();
                return;
            }
            // 回调
            if (htmlCallback != null) {
                dispose();
            }
        }
    }
}