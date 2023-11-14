package org.qiah.balabala.util;

import android.graphics.Typeface;

import org.qiah.balabala.BaseApplication;

public class IconFontManager {

    private static Typeface iconAsset;

    public static void initAsset(String path) {
        iconAsset = Typeface.createFromAsset(BaseApplication.context().getAssets(), path);
    }

    public static Typeface getIconAsset() {
        return iconAsset;
    }
}
