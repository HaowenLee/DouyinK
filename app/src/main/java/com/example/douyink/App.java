package com.example.douyink;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2020/1/3
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
