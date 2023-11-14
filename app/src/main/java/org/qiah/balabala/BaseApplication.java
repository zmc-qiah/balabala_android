package org.qiah.balabala;

import android.app.Application;
import android.content.Context;


import org.qiah.balabala.util.IconFontManager;

import java.lang.ref.WeakReference;

public abstract class BaseApplication extends Application {

    private static WeakReference<Context> contextReference;

    public static Context context() {
        return contextReference.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contextReference = new WeakReference<>(getApplicationContext());
        IconFontManager.initAsset(iconFontPath());
    }

    protected abstract String iconFontPath();
}