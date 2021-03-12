package com.example.douyink;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView tvResult;
    private KWebView webView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        editText = findViewById(R.id.editText);
        webView = findViewById(R.id.webView);
        button = findViewById(R.id.button);

        // 网页内容获取回调
        webView.setHtmlCallback(this::parseKuaiShouHtml);
    }

    /**
     * 解析网页获取视频播放地址
     */
    private void parseKuaiShouHtml(String html) {
        Document document = Jsoup.parse(html);
        if (document == null) {
            runOnUiThread(() -> ToastUtils.showShort("网页内容获取失败"));
            return;
        }
        // 直接查找video标签
        Elements theVideo = document.getElementsByTag("video");
        if (theVideo == null) {
            runOnUiThread(() -> ToastUtils.showShort("视频标签获取失败"));
            return;
        }
        String videoUrl = theVideo.attr("src");
        if (TextUtils.isEmpty(videoUrl)) {
            runOnUiThread(() -> ToastUtils.showShort("视频地址获取失败"));
            return;
        }
        // 获取重定向的URL
        String finalVideoUrl = getRealUrl(videoUrl);
        if (TextUtils.isEmpty(finalVideoUrl)) {
            runOnUiThread(() -> ToastUtils.showShort("重新想地址获取失败"));
            return;
        }
        // 跳转下载和视频播放页
        runOnUiThread(() -> {
            button.setText("开始解析");
            button.setEnabled(true);
            tvResult.setText(finalVideoUrl);
            Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
            intent.putExtra("video_url", finalVideoUrl);
            startActivity(intent);
        });
    }

    /**
     * 获取重定向地址
     */
    private String getRealUrl(String urlStr) {
        String realUrl = urlStr;
        LogUtils.i("链接地址：" + urlStr);
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

    /**
     * 解析网页获取视频播放地址
     */
    private void parseDouYinHtml(String html) {
        Document document = Jsoup.parse(html);
        if (document == null) {
            runOnUiThread(() -> ToastUtils.showShort("网页内容获取失败"));
            return;
        }
        // 直接查找video标签
        Elements theVideo = document.getElementsByTag("video");
        if (theVideo == null) {
            runOnUiThread(() -> ToastUtils.showShort("视频标签获取失败"));
            return;
        }
        String videoUrl = theVideo.attr("src");
        if (TextUtils.isEmpty(videoUrl)) {
            runOnUiThread(() -> ToastUtils.showShort("视频地址获取失败"));
            return;
        }
        // 替换成无水印地址
        videoUrl = videoUrl.replace("playwm", "play");
        // 获取重定向的URL
        String finalVideoUrl = getRealUrl(videoUrl);
        if (TextUtils.isEmpty(finalVideoUrl)) {
            runOnUiThread(() -> ToastUtils.showShort("重新想地址获取失败"));
            return;
        }
        // 跳转下载和视频播放页
        runOnUiThread(() -> {
            button.setText("开始解析");
            button.setEnabled(true);
            tvResult.setText(finalVideoUrl);
            Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
            intent.putExtra("video_url", finalVideoUrl);
            startActivity(intent);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // 在 Android Q（10）中，应用在前台的时候才可以获取到剪切板内容。
        // https://www.jianshu.com/p/8f2100cd1cc5
        String shareText = getShareText();
        if (!TextUtils.isEmpty(shareText) && (shareText.contains(" v.douyin.com") || shareText.contains(" v.kuaishou.com"))) {
            editText.setText(shareText);
        }
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
        button.setText("解析中...");
        button.setEnabled(false);
        webView.loadUrl(url);
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

    public void onResultClick(View view) {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text;
        text = tvResult.getText().toString();

        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, text + " 已复制", Toast.LENGTH_SHORT).show();
    }
}
