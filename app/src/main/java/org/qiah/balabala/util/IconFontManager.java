package org.qiah.balabala.util;

import android.graphics.Typeface;

import org.qiah.balabala.BaseApplication;

import java.util.List;

public class IconFontManager {

    private static Typeface iconAsset;
    private static Typeface normalAsset;
    private static Typeface boldAsset;

    public static Typeface getNormalAsset() {
        return normalAsset;
    }

    public static void initAsset(List<String> paths) {
        iconAsset = Typeface.createFromAsset(BaseApplication.context().getAssets(), paths.get(0));
        normalAsset = Typeface.createFromAsset(BaseApplication.context().getAssets(), paths.get(1));
        boldAsset = Typeface.createFromAsset(BaseApplication.context().getAssets(), paths.get(2));
    }

    public static Typeface getIconAsset() {
        return iconAsset;
    }

    public static Typeface getBoldAsset() {
        return boldAsset;
    }
}
