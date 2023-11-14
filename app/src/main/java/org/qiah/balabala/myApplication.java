package org.qiah.balabala;

import androidx.annotation.NonNull;

public class myApplication extends BaseApplication {
    @NonNull
    @Override
    public String iconFontPath() {
        return "iconfont.ttf";
    }

    @Override
    protected String normalPath()  {
        return "sourcehansans.ttf";
    }
}
