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

import com.blankj.utilcode.util.ToastUtils;
import com.example.douyink.parse.Parser;
import com.example.douyink.parse.ParserFactory;
import com.example.douyink.parse.callback.ParseCallback;
import com.example.douyink.parse.error.ParseError;
import com.example.douyink.utils.ClipboardUtil;
import com.example.douyink.utils.UrlUtil;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {

    private final ParserFactory parserFactory = new ParserFactory();
    private EditText editText;
    private TextView tvResult;
    private KWebView webView;
    private Button button;
    /**
     * 解析器
     */
    private Parser mParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        editText = findViewById(R.id.editText);
        webView = findViewById(R.id.webView);
        button = findViewById(R.id.button);

        // 网页内容获取回调
        webView.setHtmlCallback((html) -> mParser.parseHtml(html, new ParseCallback() {
            @Override
            public void error(ParseError error) {
                runOnUiThread(() -> ToastUtils.showShort(error.getMsg()));
            }

            @Override
            public void success(String url) {
                runOnUiThread(() -> {
                    button.setText("开始解析");
                    button.setEnabled(true);
                    tvResult.setText(url);
                    Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                    intent.putExtra("video_url", url);
                    startActivity(intent);
                });
            }
        }));
    }

    /**
     * 窗体得到或失去焦点的时候的时候调用
     *
     * @param hasFocus 是否聚焦
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // 在 Android Q（10）中，应用在前台的时候才可以获取到剪切板内容。
        // https://www.jianshu.com/p/8f2100cd1cc5
        String shareText = ClipboardUtil.getClipboardText(this);
        if (parserFactory.isSupportPlatform(shareText)) {
            editText.setText(shareText);
        }
    }

    /**
     * 开始解析
     */
    public void button(View view) {
        final String url = UrlUtil.getUrl(editText.getText().toString());
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "未找到分享链接", Toast.LENGTH_SHORT).show();
            return;
        }
        button.setText("解析中...");
        button.setEnabled(false);
        // 获取解析器
        mParser = parserFactory.getParser(url);
        // 加载网页
        webView.loadUrl(url);
    }

    public void onResultClick(View view) {
        String text = tvResult.getText().toString();
        ClipboardUtil.setPrimaryClip(this, text);
        ToastUtils.showShort(text + " 已复制");
    }
}
