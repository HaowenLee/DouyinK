package com.example.douyink;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView tvResult;
    private Disposable subscribe;
    private Disposable qSubscribe;

    private static final String userAgent = "Mozilla/5.0.html (iPhone; U; CPU iPhone OS 4_3_3 like Mac " +
            "OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) " +
            "Version/5.0.html.2 Mobile/8J2 Safari/6533.18.5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        editText = findViewById(R.id.editText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
        if (qSubscribe != null && !qSubscribe.isDisposed()) {
            qSubscribe.dispose();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (qSubscribe != null && !qSubscribe.isDisposed()) {
            qSubscribe.dispose();
        }
        // 延迟获取，Android Q 以上问题
        qSubscribe = Flowable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    String shareText = getShareText();
                    if (!TextUtils.isEmpty(shareText) && shareText.contains(" https://v.douyin.com/")) {
                        editText.setText(shareText);
                    }
                });
    }

    /**
     * 获取剪切版内容
     *
     * @return 剪切版内容
     */
    public String getShareText() {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData data = null;
        if (cm != null) {
            data = cm.getPrimaryClip();
        }
        ClipData.Item item = null;
        if (data != null) {
            item = data.getItemAt(0);
        }
        String content = null;
        if (item != null) {
            content = item.getText().toString();
        }
        return content;
    }

    /**
     * 开始解析
     */
    public void button(View view) {
        final String url = getCompleteUrl(editText.getText().toString());
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "未找到抖音分享链接", Toast.LENGTH_SHORT).show();
            return;
        }
        // 解析到url
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
        subscribe = Flowable.create((FlowableOnSubscribe<String>) emitter -> {
            Document doc = Jsoup.connect(url).userAgent(userAgent).get();
            Elements video = doc.select("video[src]");
            for (Element el : video) {
                String videoUrl = el.attr("src");
                videoUrl = videoUrl.replace("playwm", "play");
                // 获取重定向的URL
                videoUrl = getRealUrl(videoUrl);
                emitter.onNext(videoUrl);
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoUrl -> {
                    tvResult.setText(videoUrl);
                    Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                    intent.putExtra("video_url", videoUrl);
                    startActivity(intent);
                }, Throwable::printStackTrace);
    }

    /**
     * 获取完整的域名
     *
     * @param text 获取浏览器分享出来的text文本
     */
    public static String getCompleteUrl(String text) {
        Pattern p = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9&%_./-~-]*)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);
        boolean find = matcher.find();
        if (find) {
            return matcher.group();
        } else {
            return "";
        }
    }

    /**
     * 获取重定向地址
     */
    private String getRealUrl(String urlStr) {
        String realUrl = urlStr;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("user-agent", "Mozilla/5.0.html (iPhone; U; CPU iPhone OS 4_3_3 like Mac " +
                    "OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) " +
                    "Version/5.0.html.2 Mobile/8J2 Safari/6533.18.5 ");
            conn.setInstanceFollowRedirects(false);
            int code = conn.getResponseCode();
            String redirectUrl = "";
            if (302 == code) {
                redirectUrl = conn.getHeaderField("Location");
            }
            if (redirectUrl != null && !redirectUrl.equals("")) {
                realUrl = redirectUrl;
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return realUrl;
    }

    public void onResultClick(View view) {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text;
        text = tvResult.getText().toString();

        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(this, text + " 已复制", Toast.LENGTH_SHORT).show();
    }
}
