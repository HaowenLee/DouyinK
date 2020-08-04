package com.example.douyink;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2020/1/3
 * 描    述：视频播放
 * 修订历史：
 * ================================================
 */
public class VideoPlayActivity extends AppCompatActivity {

    private StandardGSYVideoPlayer videoPlayer;

    private OrientationUtils orientationUtils;

    private String videoUrl;
    private String videoTitle;

    private Disposable subscribe;
    private Disposable cSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_play);

        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("video_url");
        videoTitle = intent.getStringExtra("video_title");

        init();

        subscribe = new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        download(videoUrl);
                    } else {
                        Toast.makeText(this, "没有存储权限，无法下载视频", Toast.LENGTH_SHORT).show();
                    }
                }, Throwable::printStackTrace);
    }

    private void init() {
        // 全屏裁减显示，为了显示正常 CoverImageView 建议使用FrameLayout作为父布局
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);

        videoPlayer = findViewById(R.id.video_player);

        videoPlayer.setUp(videoUrl, true, videoTitle == null ? "" : videoTitle);
        // 增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        // 设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        // 设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        // 设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(v -> orientationUtils.resolveByClick());
        // 是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        // 设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
        videoPlayer.startPlayLogic();
    }

    /**
     * 下载视频
     *
     * @param url 链接地址
     */
    private void download(String url) {
        String internalMoviesPath = PathUtils.getExternalAppDownloadPath() + File.separator + EncryptUtils.encryptMD5ToString(videoUrl) + ".mp4";

        Uri uri = createFile(EncryptUtils.encryptMD5ToString(videoUrl) + ".mp4");

        FileDownloader.setup(this);
        FileDownloader.getImpl().create(url)
                .setPath(internalMoviesPath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void started(BaseDownloadTask task) {
                        super.started(task);
                        Toast.makeText(VideoPlayActivity.this, "视频开始下载", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        copyFile(task.getPath(), uri);
                        FileUtils.notifySystemToScan(uri.getPath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(VideoPlayActivity.this, "视频下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    @Override
    public void onBackPressed() {
        // 先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        // 释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    /**
     * 新增文件
     */
    private Uri createFile(String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Video.VideoColumns.MIME_TYPE, "video/mp4");
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    /**
     * 文件复制到外部
     */
    private void copyFile(String path, Uri uri) {
        if (cSubscribe != null && !cSubscribe.isDisposed()) {
            cSubscribe.dispose();
        }
        cSubscribe = Flowable.create((FlowableOnSubscribe<String>) emitter -> {
            // 文件存储适配Android Q
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                FileUtils.copy(path, PathUtils.getExternalMoviesPath());
            } else {
                writeFile(path, uri);
            }
            emitter.onNext("成功");
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> Toast.makeText(VideoPlayActivity.this, "视频下载完成", Toast.LENGTH_SHORT).show(), Throwable::printStackTrace);
    }

    /**
     * 对文件进行写操作
     */
    private void writeFile(String path, Uri uri) {
        try {
            AssetFileDescriptor assetFileDescriptor = getContentResolver().openAssetFileDescriptor(uri, "rw");
            FileOutputStream os = assetFileDescriptor.createOutputStream();
            FileInputStream is = new FileInputStream(path);
            byte[] data = new byte[8192];
            int len;
            while ((len = is.read(data, 0, 8192)) != -1) {
                os.write(data, 0, len);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
        if (cSubscribe != null && !cSubscribe.isDisposed()) {
            cSubscribe.dispose();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }
}
