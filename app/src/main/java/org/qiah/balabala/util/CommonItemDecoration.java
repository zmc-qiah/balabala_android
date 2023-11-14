package org.qiah.balabala.util;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.IntDef;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CommonItemDecoration extends RecyclerView.ItemDecoration {

    public static final int VERTICAL = RecyclerView.VERTICAL;
    public static final int HORIZONTAL = RecyclerView.HORIZONTAL;

    @IntDef({VERTICAL, HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    private final float interval;
    @Orientation
    private final int orientation;

    public CommonItemDecoration(float interval, @Orientation int orientation) {
        this.interval = interval;
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        switch (orientation) {
            case VERTICAL:
                if (position == 0) {
                    outRect.top = 0;
                } else {
                    outRect.top = (int) interval;
                }
                break;
            case HORIZONTAL:
                if (position == 0) {
                    outRect.left = 0;
                } else {
                    outRect.left = (int) interval;
                }
                break;
            default:
                // do nothing
        }
    }
}