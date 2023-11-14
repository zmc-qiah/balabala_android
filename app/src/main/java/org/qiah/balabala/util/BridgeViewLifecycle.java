package org.qiah.balabala.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelectorPreviewFragment;
import com.luck.picture.lib.basic.IBridgeViewLifecycle;
import com.luck.picture.lib.widget.MarqueeTextView;
import com.luck.picture.lib.widget.PreviewTitleBar;

public class BridgeViewLifecycle implements IBridgeViewLifecycle {
    public BridgeViewLifecycle() {
    }

    private static final int MSG_HIDE_VIEWS = 1;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_HIDE_VIEWS) {
                PictureSelectorPreviewFragment fragment = (PictureSelectorPreviewFragment) msg.obj;
                if (fragment != null && fragment.getView() != null) {
                    View titleBar = fragment.getView().findViewById(com.luck.picture.lib.R.id.title_bar);
                    if (titleBar instanceof PreviewTitleBar) {
                        ImageView deleteImageView = titleBar.findViewById(com.luck.picture.lib.R.id.ps_iv_delete);
                        MarqueeTextView titleTextView = titleBar.findViewById(com.luck.picture.lib.R.id.ps_tv_title);
                        if (deleteImageView != null) {
                            deleteImageView.setVisibility(View.GONE);
                        }
                        if (titleTextView != null) {
                            titleTextView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onViewCreated(final Fragment fragment, View view, Bundle savedInstanceState) {
        handler.postDelayed(() -> {
            Message message = handler.obtainMessage(MSG_HIDE_VIEWS, fragment);
            handler.sendMessage(message);
        }, 100);
    }

    @Override
    public void onDestroy(Fragment fragment) {
        // Clean up resources if needed
    }
}