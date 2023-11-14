package org.qiah.balabala.util;

import android.graphics.Typeface;

import org.qiah.balabala.BaseApplication;

import java.util.List;

public class IconFontManager {

    private static Typeface iconAsset;
    private static Typeface normalAsset;

    public static Typeface getNormalAsset() {
        return normalAsset;
    }

    public static void initAsset(List<String> paths) {
        iconAsset = Typeface.createFromAsset(BaseApplication.context().getAssets(), paths.get(0));
        normalAsset = Typeface.createFromAsset(BaseApplication.context().getAssets(), paths.get(1));
    }

    public static Typeface getIconAsset() {
        return iconAsset;
    }
}
