package org.qiah.balabala;

import android.app.Application;
import android.content.Context;


import org.qiah.balabala.util.IconFontManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseApplication extends Application {

    private static WeakReference<Context> contextReference;

    public static Context context() {
        return contextReference.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contextReference = new WeakReference<>(getApplicationContext());
        List<String> paths = new ArrayList<>();
        paths.add(iconFontPath());
        paths.add(normalPath());
        IconFontManager.initAsset(paths);
    }

    protected abstract String iconFontPath();
    protected abstract String normalPath();
}