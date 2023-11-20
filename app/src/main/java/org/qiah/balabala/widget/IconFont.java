package org.qiah.balabala.widget;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import org.qiah.balabala.util.IconFontManager;

public class IconFont extends AppCompatTextView {

    public IconFont(Context context) {
        super(context);
        init();
    }

    public IconFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(IconFontManager.getIconAsset());
    }
}

