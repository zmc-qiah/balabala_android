package org.qiah.balabala.MyListener;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public interface ModifyDialogListener {
    void submit(String text, List<LocalMedia> list);
}